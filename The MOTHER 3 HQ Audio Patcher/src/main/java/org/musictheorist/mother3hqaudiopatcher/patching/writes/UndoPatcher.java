package org.musictheorist.mother3hqaudiopatcher.patching.writes;

import org.musictheorist.mother3hqaudiopatcher.patching.Patch;
import org.musictheorist.mother3hqaudiopatcher.patching.PatchIO;
import org.musictheorist.mother3hqaudiopatcher.rom.rng.GameEngines;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.SampleRates;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.TrackPlayers;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.SoundEngine;

public final class UndoPatcher extends ROMPatcher {
    public UndoPatcher(PatchIO patchIO) {
        super(patchIO);
    }

    @Override
    public void initPatch(byte[] m4aHQMixer, int mixerROMAddress, SampleRates sampleRate) {
        if(mixerROMAddress == SoundEngine.OLD_MIXER_DEFAULT_ROM_OFFSET) {
            throw new UndoDefaultMixerException();
        }

        GameEngines newBattleRNG = GameEngines.BATTLE;
        SoundEngine soundEngine = new SoundEngine.Builder(m4aHQMixer, mixerROMAddress, SoundEngine.OLD_MIXER_DEFAULT_ROM_OFFSET, sampleRate)
                                                 .mixerCpuSetAddress(SoundEngine.OLD_MIXER_DEFAULT_RAM_ADDRESS)
                                                 .soundInfoRAMAddress(SoundEngine.SOUND_INFO_DEFAULT_RAM_ADDRESS)
                                                 .repointedTrackPlayers(TrackPlayers.ONE.defaultAddress(),
                                                                        TrackPlayers.TWO.defaultAddress(),
                                                                        TrackPlayers.THREE.defaultAddress(),
                                                                        TrackPlayers.FOUR.defaultAddress(),
                                                                        TrackPlayers.FIVE.defaultAddress(),
                                                                        TrackPlayers.SIX.defaultAddress(),
                                                                        TrackPlayers.SEVEN.defaultAddress(),
                                                                        TrackPlayers.EIGHT.defaultAddress(),
                                                                        TrackPlayers.NINE.defaultAddress(),
                                                                        TrackPlayers.TEN.defaultAddress()).build();
        this.patch = new Patch(newBattleRNG, soundEngine);
    }
}