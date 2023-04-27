import db.IDBConnector;
import dto.PredicatesData;
import dto.Student;
import tables.CuratorsTable;
import tables.GroupsTable;
import tables.StudentsTable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javafaker.Faker;

public class Main {


    private static Map<String, String> columnsStudentTable = new HashMap<String, String>(){{
        put("id", "int primary key AUTO_INCREMENT");
        put("fio", "varchar(255)");
        put("sex", "enum('Male','Female')");
        put("id_group", "int");
    }};

    private static Map<String, String> columnsCuratorsTable = new HashMap<String, String>(){{
        put("id", "int primary key AUTO_INCREMENT");
        put("fio", "varchar(255)");
    }};

    private static Map<String, String> columnsGroupsTable = new HashMap<String, String>(){{
        put("id", "int primary key AUTO_INCREMENT");
        put("name", "varchar(255)");
        put("id_curator", "int");
    }};

    public static void main(String[] args) {

//        StudentsTable studentsTable = new StudentsTable();
        CuratorsTable curatorsTable = new CuratorsTable();
        GroupsTable groupsTable = new GroupsTable();

        StudentsTable studentsTable = null;
        try {
            studentsTable = new StudentsTable();

            Faker faker = new Faker();

            curatorsTable.create(columnsCuratorsTable);
            groupsTable.create(columnsGroupsTable);
            studentsTable.create(columnsStudentTable);
            groupsTable.addForeignKey("id_curator", "Curator", "id");
            studentsTable.addForeignKey("id_group", "Class", "id");

            for (int i = 0; i <= 4; i++) {
                String fio = "\"" + faker.name().fullName() + "\"";

                Map<String, Object> values = new HashMap<>();
                values.put("id", null);
                values.put("fio", fio);

                curatorsTable.insert(values);
            }

            for (int i = 0; i <= 2; i++) {
                String name = "\"" + faker.ancient().god() + "\"";
                int id_curator = faker.number().numberBetween(1, 5);
                Map<String, Object> values = new HashMap<>();
                values.put("id", null);
                values.put("name", name);
                values.put("id_curator", id_curator);

                groupsTable.insert(values);
            }

            for (int i = 0; i <= 14; i++) {
                String fio = "\"" + faker.name().fullName() + "\"";
                String sex = "\"" + (faker.random().nextBoolean() ? "Male" : "Female") + "\"";
                int id_group = faker.number().numberBetween(1, 3);

                Map<String, Object> values = new HashMap<>();
                values.put("id", null);
                values.put("fio", fio);
                values.put("sex", sex);
                values.put("id_group", id_group);

                studentsTable.insert(values);
            }


            String[] colomns = new String[2];
            colomns[0] = "fio";
            colomns[1] = "id";

            Map<PredicatesData, List<String>> predicates = new HashMap<>();
            List<String> predicateValue = new ArrayList<>();
            predicateValue.add("fio=\"Sasha\"");
            predicateValue.add("id=\"5\"");

            predicates.put(PredicatesData.AND, predicateValue);

            List<Student> students = StudentsTable.getStudents(colomns, predicates);
            students.forEach(System.out::println);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            studentsTable.delete();
            groupsTable.delete();
            curatorsTable.delete();
            groupsTable.close();
            curatorsTable.close();
            studentsTable.close();
        }
    }
}
