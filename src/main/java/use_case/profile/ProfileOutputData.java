package use_case.profile;

public class ProfileOutputData {
    private final String name;
    private final String lastName;
    private final String email;
    private final String gender;

    public ProfileOutputData(String name, String lastName, String email, String gender) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }
}



