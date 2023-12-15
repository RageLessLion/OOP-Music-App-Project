package MyClasses;

import MyClasses.Enum.UserStatus;
import MyClasses.Enum.UserType;
import app.Admin;
import app.audio.Collections.AudioCollection;
import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.player.PlayerSource;
import app.player.PlayerStats;
import app.user.User;
import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Artist extends User {
    private static ArrayList<Album> albums = new ArrayList<>();

    private static ArrayList<Event> events = new ArrayList<>();

    private static ArrayList<Merch> merch = new ArrayList<>();

    private static final ArrayList<Song> artistSongs = new ArrayList<>();

    public Artist(String username, int age, String city) {
        super(username, age, city);
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merch = new ArrayList<>();
        setType(UserType.ARTIST);
        super.setPage(new Page("artist","","artist"));
    }

    public static Album getAlbum(String album) {
        for (Album index : getAlbums()) {
            if (index.getName().equals(album)) {
                return index;
            }
        }
        return null;
    }


    public static void setAlbums(ArrayList<Album> albums) {
        Artist.albums = albums;
    }

    public static void setEvents(ArrayList<Event> events) {
        Artist.events = events;
    }

    public static void setMerch(ArrayList<Merch> merch) {
        Artist.merch = merch;
    }

    public static ArrayList<Album> getAlbums() {
        return albums;
    }

    public static ArrayList<Event> getEvents() {
        return events;
    }

    public static ArrayList<Merch> getMerch() {
        return merch;
    }

    public static ArrayList<Song> getArtistSongs() {
        return artistSongs;
    }

    public String addEvent(User user, String event, String description, String datePass) {
        int flag = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        dateFormat.setLenient(false); // set lenient to false for strict validation

        try {
            // parse the string into a Date object
            Date date = dateFormat.parse(datePass);
            for (Event index : getEvents()) {
                if (index.getName().equals(event)) {
                    flag = 1;
                }
            }

            if (flag == 0) {
                Event newEvent = new Event((Artist) user, event, description, datePass);
                getEvents().add(newEvent);
                return user.getUsername() + " has added new event successfully.";
            }
            return getUsername() + " has another event with the same name.";
        } catch (ParseException e) {
            // if parsing fails, the date is not valid
            return "Event for " + user.getUsername() + " does not have a valid date.";
        }
    }

    public String removeEvent(Artist artist, String event) {
        int flag = 0;
        Iterator<Event> iterator = getEvents().iterator();
        while (iterator.hasNext()) {
            Event element = iterator.next();
            if (element.getName().equals(event)) {
                iterator.remove();
                flag = 1;
            }
        }
        if (flag == 1) {
            return artist.getName() + " deleted the event successfully.";
        } else {
            return artist.getName() + " doesn't have an event with the given name.";
        }
    }

    public String addMerch(User user, String event, String description, Integer price) {
        if (price < 0) {
            return "Price for merchandise can not be negative.";
        }
        int flag = 0;
        for (Merch index : getMerch()) {
            if (index.getName().equals(event)) {
                flag = 1;
            }
        }

        if (flag == 0) {
            Merch newMerch = new Merch((Artist) user, event, description, price);
            getMerch().add(newMerch);
            return user.getUsername() + " has added new merchandise successfully.";
        }
        return getUsername() + " has merchandise with the same name.";
    }

    public String removeAlbum(Artist artist, String album,Integer timestamp) {
        int flag = 0;
        int flagAlbum = 0;
        Album album1 = Admin.getAlbum(album);
        if (album1 == null) {
            return artist.getName() + " doesn't have an album with the given name.";
        }
        for (User user : Admin.getUsers()) {
            PlayerStats playerStats = user.getPlayerStats();
            for (Song song : album1.getSongs()) {
                if (song.getName().equals(playerStats.getName())) {
                    flagAlbum = 1;
                    break;
                }
            }
        }

        for (User user : Admin.getUsers()) {
            AudioCollection loadedAlbum = user.getPlayer().getCurrentAudioCollection();
            if (loadedAlbum != null && album1.getName().equals(loadedAlbum.getName())) {
                flagAlbum = 1;
                break;
            }
        }

        for(Playlist playlist : Admin.getPlaylists()){
            for(Song song : playlist.getSongs()){
                if(album1.getSongs().contains(song)){
                    flagAlbum = 1;
                }
            }
        }

        if (flagAlbum == 1) {
            return artist.getUsername() + " can't delete this album.";
        }
        Iterator<Album> iterator = getAlbums().iterator();
        while (iterator.hasNext()) {
            Album element = iterator.next();
            if (element.getName().equals(album)) {
                iterator.remove();
                flag = 1;
            }
        }
        if (flag == 1) {
            return artist.getName() + " deleted the album successfully.";
        } else {
            return artist.getName() + " has no album with the given name.";
        }
    }

    public String addAlbum(User user, String album, String description, ArrayList<Songs> songs) {
        int flag = 0;
        int songFlag = 1;
        for (Album index : getAlbums()) {
            if (index.getName().equals(album)) {
                flag = 1;
            }
        }
        if (flag == 0) {
            Album newAlbum = new Album(album, user.getUsername(), description, (Artist) user);
            Map<String, Integer> songCountMap = new HashMap<>();
            for (Songs songIndex : songs) {
                String title = songIndex.getName();
                Song newSong = new Song(songIndex.getName(), songIndex.getDuration(), newAlbum.getName(), songIndex.getTags(), songIndex.getLyrics(), songIndex.getGenre(), songIndex.getReleaseYear(), songIndex.getArtist());
                for (Song traversalSong : Admin.getSongs()) {
                    if (traversalSong.getName().equals(newSong.getName())) {
                        songFlag = 0;
                        break;
                    }
                }
                if (songFlag == 1) {
                    Admin.getSongs().add(newSong);
                    artistSongs.add(newSong);
                    newAlbum.getSongs().add(newSong);
                }

                songCountMap.put(title, songCountMap.getOrDefault(title, 0) + 1);
            }
            List<String> duplicateSongs = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : songCountMap.entrySet()) {
                if (entry.getValue() > 1) {
                    duplicateSongs.add(entry.getKey());
                }
            }
            if (!duplicateSongs.isEmpty()) {
                return user.getUsername() + " has the same song at least twice in this album.";
            }
            getAlbums().add(newAlbum);
            Admin.getAlbums().add(newAlbum);
            return user.getUsername() + " has added new album successfully.";
        }
        return getUsername() + " has another album with the same name.";
    }

    public static void reset() {
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merch = new ArrayList<>();
    }
}
