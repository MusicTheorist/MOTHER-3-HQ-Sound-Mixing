package org.musictheorist.mother3hqaudiopatcher.rom.sound.engine;

public final class TrackPlayerUnderflowException extends IllegalArgumentException {
    @Override
    public String getLocalizedMessage() {
        return "errorTrackPlayerUnderflow";
    }
}
