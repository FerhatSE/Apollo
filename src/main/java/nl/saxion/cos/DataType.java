package nl.saxion.cos;

public enum DataType {
    INTEGER, TEXT, BOOLEAN, DOUBLE, VOID;

    public static DataType getTypeByString(String type) {
        return switch (type.toLowerCase()) {
            case "text" -> TEXT;
            case "boolean" -> BOOLEAN;
            case "double" -> DOUBLE;
            case "void" -> VOID;
            default -> INTEGER;
        };
    }

    public static String getMnemonic(DataType type) {
        return switch (type) {
            case TEXT -> "a";
            case DOUBLE -> "d";
            case VOID -> "";
            default -> "i";
        };
    }

    public static String getTypeDescriptor(DataType type) {
        return switch (type) {
            case TEXT -> "Ljava/lang/String;";
            case DOUBLE -> "D";
            case BOOLEAN -> "Z";
            case VOID -> "V";
            default -> "I";
        };
    }
}
