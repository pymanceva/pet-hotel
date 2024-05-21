package ru.modgy.utility;

public final class UpdateField {
    private UpdateField() {
    }

    public static String stringField(String oldField, String newField) {
        return newField == null ? oldField : newField;
    }

    public static Integer intField(Integer oldField, Integer newField) {
        return newField == null ? oldField : newField;
    }
}
