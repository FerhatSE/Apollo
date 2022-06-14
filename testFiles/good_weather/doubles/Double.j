.bytecode 49.0
.class public Double
.super java/lang/Object

.method public static divide(DD)D
.limit stack 99
.limit locals 99
dload 0
dload 2
ddiv
dreturn
.end method
.method public static main([Ljava/lang/String;)V
.limit stack 50
.limit locals 50
ldc2_w 0.00
dstore 2
ldc2_w 10.00
dstore 2
ldc2_w 5.00
dstore 4
dload 2
dload 4
invokestatic Double/divide(DD)D
dstore 6
getstatic java/lang/System/out Ljava/io/PrintStream;
dload 6
invokevirtual java/io/PrintStream/println(D)V
new java/util/Scanner
dup
getstatic java/lang/System/in Ljava/io/InputStream;
invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
astore 10
aload 10
invokevirtual java/util/Scanner/nextDouble()D
dstore 8
getstatic java/lang/System/out Ljava/io/PrintStream;
dload 8
invokevirtual java/io/PrintStream/println(D)V
ldc 10
istore 12
ldc 20
istore 13
return
.end method
