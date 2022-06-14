grammar ApollyScript;

compiler            : statement* EOF;

statement           : TYPE IDENTIFIER ASSIGN expression+ SEMICOLON                               #initStatement
                    | TYPE IDENTIFIER SEMICOLON                                                  #declarationStatement
                    | RETURN expression* SEMICOLON                                               #returnStatement
                    | IDENTIFIER ASSIGN expression+ SEMICOLON                                    #assignStatement
                    | PRINT '(' expression+ ')' SEMICOLON                                        #printCallStatement
                    | REPEAT INT_VALUE TIMES block                                               #repeatLoopStatement
                    | WHILE condition block                                                      #whileLoopStatement
                    | IF ifCondition=condition trueBlock=block (ELSE falseBlock=block)?          #ifStatement
                    | TYPE IDENTIFIER '(' parameters ')' block                                   #functionInitStatement
                    | IDENTIFIER '(' (expression ( ',' expression )*)? ')' SEMICOLON             #functionCallStatement
                    | block                                                                      #blockStatement;

block               :  '{' statement* '}';

// Parse rule for expressions
expression          : leftChild=expression operator=('*'|'/') rightChild=expression              #opExpr
                    | leftChild=expression operator=('+'|'-') rightChild=expression              #opExpr
                    | leftChild=expression operator=('=='|'!=') rightChild=expression            #equalExpr
                    | leftChild=expression operator=('<'|'>') rightChild=expression              #compareExpr
                    | leftChild=expression operator=('&&'|'||') rightChild=expression            #logicalExpr
                    | '(' expression ')'                                                         #parentExpr
                    | IDENTIFIER '(' (expression ( ',' expression )*)? ')'                       #functionCallExpr
                    | INT_VALUE                                                                  #intExpr
                    | TEXT_VALUE                                                                 #textExpr
                    | BOOLEAN_VALUE                                                              #booleanExpr
                    | DOUBLE_VALUE                                                               #doubleExpr
                    | IDENTIFIER                                                                 #identifierExpr
                    | SCANNER                                                                    #scannerExpr;

// Declared parameters inside a function
parameters          : paramDecl (',' paramDecl)*;
paramDecl           : TYPE IDENTIFIER;

// Boolean expression for if-statements
condition           : '(' expression+ ')';

// Variables
TYPE                : INT
                    | DOUBLE
                    | TEXT
                    | BOOLEAN
                    | VOID;

INT                 : 'int';
DOUBLE              : 'double';
TEXT                : 'Text';
BOOLEAN             : 'boolean';
VOID                : 'void';
RETURN              : 'return';

WHILE               : 'while';
IF                  : 'if';
ELSEIF              : 'else if';
ELSE                : 'else';

INT_VALUE           : [0-9]+;
DOUBLE_VALUE        : INT_VALUE '.' INT_VALUE;
TEXT_VALUE          : QUOTE[a-zA-Z0-9 ]*QUOTE;
BOOLEAN_VALUE       : 'true' | 'false';

// Keywords
ASSIGN              : '=';
MULTIPLY            : '*';
DIVIDE              : '/';
PLUS                : '+';
MINUS               : '-';
SEMICOLON           : ';';

QUOTE               : '"';
REPEAT              : 'repeat';
TIMES               : 'times';
PRINT               : 'print';

SCANNER             : 'insertInt()'
                    | 'insertText()'
                    | 'insertBoolean()'
                    | 'insertDouble()';

IDENTIFIER          : [a-zA-Z0-9]+;
WS                  : [ \t\r\n]+ -> skip ;
