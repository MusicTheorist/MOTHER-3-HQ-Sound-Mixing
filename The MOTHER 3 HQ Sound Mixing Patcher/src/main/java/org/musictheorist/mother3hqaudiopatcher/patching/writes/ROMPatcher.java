package org.musictheorist.mother3hqaudiopatcher.patching.writes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.musictheorist.mother3hqaudiopatcher.patching.Patch;
import org.musictheorist.mother3hqaudiopatcher.patching.PatchIO;
import org.musictheorist.mother3hqaudiopatcher.rom.rng.BattlePointers;
import org.musictheorist.mother3hqaudiopatcher.rom.rng.GameEngines;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.SampleRates;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.TrackPlayers;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.SoundEngine;

public abstract sealed class ROMPatcher permits JapanROMPatcher, FanROMPatcher, UndoPatcher {
    final ByteBuffer buffer;
    final FileChannel romChannel;

    Patch patch;

    ROMPatcher(PatchIO patchIO) {
        this.buffer = patchIO.buffer();
        this.romChannel = patchIO.romChannel();
    }

    private void checkNullPatch() {
        if(patch == null) {
            throw new WriteNullPatchException();
        }
    }

    public final Patch getPatch() {
        checkNullPatch();
        return patch;
    }

    final void patch(byte[] bytesToPatch, long address) throws IOException {
        buffer.put(bytesToPatch).flip();
        romChannel.position(address);
        do {
            romChannel.write(buffer);
        } while(buffer.hasRemaining());
        buffer.clear();
    }

    public final void patchROM() throws IOException {
        checkNullPatch();

        SoundEngine newAudio = patch.soundEngine();
        GameEngines newBattleRNG = patch.battleRNG();

        byte[] rngPointer = newBattleRNG.ramPointer();
        patch(rngPointer, BattlePointers.RNG_ONE.offset());
        patch(rngPointer, BattlePointers.RNG_TWO.offset());

        patch(newAudio.m4aHQMixer(), newAudio.mixerROMOffset());
        patch(newAudio.soundInfoRAMAddress(), SoundEngine.SOUND_INFO_POINTER);
        patch(newAudio.mixerROMAddress(), SoundEngine.MIXER_ROM_POINTER);
        patch(newAudio.mixerCpuSetAddress(false), SoundEngine.MIXER_CPUSET_POINTER);
        patch(newAudio.mixerSize(), SoundEngine.MIXER_CPUSET_SIZE_OFFSET);
        patch(newAudio.mixerCpuSetAddress(true), SoundEngine.MIXER_RAM_BRANCH);

        patch(newAudio.trackPlayerAddress( 1), TrackPlayers.ONE.romPointer());
        patch(newAudio.trackPlayerAddress( 2), TrackPlayers.TWO.romPointer());
        patch(newAudio.trackPlayerAddress( 3), TrackPlayers.THREE.romPointer());
        patch(newAudio.trackPlayerAddress( 4), TrackPlayers.FOUR.romPointer());
        patch(newAudio.trackPlayerAddress( 5), TrackPlayers.FIVE.romPointer());
        patch(newAudio.trackPlayerAddress( 6), TrackPlayers.SIX.romPointer());
        patch(newAudio.trackPlayerAddress( 7), TrackPlayers.SEVEN.romPointer());
        patch(newAudio.trackPlayerAddress( 8), TrackPlayers.EIGHT.romPointer());
        patch(newAudio.trackPlayerAddress( 9), TrackPlayers.NINE.romPointer());
        patch(newAudio.trackPlayerAddress(10), TrackPlayers.TEN.romPointer());

        patch(new byte[] {newAudio.sampleRate()}, SoundEngine.SAMPLE_RATE_OFFSET);
    }

    public abstract void initPatch(byte[] m4aHQMixer, int mixerROMAddress, SampleRates sampleRate);
}