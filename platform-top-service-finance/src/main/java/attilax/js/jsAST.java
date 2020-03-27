package attilax.js;

import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.FunctionNode;
import jdk.nashorn.internal.ir.IfNode;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.parser.Parser;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ErrorManager;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.options.Options;

import java.util.List;

public class jsAST {

    public static void main(String[] args) {

        IfNode
        Options options = new Options("nashorn");
        options.set("anon.functions", true);
        options.set("parse.only", true);
        options.set("scripting", true);

        ErrorManager errors = new ErrorManager();
        Context context = new Context(options, errors, Thread.currentThread().getContextClassLoader());
//        Source source   = new Source("test", "var a = 10; var b = a + 1;" +
//                "function someFunction() { return b + 1; }  ");
        String js_content = "var a = 10; var b = a + 1;" +
                "function someFunction() { return b + 1; }  ";
        js_content="if(a>1){ aql('then block stt');}  else{ aql('else block stt'); }";
        Source source   =   Source.sourceFor("test", js_content);
        Parser parser = new Parser(context.getEnv(), source, errors);
        FunctionNode functionNode = parser.parse();
        Block block = functionNode.getBody();
        List<Statement> statements = block.getStatements();
        System.out.println(statements);
    }
}
