package attilax.mybatis;

import attilax.sqlImpt.ConnectionImp;
import attilax.sqlImpt.PreparedStatementImp;
import attilax.sqlImpt.StatementImp;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

//   attilax.mybatis.MysqlDriver4dbg
public class MysqlDriver4dbg  implements Driver {


    static {
        try {
            DriverManager.registerDriver(new MysqlDriver4dbg());
        } catch (SQLException var1) {
            throw new RuntimeException("Can't register driver!");
        }
    }
    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return new ConnectionImp() ;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return false;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
