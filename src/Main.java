import db.IDBConnector;
import dto.Curator;
import dto.PredicatesData;
import dto.Student;
import tables.CuratorsTable;
import tables.GroupsTable;
import tables.StudentsTable;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

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
        put("fioCurator", "varchar(255)");
    }};

    private static Map<String, String> columnsGroupsTable = new HashMap<String, String>(){{
        put("id", "int primary key AUTO_INCREMENT");
        put("name", "varchar(255)");
        put("id_curator", "int");
    }};

    public static void main(String[] args) {

        StudentsTable studentsTable = null;
        CuratorsTable curatorsTable = null;
        GroupsTable groupsTable = null;

        try {
            studentsTable = new StudentsTable();
            curatorsTable = new CuratorsTable();
            groupsTable = new GroupsTable();

            Faker faker = new Faker();

            curatorsTable.create(columnsCuratorsTable);
            groupsTable.create(columnsGroupsTable);
            studentsTable.create(columnsStudentTable);
            groupsTable.addForeignKey("id_curator", "Curator", "id");
            studentsTable.addForeignKey("id_group", "Class", "id");

            //faker для кураторов
            for (int i = 0; i <= 4; i++) {
                String fio = "\"" + faker.name().fullName() + "\"";

                Map<String, Object> values = new HashMap<>();
                values.put("id", null);
                values.put("fioCurator", fio);

                curatorsTable.insert(values);
            }


            //faker для групп
            for (int i = 0; i <= 2; i++) {
                String name = "\"" + faker.ancient().god() + "\"";
                int id_curator = faker.number().numberBetween(1, 5);
                Map<String, Object> values = new HashMap<>();
                values.put("id", null);
                values.put("name", name);
                values.put("id_curator", id_curator);

                groupsTable.insert(values);
            }

            //faker для учеников
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

            //первый инфо о всех студентах с группой и кураторов
            String[] columns = new String[3];
            columns[0] = "Student.fio";
            columns[1] = "Class.name";
            columns[2]= "Curator.fioCurator";

            Map<PredicatesData, List<String>> predicates = new HashMap<>();
            List<String> predicateValue = new ArrayList<>();
//            predicateValue.add("sex=\"Female\"");
//            predicateValue.add("id_group=\"2\"");
//           predicates.put(PredicatesData.AND, predicateValue);
           System.out.println(predicates);
            Map<PredicatesData, List<String>> predicates1 = new HashMap<>();


            Map<String, String> join = new HashMap<>();
            join.put("Class", "Student.id_group = Class.id");
            join.put("Curator", "Class.id_curator = Curator.id");

            ResultSet resultSet = studentsTable.getData(columns, predicates, join);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnValue = resultSet.getString(i);
                    System.out.print(rsmd.getColumnName(i) + ": " + columnValue + ", ");
                }
                System.out.println();
            }


            //кол-во студентов
            int count = studentsTable.getStudentsCount();
            System.out.println("Количество студентов: " + count);

            //кол-во студенток
            System.out.println("Список студенток:");
            String[] columns1 = new String[0];
            List<String> predicateValue1 = new ArrayList<>();
            predicateValue1.add("sex=\"Female\"");
            predicates.put(PredicatesData.AND, predicateValue1);
            List<Student> femaleStudents = studentsTable.getStudents(columns1, predicates, join);
            femaleStudents.forEach(System.out::println);

            //Обновление таблицы куратор
            Map<String, Object> values = new HashMap<>();
            String fio = "\"" + faker.name().fullName() + "\"";
            values.put("fioCurator", fio);

            Map<PredicatesData, List<String>> predicates2 = new HashMap<>();
            List<String> predicateValue2 = new ArrayList<>();
            predicateValue2.add("id = 1");
            predicates2.put(PredicatesData.AND, predicateValue2);
            curatorsTable.update(values, predicates2);

            //Вывод списка групп с их кураторами
            String[] columns3 = new String[3];
            columns3[0] = "Class.id";
            columns3[1] = "Class.name";
            columns3[2]= "Curator.fioCurator";

            List<String> predicateValue3 = new ArrayList<>();
//            predicateValue.add("sex=\"Female\"");
//            predicateValue.add("id_group=\"2\"");
//           predicates.put(PredicatesData.AND, predicateValue);
            Map<PredicatesData, List<String>> predicates3 = new HashMap<>();


            Map<String, String> join3 = new HashMap<>();
            join3.put("Curator", "Class.id_curator = Curator.id");

            ResultSet resultSet3 = groupsTable.getData(columns3, predicates3, join3);
            ResultSetMetaData rsmd3 = resultSet3.getMetaData();
            int columnCount3 = rsmd3.getColumnCount();
            while (resultSet3.next()) {
                for (int i = 1; i <= columnCount3; i++) {
                    String columnValue = resultSet3.getString(i);
                    System.out.print(rsmd3.getColumnName(i) + ": " + columnValue + ", ");
                }
                System.out.println();
            }


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
