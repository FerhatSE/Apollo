package nl.saxion.cos.models.scopes;

import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.nodes.function.FunctionCallNode;

public class LocalScope extends Scope {

    private final Scope parentScope;

    protected LocalScope(Scope parentScope) {
        super(parentScope);
        this.parentScope = parentScope;
    }

    @Override
    public TypedNode lookup(String identifier) {
        var node = getVariables().get(identifier);

        if (node != null) {
            return node;
        } else if (parentScope != null) {
            return this.parentScope.lookup(identifier);
        }
        return null;
    }

    @Override
    public int getVariableCount() {
        var size = (int) getVariables().entrySet()
                .stream()
                .filter(value -> !(value.getValue() instanceof FunctionCallNode))
                .count() + getTotalDoubles();
        size += this.parentScope.getVariableCount();
        return size;
    }
}
