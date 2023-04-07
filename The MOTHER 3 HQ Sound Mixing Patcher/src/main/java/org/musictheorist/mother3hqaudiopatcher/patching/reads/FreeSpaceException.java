package org.musictheorist.mother3hqaudiopatcher.patching.reads;

import org.musictheorist.mother3hqaudiopatcher.ROMException;

public final class FreeSpaceException extends ROMException {
    public FreeSpaceException(boolean canceled) {
        super(canceled);
    }

    @Override
    public String getLocalizedMessage() {
        return canceled ? "infoNoChanges" : "infoNotEnoughFreeSpace";
    }
}