package MyClasses;

import app.audio.Collections.AudioCollection;
import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public final class Album extends AudioCollection{
    @Getter
    private final Artist artist;

    private String owner;
    @Getter
    private String name;
    @Getter
    private final String description;
    @Getter
    private final ArrayList <Song> songs = new ArrayList<>();

    public Album(String name,String owner,String description,Artist artist){
        super(name, owner);
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.artist = artist;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(int index) {
        return songs.get(index);
    }

    public Integer getNumberOfLikes(){
        Integer totalNumberOfLikes = 0;
        for(Song song : getSongs()){
            totalNumberOfLikes += song.getLikes();
        }
        return totalNumberOfLikes;
    }
}
