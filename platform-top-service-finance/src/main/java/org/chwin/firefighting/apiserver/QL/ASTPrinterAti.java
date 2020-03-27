package org.chwin.firefighting.apiserver.QL;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import antlr.collections.AST;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.hql.internal.ast.tree.DisplayableNode;
import org.hibernate.hql.internal.ast.util.ASTUtil;
import org.hibernate.internal.util.StringHelper;

@Slf4j
public class ASTPrinterAti {

    public JqplAst2SqlAstConverterExt jqplAst2SqlAstConverter1 = new JqplAst2SqlAstConverterExt();
    private final Map<Integer, String> tokenTypeNameCache;

    public ASTPrinterAti(Class tokenTypeConstants) {
        this.tokenTypeNameCache = ASTUtil.generateTokenNameCache(tokenTypeConstants);
    }

    public String showAsString(AST ast, String header) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        ps.println(header);
        this.showAst(ast, ps);
        ps.flush();
        return new String(baos.toByteArray());
    }

    public void showAst(AST ast, PrintStream out) {
        this.showAst(ast, new PrintWriter(out));
    }

    public void showAst(AST ast, PrintWriter pw) {
        ArrayList<AST> parents = new ArrayList();
        this.showAst(parents, pw, ast);
        pw.flush();
    }

    public String getTokenTypeName(int type) {
        Integer typeInteger = type;
        String value = (String) this.tokenTypeNameCache.get(typeInteger);
        if (value == null) {
            value = typeInteger.toString();
        }

        return value;
    }

    private void showAst(ArrayList<AST> parents, PrintWriter pw, AST ast) {
        if (ast == null) {
            pw.println("AST is null!");
        } else {
            Iterator var4 = parents.iterator();

            AST child;
            while (var4.hasNext()) {
                child = (AST) var4.next();
                if (child.getNextSibling() == null) {
                    pw.print("   ");
                } else {
                    pw.print(" | ");
                }
            }

            if (ast.getNextSibling() == null) {
                pw.print(" \\-");
            } else {
                pw.print(" +-");
            }

            this.showNode(pw, ast);
            ArrayList<AST> newParents = new ArrayList(parents);
            newParents.add(ast);

            for (child = ast.getFirstChild(); child != null; child = child.getNextSibling()) {
                this.showAst(newParents, pw, child);
            }

            newParents.clear();
        }
    }

    private void showNode(PrintWriter pw, AST ast) {
        String s = this.nodeToString(ast);
        pw.println(s);
    }

    public String nodeToString(AST ast) {
        if (ast == null) {
            return "{node:null}";
        } else {
            StringBuilder buf = new StringBuilder();
            String tokenTypeName = this.getTokenTypeName(ast.getType());
            jqplAst2SqlAstConverter1.processTokenTypename(tokenTypeName);


            buf.append("[").append(tokenTypeName).append("] ");
            buf.append(StringHelper.unqualify(ast.getClass().getName())).append(": ");
            buf.append("'");
            String text = ast.getText();
            if (text == null) {
                text = "{text:null}";
            }
            if (text.equals("c11"))
                System.out.println(ast.getNextSibling());

            jqplAst2SqlAstConverter1.processByTokentypenameAndTxt(tokenTypeName, text, ast.getNextSibling());


            appendEscapedMultibyteChars(text, buf);
            buf.append("'");
            if (ast instanceof DisplayableNode) {
                DisplayableNode displayableNode = (DisplayableNode) ast;
                buf.append(" ").append(displayableNode.getDisplayText());
            }

            return buf.toString();
        }
    }


    public static void appendEscapedMultibyteChars(String text, StringBuilder buf) {
        char[] chars = text.toCharArray();
        char[] var3 = chars;
        int var4 = chars.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            char aChar = var3[var5];
            if (aChar > 256) {
                buf.append("\\u");
                buf.append(Integer.toHexString(aChar));
            } else {
                buf.append(aChar);
            }
        }

    }

    public static String escapeMultibyteChars(String text) {
        StringBuilder buf = new StringBuilder();
        appendEscapedMultibyteChars(text, buf);
        return buf.toString();
    }
}
