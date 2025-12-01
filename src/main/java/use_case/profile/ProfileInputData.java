package use_case.profile;

public class ProfileInputData {
    private final String name;
    private final String lastName;
    private final String gender;

    public ProfileInputData(String name, String lastName, String gender) {
        this.name = name;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }
}



