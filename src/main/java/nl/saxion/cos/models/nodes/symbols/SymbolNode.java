package nl.saxion.cos.models.nodes.symbols;

import nl.saxion.cos.DataType;
import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.scopes.Scope;

public abstract class SymbolNode extends TypedNode {

    private final String identifier;

    private final Scope scope;

    private final DataType dataType;

    protected SymbolNode(String identifier, DataType dataType, Scope scope) {
        this.identifier = identifier;
        this.dataType = dataType;
        this.scope = scope;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Scope getScope() {
        return scope;
    }

    public abstract int getIndex();

    @Override
    public DataType getDataType() {
        return dataType;
    }
}
