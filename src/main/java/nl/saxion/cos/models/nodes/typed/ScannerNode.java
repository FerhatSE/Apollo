package nl.saxion.cos.models.nodes.typed;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.models.nodes.symbols.InsertNode;
import nl.saxion.cos.models.scopes.Scope;

public class ScannerNode extends TypedNode {

    private final DataType type;

    private InsertNode scanner;

    private boolean newScanner;

    public ScannerNode(String type) {
        this.type = extractDataType(type);
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        if (newScanner) {
            jasminBytecode.add("new java/util/Scanner")
                    .add("dup")
                    .add("getstatic java/lang/System/in Ljava/io/InputStream;")
                    .add("invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V")
                    .add("astore " + scanner.getIndex());
        }
        jasminBytecode.add("aload " + scanner.getIndex());
        jasminBytecode.add("invokevirtual java/util/Scanner/" + getScannerByType());
    }

    @Override
    public void checkType(Scope scope) {
        this.scanner = (InsertNode) scope.lookup("scanner");

        if (scanner == null) {
            var scannerNode = new InsertNode(getDataType(), scope);
            scope.declare("scanner", scannerNode);
            this.scanner = scannerNode;
            this.newScanner = true;
        }
    }

    public DataType extractDataType(String command) {
        // Extracts the datatype from the scanner command
        String type = command.substring(6, (command.length() - 2));
        return DataType.getTypeByString(type);
    }

    @Override
    public DataType getDataType() {
        return type;
    }

    public String getScannerByType() {
        return switch (type) {
            case TEXT -> "next()Ljava/lang/String;";
            case BOOLEAN -> "nextBoolean()Z";
            case DOUBLE -> "nextDouble()D";
            default -> "nextInt()I";
        };
    }
}
