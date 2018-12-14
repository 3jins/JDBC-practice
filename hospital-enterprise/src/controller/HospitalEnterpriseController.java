package controller;

import model.User;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import view.HospitalEnterpriseView;

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


    public void select(String selectionItems[], int lowerLimit, int upperLimit) {
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

    public void initMainMenu() {
        String mainSelectionItems[] = view.getMainSelectionItems();
        view.showSelectionView(mainSelectionItems);
        select(mainSelectionItems, 0, mainSelectionItems.length);
        switch (userIntegerInput) {
            case 1: // sign in/out
                if (isSignedIn) signOut();
                else signIn();
                break;
            case 2: // sign up
                signUp();
                break;
            default:
                System.out.println("Something\'s wrong.");
                return;
        }
    }

    public void signIn() {
        String roles[] = view.getRoles();
        view.showSelectionView(roles);
        select(roles, 0, roles.length);
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
        String userId = userLineInput;
        // TODO(3jin): Apply hash function to the given password
        view.showInputView("password");
        userLineInput = scanner.nextLine();
        String userPassword = userLineInput;

        User user = queryController.signIn(userRole, userId, userPassword);
        userName = user.getName();
        isSignedIn = true;
        view.setStates(isSignedIn);
        view.showGreeting(userName, userRole);
        initMainMenu();
    }

    public void signOut() {
        userRole = "unknown";
        userId = "";
        userName = "";
        isSignedIn = false;
        view.setStates(isSignedIn);
        view.notifySignOut();
        initMainMenu();
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

    public void closeConnection() {
        queryController.closeConnection();
    }
}
