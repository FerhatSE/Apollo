package nl.saxion.cos.models.nodes;

import nl.saxion.cos.JasminBytecode;
import nl.saxion.cos.exceptions.IdentifierAlreadyExistsException;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.scopes.Scope;

public abstract class BaseNode {

    public abstract void generateJasminCode(JasminBytecode jasminBytecode, Scope scope);

    public abstract void checkType(Scope scope) throws ValidateTypeException;
}




