package org.musictheorist.mother3hqaudiopatcher.patching;

import org.musictheorist.mother3hqaudiopatcher.rom.rng.GameEngines;
import org.musictheorist.mother3hqaudiopatcher.rom.sound.engine.SoundEngine;

public record Patch (GameEngines battleRNG, SoundEngine soundEngine) {}