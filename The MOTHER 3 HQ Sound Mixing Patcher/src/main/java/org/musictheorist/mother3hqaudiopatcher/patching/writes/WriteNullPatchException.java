package org.musictheorist.mother3hqaudiopatcher.patching.writes;

final class WriteNullPatchException extends IllegalStateException {
    @Override
    public String getLocalizedMessage() {
        return "errorWriteNullPatch";
    }
}