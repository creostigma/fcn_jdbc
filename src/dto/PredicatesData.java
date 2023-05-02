package dto;

public enum PredicatesData {
    AND("AND"),
    OR("OR");

    private final String sqlOperator;

    PredicatesData(String sqlOperator) {
        this.sqlOperator = sqlOperator;
    }

    public String getSqlOperator() {
        return sqlOperator;
    }
}
