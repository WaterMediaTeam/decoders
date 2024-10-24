package org.watermedia.decoders;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public interface IDecoder {
    BufferedImage[] decode(ByteBuffer buffer);
}
