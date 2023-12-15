package MyClasses;

import MyClasses.Enum.UserType;
import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.player.PlayerStats;
import app.user.User;
import lombok.Getter;

import java.util.*;

public class Host extends User {
    @Getter
    private static ArrayList<Podcast> podcasts = new ArrayList<>();
    @Getter
    private static ArrayList<Announcement> announcements = new ArrayList<>();

    public Host(String username, int age, String city) {
        super(username, age, city);
        setType(UserType.HOST);
        super.setPage(new Page("host","","host"));
    }

    public String addAnnouncement(Host host, String announcement, String description) {
        int flag = 0;
        int songFlag = 1;
        for (Announcement index : getAnnouncements()) {
            if (index.getName().equals(announcement)) {
                flag = 1;
            }
        }
        if (flag == 0) {
            Announcement announcement1 = new Announcement(host.getName(), announcement, description);
            getAnnouncements().add(announcement1);
            return host.getUsername() + " has successfully added new announcement.";
        }
        return getUsername() + " has already added an announcement with this name.";
    }

    public String removeAnnouncement(Host host, String announcement) {
        int flag = 0;
        Iterator<Announcement> iterator = getAnnouncements().iterator();
        while (iterator.hasNext()) {
            Announcement element = iterator.next();
            if (element.getName().equals(announcement)) {
                iterator.remove();
                flag = 1;
            }
        }
        if (flag == 1) {
            return host.getName() + " has successfully deleted the announcement.";
        } else {
            return host.getName() + " has no announcement with the given name.";
        }
    }

    public String removePodcast(Host host, String podcast) {
        int flag = 0;
        int flagPodcast = 0;
        Podcast podcast1 = Admin.getPodcast(podcast);
        if (podcast1 == null) {
            return host.getName() + " doesn't have a podcast with the given name.";
        }
        for (User user : Admin.getUsers()) {
            PlayerStats playerStats = user.getPlayerStats();
            //just needs to be loaded
            for (Episode episode : podcast1.getEpisodes()) {
                if (episode.getName().equals(playerStats.getName()))
                    flagPodcast = 1;
            }
        }

        if (flagPodcast == 1) {
            return host.getUsername() + " can't delete this podcast.";
        }
        Iterator<Podcast> iterator = getPodcasts().iterator();
        while (iterator.hasNext()) {
            Podcast element = iterator.next();
            if (element.getName().equals(podcast)) {
                iterator.remove();
                flag = 1;
            }
        }
        if (flag == 1) {
            return host.getName() + " deleted the podcast successfully.";
        } else {
            return host.getName() + " has no album with the given name.";
        }
    }

    public String addPodcast(User user, String podcast, ArrayList<Episodes> episodes) {
        int flag = 0;
        ArrayList<Episode> episodesForNewPodcast = new ArrayList<>();
        for (Podcast index : getPodcasts()) {
            if (index.getName().equals(podcast)) {
                flag = 1;
            }
        }
        if (flag == 0) {
            Map<String, Integer> episodeCountMap = new HashMap<>();
            for (Episodes episodeIndex : episodes) {
                String title = episodeIndex.getName();
                Episode newEpisode = new Episode(episodeIndex.getName(), episodeIndex.getDuration(), episodeIndex.getDescription());
                episodesForNewPodcast.add(newEpisode);
                episodeCountMap.put(title, episodeCountMap.getOrDefault(title, 0) + 1);
            }
            Podcast newPodcast = new Podcast(podcast, user.getUsername(), episodesForNewPodcast);
            List<String> duplicateEpisodes = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : episodeCountMap.entrySet()) {
                if (entry.getValue() > 1) {
                    duplicateEpisodes.add(entry.getKey());
                }
            }
            if (!duplicateEpisodes.isEmpty()) {
                return user.getUsername() + " has the same episode in this podcast.";
            }
            getPodcasts().add(newPodcast);
            Admin.getPodcasts().add(newPodcast);
            return user.getUsername() + " has added new podcast successfully.";
        }
        return getUsername() + " has another podcast with the same name.";
    }

    public static void reset() {
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
    }
}
