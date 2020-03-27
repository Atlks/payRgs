package org.chwin.firefighting.apiserver.dsl;

import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.var;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.eclipse.jdt.internal.compiler.ast.IfStatement;

@Data
@Slf4j
//org.chwin.firefighting.apiserver.ds.lombokTest
public class lombokTest {

    public  String m1()
    {
        return "m1v";
    }

    @SneakyThrows
    public static void main(String[] args) {


       // var t="str";
        log.info("T");
    //    IfStatement

    }
}
