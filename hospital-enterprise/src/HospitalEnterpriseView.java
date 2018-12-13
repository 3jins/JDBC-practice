public class HospitalEnterpriseView {
    private String[] mainSelectionItems = { "Sign in", "Sign up" };
    private String[] roles = { "patient", "doctor", "nurse" };

    public void showSelectionView(String selectionItems[]) {
        int numItems = selectionItems.length;
        for (int i = 0; i < numItems; i++) {
            System.out.println((i + 1) + ". " + selectionItems[i]);
        }
        System.out.print("> ");
    }

    public void showInputView(String inputTitle) {
        System.out.print(inputTitle + ": ");
    }

    public void showGreeting(String userName) {
        System.out.println("Welcome, " + userName + "!");
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
