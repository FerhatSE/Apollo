package nl.saxion.cos.models.nodes.symbols;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.scopes.Scope;

public class InsertNode extends SymbolNode {

    private final int index;

    public InsertNode(DataType dataType, Scope scope) {
        super("scanner", dataType, scope);
        this.index = scope.getVariableCount();
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
    }

    @Override
    public void checkType(Scope scope) {
    }
}
