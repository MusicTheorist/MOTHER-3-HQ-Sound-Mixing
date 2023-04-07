package org.musictheorist.mother3hqaudiopatcher.rom.metadata;

public final class Header {
    public static final byte[] ROM_ENTRY_POINT = {(byte) 0x2E, (byte) 0x00, (byte) 0x00, (byte) 0xEA};

    public static final byte[] GAME_TITLE_AND_CODE = {(byte) 0x4D, (byte) 0x4F, (byte) 0x54, (byte) 0x48, (byte) 0x45, (byte) 0x52, (byte) 0x33,
                                                      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                                      (byte) 0x41, (byte) 0x33, (byte) 0x55, (byte) 0x4A};
    public static final long GAME_TITLE_AND_CODE_OFFSET = 0xA0;

    public static final byte[] MAKER_CODE_AND_CHECKSUM = {(byte) 0x30, (byte) 0x31, (byte) 0x96, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                                          (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xDB, (byte) 0x00, (byte) 0x00};
    public static final long MAKER_CODE_AND_CHECKSUM_OFFSET = 0xB0;

    private Header() {};
}