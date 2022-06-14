.bytecode 49.0
.class public MustTwo
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
.limit stack 50
.limit locals 50
ldc 99899
istore 2
new java/util/Scanner
dup
getstatic java/lang/System/in Ljava/io/InputStream;
invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
astore 2
aload 2
invokevirtual java/util/Scanner/nextInt()I
istore 2
iload 2
ldc 5
if_icmpgt trueLabel_1688019098
iconst_0
goto stopLabel_1688019098
trueLabel_1688019098:
iconst_1
stopLabel_1688019098:
istore 3
getstatic java/lang/System/out Ljava/io/PrintStream;
iload 3
invokevirtual java/io/PrintStream/println(Z)V
return
.end method
