package org.musictheorist.mother3hqaudiopatcher.patching.writes;

import org.musictheorist.mother3hqaudiopatcher.patching.Patch;
import org.musictheorist.mother3hqaudiopatcher.patching.PatchIO;
import org.musictheorist.mother3hqaudiopatcher.rom.rng.GameEngines;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.SampleRates;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.SoundEngine;

public final class FanROMPatcher extends ROMPatcher {
    public FanROMPatcher(PatchIO patchIO) {
        super(patchIO);
    }

    @Override
    public void initPatch(byte[] m4aHQMixer, int mixerROMAddress, SampleRates sampleRate) {
        GameEngines newBattleRNG = GameEngines.OVERWORLD;
        SoundEngine soundEngine = new SoundEngine.Builder(m4aHQMixer, mixerROMAddress, sampleRate)
                                                 .mixerCpuSetAddress(0x03001C00)
                                                 .soundInfoRAMAddress(0x03000C50)
                                                 .repointedTrackPlayers(0x03002490,
                                                                        0x030027B0,
                                                                        0x03000930,
                                                                        0x02001090,
                                                                        0x03000570,
                                                                        0x030007F0,
                                                                        0x020011D0,
                                                                        0x030009D0,
                                                                        0x030004D0,
                                                                        0x020016D0).build();
        this.patch = new Patch(newBattleRNG, soundEngine);
    }
}