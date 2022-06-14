package nl.saxion.cos.models.nodes.symbols;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.IdentifierAlreadyExistsException;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.scopes.Scope;

import static nl.saxion.cos.DataType.getMnemonic;

public class DeclarationNode extends SymbolNode {

    private int index;

    public DeclarationNode(String identifier, DataType dataType, Scope scope) {
        super(identifier, dataType, scope);
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        this.index = scope.getVariableCount();
        scope.declare(getIdentifier(), this);

        // Default declaration of an integer
        jasminBytecode.add(getLDC());
        jasminBytecode.add(getMnemonic(getDataType()) + "store " + index);
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        var variable = getScope().lookup(getIdentifier());

        if (variable != null) {
            throw new IdentifierAlreadyExistsException("Variable already exists within the scope.");
        }
    }

    @Override
    public int getIndex() {
        return index;
    }

    public String getLDC() {
        return switch (getDataType()) {
            case DOUBLE -> "ldc2_w 0.00";
            case TEXT -> "ldc" + " empty";
            default -> "ldc 0";
        };
    }
}

