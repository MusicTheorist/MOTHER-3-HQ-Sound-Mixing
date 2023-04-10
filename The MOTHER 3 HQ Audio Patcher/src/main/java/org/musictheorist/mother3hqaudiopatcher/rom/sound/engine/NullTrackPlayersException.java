package org.musictheorist.mother3hqaudiopatcher.rom.sound.engine;

public final class NullTrackPlayersException extends IllegalStateException {
    @Override
    public String getLocalizedMessage() {
        return "errorNullTrackPlayerAddresses";
    }
}
