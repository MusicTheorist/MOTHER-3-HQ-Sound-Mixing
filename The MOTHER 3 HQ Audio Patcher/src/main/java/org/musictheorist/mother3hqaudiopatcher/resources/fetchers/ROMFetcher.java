package org.musictheorist.mother3hqaudiopatcher.resources.fetchers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ROMFetcher {
    private final RandomAccessFile mother3ROM;
    private final Path romPath;

    public ROMFetcher(File file) throws FileNotFoundException {
        this.mother3ROM = new RandomAccessFile(file, "rw");
        this.romPath = Paths.get(file.getAbsolutePath());
    }

    public RandomAccessFile getROM() {
        return mother3ROM;
    }
    public Path getROMPath() {
        return romPath;
    }
}