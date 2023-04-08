package org.musictheorist.mother3hqaudiopatcher.resources;

import java.util.Arrays;

import org.musictheorist.mother3hqaudiopatcher.rom.sound.MixerSizes;

public enum Mixers {
    DMA_FIX_DISABLED("m4a_hq_mixer_no_fix.bin"),
    DMA_FIX_ENABLED("m4a_hq_mixer_dma_fix.bin");

    private final String string;

    private Mixers(String string) {
        this.string = string;
    }

    public String classPath() {
        return "/mixer/bin/" + string;
    }

    public static Mixers selectMixer(byte[] mixerSize) {
        if(Arrays.equals(mixerSize, MixerSizes.DMA_FIX.cpuSetBytes())) {
            return Mixers.DMA_FIX_ENABLED;
        }
        else return Mixers.DMA_FIX_DISABLED;
    }
    public static Mixers selectMixer(boolean dmaFix) {
        return dmaFix ? Mixers.DMA_FIX_ENABLED : Mixers.DMA_FIX_DISABLED;
    }
}