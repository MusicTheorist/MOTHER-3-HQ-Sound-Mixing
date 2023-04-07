package org.musictheorist.mother3hqaudiopatcher.resources.images;

import java.util.EnumSet;

public enum Alerts {
    CONFIRM("confirm"),
    ERROR("error"),
    INFO("info"),
    NONE("none"),
    WARNING("warning");

    private final String icon;
    private static final String[] classPaths = new String[Alerts.values().length];

    private Alerts(String icon) {
        this.icon = icon;
    }

    static {
        int i = 0;
        for(Alerts alert : EnumSet.allOf(Alerts.class)) {
            classPaths[i] = "/icon/alert/" + alert.icon + ".png";
            i++;
        }
    }

    public static String[] classPaths() {
        return classPaths;
    }
}