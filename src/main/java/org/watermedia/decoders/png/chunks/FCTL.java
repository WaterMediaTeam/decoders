package org.watermedia.decoders.png.chunks;

import org.watermedia.decoders.Tools;

import java.nio.ByteBuffer;

// https://www.w3.org/TR/png/#fcTL-chunk
public record FCTL(int seq, int width, int height, int xOffset, int yOffset,
            short delay, short delay_den, byte dispose, byte blend) {

    public static final int SIGNATURE = 0x66_63_54_4c; // Frame control chunk

    public static FCTL read(ByteBuffer buffer) {
        final CHUNK chunk = CHUNK.read(buffer);
        buffer.order()
        final byte[] data = chunk.data();
        return new FCTL(
                Tools.toInt(data[0], data[1], data[2], data[3]),
                Tools.toInt(data[4], data[5], data[6], data[7]),
                Tools.toInt(data[8], data[9], data[10], data[11]),
                Tools.toInt(data[12], data[13], data[14], data[15]),
                Tools.toInt(data[16], data[17], data[18], data[19]),
                buffer.getShort(),
                buffer.getShort(),
                buffer.get(),
                buffer.get()
        );
    }
}
