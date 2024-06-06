package ru.modgy.owner.controller;

public enum SearchDirection {
    NAME("name"),
    PHONE("phone");

    private final String title;

    SearchDirection(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static SearchDirection fromString(String title) {
        for (var d : SearchDirection.values()) {
            if (d.title.equalsIgnoreCase(title)) {
                return d;
            }
        }
        return null;
    }
}
