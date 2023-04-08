package org.musictheorist.mother3hqaudiopatcher.resources.fetchers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class SourceFetcher {
    private static final String classPath = "/mixer/src/m4a_hq_mixer.s";

    private SourceFetcher() {}

    public static String getMixerCode() throws IOException {
        try (InputStream input = SourceFetcher.class.getResourceAsStream(classPath);
             BufferedInputStream bufferedInput = new BufferedInputStream(input)) {
            return new String(bufferedInput.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}