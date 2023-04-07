package org.musictheorist.mother3hqaudiopatcher.rom.sound;

import org.musictheorist.mother3hqaudiopatcher.patching.Conversions;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.UnusedBytes;
import org.musictheorist.mother3hqaudiopatcher.rom.metadata.Lengths;

public enum MixerSizes {
       VANILLA(0x0100, 0x400),
    NO_DMA_FIX(0x021F, 0x87C),
       DMA_FIX(0x0223, 0x88C);

    private final short cpuSetUnits;
    private final int sizeInBytes;

    private MixerSizes(int cpuSetUnits, int sizeInBytes) {
        this.cpuSetUnits = (short) cpuSetUnits;
        this.sizeInBytes = sizeInBytes;
    }

    public byte[] cpuSetBytes() {
        return Conversions.getCpuSetSize(cpuSetUnits);
    }
    public int sizeInBytes() {
        return sizeInBytes;
    }

    public static MixerSizes getSize(byte[] mixer) {
        if(mixer.length == MixerSizes.NO_DMA_FIX.sizeInBytes) {
            return MixerSizes.NO_DMA_FIX;
        }
        else if(mixer.length == MixerSizes.DMA_FIX.sizeInBytes) {
            return MixerSizes.DMA_FIX;
        }
        else if(mixer.length == (Lengths.FREE_SPACE_LENGTH + 1)
            && (mixer[0] == UnusedBytes.ZERO.value() || mixer[0] == UnusedBytes.NULL.value())) {
            return MixerSizes.VANILLA;
        }
        else {
            throw new InvalidMixerSizeException();
        }
    }
}