package nl.saxion.cos.models.nodes.typed;

import nl.saxion.cos.DataType;
import nl.saxion.cos.models.nodes.BaseNode;

public abstract class TypedNode extends BaseNode {

    public abstract DataType getDataType();
}
