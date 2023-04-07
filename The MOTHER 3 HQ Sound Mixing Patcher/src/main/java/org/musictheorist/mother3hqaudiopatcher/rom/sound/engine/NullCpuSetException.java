package org.musictheorist.mother3hqaudiopatcher.rom.sound.engine;

final class NullCpuSetException extends IllegalStateException {
    @Override
    public String getLocalizedMessage() {
        return "errorNullCpuSet";
    }
}
