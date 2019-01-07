package dtos;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties({"name"})
public class PlaylistSubscriptions {
    private int id;
    public int getId() {return id;}
}
