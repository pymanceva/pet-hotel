package ru.modgy.owner.controller;

public enum Direction {
    NAME("name"),
    PHONE("phone");

    private final String title;

    Direction(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static Direction fromString(String title) {
        for (var d : Direction.values()) {
            if (d.title.equalsIgnoreCase(title)) {
                return d;
            }
        }
        return null;
    }
}
