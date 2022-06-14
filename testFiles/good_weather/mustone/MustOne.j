.bytecode 49.0
.class public MustOne
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
.limit stack 50
.limit locals 50
ldc 3
ldc 5
ldc 6
imul
ldc 5
ldc 2
imul
idiv
iadd
ldc 6
isub
istore 2
iload 2
ldc 10
iadd
istore 2
getstatic java/lang/System/out Ljava/io/PrintStream;
iload 2
invokevirtual java/io/PrintStream/println(I)V
return
.end method
