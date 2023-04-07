package org.musictheorist.mother3hqaudiopatcher.rom.sound;

final class InvalidMixerSizeException extends IllegalArgumentException {
    @Override
    public String getLocalizedMessage() {
        return "errorInvalidMixerSize";
    }
}
