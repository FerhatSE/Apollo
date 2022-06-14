package nl.saxion.cos.models.nodes.conditional;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.TypeMismatchException;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.nodes.BaseNode;
import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.scopes.Scope;

public abstract class ConditionalNode extends TypedNode {

    private final BaseNode leftChild;

    private final BaseNode rightChild;

    public ConditionalNode(BaseNode leftChild, BaseNode rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        leftChild.generateJasminCode(jasminBytecode, scope);
        rightChild.generateJasminCode(jasminBytecode, scope);
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        var leftNode = (TypedNode) leftChild;
        var rightNode = (TypedNode) rightChild;

        leftNode.checkType(scope);
        rightNode.checkType(scope);

        if (leftNode.getDataType() != rightNode.getDataType()) {
            throw new TypeMismatchException("Left and right type do not match.");
        }
    }

    public TypedNode getLeftChild() {
        return (TypedNode) leftChild;
    }

    public TypedNode getRightChild() {
        return (TypedNode) rightChild;
    }

    public DataType getType() {
        var node = (TypedNode) leftChild;
        return node.getDataType();
    }
}
