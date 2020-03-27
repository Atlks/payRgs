package org.chwin.firefighting.apiserver.sql;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLoadDataInFileStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlLexer;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.baomidou.mybatisplus.core.enums.SqlMethod;

import java.util.ArrayList;

public class DruidSqlParseUtil {

    public static void main(String[] args) {
        String sql = "select * from user order by id";

        sql = "set @v=1;select @v;";
        sql = "set @v=1;select @v;";

        sql = "  DECLARE CONTINUE HANDLER FOR NOT FOUND SET @NOT_FOUND=1; ";
        sql = "set @v='a';select @v;";
       // sql = " select @v ";
        // else   aql(测试语句2;)
        sql = " if @v>1  then  call aql('查询表格(操作日志表),条件( 操作人=小新)'); end if;";
     //   sql="select fun1(11,'aa'),fun2();set @v='a';";
    //    sql="  if a>1 then select 1; else select 2; end if;";

        MySqlLexer o;
        //  MySqlLoadDataInFileStatement
     //   SQLMethodInvokeExpr

        // 新建 MySQL Parser
        SQLStatementParser parser = new MySqlStatementParser(sql);

        // 使用Parser解析生成AST，这里SQLStatement就是AST
        SQLStatement statement = parser.parseStatement();

        // 使用visitor来访问AST
        MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
        statement.accept(visitor);

        System.out.println(visitor.getColumns());
        System.out.println(visitor.getOrderByColumns());

        SQLSetStatement setStt = new SQLSetStatement();
        setStt.setItems(new ArrayList() {{

            this.add(new SQLAssignItem() {{
                SQLVariantRefExpr target = new SQLVariantRefExpr("@a");
                //    target.setGlobal(true);
                target.setSession(true);
                setTarget(target);
                setValue(new SQLIntegerExpr(666));
            }});


            this.add(new SQLAssignItem() {{
                SQLVariantRefExpr target = new SQLVariantRefExpr("b");
                //    target.setGlobal(true);
                target.setSession(true);
                setTarget(target);
                setValue(new SQLIntegerExpr(777));
            }});

        }});
        System.out.println(setStt);

    }
}
