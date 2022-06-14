package nl.saxion.cos;

import nl.saxion.cos.exceptions.*;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.TextFromStandardInputStream;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static com.github.stefanbirkner.systemlambda.SystemLambda.withTextFromSystemIn;
import static org.junit.contrib.java.lang.system.TextFromStandardInputStream.emptyStandardInputStream;
import static org.junit.jupiter.api.Assertions.*;

public class CompilerTest {

    @Rule // For simulating the scanner input
    public final TextFromStandardInputStream systemInMock = emptyStandardInputStream();

    /**
     * Helper method that takes some compiled Jasmin byte code, assembles it and
     * runs the class. It returns the output of the execution, which you can use
     * to check in an assert.
     */
    private List<String> runCode(JasminBytecode code) throws AssembleException {
        // Turn the Jasmin code into a (hopefully) working class file
        if (code == null) {
            throw new AssembleException("No valid Jasmin code to assemble");
        }
        AssembledClass aClass = AssembledClass.assemble(code);

        // Run the class and return the output
        SandBox s = new SandBox();
        s.runClass(aClass);
        return s.getOutput();
    }

    public JasminBytecode compile(String inputPath, String className) throws ValidateTypeException, IOException, AssembleException {
        Compiler compiler = new Compiler();
        return compiler.compileFile(inputPath, className);
    }

    @Test
    public void mustOne() throws AssembleException, IOException, ValidateTypeException {
        var bytecode = compile("testFiles/good_weather/mustone/MustOne.txt", "MustOne");
        var result = runCode(bytecode);
        assertEquals("10", result.get(0));
    }

    @Test
    public void mustTwo() throws Exception {
        var bytecode = compile("testFiles/good_weather/musttwo/MustTwo.txt", "MustTwo");

        // Mocking an int scanner by setting the input stream to 10
        withTextFromSystemIn("10").execute(() -> {
            var result = runCode(bytecode);

            // Answer > 5 which should return true
            assertEquals("true", result.get(0));
        });
    }

    @Test
    public void mustThree() throws Exception {
        var bytecode = compile("testFiles/good_weather/mustthree/MustThree.txt", "MustThree");

        withTextFromSystemIn("90", "7000").execute(() -> {
            var resultOne = runCode(bytecode);
            assertArrayEquals(new String[]{"false", "false"}, resultOne.toArray());
        });

        withTextFromSystemIn("60", "-90").execute(() -> {
            var resultTwo = runCode(bytecode);
            assertArrayEquals(new String[]{"You made a mistake", "true"}, resultTwo.toArray());
        });
    }

    @Test
    public void mustFour() throws Exception {
        var bytecode = compile("testFiles/good_weather/mustfour/MustFour.txt", "MustFour");

        withTextFromSystemIn("884", "231", "33", "4", "34").execute(() -> {
            var result = runCode(bytecode);

            assertArrayEquals(new String[]{"Wrong answer", "Wrong answer",
                    "Wrong answer", "Correct"}, result.toArray());
        });
    }

    @Test
    public void mustFive() throws AssembleException, IOException, ValidateTypeException {
        var bytecode = compile("testFiles/good_weather/mustfive/MustFive.txt", "MustFive");
        var result = runCode(bytecode);
        assertEquals("Correct", result.get(0));
    }

    @Test
    public void doubleVariable() throws Exception {
        var bytecode = compile("testFiles/good_weather/doubles/Double.txt", "Double");

        withTextFromSystemIn("200").execute(() -> {
            var result = runCode(bytecode);
            assertArrayEquals(new String[]{"2.0", "200.0"}, result.toArray());
        });
    }

    @Test
    public void completeProgram() throws Exception {
        var bytecode = compile("testFiles/good_weather/completeprogram/CompleteProgram.txt", "CompleteProgram");

        withTextFromSystemIn("90", "true", "90", "Test").execute(() -> {
            var result = runCode(bytecode);
            assertArrayEquals(new String[]{"100", "false", "Changed text", "100.0"}, result.toArray());
        });
    }

    @Test
    public void variableNotFound() {
        assertThrows(VariableNotFoundException.class,
                () -> compile("testFiles/bad_weather/VariableNotFound", "VariableNotFound"));
    }

    @Test
    public void variableAlreadyDefined() {
        assertThrows(IdentifierAlreadyExistsException.class,
                () -> compile("testFiles/bad_weather/VariableAlreadyExists", "VariableAlreadyExists"));
    }

    @Test
    public void typeMismatch() {
        assertThrows(TypeMismatchException.class,
                () -> compile("testFiles/bad_weather/TypeMismatch", "TypeMismatch"));
    }

    @Test
    public void nonBooleanCondition() {
        assertThrows(NonBooleanConditionException.class,
                () -> compile("testFiles/bad_weather/NonBooleanCondition", "NonBooleanCondition"));
    }

    @Test
    public void invalidFunctionCall() {
        assertThrows(InvalidFunctionCallException.class,
                () -> compile("testFiles/bad_weather/InvalidFunctionCall", "InvalidFunctionCall"));
    }

    @Test
    public void parameterTypeMismatch() {
        assertThrows(ParameterTypeMismatchException.class,
                () -> compile("testFiles/bad_weather/ParameterTypeMismatch", "ParameterTypeMismatch"));
    }

    @Test
    public void duplicateParameter() {
        assertThrows(DuplicateParameterException.class,
                () -> compile("testFiles/bad_weather/DuplicateParameter", "DuplicateParameter"));
    }

    @Test
    public void incorrectReturnType() {
        assertThrows(IncorrectReturnTypeException.class,
                () -> compile("testFiles/bad_weather/IncorrectReturnType", "IncorrectReturnType"));
    }
}
