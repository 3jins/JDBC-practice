package controller;

import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryController {
    private JDBC db = JDBC.getInstance();

    public User signIn(String role, String id, String password) {
        User user = null;
        String query = "";
        switch (role) {
            case "patient":
                query = "SELECT name FROM " + role +
                        " WHERE " + role + "_id" + "=\'" + id + "\' and password=\'" + password + "\'";
                break;
            case "doctor":
            case "nurse":
                query = "SELECT name, department_id FROM " + role +
                        " WHERE " + role + "_id" + "=\'" + id + "\' and password=\'" + password + "\'";
                break;
            default:
                return null;
        }
        ResultSet rs = db.sendQuery(query);
        try {
            rs.first();
            rs.previous();
            if (!rs.next()) return null;

            String name = rs.getString(1);
            int departmentId = 0;
            switch (role) {
                case "patient":
                    user = new Patient(id, name);
                    break;
                case "doctor":
                    departmentId = rs.getInt(2);
                    user = new Doctor(id, name, departmentId);
                case "nurse":
                    departmentId = rs.getInt(2);
                    user = new Nurse(id, name, departmentId);
                    break;
                default:
                    return null;
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
        return user;
    }

    public void closeConnection() {
        db.closeConnection();
    }
}
