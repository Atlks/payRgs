package attilax.sqlImpt;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class mysqlJDbcT {

    public static void main(String[] args) throws Exception {
        //1.加载驱动
        Class.forName("attilax.mybatis.MysqlDriver4dbg");

        String url = "jdbc:atidb://xxx.db";

        String user = "root";

        String password = "123456";
        //2.建立连接
        Connection connections = DriverManager.getConnection(url, user, password);
        //返回连接对象
        //3.准备SQL语句
        Statement pStatement = connections.createStatement(  );
        if(pStatement instanceof  StatementImp)
        {
            StatementImp PreparedStatementImp1= (StatementImp) pStatement;
            PreparedStatementImp1.li_full =(new ArrayList(){{
                this.add(    new HashMap(){{
                    this.put("age",18);
                }});
                this.add(    new HashMap(){{
                    this.put("age",19);
                }});
            }});
        }
        //4.执行SQL语句
        ResultSet resultSet = pStatement.executeQuery("select 1");


        while (resultSet.next()) {

            System.out.println(resultSet.getString("age"));

        }

    }
}
