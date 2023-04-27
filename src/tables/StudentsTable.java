package tables;

import dto.PredicatesData;
import dto.Student;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentsTable extends AbsTables{
    private final static String TABLE_NAME = "Student";

    public StudentsTable(){
        super(TABLE_NAME);
    }

   public List<Student> getStudents(String[] columns, Map<PredicatesData, List<String>> predicates) throws SQLException {
        ResultSet resultSet = getData(columns, predicates);
        List<Student> students = new ArrayList<>();
        while (resultSet.next()){
            Student student = new Student(
                    resultSet.getInt("id"),
                    resultSet.getString("fio"),
                    resultSet.getString("sex"),
                    resultSet.getInt("idGroup")
            );
            students.add(student);
        }
        return students;
   }



}
