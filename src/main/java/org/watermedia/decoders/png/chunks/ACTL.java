package org.watermedia.decoders.png.chunks;

// https://www.w3.org/TR/png/#acTL-chunk
public record ACTL(int frameCount, int loopCount) {
    static final int SIGNATURE = 0x61_63_54_4C; // Animation Control Chunk
}
