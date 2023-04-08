package org.musictheorist.mother3hqaudiopatcher.patching.reads;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.musictheorist.mother3hqaudiopatcher.patching.Conversions;

public final class BufferReads {
    private BufferReads() {}

    static void intoBuffer(ByteBuffer buffer, int length, FileChannel romChannel, long address) throws IOException {
        buffer.limit(length);
        romChannel.position(address);
        do {
            romChannel.read(buffer);
        } while(buffer.hasRemaining());
        buffer.flip();
    }

    static byte[] intoArray(ByteBuffer buffer, int length, FileChannel romChannel, long address) throws IOException {
        BufferReads.intoBuffer(buffer, length, romChannel, address);
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes).clear();
        return bytes;
    }

    static int intoInt(ByteBuffer buffer, int length, FileChannel romChannel, long address, boolean romAddress) throws IOException {
        return Conversions.getInt(BufferReads.intoArray(buffer, length, romChannel, address), romAddress);
    }
}