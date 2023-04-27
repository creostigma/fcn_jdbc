package tables;

import db.DBConnector;
import db.IDBConnector;
import dto.PredicatesData;
import dto.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbsTables {

    private IDBConnector idbConnector = new DBConnector();
    private String tableName;

    public AbsTables(String tableName){
        this.tableName = tableName;

    }

    private String convertMapColumnsToString(Map<String, String> columns){
        return columns.entrySet().stream().map((Map.Entry entry) -> String.format("%s %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(", "));
    }

    public void create(Map<String, String> columns){
        String sqlRequest = String.format("create table %s (%s);", this.tableName, convertMapColumnsToString(columns));
        idbConnector.executeRequest(sqlRequest);
    }

    public void close(){
        idbConnector.close();
    }

    public void delete(){
        idbConnector.executeRequest(String.format("drop table if exists %s", this.tableName));
    }


    public ResultSet getData(String[] columns, Map<PredicatesData, List<String>> predicates){
        String columnsRequest = columns.length == 0 ? "*" : String.join(", ", columns);
        String result = "";

        if (predicates.size() ==1 ) {
            if (predicates.entrySet().size() == 1) {
                List<Map.Entry<PredicatesData, List<String>>> predicateList = new ArrayList<>();
                predicateList.addAll(predicates.entrySet());
                if (predicateList.get(0).getValue().size() == 1) {
                    result = predicateList.get(0).getValue().get(0);
                }
            }
        }

        for(Map.Entry<PredicatesData, List<String>> predicate: predicates.entrySet()) {
            if (predicate.getValue().size() > 1) {
                result += String.join(String.format(" %s ", predicate.getKey().name()), predicate.getValue());
            }
        }

        for(Map.Entry<PredicatesData, List<String>> predicate: predicates.entrySet()){
            if(predicate.getValue().size() == 1){
                result += String.format("%s %s", predicate.getKey(),  predicate.getValue());
            }

        }

        if (!result.isEmpty()){
            result = "where " + result;
        }

        return idbConnector.execute(String.format("select %s from %s %s", columnsRequest, tableName, result));
    }

    private String getResultPredicates(String[] predicates, PredicatesData predicatesData){
        return String.format("%s", String.join(String.format(" %s ", predicatesData.name()), predicates));
    }

    public void addForeignKey(String columnName, String foreignTableName, String foreignColumnName) {
        String sqlRequest = String.format("ALTER TABLE %s ADD FOREIGN KEY (%s) REFERENCES %s(%s);",
                this.tableName, columnName, foreignTableName, foreignColumnName);
        idbConnector.executeRequest(sqlRequest);
    }

    public void insert(Map<String, Object> values) {
        StringBuilder columns = new StringBuilder();
        StringBuilder value = new StringBuilder();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (columns.length() > 0) {
                columns.append(", ");
                value.append(", ");
            }
            columns.append(entry.getKey());
            value.append(entry.getValue());
        }

        String query = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns.toString(), value.toString());
        idbConnector.executeRequest(query);
    }



}
