package org.musictheorist.mother3hqaudiopatcher.patching.writes;

import org.musictheorist.mother3hqaudiopatcher.patching.Patch;
import org.musictheorist.mother3hqaudiopatcher.patching.PatchIO;
import org.musictheorist.mother3hqaudiopatcher.rom.rng.GameEngines;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.SampleRates;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.TrackPlayers;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.SoundEngine;

public final class JapanROMPatcher extends ROMPatcher {
    public JapanROMPatcher(PatchIO patchIO) {
        super(patchIO);
    }

    @Override
    public void initPatch(byte[] m4aHQMixer, int mixerROMAddress, SampleRates sampleRate) {
        GameEngines battleRNG = GameEngines.BATTLE;
        SoundEngine soundEngine = new SoundEngine.Builder(m4aHQMixer, mixerROMAddress, sampleRate)
                                                 .mixerCpuSetAddress(SoundEngine.SOUND_INFO_DEFAULT_RAM_ADDRESS + 0x40)
                                                 .soundInfoRAMAddress(0x0203E000)
                                                 .repointedTrackPlayers(0x030023F0,
                                                                        0x03002710,
                                                                        0x03002A30,
                                                                        TrackPlayers.FOUR.defaultAddress()  + 0x40,
                                                                        TrackPlayers.FIVE.defaultAddress()  + 0x40,
                                                                        TrackPlayers.SIX.defaultAddress()   + 0x40,
                                                                        TrackPlayers.SEVEN.defaultAddress() + 0x40,
                                                                        TrackPlayers.EIGHT.defaultAddress() + 0x40,
                                                                        TrackPlayers.NINE.defaultAddress()  + 0x40,
                                                                        TrackPlayers.TEN.defaultAddress()   + 0x40).build();
        this.patch = new Patch(battleRNG, soundEngine);
    }
}