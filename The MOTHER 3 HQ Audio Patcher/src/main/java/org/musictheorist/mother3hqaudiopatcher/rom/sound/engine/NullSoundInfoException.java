package org.musictheorist.mother3hqaudiopatcher.rom.sound.engine;

final class NullSoundInfoException extends IllegalStateException {
    @Override
    public String getLocalizedMessage() {
        return "errorNullSoundInfoAddress";
    }
}
