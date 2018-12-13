import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Scanner;

public class HospitalEnterpriseController {
    private Scanner scanner = new Scanner(System.in);
    private int userIntegerInput = 0;
    private String userLineInput = "";
    private boolean isSignedIn = false;
    private String userRole = "unknown";
    private String userId = "";
    private String userName = "";
    private HospitalEnterpriseView view = new HospitalEnterpriseView();

    public void select(String selectionItems[], int lowerLimit, int upperLimit) {
        String errorMessage = "[!] Only numbers between " + lowerLimit + " and " + upperLimit + " are permitted as an input.";
        userIntegerInput = scanner.nextInt();
        while (userIntegerInput > upperLimit || userIntegerInput < lowerLimit) {
            System.out.println(errorMessage);
            view.showSelectionView(selectionItems);
            userIntegerInput = scanner.nextInt();
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
        switch(userIntegerInput) {
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

        // sql select
        userName = "A name selected from table " + userRole;

        isSignedIn = true;
        view.setStates(isSignedIn);

        view.showGreeting(userName);
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
        throw new NotImplementedException();
    }
}
