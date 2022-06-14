.bytecode 49.0
.class public CompleteProgram
.super java/lang/Object

.method public static printValues(IZLjava/lang/String;D)V
.limit stack 99
.limit locals 99
iload 0
ldc 100
if_icmpne true1504109395
iconst_0
goto end2047526627
true1504109395:
iconst_1
goto end2047526627
end2047526627:
ifne trueLabel1873653341
getstatic java/lang/System/out Ljava/io/PrintStream;
iload 0
invokevirtual java/io/PrintStream/println(I)V
goto falseLabel1873653341
trueLabel1873653341:
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "Incorrect"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
falseLabel1873653341:
getstatic java/lang/System/out Ljava/io/PrintStream;
iload 1
invokevirtual java/io/PrintStream/println(Z)V
getstatic java/lang/System/out Ljava/io/PrintStream;
aload 2
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
dload 3
ldc2_w 100.0
dcmpl
ifle true1740000325
iconst_0
goto end1142020464
true1740000325:
iconst_1
goto end1142020464
end1142020464:
ifne trueLabel1627960023
getstatic java/lang/System/out Ljava/io/PrintStream;
dload 3
invokevirtual java/io/PrintStream/println(D)V
goto falseLabel1627960023
trueLabel1627960023:
getstatic java/lang/System/out Ljava/io/PrintStream;
ldc "Incorrect"
invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
falseLabel1627960023:
return
.end method

.method public static incrementInteger(I)I
.limit stack 99
.limit locals 99
iload 0
ldc 1
iadd
ireturn
.end method

.method public static incrementDouble(D)D
.limit stack 99
.limit locals 99
dload 0
ldc2_w 1.0
dadd
dreturn
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 50
.limit locals 50
aload 2
invokevirtual java/util/Scanner/nextInt()I
istore 3
aload 2
invokevirtual java/util/Scanner/nextBoolean()Z
istore 4
aload 2
invokevirtual java/util/Scanner/nextLine()Ljava/lang/String;
astore 5
aload 2
invokevirtual java/util/Scanner/nextDouble()D
dstore 6
begin:
iload 4
aload 5
ldc "Test"
if_acmpeq true3447021
iconst_0
goto end440434003
true3447021:
iconst_1
goto end440434003
end440434003:
iadd
iconst_2
if_icmpeq true1032616650
iconst_0
goto stop99347477
true1032616650:
iconst_1
stop99347477:
iload 3
ldc 100
if_icmplt trueLabel_940553268
iconst_0
goto stopLabel_566034357
trueLabel_940553268:
iconst_1
goto stopLabel_566034357
stopLabel_566034357:
iadd
iconst_2
if_icmpeq true1720435669
iconst_0
goto stop1020923989
true1720435669:
iconst_1
stop1020923989:
dload 6
ldc2_w 100.0
dcmpl
ifle true2052915500
iconst_0
goto end1068934215
true2052915500:
iconst_1
goto end1068934215
end1068934215:
iadd
ifne true127618319
iconst_0
goto stop1798286609
true127618319:
iconst_1
stop1798286609:
iconst_1
if_icmpne end
ldc 0
istore 9
iteration:
iload 9
ldc 10
if_icmpeq end
iload 3
invokestatic CompleteProgram/incrementInteger(I)I
istore 3
dload 6
invokestatic CompleteProgram/incrementDouble(D)D
dstore 6
iinc 9 1
goto iteration
end:

ldc 0
istore 4
ldc "Changed text"
astore 5
goto begin
end:
iload 3
iload 4
aload 5
dload 6
invokestatic CompleteProgram/printValues(IZLjava/lang/String;D)V
return
.end method

