package org.musictheorist.mother3hqaudiopatcher.rom.sound.engine;

public final class NullSoundInfoException extends IllegalStateException {
    @Override
    public String getLocalizedMessage() {
        return "errorNullSoundInfoAddress";
    }
}
