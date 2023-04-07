package org.musictheorist.mother3hqaudiopatcher.patching.reads;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.musictheorist.mother3hqaudiopatcher.rom.metadata.Lengths;

public final class UnusedByteScanner {
    private UnusedByteScanner() {}

    private static long checkForUsedBytes(FileChannel romAccess, ByteBuffer buffer, long address, byte unused) throws IOException {
        BufferReads.intoBuffer(buffer, Lengths.FREE_SPACE_LENGTH, romAccess, address);

        for(int i = 0; i < Lengths.FREE_SPACE_LENGTH; i += 4) {
            for(int j = 0; j < 4; j++) {
                if(buffer.get() != unused) {
                    buffer.clear();
                    return address + i;
                }
            }
        }

        buffer.clear();
        return -1;
    }

    private static boolean isByteUnused(RandomAccessFile mother3ROM, long address, byte unused) throws IOException {
        mother3ROM.seek(address);
        return ((byte) mother3ROM.read()) == unused;
    }

    public static long scan(RandomAccessFile mother3ROM, ByteBuffer buffer, long startAddress, UnusedBytes unusedByte) throws IOException {
        long  lastByte = startAddress - 4;
        long firstByte = lastByte - Lengths.FREE_SPACE_LENGTH;

        byte unused = unusedByte.value();

        do {
            boolean  lastUnused = isByteUnused(mother3ROM,  lastByte, unused);
            boolean firstUnused = isByteUnused(mother3ROM, firstByte, unused);

            if(firstUnused) {
                if(lastUnused) {
                    long usedBytes = checkForUsedBytes(mother3ROM.getChannel(), buffer, firstByte, unused);
                    if(usedBytes == -1) {
                        return firstByte;
                    }
                    else {
                         lastByte = usedBytes - (usedBytes != firstByte ? 4 : 0);
                        firstByte = lastByte  - Lengths.FREE_SPACE_LENGTH;
                    }
                }
                else {
                     lastByte -= 4;
                    firstByte -= 4;
                }
            }
            else {
                 lastByte = firstByte - 4;
                firstByte =  lastByte - Lengths.FREE_SPACE_LENGTH;
            }
        } while(firstByte > -1);

        return -1;
    }
}