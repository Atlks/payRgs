package org.chwin.firefighting.apiserver.QL;

import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.SelectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class atiql2SqlAstConverterSelectDelete extends atiql2sqlAstConverterBase {
    Delete Deletestt1 = new Delete();
    private Function atiqlAst1;

    atiql2SqlAstConverterSelectDelete() {

    }

    public static void main(String[] args) {
        String s = "查询表格(操作日志表),条件( 操作人=小新),and(时间=2018),查询字段(操作人,时间,事件),排序(时间),翻页(页数=1,每页=20)";
        s = "删除记录(操作日志表),条件( 操作人id=123)";
        Function fun = astiqlAstBuilder.atiql2ast(s);
        System.out.println(fun);
        Statement stt = new atiql2SqlAstConverterSelectDelete().toSqlAst(fun);
        System.out.println(stt.toString());
    }

    public Select select_where_and = SelectUtils.buildSelectFromTable(new Table("没啥用"));

    public Statement toSqlAst(Function atiqlAst) {

        super.traveTree(atiqlAst);
        atiql2SqlAstConverterSelectDelete c = new atiql2SqlAstConverterSelectDelete();
        // String table = traveNodes(atiqlAst);

//        PlainSelect ps=  new PlainSelect();
//        ps.    setFromItem(new Table(table));
        //     select_where_and.setSelectBody(ps );

        and_processFinaly();
        // Deletestt1.setWhere(stack.pop());

        return Deletestt1;
    }


    public void and_processFinaly() {
        if (andList.size() == 1) {
            Deletestt1.setWhere(andList.get(0));
        } else {
            and_reduce();
            Deletestt1.setWhere(stack.pop());

        }
    }


    public void traveTree_subprocess(Function f) {
        if (f.getName().equals("删除记录")) {
            StringValue expression = (StringValue) f.getParameters().getExpressions().get(0);
            tab = expression.getValue();
            Deletestt1.setTable(new Table(tab));
            return;
        }


        return;
    }

    //List<Expression> andList = new ArrayList<>();
    String tab = "";


}
