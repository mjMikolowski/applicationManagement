package cloudservices.maciejmikolowski.ApplicationManagement.dto;

public enum ApplicationStateEnum {
    CREATED("CREATED"),
    REJECTED("REJECTED"),
    DELETED("DELETED"),
    ACCEPTED("ACCEPTED"),
    VERIFIED("VERIFIED"),
    PUBLISHED("PUBLISHED");

    private String value;

    ApplicationStateEnum(String value) {
        this.value = value;
    }
}
