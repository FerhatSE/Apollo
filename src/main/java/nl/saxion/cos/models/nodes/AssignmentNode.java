package nl.saxion.cos.models.nodes;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.TypeMismatchException;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.exceptions.VariableNotFoundException;
import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.nodes.symbols.SymbolNode;
import nl.saxion.cos.models.scopes.Scope;

import java.util.List;

import static nl.saxion.cos.DataType.getMnemonic;

public class AssignmentNode extends TypedNode {

    private final List<TypedNode> typedNodes;

    private final String identifier;

    private DataType assignedType;

    private SymbolNode variable;

    public AssignmentNode(List<TypedNode> typedNodes, String identifier) {
        this.typedNodes = typedNodes;
        this.identifier = identifier;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        typedNodes.forEach(node -> node.generateJasminCode(jasminBytecode, scope));
        jasminBytecode.add(getMnemonic(variable.getDataType()) + "store " + (variable.getIndex()));
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        for (TypedNode node : typedNodes) {
            node.checkType(scope);
        }

        this.variable = (SymbolNode) scope.lookup(identifier);

        if (variable == null) {
            throw new VariableNotFoundException(
                    "Variable " + identifier + " not found within the scope.");
        }
        this.assignedType = variable.getDataType();


        var variableDataType = variable.getDataType();

        if (assignedType != variableDataType) {
            throw new TypeMismatchException("Assigned type does not match type: " + variableDataType);
        }
    }

    @Override
    public DataType getDataType() {
        return assignedType;
    }
}
