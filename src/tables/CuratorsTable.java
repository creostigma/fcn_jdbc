package tables;

import dto.Curator;
import dto.PredicatesData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CuratorsTable extends AbsTables{
    private final static String TABLE_NAME = "Curator";

    public CuratorsTable(){
        super(TABLE_NAME);
    }

    public List<Curator> getCurators(String[] columns, Map<PredicatesData, List<String>> predicates) throws SQLException {
        ResultSet resultSet = getData(columns, predicates);
        List<Curator> curators = new ArrayList<>();
        while (resultSet.next()){
            Curator curator = new Curator();
            curators.add(curator);
        }
        return curators;
    }
}
