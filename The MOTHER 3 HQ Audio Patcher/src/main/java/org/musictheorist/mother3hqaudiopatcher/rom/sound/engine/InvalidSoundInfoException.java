package org.musictheorist.mother3hqaudiopatcher.rom.sound.engine;

public final class InvalidSoundInfoException extends IllegalArgumentException {
    @Override
    public String getLocalizedMessage() {
        return "errorInvalidSoundInfoAddress";
    }
}
