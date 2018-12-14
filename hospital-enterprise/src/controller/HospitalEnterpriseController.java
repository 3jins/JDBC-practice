package controller;

import model.*;
import view.HospitalEnterpriseView;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HospitalEnterpriseController {
    private Scanner scanner = new Scanner(System.in);
    private QueryController queryController = new QueryController();
    private HospitalEnterpriseView view = new HospitalEnterpriseView();

    private int userIntegerInput = 0;
    private String userLineInput = "";

    private boolean isSignedIn = false;
    private String userRole = "unknown";
    private String userId = "";
    private String userName = "";
    private int departmeentId = 0;


    public void select(int lowerLimit, int upperLimit) {
        String errorMessage = "[!] Only numbers between " + lowerLimit + " and " + upperLimit + " are permitted as an input.";
        boolean infiniteLoopOn = true;
        while (infiniteLoopOn) {
            try {
                userIntegerInput = scanner.nextInt();
                if (userIntegerInput > upperLimit || userIntegerInput < lowerLimit) {
                    System.out.println(errorMessage);
                    view.showSelectionViewShell();
                    continue;
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println(errorMessage);
                view.showSelectionViewShell();
                continue;
            }
            infiniteLoopOn = false;
        }
    }

    public void numberInput(int lowerLimit, int upperLimit, String inputTitle) {
        String errorMessage = "[!] Only numbers between " + lowerLimit + " and " + upperLimit + " are permitted as an input.";
        if (upperLimit < 0) {
            errorMessage = "[!] Only numbers upper than or equals to " + lowerLimit + " are permitted as an input.";
            upperLimit = Integer.MAX_VALUE;
        }
        boolean infiniteLoopOn = true;
        while (infiniteLoopOn) {
            try {
                view.showInputView(inputTitle);
                userIntegerInput = scanner.nextInt();
                if (userIntegerInput > upperLimit || userIntegerInput < lowerLimit) {
                    System.out.println(errorMessage);
                    view.showInputView(inputTitle);
                    continue;
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println(errorMessage);
                view.showInputView(inputTitle);
                continue;
            }
            infiniteLoopOn = false;
        }
    }

    public void initMainMenu() {
        String[] mainSelectionItems = view.getMainSelectionItems();
        view.showSelectionView(mainSelectionItems);
        select(1, mainSelectionItems.length);

        switch (userIntegerInput) {
            case 1: // sign in/out
                if (isSignedIn) {
                    signOut();
                    initMainMenu();
                } else {
                    signIn();
                    initMainMenu();
                }
                break;
            case 2:
                if (isSignedIn) {
                    checkAppointments();
                    initMainMenu();
                } else {
                    signUp();
                    initMainMenu();
                }
                break;
            case 3:
                switch (userRole) {
                    case "patient":
                        payBill();
                        initMainMenu();
                        break;
                    case "doctor":
                        claimBill();
                        initMainMenu();
                        break;
                    case "nurse":
                        addAppointment();
                        initMainMenu();
                        break;
                    default:
                        System.out.println("Something\'s wrong.");
                        return;
                }
                break;
            default:
                System.out.println("Something\'s wrong.");
                return;
        }
    }

    public void signIn() {
        String roles[] = view.getRoles();
        view.showSelectionView(roles);
        select(1, roles.length);
        switch (userIntegerInput) {
            case 1:
                userRole = "patient";
                break;
            case 2:
                userRole = "doctor";
                break;
            case 3:
                userRole = "nurse";
                break;
            default:
                userRole = "unknown";
                break;
        }
        scanner.nextLine();
        view.showInputView("ID");
        userLineInput = scanner.nextLine();
        this.userId = userLineInput;
        // TODO(3jin): Apply hash function to the given password
        view.showInputView("password");
        userLineInput = scanner.nextLine();
        String userPassword = userLineInput;

        switch (userRole) {
            case "patient":
                Patient patient = (Patient) queryController.signIn(userRole, userId, userPassword);
                userName = patient.getName();
                break;
            case "doctor":
                Doctor doctor = (Doctor) queryController.signIn(userRole, userId, userPassword);
                userName = doctor.getName();
                departmeentId = doctor.getDepartmentId();
                break;
            case "nurse":
                Nurse nurse = (Nurse) queryController.signIn(userRole, userId, userPassword);
                userName = nurse.getName();
                departmeentId = nurse.getDepartmentId();
                break;
            default:
                break;
        }
        isSignedIn = true;
        view.setStates(isSignedIn, userRole);
        view.showGreeting(userName, userRole);
    }

    public void signOut() {
        userRole = "unknown";
        userId = "";
        userName = "";
        departmeentId = 0;
        isSignedIn = false;
        view.setStates(isSignedIn, userRole);
        view.notifySignOut();
    }

    public void signUp() {
        // TODO(3jin): Implement this
        System.out.println("There is no sign up function yet.");
        System.out.println("Below are users list.");
        System.out.println("==== patients ====");
        System.out.println("id: p1, pw: 1");
        System.out.println("id: p2, pw: 2");
        System.out.println("==== doctors ====");
        System.out.println("id: d1, pw: 1");
        System.out.println("id: d2, pw: 2");
        System.out.println("==== nurses ====");
        System.out.println("id: n1, pw: 1");
        System.out.println("id: n2, pw: 2");
        System.out.println();
    }

    public void checkAppointments() {
        ArrayList<Appointment> appointments = queryController.checkAppointments(userRole, userId, departmeentId);
        int numAppointments = appointments.size();

        System.out.println();
        for (int i = 0; i < numAppointments; i++) {
            Appointment appointment = appointments.get(i);
            int timeSlot = appointment.getTimeSlot();
            System.out.println("<Appointment " + (i + 1) + ">");
            System.out.println(" time: " + (timeSlot / 4) + ":" + (timeSlot % 4 * 15));
            System.out.println(" doctor: " + appointment.getDoctorName());
            System.out.println(" patient: " + appointment.getPatientName());
            if (!userRole.equals("nurse")) {
                System.out.println(" bill: " + appointment.getBill());
            }
            System.out.println();
        }
        if (numAppointments == 0) System.out.println("There is no appointment yet.");
    }

    public void addAppointment() {
        ArrayList<Appointment> appointments = queryController.checkAppointments(userRole, userId, departmeentId);
        int numAppointments = appointments.size();
        // TODO(3jin): Remove flags
        int timeSlot = 0;
        System.out.println("Input time for new appointment");
        boolean infiniteLoopOn = true;
        while (infiniteLoopOn) {
            numberInput(0, 23, "hour");
            timeSlot = userIntegerInput * 4;
            numberInput(0, 3, "quarter(15minutes)");
            timeSlot += userIntegerInput;
            boolean isDuplicated = false;
            for (int i = 0; i < numAppointments; i++) {
                Appointment appointment = appointments.get(i);
                if (timeSlot == appointment.getTimeSlot()) {
                    System.out.println("Duplicated! Choose another time please.");
                    timeSlot = 0;
                    isDuplicated = true;
                    break;
                }
            }
            if (!isDuplicated) infiniteLoopOn = false;
        }
        scanner.nextLine();
        view.showInputView("doctor id");
        userLineInput = scanner.nextLine();
        String doctorId = userLineInput;
        view.showInputView("patient id");
        userLineInput = scanner.nextLine();
        String patientId = userLineInput;
        if(!queryController.makeAppointment(doctorId, patientId, timeSlot, departmeentId)) {
            view.notifyNoDoctor(doctorId);
        }
    }

    public void claimBill() {
        checkAppointments();
        ArrayList<Appointment> appointments = queryController.checkAppointments(userRole, userId, departmeentId);
        int numAppointments = appointments.size();
        System.out.println("Choose an appointment");
        view.showSelectionViewShell();
        select(1, numAppointments);
        int appointmentIdx = userIntegerInput - 1;
        numberInput(0, -1, "bill");
        int bill = userIntegerInput;
        queryController.claimBill(appointments.get(appointmentIdx).getAppointmentId(), bill);
    }

    public void payBill() {
        checkAppointments();
        ArrayList<Appointment> appointments = queryController.checkAppointments(userRole, userId, departmeentId);
        int numAppointments = appointments.size();
        System.out.println("Choose an appointment");
        view.showSelectionViewShell();
        select(1, numAppointments);
        int appointmentIdx = userIntegerInput - 1;
        queryController.payBill(appointments.get(appointmentIdx).getAppointmentId());
        view.notifyBillCompleted();
    }

    public void closeConnection() {
        queryController.closeConnection();
    }
}
