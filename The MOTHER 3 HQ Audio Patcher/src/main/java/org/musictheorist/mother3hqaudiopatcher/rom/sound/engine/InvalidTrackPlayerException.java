package org.musictheorist.mother3hqaudiopatcher.rom.sound.engine;

public final class InvalidTrackPlayerException extends IllegalArgumentException {
    @Override
    public String getLocalizedMessage() {
        return "errorInvalidTrackPlayerAddress";
    }
}
