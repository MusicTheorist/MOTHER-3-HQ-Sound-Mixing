package org.musictheorist.mother3hqaudiopatcher.rom.rng;

public enum BattlePointers {
    RNG_ONE(0x00069314),
    RNG_TWO(0x00069434);

    private final long offset;

    private BattlePointers(long offset) {
        this.offset = offset;
    }

    public long offset() {
        return offset;
    }
}