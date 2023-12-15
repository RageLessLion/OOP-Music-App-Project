package MyClasses;

import lombok.Getter;

public class Merch {
    private Artist artist;
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final Integer price;

    public Merch(Artist artist, String name, String description, Integer price) {
        this.artist = artist;
        this.name = name;
        this.description = description;
        this.price = price;
    }


}
