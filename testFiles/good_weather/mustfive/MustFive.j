.bytecode 49.0
.class public MustFive
.super java/lang/Object

.method public static power(II)I
.limit stack 99
.limit locals 99
ldc 1
istore 2
begin:
iload 1
ldc 0
if_icmpgt trueLabel_968514068
iconst_0
goto stopLabel_920011586
trueLabel_968514068:
iconst_1
goto stopLabel_920011586
stopLabel_920011586:
iconst_1
if_icmpne end
iload 2
iload 0
imul
istore 2
iload 1
ldc 1
isub
istore 1
goto begin
end:
iload 2
ireturn
.end method
.method public static main([Ljava/lang/String;)V
.limit stack 50
.limit locals 50
ldc 2
ldc 8
invokestatic MustFive/power(II)I
istore 1
iload 1
ldc 256
if_icmpeq true157627094
iconst_0
goto end932607259
true157627094:
iconst_1
goto end932607259
end932607259:
ifne truelabel
getstatic java/lang/System/out Ljava/io/PrintStream;
iload 1
invokevirtual java/io/PrintStream/println(I)V
goto stoplabel
truelabel:
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "Correct"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
stoplabel:
return
.end method
