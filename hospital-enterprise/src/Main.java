import controller.HospitalEnterpriseController;

public class Main {
    public static void main(String args[]) {
        HospitalEnterpriseController controller = new HospitalEnterpriseController();
        controller.initMainMenu();
        controller.closeConnection();
    }
}
