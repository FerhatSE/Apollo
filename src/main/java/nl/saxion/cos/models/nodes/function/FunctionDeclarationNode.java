package nl.saxion.cos.models.nodes.function;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.DuplicateParameterException;
import nl.saxion.cos.exceptions.IdentifierAlreadyExistsException;
import nl.saxion.cos.exceptions.IncorrectReturnTypeException;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.nodes.BaseNode;
import nl.saxion.cos.models.nodes.BlockStatementNode;
import nl.saxion.cos.models.nodes.ReturnNode;
import nl.saxion.cos.models.nodes.symbols.ParameterNode;
import nl.saxion.cos.models.nodes.symbols.SymbolNode;
import nl.saxion.cos.models.scopes.Scope;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static nl.saxion.cos.DataType.*;

public class FunctionDeclarationNode extends SymbolNode {

    private final BlockStatementNode blockStatement;

    private final List<ParameterNode> parameters;

    private final Scope globalScope;

    private final Scope currentScope;

    public FunctionDeclarationNode(String identifier, DataType dataType, List<ParameterNode> parameters,
                                   BaseNode blockStatement, Scope globalScope, Scope currentScope) {
        super(identifier, dataType, currentScope);
        this.parameters = parameters;
        this.blockStatement = (BlockStatementNode) blockStatement;
        this.globalScope = globalScope;
        this.currentScope = currentScope;
        declareParameters();
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        // Functions are added to the global scope because of them being static.
        globalScope.declare(getIdentifier(), this);
        jasminBytecode.add(".method public static " + getIdentifier() + "(" +
                getArgumentSequence() + ")" + getTypeDescriptor(getDataType()))
                .add(".limit stack 99")
                .add(".limit locals 99");
        blockStatement.generateJasminCode(jasminBytecode, currentScope);

        jasminBytecode.add(getMnemonic(getDataType()) + "return")
                .add(".end method")
                .add("");
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        var function = scope.lookup(getIdentifier());
        if (function != null) {
            throw new IdentifierAlreadyExistsException("Function is already defined within the scope.");
        }

        var statementNodes = blockStatement.getStatementNodes();

        for (BaseNode statementNode : statementNodes) {
            statementNode.checkType(currentScope);
        }

        // Check if the method returns anything
        var returns = statementNodes
                .stream()
                .filter(node -> node instanceof ReturnNode)
                .map(node -> (ReturnNode) node)
                .collect(Collectors.toList());
        if (returns.isEmpty() && getDataType() != VOID) {
            throw new IncorrectReturnTypeException("Missing a return value");
        }

        if (getDataType() != VOID) {
            // Check if the method returns the correct datatype
            var returnNode = returns.get(0);

            if (returnNode.getDataType() != getDataType()) {
                throw new IncorrectReturnTypeException(
                        "Function " + getIdentifier() + " does not return a " + getDataType().name());
            }
        }

        // Check if any duplicate parameters have been declared
        var names = new HashSet<>();
        var parameterNamesSet = parameters.stream()
                // Set.add() returns false if the element was already in the set.
                .filter(node -> !names.add(node.getIdentifier()))
                .collect(Collectors.toSet());

        if (parameterNamesSet.size() > 0) {
            throw new DuplicateParameterException(
                    "Parameters " + names + " are duplicates in function " + getIdentifier());
        }
    }

    public String getArgumentSequence() {
        var sequence = new StringBuilder();
        parameters.forEach(node -> sequence.append(getTypeDescriptor(node.getDataType())));
        return sequence.toString();
    }

    public void declareParameters() {
        parameters.forEach(node -> node.declare(getScope()));
    }

    @Override
    public int getIndex() {
        return 0;
    }

    public List<ParameterNode> getParameters() {
        return parameters;
    }
}
