package nl.saxion.cos.models.nodes.loops;

import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.NonBooleanConditionException;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.nodes.BaseNode;
import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.scopes.Scope;

import java.util.List;

import static nl.saxion.cos.DataType.BOOLEAN;

public class WhileLoopNode extends BaseNode {

    private final BaseNode blockNode;

    private final List<TypedNode> expressions;

    public WhileLoopNode(List<TypedNode> expressions, BaseNode blockNode) {
        this.expressions = expressions;
        this.blockNode = blockNode;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        var endLabel = "end" + hashCode();

        jasminBytecode.add("begin:");
        expressions.forEach(node -> node.generateJasminCode(jasminBytecode, scope));
        jasminBytecode.add("iconst_1");
        jasminBytecode.add("if_icmpne " + endLabel);
        blockNode.generateJasminCode(jasminBytecode, scope);
        jasminBytecode.add("goto begin");
        jasminBytecode.add(endLabel + ":");
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        blockNode.checkType(scope);
        for (TypedNode node : expressions) {
            node.checkType(scope);
            if (node.getDataType() != BOOLEAN) {
                throw new NonBooleanConditionException(
                        "Expression in while condition does not return a boolean");
            }
        }
    }
}
