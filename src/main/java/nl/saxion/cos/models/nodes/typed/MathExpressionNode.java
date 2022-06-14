package nl.saxion.cos.models.nodes.typed;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.TypeMismatchException;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.scopes.Scope;

import static nl.saxion.cos.DataType.getMnemonic;

public class MathExpressionNode extends TypedNode {

    private final String operator;

    private final TypedNode left;

    private final TypedNode right;

    public MathExpressionNode(String operator, TypedNode left, TypedNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        left.generateJasminCode(jasminBytecode, scope);
        right.generateJasminCode(jasminBytecode, scope);

        String operatorCommand = switch (operator) {
            case "/" -> "div";
            case "+" -> "add";
            case "-" -> "sub";
            default -> "mul";
        };
        jasminBytecode.add(getMnemonic(getDataType()) + operatorCommand);
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        left.checkType(scope);
        right.checkType(scope);

        var leftType = left.getDataType();
        var rightType = right.getDataType();

        if (leftType != rightType) {
            throw new TypeMismatchException("Type of left and right expression do not match");
        }
    }

    @Override
    public DataType getDataType() {
        return left.getDataType();
    }
}
