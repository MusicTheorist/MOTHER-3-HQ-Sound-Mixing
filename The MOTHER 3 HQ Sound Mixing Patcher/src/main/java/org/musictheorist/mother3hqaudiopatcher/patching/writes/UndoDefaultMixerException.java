package org.musictheorist.mother3hqaudiopatcher.patching.writes;

final class UndoDefaultMixerException extends IllegalArgumentException {
    @Override
    public String getLocalizedMessage() {
        return "errorUndoDefaultMixer";
    }
}