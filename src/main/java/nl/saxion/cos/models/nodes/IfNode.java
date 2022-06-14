package nl.saxion.cos.models.nodes;

import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.NonBooleanConditionException;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.scopes.Scope;

import java.util.List;

import static nl.saxion.cos.DataType.BOOLEAN;

public class IfNode extends BaseNode {

    private final BaseNode trueBlock;

    private BaseNode elseBlock;

    private final List<TypedNode> conditionals;

    private final boolean containsElse;

    public IfNode(BaseNode trueBlock, BaseNode elseBlock, List<TypedNode> conditionals) {
        this.trueBlock = trueBlock;
        this.elseBlock = elseBlock;
        this.conditionals = conditionals;
        this.containsElse = true;
    }

    public IfNode(BaseNode trueBlock, List<TypedNode> conditionals) {
        this.trueBlock = trueBlock;
        this.conditionals = conditionals;
        this.containsElse = false;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        for (TypedNode node : conditionals) {
            node.generateJasminCode(jasminBytecode, scope);
        }
        String trueLabel = "trueLabel" + hashCode();
        String falseLabel = "falseLabel" + hashCode();

        jasminBytecode.add("ifne " + trueLabel);
        if (containsElse) {
            elseBlock.generateJasminCode(jasminBytecode, scope);
        }
        jasminBytecode.add("goto " + falseLabel);
        jasminBytecode.add(trueLabel + ":");
        trueBlock.generateJasminCode(jasminBytecode, scope);
        jasminBytecode.add(falseLabel + ":");
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        for (TypedNode node : conditionals) {
            node.checkType(scope);
            if (!node.getDataType().equals(BOOLEAN)) {
                throw new NonBooleanConditionException("Condition does not return a boolean");
            }
        }
        trueBlock.checkType(scope);
        if (elseBlock != null) {
            elseBlock.checkType(scope);
        }
    }
}

