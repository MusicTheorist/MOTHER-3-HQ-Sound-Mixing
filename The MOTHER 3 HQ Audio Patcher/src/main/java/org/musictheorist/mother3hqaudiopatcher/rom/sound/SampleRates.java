package org.musictheorist.mother3hqaudiopatcher.rom.sound;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum SampleRates {
          FIVE_KHZ((byte) 0x91,  0),
           TEN_KHZ((byte) 0x93,  1),
      THIRTEEN_KHZ((byte) 0x94,  2),
           DEFAULT((byte) 0x95,  3),
      EIGHTEEN_KHZ((byte) 0x96,  4),
    TWENTY_ONE_KHZ((byte) 0x97,  5),
    TWENTY_SIX_KHZ((byte) 0x98,  6),
    THIRTY_ONE_KHZ((byte) 0x99,  7),
    THIRTY_SIX_KHZ((byte) 0x9A,  8),
        FOURTY_KHZ((byte) 0x9B,  9),
    FOURTY_TWO_KHZ((byte) 0x9C, 10);

    private final byte rateByte;
    private final int index;

    private static final Map<Integer, SampleRates> lookup = new HashMap<Integer, SampleRates>();
    public static final int LENGTH = SampleRates.values().length;

    private SampleRates(byte rateByte, int index) {
        this.rateByte = rateByte;
        this.index = index;
    }

    public byte rateByte() {
        return rateByte;
    }
    public int index() {
        return index;
    }

    static {
        for(SampleRates i : EnumSet.allOf(SampleRates.class)) {
            lookup.put((int) i.rateByte(), i);
            lookup.put(i.index(), i);
        }
    }

    public static SampleRates get(int key) {
        SampleRates sampleRate = lookup.get(key);
        return (sampleRate != null ? sampleRate : SampleRates.DEFAULT);
    }
    public static SampleRates get(byte rateByte) {
        return SampleRates.get((int) rateByte);
   }
}