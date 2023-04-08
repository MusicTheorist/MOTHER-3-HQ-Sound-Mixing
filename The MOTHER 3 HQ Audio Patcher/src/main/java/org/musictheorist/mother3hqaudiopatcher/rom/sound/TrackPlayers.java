package org.musictheorist.mother3hqaudiopatcher.rom.sound;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum TrackPlayers {
      ONE(0x00120E20L, 0x03000400, 0x320),
      TWO(0x00120E2CL, 0x03000720, 0x320),
    THREE(0x00120E38L, 0x03000A40, 0x0A0),
     FOUR(0x00120E44L, 0x03000AE0, 0x140),
     FIVE(0x00120E50L, 0x03000C20, 0x280),
      SIX(0x00120E5CL, 0x03000EA0, 0x140),
    SEVEN(0x00120E68L, 0x03000FE0, 0x500),
    EIGHT(0x00120E74L, 0x030014E0, 0x280),
     NINE(0x00120E80L, 0x03001760, 0x0A0),
      TEN(0x00120E8CL, 0x03001800, 0x320);

    private final long pointer;
    private final int defaultAddress;
    private final int sizeInBytes;

    private static final Map<Integer, TrackPlayers> lookup = new HashMap<Integer, TrackPlayers>();

    private TrackPlayers(long pointer, int defaultAddress, int sizeInBytes) {
        this.pointer = pointer;
        this.defaultAddress = defaultAddress;
        this.sizeInBytes = sizeInBytes;
    }

    public long romPointer() {
        return pointer;
    }
    public int defaultAddress() {
        return defaultAddress;
    }
    public int sizeInBytes() {
        return sizeInBytes;
    }

    static {
        int i = 1;
        for(TrackPlayers player : EnumSet.allOf(TrackPlayers.class)){
            lookup.put(i, player);
            i++;
        }
    }

    public static TrackPlayers get(int player) {
        return lookup.get(player);
   }
}