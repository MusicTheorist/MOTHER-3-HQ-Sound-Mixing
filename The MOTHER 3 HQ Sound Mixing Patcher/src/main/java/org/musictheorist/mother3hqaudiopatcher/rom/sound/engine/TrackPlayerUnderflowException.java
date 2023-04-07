package org.musictheorist.mother3hqaudiopatcher.rom.sound.engine;

final class TrackPlayerUnderflowException extends IllegalArgumentException {
    @Override
    public String getLocalizedMessage() {
        return "errorTrackPlayerUnderflow";
    }
}
