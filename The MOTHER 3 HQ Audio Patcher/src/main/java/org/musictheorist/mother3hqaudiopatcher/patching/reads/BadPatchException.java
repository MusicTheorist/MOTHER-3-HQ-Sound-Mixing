package org.musictheorist.mother3hqaudiopatcher.patching.reads;

import org.musictheorist.mother3hqaudiopatcher.ROMException;

public final class BadPatchException extends ROMException {
    @Override
    public String getLocalizedMessage() {
        return "infoNoChanges";
    }
}