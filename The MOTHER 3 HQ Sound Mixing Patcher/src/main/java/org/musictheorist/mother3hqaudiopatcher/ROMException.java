package org.musictheorist.mother3hqaudiopatcher;

import org.musictheorist.mother3hqaudiopatcher.patching.reads.BadHeaderException;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.BadSizeException;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.BadPatchException;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.FreeSpaceException;
import org.musictheorist.mother3hqaudiopatcher.patching.reads.IrreparableROMException;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.CustomPatchException;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.CustomRemovalException;
import org.musictheorist.mother3hqaudiopatcher.patching.writes.ROMRepairException;

public sealed class ROMException extends Exception
    permits BadHeaderException, BadSizeException,
            BadPatchException, FreeSpaceException,
            ROMRepairException, IrreparableROMException,
            CustomPatchException, CustomRemovalException {

    protected boolean canceled;

    public ROMException() {
        super();
        this.canceled = false;
    }
    public ROMException(boolean canceled) {
        super();
        this.canceled = canceled;
    }
    public ROMException(Throwable cause) {
        super(cause);
    }

    public boolean wasTaskCanceled() {
        return canceled;
    }
}