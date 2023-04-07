package org.musictheorist.mother3hqaudiopatcher.patching;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class Conversions {
    private static final long GBA_ROM_BASE = 0x08000000;

    private Conversions() {}

    private static ByteBuffer putVal(ByteBuffer buffer, int value, boolean isPointer) {
        if(isPointer) return buffer.putInt(value);
        else          return buffer.putShort((short) value);
    }

    private static byte[] getBytes(int input, boolean isPointer, boolean isROMAddress) {
        int numBytes = isPointer ? 4 : 2;
        byte[] bytes = new byte[numBytes];

        ByteBuffer buffer = ByteBuffer.allocate(numBytes).order(ByteOrder.LITTLE_ENDIAN);

        int value = (int) (input + (isROMAddress ? GBA_ROM_BASE : 0));
        putVal(buffer, value, isPointer).flip().get(bytes);

        if(isROMAddress) bytes[0] |= 0x01;
        return bytes;
    }

    public static byte[] getPointer(int offset, boolean isROMAddress) {
        return getBytes(offset, true, isROMAddress);
    }
    public static byte[] getPointer(long offset, boolean isROMAddress) {
        return getBytes((int) offset, true, isROMAddress);
    }

    public static byte[] getCpuSetSize(short transferUnits) {
        return getBytes(transferUnits, false, false);
    }

    public static long getLong(byte[] address, boolean isROMAddress) {
        return (ByteBuffer.wrap(address).order(ByteOrder.LITTLE_ENDIAN).getInt() & ~1) - (isROMAddress ? GBA_ROM_BASE : 0);
    }
    public static int getInt(byte[] address, boolean isROMAddress) {
        return (int) getLong(address, isROMAddress);
    }

    public static String getString(long address) {
        String hexString = Long.toHexString(address).toUpperCase();
        StringBuilder fullAddress = new StringBuilder(hexString);
        while(fullAddress.length() < 8) {
            fullAddress.insert(0, 0);
        }
        fullAddress.insert(0, "0x");
        return fullAddress.toString();
    }
}