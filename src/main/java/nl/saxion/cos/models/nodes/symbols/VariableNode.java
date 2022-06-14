package nl.saxion.cos.models.nodes.symbols;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.exceptions.VariableNotFoundException;
import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.scopes.Scope;

import static nl.saxion.cos.DataType.getMnemonic;

public class VariableNode extends TypedNode {

    private final String identifier;

    private TypedNode variable;

    public VariableNode(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        var symbol = (SymbolNode) variable;
        jasminBytecode.add(getMnemonic(getDataType()) + "load " + symbol.getIndex());
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        this.variable = scope.lookup(identifier);

        if (variable == null) {
            throw new VariableNotFoundException("Variable not found within scope");
        }
    }

    @Override
    public DataType getDataType() {
        return variable.getDataType();
    }

    @Override
    public String toString() {
        return "VariableNode{" +
                "identifier='" + identifier + '\'' +
                ", variable=" + variable +
                '}';
    }
}


