package org.musictheorist.mother3hqaudiopatcher.patching.reads;

import org.musictheorist.mother3hqaudiopatcher.ROMException;

public final class BadSizeException extends ROMException {
    @Override
    public String getLocalizedMessage() {
        return "errorBadSize";
    }
}