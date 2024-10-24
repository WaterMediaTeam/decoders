package org.watermedia.decoders.png.chunks;

public record FDAT(int sequence, byte[] data) {
    static final int SIGNATURE = 0x66_64_41_54; // Frame Data Chunk
}