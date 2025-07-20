package org.watermedia.decoders.png.chunks;

import org.watermedia.decoders.Tools;

import java.nio.ByteBuffer;

// https://www.w3.org/TR/png/#acTL-chunk
public record ACTL(int frameCount, int loopCount) {
    static final int SIGNATURE = 0x61_63_54_4C; // Animation Control Chunk

    public static ACTL read(ByteBuffer buffer) {
        while (true) {
            CHUNK c = CHUNK.read(buffer);
            if (c.type() == SIGNATURE) {
                byte[] data = c.data();
                int frameCount = Tools.toInt(data[0], data[1], data[2], data[3], buffer.order());
                int loopCount = Tools.toInt(data[4], data[5], data[6], data[7], buffer.order());
                return new ACTL(frameCount, loopCount);
            }
        }
    }
}
