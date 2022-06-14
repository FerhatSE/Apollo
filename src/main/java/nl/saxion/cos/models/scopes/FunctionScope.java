package nl.saxion.cos.models.scopes;

import nl.saxion.cos.models.nodes.typed.TypedNode;

public class FunctionScope extends Scope {

    protected FunctionScope(Scope parentScope) {
        super(parentScope);
    }

    @Override
    public TypedNode lookup(String identifier) {
        return getVariables().get(identifier);
    }

    @Override
    public int getVariableCount() {
        return getVariables().size() + getTotalDoubles();
    }
}
