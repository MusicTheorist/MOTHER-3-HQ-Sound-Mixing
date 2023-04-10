package org.musictheorist.mother3hqaudiopatcher.patching.reads;

public final class ReadNullPatchException extends IllegalStateException {
    @Override
    public String getLocalizedMessage() {
        return "errorReadNullPatch";
    }
}