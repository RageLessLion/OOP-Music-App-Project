package MyClasses;

import lombok.Getter;

public class Announcement {
    @Getter
    private final String owner;
    @Getter
    private final String name;
    @Getter
    private final String description;

    public Announcement(String owner, String name, String description) {
        this.owner = owner;
        this.name = name;
        this.description = description;
    }
}
