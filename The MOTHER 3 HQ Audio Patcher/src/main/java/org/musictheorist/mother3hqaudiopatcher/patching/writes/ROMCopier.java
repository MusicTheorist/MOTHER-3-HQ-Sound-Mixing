package org.musictheorist.mother3hqaudiopatcher.patching.writes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ROMCopier {
    private static final String FULL_EXTENSION = ".gba.bak";

    private ROMCopier() {}

    public static String saveBackup(Path romPath) throws IOException {
        Path backupPath = Paths.get(romPath.toString() + ".bak");

        if(Files.exists(backupPath)) {
            String backupName = backupPath.getFileName().toString();
            String fileName = backupName.substring(0, backupName.length() - FULL_EXTENSION.length());
            String filePath = backupPath.getParent().toString() + "/" + fileName;

            Path newBackup;
            int i = 2;
            do {
                newBackup = Paths.get(filePath + " (" + i + ")" + FULL_EXTENSION);
                i++;
            } while(Files.exists(newBackup));

            backupPath = newBackup;
        }

        Files.copy(romPath, backupPath);
        return backupPath.toString();
    }
}