package use_case.profile;

public class ProfileState {
    private String name = "";
    private String lastName = "";
    private String email = "";
    private String gender = "";
    private String profileError = null;
    private String warning = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfileError() {
        return profileError;
    }

    public void setProfileError(String profileError) {
        this.profileError = profileError;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
}

