package entity;

public enum Gender {
    FEMALE("female"),
    MALE("male");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

