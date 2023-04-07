package org.musictheorist.mother3hqaudiopatcher.patching.reads;

public enum UnusedBytes {
    ZERO((byte) 0x00),
    NULL((byte) 0xFF);

    private final byte val;

    private UnusedBytes(byte val) {
        this.val = val;
    }

    public byte value() {
        return val;
    }

    static UnusedBytes get(byte val) throws IrreparableROMException {
        if(val == UnusedBytes.ZERO.val) {
            return UnusedBytes.ZERO;
        }
        else if(val == UnusedBytes.NULL.val) {
            return UnusedBytes.NULL;
        }
        else {
            // happens when this hack's mixer is overwritten if it was at
            // a memory address other than the patch's default
            //
            // this patcher uses the byte after the end of the mixer to
            // determine which bytes were overwritten in case the mixer
            // was moved to possible unused space to make room for another
            // ROM hack
            //
            // if that byte is overwritten, the patcher will have no way
            // to know which unused bytes were originally at the mixer's
            // location
            throw new IrreparableROMException();
        }
    }
    static boolean isZero(byte val) {
        return val == UnusedBytes.ZERO.val;
    }
}