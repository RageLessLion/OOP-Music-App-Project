package MyClasses;

public class Event {
    private Artist artist;

    private String name;

    private String description;

    private String date;

    public Event(Artist artist,String name,String description,String date){
        this.artist = artist;
        this.name = name;
        this.description = description;
        this.date = date;
    }

// --Commented out by Inspection START (15.12.2023, 19:01):
//    public Artist getArtist() {
//        return artist;
//    }
// --Commented out by Inspection STOP (15.12.2023, 19:01)

// --Commented out by Inspection START (15.12.2023, 19:01):
//    public void setArtist(Artist artist) {
//        this.artist = artist;
//    }
// --Commented out by Inspection STOP (15.12.2023, 19:01)

    public String getName() {
        return name;
    }

// --Commented out by Inspection START (15.12.2023, 19:01):
//    public void setName(String name) {
//        this.name = name;
//    }
// --Commented out by Inspection STOP (15.12.2023, 19:01)

    public String getDescription() {
        return description;
    }

// --Commented out by Inspection START (15.12.2023, 19:01):
//    public void setDescription(String description) {
//        this.description = description;
//    }
// --Commented out by Inspection STOP (15.12.2023, 19:01)

    public String getDate() {
        return date;
    }

// --Commented out by Inspection START (15.12.2023, 19:01):
//    public void setDate(String date) {
//        this.date = date;
//    }
// --Commented out by Inspection STOP (15.12.2023, 19:01)
}
