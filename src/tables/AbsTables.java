package tables;

import db.DBConnector;
import db.IDBConnector;
import dto.PredicatesData;

import java.sql.ResultSet;
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

    public String formatPredicatesData(Map<PredicatesData, List<String>> predicates) {
        String result = "";
        for (Map.Entry<PredicatesData, List<String>> predicate : predicates.entrySet()) {
            PredicatesData predicateData = predicate.getKey();
            List<String> values = predicate.getValue();
            result += String.join(String.format(" %s ", predicateData.name()), values);
        }
        return result;
    }

    public  ResultSet getData(String[] columns, Map<PredicatesData, List<String>> predicates, Map<String, String> join){
        String columnsRequest = columns.length == 0 ? "*" : String.join(", ", columns);
        String result = "";
        String joinResult = "";

        if (predicates.size() == 1) {
            List<String> predicateValue = predicates.get(PredicatesData.AND);
            if (predicateValue.size() == 1) {
                result = predicateValue.get(0);
            } else {
                formatPredicatesData(predicates);
            }
        } else {
            formatPredicatesData(predicates);
        }

        if(join.size() >= 1){
                for (Map.Entry<String, String> entry : join.entrySet()){
                    joinResult += String.format("join %s on %s ", entry.getKey(), entry.getValue());
                }
        }

        if (!result.isEmpty()){
            result = "where " + result;
        }

        System.out.println(String.format("select %s from %s %s %s", columnsRequest, tableName, joinResult, result));
        return idbConnector.execute(String.format("select %s from %s %s %s", columnsRequest, tableName, joinResult, result));
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

    public void update(Map<String, Object> values, Map<PredicatesData, List<String>> predicates) {
        StringBuilder setClause = new StringBuilder();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (setClause.length() > 0) {
                setClause.append(", ");
            }
            setClause.append(entry.getKey())
                    .append(" = ")
                    .append(entry.getValue());
        }

        String whereClause = "";
        if (predicates != null && !predicates.isEmpty()) {
            StringBuilder predicateBuilder = new StringBuilder();
            boolean firstPredicate = true;
            for (Map.Entry<PredicatesData, List<String>> entry : predicates.entrySet()) {
                List<String> valuesPr = entry.getValue();
                if (!valuesPr.isEmpty()) {
                    if (!firstPredicate) {
                        predicateBuilder.append(" ")
                                .append(entry.getKey().getSqlOperator())
                                .append(" ");
                    }
                    predicateBuilder.append(valuesPr.get(0));
                    firstPredicate = false;
                }
            }
            if (predicateBuilder.length() > 0) {
                whereClause = " WHERE " + predicateBuilder.toString();
            }
        }
        String query = String.format("UPDATE %s SET %s%s", tableName, setClause.toString(), whereClause);
        System.out.println(query);
        idbConnector.executeRequest(query);
    }



}
