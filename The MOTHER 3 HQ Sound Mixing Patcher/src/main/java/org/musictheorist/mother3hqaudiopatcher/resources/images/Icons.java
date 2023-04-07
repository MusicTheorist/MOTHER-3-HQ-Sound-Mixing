package org.musictheorist.mother3hqaudiopatcher.resources.images;

import java.util.EnumSet;

public enum Icons {
    ICON_01( 16),
    ICON_02( 20),
    ICON_03( 24),
    ICON_04( 32),
    ICON_05( 40),
    ICON_06( 48),
    ICON_07( 60),
    ICON_08( 64),
    ICON_09( 96),
    ICON_10(128),
    ICON_11(256);

    private final int size;
    private static final String[] classPaths = new String[Icons.values().length];

    private Icons(int size) {
        this.size = size;
    }

    static {
        int i = 0;
        for(Icons icon : EnumSet.allOf(Icons.class)) {
            classPaths[i] = "/icon/app/icon_" + icon.size + "x" + icon.size + ".png";
            i++;
        }
    }

    public static String[] classPaths() {
        return classPaths;
    }
}