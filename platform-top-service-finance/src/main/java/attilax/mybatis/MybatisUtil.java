package attilax.mybatis;


import lombok.SneakyThrows;
import ognl.OgnlException;
import org.chwin.firefighting.apiserver.data.MybatisUtil4game;

import java.io.IOException;

//  attilax.mybatis.MybatisUtil
public class MybatisUtil {

    public static void main(String[] args) throws Exception {
        System.out.println(setNextProcess("aa"));
    }
//@SneakyThrows
    private static Object setNextProcess(String selectid_processName) throws  Exception {
        System.out.println( MybatisUtil4game.selectList(selectid_processName,null));
        return true;
    }
}
