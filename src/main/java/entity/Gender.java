package entity;

public enum Gender {
    FEMALE("Female"),
    MALE("Male"),
    NON_BINARY("Non-binary"),
    OTHER("Other");

    private final String label;

    Gender(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

