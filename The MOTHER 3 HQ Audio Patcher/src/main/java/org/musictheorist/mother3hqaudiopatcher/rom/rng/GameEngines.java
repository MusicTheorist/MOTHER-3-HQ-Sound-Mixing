package org.musictheorist.mother3hqaudiopatcher.rom.rng;

import org.musictheorist.mother3hqaudiopatcher.patching.Conversions;

public enum GameEngines {
    BATTLE(0x02001084),
    OVERWORLD(0x02005230);

    private final int rngAddress;

    private GameEngines(int rngAddress) {
        this.rngAddress = rngAddress;
    }

    public int ramAddress() {
        return rngAddress;
    }
    public byte[] ramPointer() {
        return Conversions.getPointer(rngAddress, false);
    }
}