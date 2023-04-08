package org.musictheorist.mother3hqaudiopatcher.patching.reads;

import java.io.IOException;

import org.musictheorist.mother3hqaudiopatcher.ROMException;
import org.musictheorist.mother3hqaudiopatcher.patching.Patch;
import org.musictheorist.mother3hqaudiopatcher.patching.PatchIO;
import org.musictheorist.mother3hqaudiopatcher.rom.rng.BattlePointers;
import org.musictheorist.mother3hqaudiopatcher.rom.rng.GameEngines;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.TrackPlayers;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.SoundEngine;

public final class PatchVerifier extends ROMVerifier {
    Patch patch;

    public PatchVerifier(PatchIO patchIO) {
        super(patchIO);
    }

    private void checkNullPatch() {
        if(patch == null) {
            throw new ReadNullPatchException();
        }
    }

    public void setPatch(Patch patch) {
        this.patch = patch;
    }

    public void verifyData() throws IOException, ROMException {
        checkNullPatch();

        SoundEngine soundData = patch.soundEngine();
        GameEngines patchedBattleRNG = patch.battleRNG();

        byte[] rngPointer = patchedBattleRNG.ramPointer();
        verify(rngPointer, BattlePointers.RNG_ONE.offset());
        verify(rngPointer, BattlePointers.RNG_TWO.offset());

        verify(soundData.m4aHQMixer(), soundData.mixerROMOffset());
        verify(soundData.soundInfoRAMAddress(), SoundEngine.SOUND_INFO_POINTER);
        verify(soundData.mixerROMAddress(), SoundEngine.MIXER_ROM_POINTER);
        verify(soundData.mixerCpuSetAddress(false), SoundEngine.MIXER_CPUSET_POINTER);
        verify(soundData.mixerSize(), SoundEngine.MIXER_CPUSET_SIZE_OFFSET);
        verify(soundData.mixerCpuSetAddress(true), SoundEngine.MIXER_RAM_BRANCH);

        verify(soundData.trackPlayerAddress( 1), TrackPlayers.ONE.romPointer());
        verify(soundData.trackPlayerAddress( 2), TrackPlayers.TWO.romPointer());
        verify(soundData.trackPlayerAddress( 3), TrackPlayers.THREE.romPointer());
        verify(soundData.trackPlayerAddress( 4), TrackPlayers.FOUR.romPointer());
        verify(soundData.trackPlayerAddress( 5), TrackPlayers.FIVE.romPointer());
        verify(soundData.trackPlayerAddress( 6), TrackPlayers.SIX.romPointer());
        verify(soundData.trackPlayerAddress( 7), TrackPlayers.SEVEN.romPointer());
        verify(soundData.trackPlayerAddress( 8), TrackPlayers.EIGHT.romPointer());
        verify(soundData.trackPlayerAddress( 9), TrackPlayers.NINE.romPointer());
        verify(soundData.trackPlayerAddress(10), TrackPlayers.TEN.romPointer());

        verify(new byte[] {soundData.sampleRate()}, SoundEngine.SAMPLE_RATE_OFFSET);
    }
}