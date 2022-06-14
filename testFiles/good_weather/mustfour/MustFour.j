.bytecode 49.0
.class public MustFour
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
.limit stack 50
.limit locals 50
ldc 0
istore 1
ldc 4
istore 1
ldc 0
istore 2
ldc "Wrong answer"
astore 3
new java/util/Scanner
dup
getstatic java/lang/System/in Ljava/io/InputStream;
invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
astore 4
aload 4
invokevirtual java/util/Scanner/nextInt()I
istore 2
begin:
iload 2
iload 1
if_icmpne true1207140081
iconst_1
goto end1510067370
true1207140081:
iconst_0
goto end1510067370
end1510067370:
iconst_1
if_icmpeq end
getstatic java/lang/System/out Ljava/io/PrintStream;
aload 3
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
aload 4
invokevirtual java/util/Scanner/nextInt()I
istore 2
goto begin
end:
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "Correct"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
return
.end method
