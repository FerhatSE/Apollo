package nl.saxion.cos.models.nodes.conditional;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.nodes.BaseNode;
import nl.saxion.cos.models.scopes.Scope;

import static nl.saxion.cos.DataType.*;

public class EqualsNode extends ConditionalNode {

    private final String operator;

    public EqualsNode(BaseNode leftChild, BaseNode rightChild, String operator) {
        super(leftChild, rightChild);
        this.operator = operator;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        super.generateJasminCode(jasminBytecode, scope);

        // Generate an unique label name
        var trueLabel = "true" + getLeftChild().hashCode();
        var endLabel = "end" + getRightChild().hashCode();

        if (super.getType() == DOUBLE) {
            addDoubleByteCode(jasminBytecode, trueLabel);
        } else {
            jasminBytecode.add("if_" + getMnemonic() + "cmp" + (operator.equals("==") ? "eq " : "ne ") + trueLabel);
        }
        jasminBytecode.add("iconst_0")
                .add("goto " + endLabel)
                .add(trueLabel + ":")
                .add("iconst_1")
                .add("goto " + endLabel)
                .add(endLabel + ":");
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        super.checkType(scope);
    }

    @Override
    public DataType getDataType() {
        return BOOLEAN;
    }

    public String getMnemonic() {
        return getLeftChild().getDataType() == INTEGER ? "i" : "a";
    }

    public void addDoubleByteCode(JasminBytecode jasminBytecode, String trueLabel) {
        boolean equals = operator.equals("==");
        jasminBytecode.add("dcmp" + (equals ? "g" : "l"))
                .add("if" + (equals ? "le " : "gt ") + trueLabel);
    }
}
