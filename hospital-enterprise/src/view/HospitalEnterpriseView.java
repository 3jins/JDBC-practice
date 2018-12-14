package view;

import java.util.ArrayList;

public class HospitalEnterpriseView {
    private ArrayList<String> mainSelectionItems = new ArrayList<>();
    private String[] roles = {"patient", "doctor", "nurse"};

    public HospitalEnterpriseView() {
        mainSelectionItems.add("Sign in");
        mainSelectionItems.add("Sign up");
    }

    public void showSelectionViewShell() {
        System.out.println("> ");
    }

    public void showSelectionView(String selectionItems[]) {
        int numItems = selectionItems.length;
        for (int i = 0; i < numItems; i++) {
            System.out.println((i + 1) + ". " + selectionItems[i]);
        }
        showSelectionViewShell();
    }

    public void showInputView(String inputTitle) {
        System.out.print(inputTitle + ": ");
    }

    public void showGreeting(String userName, String userRole) {
        System.out.println();
        System.out.println("Welcome, " + userName + "!");
        System.out.println("You are signed in as a " + userRole);
        System.out.println();
    }

    public void notifyBillCompleted() {
        System.out.println("Bill is payed well :)");
    }

    public void notifyNoDoctor(String doctorId) {
        System.out.println("[!] There is no doctor whose doctor id is " + doctorId + " in your department!");
    }

    public void notifySignOut() {
        System.out.println("[+] You are signed out well.");
    }

    public void setStates(boolean isSignedIn, String role) {
        if (isSignedIn) {
            this.mainSelectionItems.clear();
            mainSelectionItems.add("Sign out");
            switch(role) {
                case "patient":
                    mainSelectionItems.add("Check the bill");
                    mainSelectionItems.add("Pay the bill");
                    break;
                case "doctor":
                    mainSelectionItems.add("Check the appointments");
                    mainSelectionItems.add("Claim the bill");
                    break;
                case "nurse":
                    mainSelectionItems.add("Check the appointments");
                    mainSelectionItems.add("Add an appointment");
                    break;
                default:
                    return;
            }
        } else {
            this.mainSelectionItems.clear();
            mainSelectionItems.add("Sign in");
            mainSelectionItems.add("Sign up");
        }
    }

    public String[] getMainSelectionItems() {
        return mainSelectionItems.toArray(new String[0]);
    }

    public String[] getRoles() {
        return roles;
    }
}
