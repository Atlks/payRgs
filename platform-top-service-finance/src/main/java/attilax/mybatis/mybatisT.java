package attilax.mybatis;

import lombok.SneakyThrows;
import ognl.OgnlException;
import org.chwin.firefighting.apiserver.data.MybatisUtil4game;

import java.io.IOException;

public class mybatisT {
//    java.sql.
   // @SneakyThrows
    public static void main(String[] args) throws IOException, OgnlException {

    //     com.mysql.jdbc.Driver
        //  attilax.mybatis.MysqlDriver4dbg
        System.out.println( MybatisUtil4game.selectList("if_process",null));
    }
}
