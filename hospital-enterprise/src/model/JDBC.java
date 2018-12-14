package model;

import security.SQLInjectionSentinel;

import java.sql.*;

public class JDBC {
    private static JDBC instance = null;
    private Statement smt = null;
    private Connection conn = null;
    private SQLInjectionSentinel sqlInjectionSentinel = new SQLInjectionSentinel();

    private JDBC() {
        String id = "user_201311308";
        String pw = "201311308";
        String url = "jdbc:mysql://117.16.137.108:3306/" +
                id + "?serverTimezone=Asia/Seoul&useUnicode=true&characterEncoding=utf8";
        try {
            conn = DriverManager.getConnection(url, id, pw);
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    public static JDBC getInstance() {
        if (instance == null) instance = new JDBC();
        return instance;
    }

    public ResultSet sendQuery(String query) {
        ResultSet resultSet = null;
        query = sqlInjectionSentinel.refineQuery(query);
        try {
            smt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = smt.executeQuery(query);
            if (resultSet.isAfterLast()) {
                System.out.println("Cursor is pointing to the last row");
                resultSet.first();
            }
        } catch (SQLException e) {
            System.err.println("Failed to send a query to the database: " + e.getMessage());
        }
        return resultSet;
    }

    public void closeConnection() {
        try {
            conn.close();
            if (smt != null) smt.close();
        } catch (SQLException e) {
            System.err.println("closeConnection: " + e.getMessage());
        }
    }

    public void showResults(ResultSet rs) {
        try {
            rs.first();
            rs.previous();
            while (rs.next()) {
                String id = rs.getString(1);
                int cred = rs.getInt(2);
                System.out.println(id + " | " + cred);
            }
        } catch (SQLException e) {
            System.err.println("showResults: " + e.getMessage());
        }
    }
}
