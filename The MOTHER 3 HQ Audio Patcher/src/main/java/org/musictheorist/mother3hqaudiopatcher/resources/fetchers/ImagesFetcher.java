package org.musictheorist.mother3hqaudiopatcher.resources.fetchers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.musictheorist.mother3hqaudiopatcher.resources.images.Alerts;
import org.musictheorist.mother3hqaudiopatcher.resources.images.Icons;

import javafx.scene.image.Image;

public final class ImagesFetcher {
    private static final String GLOBE_CLASSPATH = "/icon/globe.png";

    final String[] classPaths;

    ImagesFetcher(String classPath) {
        this.classPaths = new String[] {classPath};
    }
    ImagesFetcher(String[] classPaths) {
        this.classPaths = classPaths;
    }

    public final ArrayList<Image> getImages() throws IOException {
        ArrayList<Image> images = new ArrayList<Image>();
        for(String classPath : classPaths) {
            try(InputStream input = ImagesFetcher.class.getResourceAsStream(classPath);
                BufferedInputStream bufferedInput = new BufferedInputStream(input)) {
                Image image = new Image(bufferedInput);
                images.add(image);
            }
        }
        return images;
    }

    public static ImagesFetcher newAlertsFetcher() {
        return new ImagesFetcher(Alerts.classPaths());
    }

    public static ImagesFetcher newGlobeFetcher() {
        return new ImagesFetcher(GLOBE_CLASSPATH);
    }

    public static ImagesFetcher newIconsFetcher() {
        return new ImagesFetcher(Icons.classPaths());
    }
}