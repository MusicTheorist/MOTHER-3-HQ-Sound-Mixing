package org.musictheorist.mother3hqaudiopatcher.resources.fetchers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.musictheorist.mother3hqaudiopatcher.resources.Mixers;
import org.musictheorist.mother3hqaudiopatcher.rom.metadata.Lengths;

public final class MixerFetcher {
    private final String classPath;

    public MixerFetcher(Mixers mixer) {
        this.classPath = mixer.classPath();
    }

    public byte[] getMixer() throws IOException {
        byte[] m4aHQMixer;
        try (InputStream input = MixerFetcher.class.getResourceAsStream(classPath);
             BufferedInputStream bufferedInput = new BufferedInputStream(input)) {
             m4aHQMixer = bufferedInput.readAllBytes();
        }
        return m4aHQMixer;
    }

    public static byte[] newZeroedOutMixer() {
        return new byte[Lengths.FREE_SPACE_LENGTH + 1];
    }
    public static byte[] newNulledOutMixer() {
        byte[] nullMixer = newZeroedOutMixer();
        Arrays.fill(nullMixer, (byte) 0xFF);
        return nullMixer;
    }
}