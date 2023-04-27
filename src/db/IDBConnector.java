package db;

import java.sql.ResultSet;

public interface IDBConnector {
    void executeRequest(String request);
    ResultSet execute(String request);
    void close();
}
