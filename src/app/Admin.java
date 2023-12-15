package app;

import MyClasses.Album;
import MyClasses.Artist;
import MyClasses.Enum.UserStatus;
import MyClasses.Enum.UserType;
import MyClasses.Host;
import MyClasses.Songs;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.User;
import fileio.input.*;
import lombok.Getter;

import java.util.*;

public class Admin {
    @Getter
    private static List<User> users = new ArrayList<>();

    @Getter
    private static List<Song> songs = new ArrayList<>();
    @Getter
    private static List<Podcast> podcasts = new ArrayList<>();
    private static int timestamp = 0;

    @Getter
    private static ArrayList<Album> albums = new ArrayList<>();

    public static void setUsers(List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }


    public static void setSongs(List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    public static void setPodcasts(List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(), episodeInput.getDuration(), episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    public static String addUser(String name, String userType, String city, Integer age) {
        boolean found = false;
        for (User user : getUsers()) {
            if (user.getUsername().equals(name)) {
                found = true;
            }
        }
        if (!found) {
            if (userType.equals("user")) {
                User newUser = new User(name, age, city);
                getUsers().add(newUser);
            } else if (userType.equals("host")) {
                Host host = new Host(name, age, city);
                getUsers().add(host);
                host.setStatus(UserStatus.OFFLINE);
            } else if (userType.equals("artist")) {
                Artist artist = new Artist(name, age, city);
                artist.setStatus(UserStatus.OFFLINE);
                Artist.setAlbums(new ArrayList<>());
                getUsers().add(artist);
            }
            return "The username " + name + " has been added successfully.";
        } else {
            return "The username " + name + " is already taken.";
        }
    }

    public static User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static Album getAlbum(String username) {
        for (Album album : albums) {
            if (album.getName().equals(username)) {
                return album;
            }
        }
        return null;
    }

    public static Podcast getPodcast(String username) {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(username)) {
                return podcast;
            }
        }
        return null;
    }

    public static Song findSongByName(String name) {
        for (Song song : getSongs()) {
            if (song.getName().equals(name)) {
                return song;
            }
        }
        return null;
    }

    public static String switchStatus(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user != null) {
            if (user.getStatus().equals(UserStatus.ONLINE)) {
                user.setStatus(UserStatus.OFFLINE);
            } else {
                user.setStatus(UserStatus.ONLINE);
            }
            return user.getUsername() + " has changed status successfully.";
        } else {
            return "The username " + commandInput.getUsername() + " doesn't exist.";
        }
    }

    public static ArrayList<String> getOnlineUsers() {
        ArrayList<String> onlineUsers = new ArrayList<>();
        for (User user : getUsers()) {
            if (user.getStatus().equals(UserStatus.ONLINE)) {
                onlineUsers.add(user.getUsername());
            }
        }
        return onlineUsers;
    }

    public static void updateTimestamp(int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            if (user != null && user.getStatus().equals(UserStatus.ONLINE))
                user.simulateTime(elapsed);
        }
    }

    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= 5) break;
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= 5) break;
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    public static List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(getAlbums());
        sortedAlbums.sort(Comparator.comparingInt(Album::getNumberOfLikes).reversed());
        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Album album : sortedAlbums) {
            if (count >= 5) break;
            topAlbums.add(album.getName());
            count++;
        }
        return topAlbums;
    }

    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        albums = new ArrayList<>();
        timestamp = 0;
    }
}
