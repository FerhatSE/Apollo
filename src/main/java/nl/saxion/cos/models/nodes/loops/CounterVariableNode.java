package nl.saxion.cos.models.nodes.loops;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.scopes.Scope;

import static nl.saxion.cos.DataType.INTEGER;

// Meant to store a counter in the scope for the repeat-loop
public class CounterVariableNode extends TypedNode {

    @Override
    public DataType getDataType() {
        return INTEGER;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
    }

    @Override
    public void checkType(Scope scope) {
    }
}
