package tables;

import dto.Curator;
import dto.PredicatesData;
import dto.Student;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CuratorsTable extends AbsTables{
    private final static String TABLE_NAME = "Curator";

    public CuratorsTable(){
        super(TABLE_NAME);
    }



    public List<Curator> getCurators(String[] columns, Map<PredicatesData, List<String>> predicates, Map<String, String> join) throws SQLException {
        ResultSet resultSet = getData(columns, predicates, join);
        List<Curator> curators = new ArrayList<>();
        while (resultSet.next()){
            int id = -1;
            String fio = "";

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String column = metaData.getColumnName(i);
                switch (column) {
                    case "id":
                        id = resultSet.getInt(column);
                        break;
                    case "fio":
                        fio = resultSet.getString(column);
                        break;
                }
            }

            Curator curator = new Curator(id, fio);
            curators.add(curator);
        }
        return curators;
    }
}
