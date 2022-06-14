package nl.saxion.cos.models.nodes;

import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.nodes.function.FunctionDeclarationNode;
import nl.saxion.cos.models.scopes.Scope;

import java.util.List;
import java.util.stream.Collectors;

public class RootNode extends BaseNode {

    private List<BaseNode> statements;

    private final Scope scope;

    public RootNode(List<BaseNode> statements, Scope scope) {
        this.statements = statements;
        this.scope = scope;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
    }


    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
    }

    public void checkAndGenerate(JasminBytecode jasminBytecode) throws ValidateTypeException {
        // Extract the function inits
        var functions = statements
                .stream()
                .filter(node -> node instanceof FunctionDeclarationNode)
                .collect(Collectors.toList());

        for (BaseNode function : functions) {
            function.checkType(scope);
            function.generateJasminCode(jasminBytecode, scope);
        }

        jasminBytecode.add(".method public static main([Ljava/lang/String;)V")
                .add(".limit stack 99")
                .add(".limit locals 99");

        // Filter out the function declarations
        statements = statements
                .stream()
                .filter(node -> !(node instanceof FunctionDeclarationNode))
                .collect(Collectors.toList());

        for (BaseNode statement : statements) {
            statement.checkType(scope);
            statement.generateJasminCode(jasminBytecode, scope);
        }

        jasminBytecode.add("return")
                .add(".end method")
                .add("");
    }
}
