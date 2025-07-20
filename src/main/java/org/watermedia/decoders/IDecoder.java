package org.watermedia.decoders;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public interface IDecoder {
    BufferedImage[] decode(ByteBuffer buffer) throws ImageDecodingException;

    /**
     * Reads the "expected" header of the decoder format.
     * When it matches it doesn't rewind to the initial position assuming it will proceed with the decoding
     * When it doens't, rewinds to the initial position to let other decoders properly check the value.
     * @param buffer encoded picture data
     * @return true if the decoder its compatible with the buffer data, false otherwise
     */
    boolean supported(ByteBuffer buffer);
}
