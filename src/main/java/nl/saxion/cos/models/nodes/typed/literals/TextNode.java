package nl.saxion.cos.models.nodes.typed.literals;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.scopes.Scope;

import static nl.saxion.cos.DataType.TEXT;

public class TextNode extends TypedNode {

    private final String value;

    public TextNode(String value) {
        this.value = value;
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
        return TEXT;
    }
}
