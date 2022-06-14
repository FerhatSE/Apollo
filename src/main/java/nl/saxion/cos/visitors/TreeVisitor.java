package nl.saxion.cos.visitors;

import nl.saxion.cos.ApollyScriptBaseVisitor;
import nl.saxion.cos.ApollyScriptParser;
import nl.saxion.cos.models.nodes.*;
import nl.saxion.cos.models.nodes.conditional.ComparisonNode;
import nl.saxion.cos.models.nodes.conditional.EqualsNode;
import nl.saxion.cos.models.nodes.conditional.LogicalNode;
import nl.saxion.cos.models.nodes.function.FunctionCallNode;
import nl.saxion.cos.models.nodes.function.FunctionDeclarationNode;
import nl.saxion.cos.models.nodes.loops.RepeatLoopNode;
import nl.saxion.cos.models.nodes.loops.WhileLoopNode;
import nl.saxion.cos.models.nodes.symbols.DeclarationNode;
import nl.saxion.cos.models.nodes.symbols.InitialisationNode;
import nl.saxion.cos.models.nodes.symbols.ParameterNode;
import nl.saxion.cos.models.nodes.symbols.VariableNode;
import nl.saxion.cos.models.nodes.typed.MathExpressionNode;
import nl.saxion.cos.models.nodes.typed.ScannerNode;
import nl.saxion.cos.models.nodes.typed.TypedNode;
import nl.saxion.cos.models.nodes.typed.literals.BooleanNode;
import nl.saxion.cos.models.nodes.typed.literals.DoubleNode;
import nl.saxion.cos.models.nodes.typed.literals.IntNode;
import nl.saxion.cos.models.nodes.typed.literals.TextNode;
import nl.saxion.cos.models.scopes.GlobalScope;
import nl.saxion.cos.models.scopes.Scope;

import java.util.List;
import java.util.stream.Collectors;

import static nl.saxion.cos.DataType.getTypeByString;

public class TreeVisitor extends ApollyScriptBaseVisitor<BaseNode> {

    private final String className;

    private final Scope globalScope = new GlobalScope();

    private Scope currentScope = globalScope;

    public TreeVisitor(String className) {
        this.className = className;
    }

    @Override
    public BaseNode visitCompiler(ApollyScriptParser.CompilerContext ctx) {
        var statementNodes = getStatements(ctx.statement());
        return new RootNode(statementNodes, globalScope);
    }

    @Override
    public BaseNode visitFunctionCallExpr(ApollyScriptParser.FunctionCallExprContext ctx) {
        var arguments = getExpressions(ctx.expression());
        return new FunctionCallNode(arguments, ctx.IDENTIFIER().getText(), className);
    }

    @Override
    public BaseNode visitRepeatLoopStatement(ApollyScriptParser.RepeatLoopStatementContext ctx) {
        var totalLoops = Integer.parseInt(ctx.INT_VALUE().getText());
        var statementNode = visit(ctx.block());
        return new RepeatLoopNode(totalLoops, statementNode);
    }

    @Override
    public BaseNode visitWhileLoopStatement(ApollyScriptParser.WhileLoopStatementContext ctx) {
        var expressions = getExpressions(ctx.condition().expression());
        var statementNode = visit(ctx.block());

        return new WhileLoopNode(expressions, statementNode);
    }

    @Override
    public BaseNode visitCompareExpr(ApollyScriptParser.CompareExprContext ctx) {
        var leftChild = visit(ctx.leftChild);
        var rightChild = visit(ctx.rightChild);
        return new ComparisonNode(leftChild, rightChild, ctx.operator.getText());
    }

    @Override
    public BaseNode visitInitStatement(ApollyScriptParser.InitStatementContext ctx) {
        var expressionNodes = getExpressions(ctx.expression());
        var identifier = ctx.IDENTIFIER().getText();
        var type = getTypeByString(ctx.TYPE().getText());
        return new InitialisationNode(expressionNodes, identifier, type, currentScope);
    }

    @Override
    public BaseNode visitFunctionCallStatement(ApollyScriptParser.FunctionCallStatementContext ctx) {
        var arguments = getExpressions(ctx.expression());
        return new FunctionCallNode(arguments, ctx.IDENTIFIER().getText(), className);
    }

    @Override
    public BaseNode visitReturnStatement(ApollyScriptParser.ReturnStatementContext ctx) {
        return new ReturnNode(getExpressions(ctx.expression()));
    }

    @Override
    public BaseNode visitFunctionInitStatement(ApollyScriptParser.FunctionInitStatementContext ctx) {
        var identifier = ctx.IDENTIFIER().getText();
        var type = getTypeByString(ctx.TYPE().getText());

        currentScope = currentScope.openScope(true);

        var paramList = ctx.parameters().paramDecl()
                .stream()
                .map(this::visit)
                .map(node -> (ParameterNode) node)
                .collect(Collectors.toList());

        var blockNode = visit(ctx.block());
        var functionNode = new FunctionDeclarationNode(identifier, type, paramList, blockNode, globalScope, currentScope);

        currentScope = currentScope.closeScope();
        return functionNode;
    }

