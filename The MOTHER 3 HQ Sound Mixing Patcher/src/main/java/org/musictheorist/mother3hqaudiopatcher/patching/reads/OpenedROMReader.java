package org.musictheorist.mother3hqaudiopatcher.patching.reads;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.musictheorist.mother3hqaudiopatcher.ROMException;
import org.musictheorist.mother3hqaudiopatcher.patching.PatchIO;
import org.musictheorist.mother3hqaudiopatcher.patching.PatchStates;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.FanROMPatcher;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.JapanROMPatcher;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.ROMPatcher;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.UndoPatcher;
import org.musictheorist.mother3hqaudiopatcher.resources.Mixers;
import org.musictheorist.mother3hqaudiopatcher.resources.fetchers.MixerFetcher;
import org.musictheorist.mother3hqaudiopatcher.rom.metadata.Lengths;
import org.musictheorist.mother3hqaudiopatcher.rom.rng.BattlePointers;
import org.musictheorist.mother3hqaudiopatcher.rom.rng.GameEngines;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.SampleRates;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.SoundEngine;

public final class OpenedROMReader {
    private PatchIO patchIO;

    private int mixerROMAddress;
    private int battleRNGAddressOne;

    private int patchedMixerAddress;
    private UnusedBytes overwrittenMixerData;

    private byte[] mixerSize;

    private SampleRates sampleRate;

    public OpenedROMReader(PatchIO patchIO) throws IOException {
        this.patchIO = patchIO;

        ByteBuffer  buffer     = patchIO.buffer();
        FileChannel romChannel = patchIO.romChannel();

        this.mixerROMAddress     = BufferReads.intoInt(buffer, 4, romChannel, SoundEngine.MIXER_ROM_POINTER, true);
        this.battleRNGAddressOne = BufferReads.intoInt(buffer, 4, romChannel, BattlePointers.RNG_ONE.offset(), false);

        fetchMixerSize(buffer, romChannel);
        fetchSampleRate(patchIO);
    }

    public void fetchMixerSize(ByteBuffer buffer, FileChannel romChannel) throws IOException {
        mixerSize = BufferReads.intoArray(buffer, 2, romChannel, SoundEngine.MIXER_CPUSET_SIZE_OFFSET);
    }
    public void fetchSampleRate(PatchIO patchIO) throws IOException {
        patchIO.mother3ROM().seek(SoundEngine.SAMPLE_RATE_OFFSET);
        sampleRate = SampleRates.get(patchIO.mother3ROM().readByte());
    }

    public int getMixerROMAddress() {
        return mixerROMAddress;
    }

    public boolean isMixerAtOriginalAddress() {
        return mixerROMAddress == SoundEngine.OLD_MIXER_DEFAULT_ROM_OFFSET;
    }
    public boolean usesDefaultPatchedMixerAddress() {
        return patchedMixerAddress == SoundEngine.NEW_MIXER_DEFAULT_ROM_OFFSET;
    }

    private void setPatchedMixerAddress() {
        if(isMixerAtOriginalAddress()) {
            this.patchedMixerAddress = (int) SoundEngine.NEW_MIXER_DEFAULT_ROM_OFFSET;
        }
        else {
            this.patchedMixerAddress = mixerROMAddress;
        }
    }
    public void updatePatchedMixerAddress(int newAddress) {
        patchedMixerAddress = newAddress;
    }
    public int getPatchedMixerAddress() {
        return patchedMixerAddress;
    }

    private void seekOverwrittenMixerByte(PatchIO patchIO) throws IOException {
        patchIO.mother3ROM().seek(patchedMixerAddress + Lengths.FREE_SPACE_LENGTH);
    }
    public UnusedBytes readOverwrittenMixerByte(PatchIO patchIO) throws IOException, IrreparableROMException {
        seekOverwrittenMixerByte(patchIO);
        overwrittenMixerData = UnusedBytes.get(patchIO.mother3ROM().readByte());
        return overwrittenMixerData;
    }
    public void updateOverwrittenMixerByte(PatchIO patchIO) throws IOException {
        seekOverwrittenMixerByte(patchIO);
        if(UnusedBytes.isZero(patchIO.mother3ROM().readByte())) {
            overwrittenMixerData = UnusedBytes.ZERO;
        }
        else {
            overwrittenMixerData = UnusedBytes.NULL;
        }
    }
    public UnusedBytes getOverwrittenMixerByte() {
        return overwrittenMixerData;
    }
    public void assumeDefaultOverwrittenByte() {
        overwrittenMixerData = UnusedBytes.ZERO;
    }

    public void initPatchedMixerData() throws IOException, IrreparableROMException {
        setPatchedMixerAddress();
        readOverwrittenMixerByte(patchIO);
    }

    public boolean isMixerDMAFixEnabled() {
        return Mixers.selectMixer(mixerSize) == Mixers.DMA_FIX_ENABLED;
    }

    public SampleRates getSampleRate() {
        return sampleRate;
    }

    private PatchVerifier newVerifier(ROMPatcher patcher) {
        PatchVerifier verifier = new PatchVerifier(patchIO);
        verifier.setPatch(patcher.getPatch());
        return verifier;
    }

    public PatchStates scanForPatches() throws IOException {
        ROMPatcher patcher;
        PatchStates patchState;

        byte[] mixer;

        if(isMixerAtOriginalAddress()) {
            if(overwrittenMixerData == UnusedBytes.ZERO) {
                mixer = MixerFetcher.newZeroedOutMixer();
            }
            else {
                mixer = MixerFetcher.newNulledOutMixer();
            }
            patcher = new UndoPatcher(patchIO);
            patchState = PatchStates.NO_PATCH;
        }
        else {
            if(battleRNGAddressOne == GameEngines.OVERWORLD.ramAddress()) {
                MixerFetcher fetcher = new MixerFetcher(Mixers.DMA_FIX_DISABLED);
                mixer = fetcher.getMixer();
                patcher = new FanROMPatcher(patchIO);
                patchState = PatchStates.FAN_PATCH;
            }
            else {
                MixerFetcher fetcher = new MixerFetcher(Mixers.selectMixer(mixerSize));
                mixer = fetcher.getMixer();
                patcher = new JapanROMPatcher(patchIO);
                patchState = PatchStates.JAPAN_PATCH;
            }
        }

        try {
            patcher.initPatch(mixer, patchedMixerAddress, sampleRate);
            newVerifier(patcher).verifyData();
            return patchState;
        }
        catch(ROMException e) {
            return PatchStates.CORRUPT;
        }
    }
}