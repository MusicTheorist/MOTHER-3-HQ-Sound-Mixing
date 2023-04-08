package org.musictheorist.mother3hqaudiopatcher.resources;

import java.nio.file.Path;

import org.musictheorist.mother3hqaudiopatcher.patching.PatchStates;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.UnusedBytes;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.SampleRates;

public final class ROMInfo {
    private final Path romPath;

    private PatchStates patchType;
    private SampleRates sampleRate;
    private int patchedMixerAddress;
    private UnusedBytes overwrittenMixerData;
    private boolean hasAltJapanPatch;

    public ROMInfo(Path romPath) {
        this.romPath = romPath;
    }

    Path getROMPath() {
        return romPath;
    }

    void setPatchType(PatchStates patchType) {
        this.patchType = patchType;
    }
    PatchStates getPatchType() {
        return patchType;
    }

    void setSampleRate(SampleRates sampleRate) {
        this.sampleRate = sampleRate;
    }
    SampleRates getSampleRate() {
        return sampleRate;
    }

    void setPatchedMixerAddress(int patchedMixerAddress) {
        this.patchedMixerAddress = patchedMixerAddress;
    }
    public int getPatchedMixerAddress() {
        return patchedMixerAddress;
    }

    void setOverwrittenMixerData(UnusedBytes overwrittenMixerData) {
        this.overwrittenMixerData = overwrittenMixerData;
    }
    public UnusedBytes getOverwrittenMixerData() {
        return overwrittenMixerData;
    }

    void toggleAltJapanPatch(boolean hasAltJapanPatch) {
        this.hasAltJapanPatch = hasAltJapanPatch;
    }
    boolean isAltJapanPatchEnabled() {
        return hasAltJapanPatch;
    }
}