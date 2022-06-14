package nl.saxion.cos.models.nodes.typed.literals;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.scopes.Scope;

import static nl.saxion.cos.DataType.INTEGER;

public class IntNode extends TypedNode {

    private final int value;

    public IntNode(String value) {
        this.value = Integer.parseInt(value);
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        jasminBytecode.add("ldc " + value);
    }

    @Override
    public void checkType(Scope scope) {
    }

    @Override
    public DataType getDataType() {
        return INTEGER;
    }
}
