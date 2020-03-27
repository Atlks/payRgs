package org.chwin.firefighting.apiserver.test;


import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.armedbear.lisp.Function;
import org.armedbear.lisp.Interpreter;
import org.armedbear.lisp.Packages;
import org.armedbear.lisp.Symbol;

import java.io.File;

public class lispT {

    @SneakyThrows
    public static void main(String[] args) {
        Interpreter interpreter = Interpreter.createInstance();

        interpreter.eval("(load \"d:/halo.lisp\")");

        //　“my-lisp-code.lisp”是外部lisp文件的文件名。那么加载后，怎么调用这段Lisp代码中的程序呢？首先，我们要得到一个abcl的Package：

        org.armedbear.lisp.Package defaultPackage = Packages.findPackage("CL-USER");

        //　在这个package里，我们要寻找Lisp代码中的函数，假设该函数叫做lispfunction，那么，代码应该这样写：

        //Symbol Symbol1 = defaultPackage.findAccessibleSymbol("LISPFUNCTION");


        String str = FileUtils.readFileToString(new File("d:\\halo.lisp"), "gbk");
        Symbol Symbol1 = defaultPackage.findAccessibleSymbol("fun666");


        //　根据这个Symbol对象，我们可以创建一个Fucntion对象并且运行，这里假设目标函数没有输入参数：

        Function myFunction = (Function) Symbol1.getSymbolFunction();
        myFunction.execute();

       // defaultPackage.findacc
    }
}
