package nl.saxion.cos.models.nodes.function;

import nl.saxion.cos.DataType;
import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.FunctionNotFoundException;
import nl.saxion.cos.exceptions.InvalidFunctionCallException;
import nl.saxion.cos.exceptions.ParameterTypeMismatchException;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.nodes.symbols.ParameterNode;
import nl.saxion.cos.models.nodes.symbols.VariableNode;
import nl.saxion.cos.models.scopes.Scope;

import java.util.List;

import static nl.saxion.cos.DataType.getTypeDescriptor;

public class FunctionCallNode extends TypedNode {

    private final String identifier;

    private final String className;

    private final List<TypedNode> assignedArguments;

    private FunctionDeclarationNode function;

    private List<ParameterNode> parameters;

    private DataType dataType;

    public FunctionCallNode(List<TypedNode> assignedArguments, String identifier, String className) {
        this.assignedArguments = assignedArguments;
        this.identifier = identifier;
        this.className = className;
    }

    @Override
    public void generateJasminCode(JasminBytecode jasminBytecode, Scope scope) {
        this.function = (FunctionDeclarationNode) scope.lookup(identifier);
        this.parameters = function.getParameters();

        if (!assignedArguments.isEmpty()) {
            declareArguments();
            assignedArguments.forEach(node -> node.generateJasminCode(jasminBytecode, scope));
            jasminBytecode.add("invokestatic " + className + "/" + identifier + "(" +
                    function.getArgumentSequence() + ")" + getTypeDescriptor(function.getDataType()));
        }
    }

    @Override
    public void checkType(Scope scope) throws ValidateTypeException {
        var function = (FunctionDeclarationNode) scope.lookup(identifier);

        if (function == null) {
            throw new FunctionNotFoundException("Function with identifier: " + identifier + " not found.");
        }

        var parameters = function.getParameters();
        this.dataType = function.getDataType();

        if (assignedArguments.isEmpty()) {
            return;
        }

        for (TypedNode node : assignedArguments) {
            node.checkType(scope);
        }

        // Check if all arguments are present
        if (function.getParameters().size() != assignedArguments.size()) {
            throw new InvalidFunctionCallException(
                    "Not all parameters have been assigned to function call: " + identifier);
        }

        // Check if all parameters are assigned to the correct datatype
        for (int i = 0; i < parameters.size(); i++) {
            if (parameters.get(i).getDataType() != assignedArguments.get(i).getDataType()) {
                throw new ParameterTypeMismatchException("Argument " + parameters.get(i).getIdentifier()
                        + "" + " is assigned to an incorrect datatype.");
            }
        }
    }

    public void declareArguments() {
        for (int i = 0; i < assignedArguments.size(); i++) {
            var identifier = parameters.get(i).getIdentifier();
            function.getScope().declare(identifier, new VariableNode(identifier));
        }
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }
}
