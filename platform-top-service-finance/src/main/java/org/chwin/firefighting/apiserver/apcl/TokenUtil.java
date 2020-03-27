package org.chwin.firefighting.apiserver.apcl;

import com.alibaba.druid.sql.ast.SQLStatement;
import jodd.util.function.Consumers;
import net.sf.jsqlparser.parser.Token;

import java.util.List;
import java.util.function.Consumer;

public class TokenUtil {
    public static void foreach(List<?> li) {
        li.forEach(new Consumer<Object>() {
            @Override
            public void accept(Object token) {

                if(token instanceof SQLStatement)
                {
                    SQLStatement SQLStatement1= (SQLStatement) token;
                    SQLStatement1.setAfterSemi(true);
                }
                System.out.println(token);
            }
        });
    }
}
