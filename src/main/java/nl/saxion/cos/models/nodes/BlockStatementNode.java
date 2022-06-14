package nl.saxion.cos.models.nodes;

import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.scopes.Scope;

import java.util.List;

public class BlockStatementNode extends BaseNode {

    private final List<BaseNode> statementNodes;

    public BlockStatementNode(List<BaseNode> statementNodes) {
        this.statementNodes = statementNodes;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        for (BaseNode node : statementNodes) {
            node.generateJasminCode(jasminBytecode, scope);

            if (node instanceof ReturnNode) {
                return;
            }
        }
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        for (BaseNode node : statementNodes) {
            node.checkType(scope);
        }
    }

    @Override
    public String toString() {
        return "BlockStatementNode{" +
                "statementNodes=" + statementNodes +
                '}';
    }

    public List<BaseNode> getStatementNodes() {
        return statementNodes;
    }
}

