package org.musictheorist.mother3hqaudiopatcher.rom.sound.engine;

final class TrackPlayerOverflowException extends IllegalArgumentException {
    @Override
    public String getLocalizedMessage() {
        return "errorTrackPlayerOverflow";
    }
}
