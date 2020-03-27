package org.chwin.firefighting.apiserver.sql;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import org.chwin.firefighting.apiserver.QL.SqlUtil;

import java.util.ArrayList;
import java.util.List;

public class sqlPcl {

    public static void main(String[] args) {
     String   sql = " if @v>1  then  call aql('查询表格(操作日志表),条件( 操作人=小新),and(时间=2018),and(事件=登录事件),排序(时间),翻页(页数=7,每页=50);查询表格(tab2)'); end if;";
        // 新建 MySQL Parser
        SQLStatementParser parser = new MySqlStatementParser(sql);

        // 使用Parser解析生成AST，这里SQLStatement就是AST
        SQLStatement statement = parser.parseStatement();

        List<SQLStatement> sttList=new ArrayList<>();
        sttList.add(statement);
   //     SqlUtil.printSql4dbg(sttList);
        System.out.println(   SqlUtil.printSql4aqlcall(sttList));

    }
}
