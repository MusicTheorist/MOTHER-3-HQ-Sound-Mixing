package org.musictheorist.mother3hqaudiopatcher.rom.sound.engine;

final class InvalidTrackPlayerException extends IllegalArgumentException {
    @Override
    public String getLocalizedMessage() {
        return "errorInvalidTrackPlayerAddress";
    }
}
