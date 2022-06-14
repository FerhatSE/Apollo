package nl.saxion.cos.models.scopes;

import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.nodes.function.FunctionDeclarationNode;

public class GlobalScope extends Scope {

    public GlobalScope() {
        super(null);
    }

    @Override
    public TypedNode lookup(String identifier) {
        return getVariables().get(identifier);
    }

    @Override
    public int getVariableCount() {
        // Ignore function declarations
        return (int) getVariables()
                .entrySet()
                .stream()
                .filter(value -> !(value.getValue() instanceof FunctionDeclarationNode))
                .count() + getTotalDoubles() + 1;
    }
}
