package org.musictheorist.mother3hqaudiopatcher.patching;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public final class PatchIO {
    private final RandomAccessFile mother3ROM;
    private final FileChannel romChannel;
    private final ByteBuffer buffer;

    private FileLock romLock;

    public PatchIO(RandomAccessFile mother3ROM, ByteBuffer buffer) {
        this.mother3ROM = mother3ROM;
        this.romChannel = mother3ROM.getChannel();
        this.buffer = buffer;
    }

    public RandomAccessFile mother3ROM() {
        return mother3ROM;
    }
    public FileChannel romChannel() {
        return romChannel;
    }
    public ByteBuffer buffer() {
        return buffer;
    }

    public void lockROM(boolean shared) throws IOException {
        romLock = romChannel.tryLock(0L, Long.MAX_VALUE, shared);
    }
    public void unlockROM() throws IOException {
        if(romLock != null) romLock.release();
    }

    public void closeResources() throws IOException {
        buffer.clear();
        unlockROM();
        mother3ROM.close();
    }
}