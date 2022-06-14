package nl.saxion.cos.models.nodes;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.scopes.Scope;

import java.util.List;

import static nl.saxion.cos.DataType.VOID;

public class ReturnNode extends TypedNode {

    private final List<TypedNode> typedNodes;

    public ReturnNode(List<TypedNode> typedNodes) {
        this.typedNodes = typedNodes;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        typedNodes.forEach(node -> node.generateJasminCode(jasminBytecode, scope));
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        for (TypedNode node : typedNodes) {
            node.checkType(scope);
        }
    }

    @Override
    public DataType getDataType() {
        if (typedNodes.isEmpty()) {
            return VOID;
        }
        return typedNodes.get(0).getDataType();
    }
}
