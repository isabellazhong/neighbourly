package entity;

public enum Gender {
    FEMALE("Female"),
    MALE("Male"),
    NONBINARY("Non-binary"),
    OTHER("Other");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

