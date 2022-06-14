package nl.saxion.cos.models.nodes.conditional;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.nodes.BaseNode;
import nl.saxion.cos.models.scopes.Scope;

import static nl.saxion.cos.DataType.BOOLEAN;
import static nl.saxion.cos.DataType.DOUBLE;

public class ComparisonNode extends ConditionalNode {

    private final String comparison;

    public ComparisonNode(BaseNode leftChild, BaseNode rightChild, String comparison) {
        super(leftChild, rightChild);
        this.comparison = comparison;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        super.generateJasminCode(jasminBytecode, scope);

        var stopLabel = "stopLabel_" + getLeftChild().hashCode();
        var trueLabel = "trueLabel_" + getRightChild().hashCode();

        if (super.getType() == DOUBLE) {
            addDoubleByteCode(jasminBytecode, trueLabel);
        } else {
            jasminBytecode.add("if_icmp" + (comparison.equals("<") ? "lt " : "gt ") + trueLabel);
        }
        jasminBytecode.add("iconst_0")
                .add("goto " + stopLabel)
                .add(trueLabel + ":")
                .add("iconst_1")
                .add("goto " + stopLabel)
                .add(stopLabel + ":");
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        super.checkType(scope);
    }

    @Override
    public DataType getDataType() {
        return BOOLEAN;
    }

    public void addDoubleByteCode(JasminBytecode jasminBytecode, String trueLabel) {
        boolean lessThan = comparison.equals("<");
        jasminBytecode.add("dcmp" + (lessThan ? "l" : "g"))
                .add("if" + (lessThan ? "le " : "gt ") + trueLabel);
    }
}
