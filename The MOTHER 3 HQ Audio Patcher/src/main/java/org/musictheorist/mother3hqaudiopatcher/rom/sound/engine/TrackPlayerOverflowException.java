package org.musictheorist.mother3hqaudiopatcher.rom.sound.engine;

public final class TrackPlayerOverflowException extends IllegalArgumentException {
    @Override
    public String getLocalizedMessage() {
        return "errorTrackPlayerOverflow";
    }
}
