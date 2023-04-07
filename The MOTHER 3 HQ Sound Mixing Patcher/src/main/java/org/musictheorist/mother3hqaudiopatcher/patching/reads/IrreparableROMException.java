package org.musictheorist.mother3hqaudiopatcher.patching.reads;

import org.musictheorist.mother3hqaudiopatcher.ROMException;

public final class IrreparableROMException extends ROMException {
    @Override
    public String getLocalizedMessage() {
        return "errorIrreparableROM";
    }
}