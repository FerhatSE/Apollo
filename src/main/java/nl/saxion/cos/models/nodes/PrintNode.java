package nl.saxion.cos.models.nodes;

import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.scopes.Scope;

import java.util.List;

import static nl.saxion.cos.DataType.getTypeDescriptor;

public class PrintNode extends BaseNode {

    private final List<TypedNode> nodes;

    public PrintNode(List<TypedNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        var dataType = nodes.get(0).getDataType();
        jasminBytecode.add("getstatic java/lang/System/out Ljava/io/PrintStream;");
        nodes.forEach(node -> node.generateJasminCode(jasminBytecode, scope));
        jasminBytecode.add("invokevirtual java/io/PrintStream/println(" + getTypeDescriptor(dataType) + ")V");
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        for (TypedNode node : nodes) {
            node.checkType(scope);
        }
    }
}



