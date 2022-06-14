package nl.saxion.cos.models.nodes.loops;

import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.nodes.BaseNode;
import nl.saxion.cos.models.nodes.loops.CounterVariableNode;
import nl.saxion.cos.models.scopes.Scope;

public class RepeatLoopNode extends BaseNode {

    private final int totalLoops;

    private final BaseNode blockNode;

    public RepeatLoopNode(int totalLoops, BaseNode blockNode) {
        this.totalLoops = totalLoops;
        this.blockNode = blockNode;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        scope.declare("counter", new CounterVariableNode());
        var variableCount = scope.getVariableCount();
        var endLabel = "end" + hashCode();

        jasminBytecode.add("ldc 0")
                .add("istore " + variableCount)
                .add("iteration:")
                .add("iload " + variableCount)
                .add("ldc " + totalLoops)
                .add("if_icmpeq " + endLabel);

        blockNode.generateJasminCode(jasminBytecode, scope);

        jasminBytecode.add("iinc " + variableCount + " 1")
                .add("goto iteration")
                .add(endLabel + ":")
                .add();
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        blockNode.checkType(scope);
    }
}
