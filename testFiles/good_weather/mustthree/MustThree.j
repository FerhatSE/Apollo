.bytecode 49.0
.class public MustThree
.super java/lang/Object

.method public static main([Ljava/lang/String;)V
.limit stack 99
.limit locals 99
ldc 0
istore 1
ldc 0
istore 2
new java/util/Scanner
dup
getstatic java/lang/System/in Ljava/io/InputStream;
invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
astore 3
aload 3
invokevirtual java/util/Scanner/nextInt()I
istore 2
ldc 0
istore 4
aload 3
invokevirtual java/util/Scanner/nextInt()I
istore 4
iload 2
iload 4
if_icmpgt trueLabel_1170794006
iconst_0
goto stopLabel_532445947
trueLabel_1170794006:
iconst_1
goto stopLabel_532445947
stopLabel_532445947:
iload 4
ldc 0
if_icmplt trueLabel_2101440631
iconst_0
goto stopLabel_1975358023
trueLabel_2101440631:
iconst_1
goto stopLabel_1975358023
stopLabel_1975358023:
iadd
ifne true2109957412
iconst_0
goto stop901506536
true2109957412:
iconst_1
stop901506536:
istore 1
iload 2
iload 4
if_icmpgt trueLabel_1513712028
iconst_0
goto stopLabel_747464370
trueLabel_1513712028:
iconst_1
goto stopLabel_747464370
stopLabel_747464370:
iload 4
ldc 0
if_icmplt trueLabel_1456208737
iconst_0
goto stopLabel_1018547642
trueLabel_1456208737:
iconst_1
goto stopLabel_1018547642
stopLabel_1018547642:
iadd
iconst_2
if_icmpeq true288665596
iconst_0
goto stop13648335
true288665596:
iconst_1
stop13648335:
istore 1
iload 1
ifne trueLabel312116338
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "false"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
goto falseLabel312116338
trueLabel312116338:
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "true"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
falseLabel312116338:
return
.end method

