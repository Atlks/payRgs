package attilax.sqlImpt;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PreparedStatementImp extends PreparedStatementImpBase {


    public PreparedStatementImp(String sql) {
        super();
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return new ResultSetImp(li);
    }

    List li;

    public void setList(ArrayList arrayList) {
        li = arrayList;
    }
}
