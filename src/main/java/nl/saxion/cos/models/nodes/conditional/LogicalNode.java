package nl.saxion.cos.models.nodes.conditional;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.NonBooleanConditionException;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.nodes.BaseNode;
import nl.saxion.cos.models.scopes.Scope;

import static nl.saxion.cos.DataType.BOOLEAN;

public class LogicalNode extends ConditionalNode {

    private final String operator;

    public LogicalNode(BaseNode leftChild, BaseNode rightChild, String operator) {
        super(leftChild, rightChild);
        this.operator = operator;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        super.generateJasminCode(jasminBytecode, scope);

        var trueLabel = "true" + getLeftChild().hashCode();
        var stopLabel = "stop" + getRightChild().hashCode();

        if (operator.equals("&&")) {
            generateAndOperation(jasminBytecode, trueLabel);
        } else {
            generateOrOperation(jasminBytecode, trueLabel);
        }
        jasminBytecode.add("goto " + stopLabel);
        jasminBytecode.add(trueLabel + ":");
        jasminBytecode.add("iconst_1");
        jasminBytecode.add(stopLabel + ":");
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        super.checkType(scope);

        var leftNode = getLeftChild();
        var rightNode = getRightChild();

        if (leftNode.getDataType() != BOOLEAN || rightNode.getDataType() != BOOLEAN) {
            throw new NonBooleanConditionException
                    ("All expressions inside a condition must return a boolean");
        }
    }

    private void generateAndOperation(JasminBytecode jasminBytecode, String trueLabel) {
        jasminBytecode.add("iadd");
        jasminBytecode.add("iconst_2");
        jasminBytecode.add("if_icmpeq " + trueLabel);
        jasminBytecode.add("iconst_0");
    }

    private void generateOrOperation(JasminBytecode jasminBytecode, String trueLabel) {
        jasminBytecode.add("iadd");
        jasminBytecode.add("ifne " + trueLabel);
        jasminBytecode.add("iconst_0");
    }

    @Override
    public DataType getDataType() {
        return BOOLEAN;
    }
}
