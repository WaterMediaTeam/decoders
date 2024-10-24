package org.watermedia.decoders.png.chunks;

import java.nio.ByteBuffer;

// https://www.w3.org/TR/png/#fcTL-chunk
public record FCTL(int seq, int width, int height, int xOffset, int yOffset,
            short delay, short delay_den, byte dispose, byte blend) {

    public static final int SIGNATURE = 0x66_63_54_4c; // Frame control chunk

    private FCTL read(ByteBuffer buffer) {
        return new FCTL(
                buffer.getInt(),
                buffer.getInt(),
                buffer.getInt(),
                buffer.getInt(),
                buffer.getInt(),
                buffer.getShort(),
                buffer.getShort(),
                buffer.get(),
                buffer.get()
        );
    }
}
