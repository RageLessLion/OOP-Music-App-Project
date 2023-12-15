package app;

import MyClasses.*;
import MyClasses.Enum.UserStatus;
import MyClasses.Enum.UserType;
import app.audio.Collections.Playlist;
import app.audio.Collections.PlaylistOutput;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.user.User;
import app.utils.Enums;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CommandRunner {

    static final ObjectMapper objectMapper = new ObjectMapper();

    static String name;

    static String page;

    public static void setPage(String page) {
        CommandRunner.page = page;
    }

    public static void setName(String name) {
        CommandRunner.name = name;
    }

    public static ObjectNode search(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();

        ArrayList<String> results = new ArrayList<>();
        String message = "";
        if (user != null)
            if (user.getStatus().equals(UserStatus.OFFLINE)) {
                message = user.getUsername() + " is offline.";
            } else {
                results = user.search(filters, type);
                message = "Search returned " + results.size() + " results";
            }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));


        return objectNode;
    }

    public static ObjectNode select(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = "";
        if (user != null)
            message = user.select(commandInput.getItemNumber());
        String[] stringArray = message.split(" ");
        String resultString = "";
        if (stringArray[2].length() >= 2) {
            resultString = stringArray[2].substring(0, stringArray[2].length() - 2);
            if (Admin.getUser(resultString) != null && Admin.getUser(resultString).getType().equals(UserType.ARTIST)) {
                Page page = new Page("artist",Admin.getUser(resultString).getName(),"artist");
                Admin.getUser(commandInput.getUsername()).setPage(page);
            } else if (Admin.getUser(resultString) != null && Admin.getUser(resultString).getType().equals(UserType.USER)) {
                Page page = new Page("user",Admin.getUser(resultString).getName(),"home");
                Admin.getUser(commandInput.getUsername()).setPage(page);
            } else if (Admin.getUser(resultString) != null && Admin.getUser(resultString).getType().equals(UserType.HOST)) {
                Page page = new Page("host",Admin.getUser(resultString).getName(),"host");
                Admin.getUser(commandInput.getUsername()).setPage(page);
            }
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode load(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = "";
        if (user != null)
            message = user.load();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode playPause(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.playPause();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode repeat(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = "";
        if (user != null)
            message = user.repeat();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode shuffle(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        Integer seed = commandInput.getSeed();
        String message = user.shuffle(seed);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode forward(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.forward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode backward(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.backward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode like(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = "";

        if (user != null)
            if (user.getStatus().equals(UserStatus.OFFLINE)) {
                message = user.getUsername() + " is offline.";
            } else {
                message = user.like();
            }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode next(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.next();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode prev(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.prev();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode createPlaylist(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = "";
        if (user != null)
            message = user.createPlaylist(commandInput.getPlaylistName(), commandInput.getTimestamp());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode addRemoveInPlaylist(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = "";
        if (user != null)
            message = user.addRemoveInPlaylist(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode switchVisibility(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode showPlaylists(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    public static ObjectNode follow(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = user.follow();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode status(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        PlayerStats stats = new PlayerStats("", 0, Enums.RepeatMode.NO_REPEAT, false, false);
        if (user != null)
            stats = user.getPlayerStats();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("stats", objectMapper.valueToTree(stats));

        return objectNode;
    }

    public static ObjectNode showLikedSongs(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    public static ObjectNode getPreferredGenre(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }

    public static ObjectNode getTop5Songs(CommandInput commandInput) {
        List<String> songs = Admin.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    public static ObjectNode getTop5Playlists(CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    public static ObjectNode switchConnectionStatus(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("message", Admin.switchStatus(commandInput));

        return objectNode;
    }

    public static ObjectNode getOnlineUsers(CommandInput commandInput) {
        ArrayList<String> onlineUsers = Admin.getOnlineUsers();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(onlineUsers));

        return objectNode;
    }

    public static ObjectNode addAlbum(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = "";
        if (Admin.getUser(commandInput.getUsername()) != null) {
            objectNode.put("user", Admin.getUser(commandInput.getUsername()).getUsername());
            if (Admin.getUser(commandInput.getUsername()).getType().equals(UserType.ARTIST)) {
                Artist artist = (Artist) (Admin.getUser(commandInput.getUsername()));
                message = artist.addAlbum((Admin.getUser(commandInput.getUsername())), commandInput.getName(), commandInput.getDescription(), commandInput.getSongs());
            } else {
                message = commandInput.getUsername() + " is not an artist.";
            }

        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode addUser(CommandInput commandInput) {
        String message = Admin.addUser(commandInput.getUsername(), commandInput.getType(), commandInput.getCity(), commandInput.getAge());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.put("user", Admin.getUser(commandInput.getUsername()).getUsername());

        return objectNode;
    }

    public static ObjectNode changePage(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        String message = "";
        User user = Admin.getUser(commandInput.getUsername());
        if (commandInput.getNextPage().equals("LikedContent")) {
            user.getPage().setPageName("likedContent");
            user.getPage().setWatchedUser(commandInput.getUsername());
            message = commandInput.getUsername() + " accessed " + commandInput.getNextPage() + " successfully.";
        }
        if (commandInput.getNextPage().equals("Home")) {
            user.getPage().setPageName("home");
            user.getPage().setWatchedUser(commandInput.getUsername());
            message = commandInput.getUsername() + " accessed " + commandInput.getNextPage() + " successfully.";
        }
        objectNode.put("message", message);
        return objectNode;
    }

    //todo: reduce function
    public static ObjectNode printCurrentPage(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        User user = Admin.getUser(name);
        if (user == null) {
            user = Admin.getUser(commandInput.getUsername());
        }
        objectNode.put("user", commandInput.getUsername());
        String message = "";
        if (Admin.getUser(commandInput.getUsername()).getStatus().equals(UserStatus.OFFLINE)) {
            message = commandInput.getUsername() + " is offline.";
        } else if (user.getPage().getPageName().equals("home")) {
            message = message + ("Liked songs:\n\t");
            if (user.getLikedSongs().isEmpty()) {
                message = message + "[]";
            } else {
                message = message + "[";
                for (Song song : user.getLikedSongs()) {
                    message = message + song.getName();
                }
                message = message + "]";
            }
            message = message + ("\n");
            message = message + ("\n");
            message = message + ("Followed playlists:\n\t");
            if (user.getFollowedPlaylists().isEmpty()) {
                message = message + "[]";
            } else {
                message = message + "[";
                for (Playlist playlist : user.getFollowedPlaylists()) {
                    message = message + (playlist.getName());
                }
                message = message + "]";
            }
        } else if (user.getPage().getPageName().equals("likedContent")) {
            message = message + ("Liked songs:\n\t");
            if (user.getLikedSongs().isEmpty()) {
                message = message + "[]";
            } else {
                message = message + "[";
                for (Song song : user.getLikedSongs()) {
                    message = message + song.getName() + " - " + song.getArtist();
                }
                message = message + "]";
            }
            message = message + ("\n");
            message = message + ("\n");
            message = message + ("Followed playlists:\n\t");
            if (user.getFollowedPlaylists().isEmpty()) {
                message = message + "[]";
            } else {
                message = message + "[";
                for (Playlist playlist : user.getFollowedPlaylists()) {
                    message = message + (playlist.getName()) + " - " + playlist.getOwner();
                }
                message = message + "]";
            }
        } else if (user.getPage().getPageName().equals("artist")) {
            Artist artistNew = (Artist) Admin.getUser(name);
            message = message + ("Albums:\n\t");
            if (Artist.getAlbums().isEmpty()) {
                message = message + "[]";
            } else {
                for (Album albumIndex : Artist.getAlbums()) {
                    message = message + "[" + albumIndex.getName() + "]";
                    message = message + ("\n");
                }
            }
            message = message + ("\n");
            message = message + ("Merch:\n\t");
            if (Artist.getMerch().isEmpty()) {
                message = message + "[]";
            } else {
                message = message + "[";
                for (Merch merchIndex : Artist.getMerch()) {
                    message = message + (merchIndex.getName()) + " - " + merchIndex.getPrice() + ":" + "\n\t" + merchIndex.getDescription();
                    if (Artist.getMerch().indexOf(merchIndex) != Artist.getMerch().size() - 1)
                        message = message + (", ");
                }
                message = message + "]";
            }

            message = message + ("\n");
            message = message + ("\n");
            message = message + ("Events:\n\t");
            if (Artist.getEvents().isEmpty()) {
                message = message + "[]";
            } else {
                message = message + "[";
                for (Event eventIndex : Artist.getEvents()) {
                    message = message + (eventIndex.getName()) + " - " + eventIndex.getDate() + ":" + "\n\t" + eventIndex.getDescription();
                    if (Artist.getEvents().indexOf(eventIndex) != Artist.getEvents().size() - 1)
                        message = message + (", ");
                }
                message = message + "]";
            }
        } else if (user.getPage().getPageName().equals("host")) {
            Host hostNew = (Host) Admin.getUser(name);
            message = message + ("Podcasts:\n\t");
            if (Host.getPodcasts().isEmpty()) {
                message = message + "[]";
            } else {
                message = message + "[";
                for (Podcast podcast : Host.getPodcasts()) {
                    message = message + podcast.getName() + ":\n\t";
                    message = message + "[";
                    for (Episode episode : podcast.getEpisodes()) {
                        message = message + episode.getName() + " - " + episode.getDescription();
                        if (podcast.getEpisodes().indexOf(episode) != podcast.getEpisodes().size() - 1)
                            message = message + (", ");
                        else {
                            message = message + ("]");
                        }
                    }

                    if (Host.getPodcasts().indexOf(podcast) != Host.getPodcasts().size() - 1)
                        message = message + ("\n, ");
                    else {
                        message = message + "\n]";
                    }
                }
            }
            message = message + ("\n");
            message = message + ("\n");
            message = message + ("Announcements:\n\t");
            if (Host.getAnnouncements().isEmpty()) {
                message = message + "[]";
            } else {
                message = message + "[";
                for (Announcement announcement : Host.getAnnouncements()) {
                    message = message + (announcement.getName()) + ":\n\t" + announcement.getDescription() + "\n";
                    if (Host.getAnnouncements().indexOf(announcement) != Host.getAnnouncements().size() - 1)
                        message = message + ("\n,");
                }
                message = message + "]";
            }
        }
        objectNode.put("message", message);
        return objectNode;
    }

    public static ObjectNode showAlbums(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        Artist artist = (Artist) Admin.getUser(commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        ArrayNode result = objectNode.putArray("result");
        for (Album album : Artist.getAlbums()) {
            if (album.getArtist().getUsername().equals(commandInput.getUsername())) {
                ObjectNode albumObject = result.addObject();
                albumObject.put("name", album.getName());
                ArrayNode songsArray = albumObject.putArray("songs");
                for (Song song : album.getSongs()) {
                    songsArray.add(song.getName());
                }
            }
        }
        objectNode.put("user", Admin.getUser(commandInput.getUsername()).getUsername());
        return objectNode;
    }

    public static ObjectNode addEvent(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());

        String message = "";
        if (Admin.getUser(commandInput.getUsername()) != null) {
            objectNode.put("user", Admin.getUser(commandInput.getUsername()).getUsername());
            if (Admin.getUser(commandInput.getUsername()).getType().equals(UserType.ARTIST)) {
                Artist artist = (Artist) (Admin.getUser(commandInput.getUsername()));
                message = artist.addEvent((Admin.getUser(commandInput.getUsername())), commandInput.getName(), commandInput.getDescription(), commandInput.getDate());
            } else {
                message = commandInput.getUsername() + " is not an artist.";
            }
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode addMerch(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        String message = "";
        if (Admin.getUser(commandInput.getUsername()) != null) {
            objectNode.put("user", Admin.getUser(commandInput.getUsername()).getUsername());
            if (Admin.getUser(commandInput.getUsername()).getType().equals(UserType.ARTIST)) {
                Artist artist = (Artist) (Admin.getUser(commandInput.getUsername()));
                message = artist.addMerch((Admin.getUser(commandInput.getUsername())), commandInput.getName(), commandInput.getDescription(), commandInput.getPrice());
            } else {
                message = commandInput.getUsername() + " is not an artist.";
            }
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("message", message);


        return objectNode;
    }

    public static ObjectNode getAllUsers(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        ArrayList<String> allUsers = new ArrayList<>();
        for (User user : Admin.getUsers()) {
            if (user.getType().equals(UserType.USER)) {
                allUsers.add(user.getUsername());
            }
        }
        for (User user : Admin.getUsers()) {
            if (user.getType().equals(UserType.ARTIST)) {
                allUsers.add(user.getUsername());
            }
        }
        for (User user : Admin.getUsers()) {
            if (user.getType().equals(UserType.HOST)) {
                allUsers.add(user.getUsername());
            }
        }
        objectNode.put("result", objectMapper.valueToTree(allUsers));

        return objectNode;
    }

    //todo: reduce function
    public static ObjectNode deleteUser(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("user", commandInput.getUsername());
        String message = "";
        Artist artist1 = null;
        User user1 = null;
        Integer flag = 0;
        if (Admin.getUser(commandInput.getUsername()) != null) {
            for (User user : Admin.getUsers()) {
                //if somebody has loaded a song of an artist the artist cannot be deleted
                PlayerStats playerStats = user.getPlayerStats();
                String audioFile = playerStats.getName();
                if (Admin.getUser(commandInput.getUsername()).getType().equals(UserType.ARTIST)) {
                    Artist artist = (Artist) Admin.getUser(commandInput.getUsername());
                    for (Song songIndex : Artist.getArtistSongs()) {
                        if (songIndex.getName().equals(audioFile)) {
                            flag = 1;
                            break;
                        }
                    }
                }
                else if (Admin.getUser(commandInput.getUsername()).getType().equals(UserType.HOST)) {
                    //todo:for host case
                    flag = 1;
                }
            }
            for(User user : Admin.getUsers()){
                if(user.getPage().getWatchedUser().equals(commandInput.getUsername())){
                    flag = 1;
                }
            }
            if (flag == 1) {
                message = commandInput.getUsername() + " can't be deleted.";
            } else {
                if (Admin.getUser(commandInput.getUsername()).getType().equals(UserType.ARTIST)) {
                    //removing song from the admin list of songs
                    artist1 = (Artist) Admin.getUser(commandInput.getUsername());
                    Iterator<Song> iterator = Admin.getSongs().iterator();
                    while (iterator.hasNext()) {
                        Song songAdmin = iterator.next();
                        for (Song songArtist : Artist.getArtistSongs()) {
                            if (songAdmin.getName().equals(songArtist.getName()) && songAdmin.getArtist().equals(artist1.getName())) {
                                iterator.remove();
                                break;
                            }
                        }
                    }
                    //removing song from every users's liked songs
                    for (User userIndex : Admin.getUsers()) {
                        Iterator<Song> iterator1 = userIndex.getLikedSongs().iterator();
                        while (iterator1.hasNext()) {
                            Song likedSong = iterator1.next();
                            for (Song songArtist : Artist.getArtistSongs())
                                if (likedSong.getName().equals(songArtist.getName())) {
                                    iterator1.remove();
                                    break;
                                }
                        }
                    }
                } else if (Admin.getUser(commandInput.getUsername()).getType().equals(UserType.USER)) {
                    user1 = Admin.getUser(commandInput.getUsername());
                    //removing playlists from every other user followed playlists
                    for (User userIndex : Admin.getUsers()) {
                        Iterator<Playlist> iterator2 = userIndex.getFollowedPlaylists().iterator();
                        while (iterator2.hasNext()) {
                            Playlist followedPlaylist = iterator2.next();
                            for (Playlist userPlaylist : user1.getPlaylists())
                                if (userPlaylist.getName().equals(followedPlaylist.getName())) {
                                    iterator2.remove();
                                    break;
                                }
                        }
                    }
                    //removing following playlists
                    for (Playlist playlist : Objects.requireNonNull(user1).getFollowedPlaylists()) {
                        playlist.setFollowers(playlist.getFollowers() - 1);
                    }
                }
                Admin.getUsers().remove(Admin.getUser(commandInput.getUsername()));
                message = commandInput.getUsername() + " was successfully deleted.";
            }
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("message", message);
        return objectNode;
    }

    public static ObjectNode addPodcast(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        String message = "";
        if (Admin.getUser(commandInput.getUsername()) != null) {
            objectNode.put("user", Admin.getUser(commandInput.getUsername()).getUsername());
            if (Admin.getUser(commandInput.getUsername()).getType().equals(UserType.HOST)) {
                Host host = (Host) (Admin.getUser(commandInput.getUsername()));
                message = host.addPodcast((Admin.getUser(commandInput.getUsername())), commandInput.getName(), commandInput.getEpisodes());
            } else {
                message = commandInput.getUsername() + " is not a host.";
            }

        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode addAnnouncement(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        String message = "";
        if (Admin.getUser(commandInput.getUsername()) != null) {
            objectNode.put("user", Admin.getUser(commandInput.getUsername()).getUsername());
            if (Admin.getUser(commandInput.getUsername()).getType().equals(UserType.HOST)) {
                Host host = (Host) (Admin.getUser(commandInput.getUsername()));
                message = host.addAnnouncement(host, commandInput.getName(), commandInput.getDescription());
            } else {
                message = commandInput.getUsername() + " is not a host.";
            }

        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode removeAnnouncement(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        String message = "";
        if (Admin.getUser(commandInput.getUsername()) != null) {
            objectNode.put("user", Admin.getUser(commandInput.getUsername()).getUsername());
            if (Admin.getUser(commandInput.getUsername()).getType().equals(UserType.HOST)) {
                Host host = (Host) (Admin.getUser(commandInput.getUsername()));
                message = host.removeAnnouncement(host, commandInput.getName());
            } else {
                message = commandInput.getUsername() + " is not a host.";
            }

        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode removeEvent(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        String message = "";
        if (Admin.getUser(commandInput.getUsername()) != null) {
            objectNode.put("user", Admin.getUser(commandInput.getUsername()).getUsername());
            if (Admin.getUser(commandInput.getUsername()).getType().equals(UserType.ARTIST)) {
                Artist artist = (Artist) (Admin.getUser(commandInput.getUsername()));
                message = artist.removeEvent(artist, commandInput.getName());
            } else {
                message = commandInput.getUsername() + " is not an artist.";
            }

        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode showPodcasts(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        Host host = (Host) Admin.getUser(commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        ArrayNode result = objectNode.putArray("result");
        for (Podcast podcast : Host.getPodcasts()) {
            if (podcast.getOwner().equals(commandInput.getUsername())) {
                ObjectNode albumObject = result.addObject();
                ArrayNode songsArray = albumObject.putArray("episodes");
                albumObject.put("name", podcast.getName());
                for (Episode episode : podcast.getEpisodes()) {
                    songsArray.add(episode.getName());
                }
            }
        }
        objectNode.put("user", Admin.getUser(commandInput.getUsername()).getUsername());
        return objectNode;
    }

    public static ObjectNode removeAlbum(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        String message = "";
        if (Admin.getUser(commandInput.getUsername()) != null) {
            objectNode.put("user", Admin.getUser(commandInput.getUsername()).getUsername());
            if (Admin.getUser(commandInput.getUsername()).getType().equals(UserType.ARTIST)) {
                Artist artist = (Artist) (Admin.getUser(commandInput.getUsername()));
                message = artist.removeAlbum(artist, commandInput.getName(),commandInput.getTimestamp());
            } else {
                message = commandInput.getUsername() + " is not an artist.";
            }

        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
            objectNode.put("user", commandInput.getUsername());
        }
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode removePodcast(CommandInput commandInput) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        String message = "";
        if (Admin.getUser(commandInput.getUsername()) != null) {
            objectNode.put("user", Admin.getUser(commandInput.getUsername()).getUsername());
            if (Admin.getUser(commandInput.getUsername()).getType().equals(UserType.HOST)) {
                Host host = (Host) (Admin.getUser(commandInput.getUsername()));
                message = host.removePodcast(host, commandInput.getName());
            } else {
                message = commandInput.getUsername() + " is not a host.";
            }

        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode getTop5Albums(CommandInput commandInput) {
        List<String> albums = Admin.getTop5Albums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }




}
