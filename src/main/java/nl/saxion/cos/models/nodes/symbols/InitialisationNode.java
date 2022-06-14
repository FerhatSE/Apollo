package nl.saxion.cos.models.nodes.symbols;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.IdentifierAlreadyExistsException;
import nl.saxion.cos.exceptions.TypeMismatchException;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.scopes.Scope;

import java.util.List;

import static nl.saxion.cos.DataType.getMnemonic;

public class InitialisationNode extends SymbolNode {

    private final List<TypedNode> typedNodes;

    private int index;

    public InitialisationNode(List<TypedNode> typedNodes, String identifier, DataType type, Scope scope) {
        super(identifier, type, scope);
        this.typedNodes = typedNodes;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        typedNodes.forEach(node -> node.generateJasminCode(jasminBytecode, scope));
        jasminBytecode.add(getMnemonic(getDataType()) + "store " + index);
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        for (TypedNode expressionNode : typedNodes) {
            expressionNode.checkType(scope);
        }

        var variable = getScope().lookup(getIdentifier());

        if (variable != null) {
            throw new IdentifierAlreadyExistsException("Variable already exists within the scope.");
        }

        this.index = scope.getVariableCount();
        scope.declare(getIdentifier(), this);

        var assignedDataType = typedNodes.get(0).getDataType();

        if (assignedDataType != getDataType()) {
            throw new TypeMismatchException(
                    "Assigned value to " + getIdentifier() + " is no " + getDataType().name());
        }
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public DataType getDataType() {
        return super.getDataType();
    }
}
