package org.chwin.firefighting.apiserver.QL;

import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.SelectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class atiql2SqlAstConverterSelectUpdate extends atiql2sqlAstConverterBase {
    Update updateStat = new Update();
    private Function atiqlAst1;

    atiql2SqlAstConverterSelectUpdate() {
        updateStat.setColumns(new ArrayList<>());
        updateStat.setExpressions(new ArrayList<>());
    }

    public static void main(String[] args) {
        String s = "查询表格(操作日志表),条件( 操作人=小新),and(时间=2018),查询字段(操作人,时间,事件),排序(时间),翻页(页数=1,每页=20)";
        s = "更新记录(操作日志表),设置字段(时间=2019,事件=完成事件),条件( 操作人id=123)";
        Function fun = astiqlAstBuilder.atiql2ast(s);
        System.out.println(fun);
        Statement stt = new atiql2SqlAstConverterSelectUpdate().toSqlAst(fun);
        System.out.println(stt.toString());
    }

    public Select select_where_and = SelectUtils.buildSelectFromTable(new Table("没啥用"));

    public Statement toSqlAst(Function atiqlAst) {

        traveTree(atiqlAst);
        atiql2SqlAstConverterSelectUpdate c = new atiql2SqlAstConverterSelectUpdate();
        // String table = traveNodes(atiqlAst);

//        PlainSelect ps=  new PlainSelect();
//        ps.    setFromItem(new Table(table));
        //     select_where_and.setSelectBody(ps );

        and_processFinaly();
       // Deletestt1.setWhere(stack.pop());

        return updateStat;
    }


    public void and_processFinaly() {
        if (andList.size() == 1) {
            updateStat.setWhere(andList.get(0));
        } else {
            and_reduce();
            updateStat.setWhere(stack.pop());

        }
    }



    public void traveTree_subprocess(Function f) {
        if (f.getName().equals("更新记录")) {
            StringValue expression = (StringValue) f.getParameters().getExpressions().get(0);
            tab = expression.getValue();
            updateStat.setTables(new ArrayList() {{
                add(new Table(tab));

            }});
            return;
        }

        if (f.getName().equals("条件")) {
            super.where_evt(f, null);
            return  ;
        }


        if (f.getName().equals("and")) {
            super.and_evt(f, null);
            return  ;
        }

        if (f.getName().equals("设置字段")) {
            设置字段_setCol_evt(f, null);
            return  ;
        }
        return  ;
    }

    //List<Expression> andList = new ArrayList<>();
    String tab = "";




    protected void 设置字段_setCol_evt(Function f, Object o) {
        List li = f.getParameters().getExpressions();
        li.forEach(new Consumer() {
            @Override
            public void accept(Object o) {
                StringValue sv = (StringValue) o;
                String s = sv.getValue();
                String col = s.split("=")[0];
                String colval = s.split("=")[1];
                updateStat.getColumns().add(new Column(col));
                updateStat.getExpressions().add(SqlUtil.newtStringValue(colval));

            }
        });

    }


}
