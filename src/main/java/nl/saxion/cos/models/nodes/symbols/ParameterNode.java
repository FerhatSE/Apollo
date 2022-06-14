package nl.saxion.cos.models.nodes.symbols;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.models.scopes.Scope;

/**
 * Meant for storing information about the defined parameters
 * Does not store a value nor does it produce jasminByteCode
 */
public class ParameterNode extends SymbolNode {

    private final String identifier;

    private final DataType dataType;

    private int index;

    public ParameterNode(String identifier, DataType dataType, Scope scope) {
        super(identifier, dataType, scope);
        this.identifier = identifier;
        this.dataType = dataType;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
    }

    @Override
    public void checkType(Scope scope) {
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    public void declare(Scope scope) {
        index = scope.getVariableCount();
        scope.declare(identifier, this);
    }

    public String getIdentifier() {
        return identifier;
    }
}
