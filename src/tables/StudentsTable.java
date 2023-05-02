package tables;

import dto.PredicatesData;
import dto.Student;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentsTable extends AbsTables{
    private final static String TABLE_NAME = "Student";

    public StudentsTable(){
        super(TABLE_NAME);
    }


    public List<Student> getStudents(String[] columns, Map<PredicatesData, List<String>> predicates, Map<String, String> join) throws SQLException {
        ResultSet resultSet = getData(columns, predicates, join);
        List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                int id = -1;
                String fio = "";
                String sex = "";
                int id_group = -1;


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
                        case "sex":
                            sex = resultSet.getString(column);
                            break;
                        case "id_group":
                            id_group = resultSet.getInt(column);
                            break;
                    }
                }

                Student student = new Student(id, fio, sex, id_group);
                students.add(student);
            }

            return students;
        }

    public int getStudentsCount() throws SQLException {
        String[] columns = new String[]{"COUNT(*)"};
        Map<PredicatesData, List<String>> predicates = new HashMap<>();
        Map<String, String> join = new HashMap<>();
        ResultSet resultSet = getData(columns, predicates, join);
        resultSet.next();
        return resultSet.getInt(1);
    }

}
