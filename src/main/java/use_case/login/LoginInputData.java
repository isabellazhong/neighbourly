package use_case.login;

public class LoginInputData {
    private String email; 
    private String password; 

    public LoginInputData(String email, String password) {
        this.email = email;
        this.password = password; 
    }

    String getEmail() {
        return this.email; 
    }

    String getPassword() {
        return this.password; 
    }
}
