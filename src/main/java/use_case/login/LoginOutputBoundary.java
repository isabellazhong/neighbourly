package main.java.use_case.login;

public interface LoginOutputBoundary {
    void prepareLoginFailInterface(String error);

    void prepareLoginSucessInterface(); 
}
