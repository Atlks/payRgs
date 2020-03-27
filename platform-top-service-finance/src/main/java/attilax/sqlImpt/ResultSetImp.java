package attilax.sqlImpt;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class ResultSetImp extends ResultSetImpbase {

    List<Map> li_selected;

    public ResultSetImp(List li_selected2) {

        this.li_selected = li_selected2;
    }
    int curLineCursor=0;

    @Override
    public boolean next() throws SQLException {
        curLineCursor++;
        if (curLineCursor > li_selected.size() )
            return false;
        else {
            return true;
        }
    }

    public String getString(String columnlab) {
        Map line = li_selected.get(curLineCursor-1);
        return   line.get(columnlab).toString();
    }

}
