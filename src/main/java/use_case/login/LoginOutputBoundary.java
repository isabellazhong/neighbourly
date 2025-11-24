package use_case.login;
import entity.*;

public interface LoginOutputBoundary {
    void prepareLoginFailInterface(String error);

    void prepareWrongPasswordInterface(String error); 

    void prepareLoginSucessInterface(User user); 
}
