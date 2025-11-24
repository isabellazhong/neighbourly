package use_case.login;

public class LoginState {
    private String email = "";
    private String loginError;
    private String passwordError;
    private String password = "";

    public String getEmail() {
        return email;
    }

    public String getLoginError() {
        return loginError;
    }

    public String getPasswordError() {
        return passwordError; 
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserError(String loginError) {
        this.loginError = loginError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError; 
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
