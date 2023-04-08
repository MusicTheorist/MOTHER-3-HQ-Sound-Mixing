package org.musictheorist.mother3hqaudiopatcher.rom.sound.engine;

final class NullTrackPlayersException extends IllegalStateException {
    @Override
    public String getLocalizedMessage() {
        return "errorNullTrackPlayerAddresses";
    }
}
