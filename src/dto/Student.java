package dto;

public class Student {
    private int id = -1;
    private String fio = "";
    private String sex = "";
    private int idGroup = -1;

    public Student(){
        this.id = id;
        this.fio = fio;
        this.sex = sex;
        this.idGroup = idGroup;
    }

    public Student(int id, String fio, String sex, int idGroup) {
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return fio;
    }

    public String getSex() {
        return sex;
    }

    public int getIdGroup(){
        return idGroup;
    }


}
