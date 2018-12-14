package model;

public class Doctor extends User {
    private int departmentId;

    public Doctor(String id, String name, int departmentId) {
        super(id, name);
        this.departmentId = departmentId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}
