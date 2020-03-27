package attilax.sqlImpt;



import java.sql.*;
import java.util.List;

public class StatementImp  extends  StatementImpbase{


    List li_full;
    public ResultSet executeQuery(String sql) throws SQLException {

        //li_selected.query (sql)
        List li_select=li_full;
        return new ResultSetImp(li_select);
    }



}
