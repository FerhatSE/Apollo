package nl.saxion.cos;

import nl.saxion.cos.exceptions.AssembleException;
import nl.saxion.cos.exceptions.ValidateTypeException;
import nl.saxion.cos.models.nodes.RootNode;
import nl.saxion.cos.visitors.TreeVisitor;
import org.antlr.v4.runtime.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Compiles source code in a custom language into Jasmin and then assembles a
 * JVM-compatible .class file.
 * <p>
 */
public class Compiler {
    /**
     * The number of errors detected by the lexer and parser.
     */
    private int errorCount = 0;

    /**
     * Main method.
     *
     * @param args Array of command line arguments. You can use this to supply the file name to
     *             compile.
     */
    public static void main(String[] args) throws ValidateTypeException {
        try {
            Compiler compiler = new Compiler();

            // Check that the user supplied a name of the source file
            if (args.length == 0) {
                System.err.println("Usage: java Compiler <name of source>");
                return;
            }

            // Split the file name
            // It first strips the extension, so that: tests/myFile.exlang becomes tests/myFile.
            // Then, it removes everything that seems a path, so we end up with just 'myFile' as
            // the class name.
            Path sourceCodePath = Paths.get(args[0]);

            String sourceFileName = sourceCodePath.getFileName().toString();
            int dotIndex = sourceFileName.lastIndexOf('.');
            String className = sourceFileName.substring(0, dotIndex == -1 ? sourceFileName.length() : dotIndex);

            // Read the file and compile it.
            JasminBytecode jasminBytecode = compiler.compileFile(sourceCodePath.toString(), className);
            if (jasminBytecode == null) {
                System.err.println("No Jasmin output");
                return;
            }

            // Determine which directory to write files to
            Path targetDirectory = sourceCodePath.getParent();
            if (targetDirectory == null) {
                targetDirectory = Paths.get(".");
            }

            // Write Jasmin-code to a file
            String jasminFilename = targetDirectory.resolve(className + ".j").toString();
            jasminBytecode.writeJasminToFile(jasminFilename);

            // Try to assemble the Jasmin byte code and write that to a file
            AssembledClass assembledClass = AssembledClass.assemble(jasminBytecode);
            String classFilename = targetDirectory.resolve(className + ".class").toString();
            assembledClass.writeClassToFile(classFilename);
        } catch (IOException | AssembleException e) {
            System.err.println("Something went wrong: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Compiles a complete source code file.
     *
     * @param inputPath Path to the source code to compile.
     * @param className Name of the class to create.
     * @throws IOException       if files could not be read or written
     * @throws AssembleException if Jasmin code was not valid
     */
    public JasminBytecode compileFile(String inputPath, String className)
            throws IOException, AssembleException, ValidateTypeException {
        return compile(CharStreams.fromFileName(inputPath), className);
    }

    /**
     * Compiles a string.
     *
     * @param sourceCode The source code to compile.
     * @param className  Name of the class to create.
     * @throws IOException       if files could not be read or written
     * @throws AssembleException if Jasmin code was not valid
     */
    public JasminBytecode compileString(String sourceCode, String className)
            throws IOException, AssembleException, ValidateTypeException {
        return compile(CharStreams.fromString(sourceCode), className);
    }

    /**
     * Compiles a file. The source code is lexed (turned into tokens), parsed (a parse tree
     * created) then Jasmin code is generated and assembled into a class.
     *
     * @param input     Stream to the source code input.
     * @param className Name of the class to create.
     */
    private JasminBytecode compile(CharStream input, String className) throws ValidateTypeException {
        // Phase 1/2: Run the lexer and parser
        ApollyScriptParser.CompilerContext parser = runLexerAndParser(input);

        // ANTLR tries to do its best in creating a parse tree, even if the source code contains
        // errors. So, check if that is the case and bail out if so.
        if (errorCount > 0)
            return null;

        // Phase 3: Generate code and check for errors
        return generateCode(parser, className);
    }

    /**
     * Takes the character input and turn it into tokens according to the grammar.
     * Then, tries to form a parse tree from the given tokens. In case of errors, the error listener is
     * called, but the parser still tries to create a parse tree.
     *
     * @param input The input
     * @return A parse tree
     */
    private ApollyScriptParser.CompilerContext runLexerAndParser(CharStream input) {
        ApollyScriptLexer lexer = new ApollyScriptLexer(input);
        lexer.addErrorListener(getErrorListener());

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        ApollyScriptParser parser = new ApollyScriptParser(tokens);
        parser.addErrorListener(getErrorListener());
        return parser.compiler();
    }

    /**
     * Generate the Jasmin code for the source code. This method calls the checkType function
     * on the rootNode which calls the function on all its child nodes to check for
     * syntactic and semantic errors
     *
     * @param ctx The parseTree to generate code for
     * @return All Jasmin code that is generated
     */
    private JasminBytecode generateCode(ApollyScriptParser.CompilerContext ctx, String className) throws ValidateTypeException {
        var jasminBytecode = new JasminBytecode(className);
        var codeGenerator = new TreeVisitor(className);

        jasminBytecode.add(".bytecode 49.0")
                .add(".class public " + className)
                .add(".super java/lang/Object")
                .add();

        /* Check for errors in the user input
        and add the generated bytecode into the
        jasminBytecode object
         */
        var rootNode = (RootNode) codeGenerator.visit(ctx);
        rootNode.checkAndGenerate(jasminBytecode);
        return jasminBytecode;
    }

    /**
     * Creates and returns an error listener for use in the lexer and parser that just increases
     * the errorCount-attribute in this class so we can find out if the source code had a syntax
     * error.
     *
     * @return An error listener for use with lexer.addErrorListener() and parser.addErrorListener()
     */
    private ANTLRErrorListener getErrorListener() {
        return new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg, RecognitionException e) {
                errorCount++;
            }
        };
    }
}
