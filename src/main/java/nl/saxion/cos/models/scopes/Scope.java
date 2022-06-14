package nl.saxion.cos.models.scopes;

import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.nodes.function.FunctionCallNode;

import java.util.HashMap;
import java.util.Map;

import static nl.saxion.cos.DataType.DOUBLE;

public abstract class Scope {

    private final Map<String, TypedNode> variables;

    private final Scope parentScope;

    protected Scope(Scope parentScope) {
        this.parentScope = parentScope;
        this.variables = new HashMap<>();
    }

    // Doubles require two slots
    protected int getTotalDoubles() {
        return (int) variables
                .entrySet()
                .stream()
                .filter(node -> !(node.getValue() instanceof FunctionCallNode))
                .filter(node -> node.getValue().getDataType() == DOUBLE)
                .count();
    }

    public void declare(String identifier, TypedNode node) {
        this.variables.put(identifier, node);
    }

    public abstract TypedNode lookup(String identifier);

    public abstract int getVariableCount();

    public Scope openScope(boolean isFunction) {
        return new FunctionScope(this);
    }

    public Scope openScope() {
        return new LocalScope(this);
    }

    public Map<String, TypedNode> getVariables() {
        return variables;
    }

    public Scope closeScope() {
        return parentScope;
    }
}
