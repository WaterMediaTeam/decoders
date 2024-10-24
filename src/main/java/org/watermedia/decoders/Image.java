package org.watermedia.decoders;

import java.awt.image.BufferedImage;

public record Image(BufferedImage[] images, long[] delay, long duration, int repeat) {
    public static final int REPEAT_FOREVER = 0;
    public static final int NO_REPEAT = -1;

    public Image(BufferedImage[] images, long[] delay, int repeat) {
        this(images, delay, sumArray(delay), repeat);
    }

    private static long sumArray(long[] array) {
        int i = 0;
        long duration = 0;
        while (i++ < array.length) {
            duration += array[i];
        }
        return duration;
    }
}