    @Override
    public BaseNode visitAssignStatement(ApollyScriptParser.AssignStatementContext ctx) {
        var expressionNodes = getExpressions(ctx.expression());
        var identifier = ctx.IDENTIFIER().getText();
        return new AssignmentNode(expressionNodes, identifier);
    }

    @Override
    public BaseNode visitDeclarationStatement(ApollyScriptParser.DeclarationStatementContext ctx) {
        var identifier = ctx.IDENTIFIER().getText();
        var dataType = getTypeByString(ctx.TYPE().getText());
        return new DeclarationNode(identifier, dataType, currentScope);
    }

    @Override
    public BaseNode visitScannerExpr(ApollyScriptParser.ScannerExprContext ctx) {
        return new ScannerNode(ctx.SCANNER().getText());
    }

    @Override
    public BaseNode visitEqualExpr(ApollyScriptParser.EqualExprContext ctx) {
        var leftChild = visit(ctx.leftChild);
        var rightChild = visit(ctx.rightChild);
        var operator = ctx.operator.getText();
        return new EqualsNode(leftChild, rightChild, operator);
    }

    @Override
    public BaseNode visitLogicalExpr(ApollyScriptParser.LogicalExprContext ctx) {
        var leftChild = visit(ctx.leftChild);
        var rightChild = visit(ctx.rightChild);
        var operator = ctx.operator.getText();
        return new LogicalNode(leftChild, rightChild, operator);
    }

    @Override
    public BaseNode visitIfStatement(ApollyScriptParser.IfStatementContext ctx) {
        var trueBlock = visit(ctx.trueBlock);
        var ifExpressions = getExpressions(ctx.ifCondition.expression());

        if (ctx.falseBlock == null) {
            return new IfNode(trueBlock, ifExpressions);
        }
        var elseBlock = visit(ctx.falseBlock);
        return new IfNode(trueBlock, elseBlock, ifExpressions);
    }

    @Override
    public BaseNode visitPrintCallStatement(ApollyScriptParser.PrintCallStatementContext ctx) {
        var expressions = getExpressions(ctx.expression());
        return new PrintNode(expressions);
    }

    @Override
    public BaseNode visitBlock(ApollyScriptParser.BlockContext ctx) {
        currentScope = currentScope.openScope();
        var statementNodes = getStatements(ctx.statement());
        currentScope = currentScope.closeScope();
        return new BlockStatementNode(statementNodes);
    }

    @Override
    public BaseNode visitOpExpr(ApollyScriptParser.OpExprContext ctx) {
        var operator = ctx.operator.getText();
        var left = visit(ctx.leftChild);

        var right = visit(ctx.rightChild);
        return new MathExpressionNode(operator, (TypedNode) left, (TypedNode) right);
    }

    @Override
    public BaseNode visitIntExpr(ApollyScriptParser.IntExprContext ctx) {
        return new IntNode(ctx.getText());
    }

    @Override
    public BaseNode visitDoubleExpr(ApollyScriptParser.DoubleExprContext ctx) {
        return new DoubleNode(ctx.getText());
    }

    @Override
    public BaseNode visitTextExpr(ApollyScriptParser.TextExprContext ctx) {
        return new TextNode(ctx.TEXT_VALUE().getText());
    }

    @Override
    public BaseNode visitBooleanExpr(ApollyScriptParser.BooleanExprContext ctx) {
        return new BooleanNode(ctx.getText());
    }

    @Override
    public BaseNode visitParamDecl(ApollyScriptParser.ParamDeclContext ctx) {
        var identifier = ctx.IDENTIFIER().getText();
        var dataType = getTypeByString(ctx.TYPE().getText());
        return new ParameterNode(identifier, dataType, currentScope);
    }

    @Override
    public BaseNode visitIdentifierExpr(ApollyScriptParser.IdentifierExprContext ctx) {
        String identifier = ctx.IDENTIFIER().getText();
        return new VariableNode(identifier);
    }

    @Override
    public BaseNode visitParentExpr(ApollyScriptParser.ParentExprContext ctx) {
        return visit(ctx.expression());
    }

    public List<TypedNode> getExpressions(List<ApollyScriptParser.ExpressionContext> contextList) {
        return contextList
                .stream()
                .map(this::visit)
                .map(node -> (TypedNode) node)
                .collect(Collectors.toList());
    }

    public List<BaseNode> getStatements(List<ApollyScriptParser.StatementContext> contextList) {
        return contextList
                .stream()
                .map(this::visit)
                .collect(Collectors.toList());
    }
}

