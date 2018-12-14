package controller;

import database.JDBC;
import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
        ResultSet rs = db.sendQuery(query, false);
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
                    break;
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

    public ArrayList<Appointment> checkAppointments(String userRole, String userId, int departmentId) {
        ArrayList<Appointment> appointments = new ArrayList<>();
        String query;
        switch(userRole) {
            case "patient":
                query = "SELECT a.appointment_id, d.doctor_id, p.patient_id, d.name, p.name, a.time_slot, a.bill " +
                        "FROM appointment a, doctor d, patient p " +
                        "WHERE a.patient_id = '" + userId + "\' AND d.doctor_id = a.doctor_id AND p.patient_id = a.patient_id " +
                        "ORDER BY a.time_slot";
                break;
            case "doctor":
                query = "SELECT a.appointment_id, d.doctor_id, p.patient_id, d.name, p.name, a.time_slot, a.bill " +
                        "FROM appointment a, doctor d, patient p " +
                        "WHERE a.doctor_id = '" + userId + "\' AND d.doctor_id = a.doctor_id AND p.patient_id = a.patient_id " +
                        "ORDER BY a.time_slot";
                break;
            case "nurse":
                query = "SELECT a.appointment_id, d.doctor_id, p.patient_id, d.name, p.name, a.time_slot, a.bill " +
                        "FROM appointment a, doctor d, patient p " +
                        "WHERE d.department_id = '" + departmentId + "\' AND d.doctor_id = a.doctor_id AND p.patient_id = a.patient_id " +
                        "ORDER BY a.time_slot";
                break;
            default:
                return null;
        }
        ResultSet rs = db.sendQuery(query, false);
        try {
            rs.first();
            rs.previous();
            while (rs.next()) {
                appointments.add(new Appointment(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getInt(6),
                        rs.getInt(7)
                ));
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
        return appointments;
    }

    public void claimBill(int appointmentId, int bill) {
        String query = "UPDATE appointment " +
                "SET bill=" + bill + " " +
                "WHERE appointment_id=" + appointmentId;
        db.sendQuery(query, true);
        // TODO(3jin): Make exception processing code
    }

    public boolean makeAppointment(String doctorId, String patientId, int timeSlot, int departmentId) {
        String query = "SELECT department_id FROM doctor WHERE doctor_id=\'" + doctorId + "\'";
        ResultSet rs = db.sendQuery(query, false);
        try {
            rs.first();
            rs.previous();
            if(rs.next()) {
                if(rs.getInt(1) != departmentId) return false;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }

        query = "INSERT INTO appointment " +
                "(doctor_id, patient_id, time_slot) " +
                "VALUES (\'" + doctorId + "\', \'" + patientId + "\', \'" + timeSlot + "\')";
        db.sendQuery(query, true);
        // TODO(3jin): Make exception processing code
        return true;
    }

    public void payBill(int appointmentId) {
        String query = "DELETE FROM appointment WHERE appointment_id=" + appointmentId;
        db.sendQuery(query, true);
        // TODO(3jin): Make exception processing code
    }

    public void closeConnection() {
        db.closeConnection();
    }
}
