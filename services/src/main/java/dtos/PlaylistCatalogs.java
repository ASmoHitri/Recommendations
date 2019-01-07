package dtos;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
@JsonIgnoreProperties({"name", "userId"})
public class PlaylistCatalogs {
    private int id;
    private List<Song> songs;

    public int getId() {
        return id;
    }

    public List<Song> getSongs() {
        return songs;
    }
}
