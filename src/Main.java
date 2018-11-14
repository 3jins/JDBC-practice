import experimental.JDBC;
import java.sql.ResultSet;

public class Main {
    public static void main(String args[]) {
        JDBC db = JDBC.getInstance();
        ResultSet rs = db.getResultSet();
        db.showResults(rs);
        db.closeConnection();
    }
}
