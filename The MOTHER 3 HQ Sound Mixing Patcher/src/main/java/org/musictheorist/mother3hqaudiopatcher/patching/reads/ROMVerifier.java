package org.musictheorist.mother3hqaudiopatcher.patching.reads;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.musictheorist.mother3hqaudiopatcher.ROMException;
import org.musictheorist.mother3hqaudiopatcher.patching.PatchIO;

abstract sealed class ROMVerifier permits MetadataVerifier, PatchVerifier {
    final ByteBuffer buffer;
    final FileChannel romChannel;

    ROMVerifier(PatchIO patchIO) {
        this.buffer = patchIO.buffer();
        this.romChannel = patchIO.romChannel();
    }

    final void verify(byte[] bytesToCheck, long address) throws IOException, ROMException {
        BufferReads.intoBuffer(buffer, bytesToCheck.length, romChannel, address);
        for (int i = 0; i < bytesToCheck.length; i++) {
            byte romByte = buffer.get();
            if(romByte != bytesToCheck[i]) {
                buffer.clear();
                throw new ROMException();
            }
        }
        buffer.clear();
    }

    public abstract void verifyData() throws IOException, ROMException;
}