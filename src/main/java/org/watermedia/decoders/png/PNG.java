package org.watermedia.decoders.png;

import org.watermedia.decoders.IDecoder;
import org.watermedia.decoders.Image;
import org.watermedia.decoders.ImageDecodingException;
import org.watermedia.decoders.png.chunks.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class PNG implements IDecoder {
    // from https://w3.org/TR/png/#structure
    static final int IHDR_SIG = 0x49_48_44_52; // Image header
    static final int IEND_SIG = 0x49_45_4E_44; // Image trailer

    // TL is the chunk
    static final int[] PNGF_SIG = new int[] { 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A}; // 0x8950_4E47_0D0A_1A0A
    private PNG() {}

    public boolean supported(ByteBuffer buffer) {
        buffer.mark();
        for (int value: PNGF_SIG) {
            if (buffer.get() != value) {
                buffer.reset();
                buffer.flip();
                return false;
            }
        }
        return true; // READY TO DECODE
    }

    // Layout: https://www.w3.org/TR/png/#5Chunk-layout
    public BufferedImage[] decode(ByteBuffer buffer) throws ImageDecodingException {
        ACTL actl = ACTL.read(buffer);

        Set<Image> images = new HashSet<>();
        for (int i = 0; i < actl.frameCount(); i++) {
            FCTL frameData = FCTL.read(buffer);
            int chunkSize = buffer.getInt();
            int sig = buffer.getInt();

            // Nothing that we can read
            if (sig != FDAT_SIG && sig != IDAT_SIG) {
                throw new ImageDecodingException("Unexpected chunk sig");
            }

            // uhhhh
            if (sig == FDAT_SIG) {
                buffer.getInt(); // sequence number
            }

            // STEP 1: Read the data
            ByteBuffer pngData = buffer.duplicate();
            pngData.limit(buffer.position() + (chunkSize - 4));

            // Reposition after data
            buffer.position(buffer.position() + chunkSize);

            // STEP 2: Decompression
            // TODO: read compression algorithm
            ByteBuffer pixels = ByteBuffer.allocate(frameData.width * frameData.height);
            while (pngData.hasRemaining()) {
                int filter = pngData.get();

                // https://www.w3.org/TR/png/#12Filter-selection
                switch (filter) {
                    case 0 -> {
                        pngData.limit(pngData.position() + frameData.width);
                        pixels.put(pngData);
                        pngData.limit(pngData.capacity());
                    }

                    default -> throw new ImageDecodingException("Pixels uses (" + filter + ") an unsupported");
                }
            }


        }
    }

    private ByteBuffer inflate(ByteBuffer buffer) {
        var inflate = new Inflater();
        inflate.setInput(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining());

        var output = new ByteArrayOutputStream();
        var data = new byte[1024 * 8];

        while (!inflate.finished()) {
            try {
                final int length = inflate.inflate(data);
                output.write(data, 0, length);
            } catch (DataFormatException e) {
                throw new RuntimeException(e);
            }
        }

        inflate.end();
        return ByteBuffer.wrap(data);
    }
}
