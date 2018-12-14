package view;

public class HospitalEnterpriseView {
    private String[] mainSelectionItems = { "Sign in", "Sign up" };
    private String[] roles = { "patient", "doctor", "nurse" };

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

    public void notifySignOut() {
        System.out.println("[+] You are signed out well.");
    }

    public void setStates(boolean isSignedIn) {
        this.mainSelectionItems[0] = isSignedIn ? "Sign out" : "Sign in";
    }

    public String[] getMainSelectionItems() {
        return mainSelectionItems;
    }

    public String[] getRoles() {
        return roles;
    }
}
