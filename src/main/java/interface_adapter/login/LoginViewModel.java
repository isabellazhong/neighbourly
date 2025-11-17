package main.java.interface_adapter.login;

import main.java.interface_adapter.ViewModel;
import main.java.use_case.login.LoginState;

public class LoginViewModel extends ViewModel<LoginState> {
    public LoginViewModel () {
        super("log in");
        setState(new LoginState());
    }
}
