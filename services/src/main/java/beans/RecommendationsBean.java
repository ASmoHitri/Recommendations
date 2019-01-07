package beans;

import com.kumuluz.ee.discovery.annotations.DiscoverService;
import dtos.Song;
import dtos.Genre;
import dtos.PlaylistCatalogs;
import dtos.PlaylistSubscriptions;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.util.*;

@ApplicationScoped
public class RecommendationsBean {


    @Context
    protected UriInfo uriInfo;

    private Client httpClient = ClientBuilder.newClient();

    @Inject
    @DiscoverService("microservice-catalogs")
    private Optional<String> basePathCatalogs;

    @Inject
    @DiscoverService("microservice-subscriptions")
    private Optional<String> basePathSubscriptions;


    public List<Song> getRecommendations(int userId){
        if (basePathCatalogs.isPresent() && basePathSubscriptions.isPresent()) {
            try {

//            List<PlaylistSubscriptions> playlistIds = httpClient.target(basePathSubscriptions.get()+"/api/v1/users/{userId}/playlists")
//                    .request().get(new GenericType<List<PlaylistSubscriptions>>(){});
//            List<PlaylistCatalogs> playlists = new ArrayList<PlaylistCatalogs>();
//            for (PlaylistSubscriptions p: playlistIds){
//                PlaylistCatalogs current = httpClient.target(basePathCatalogs.get()+"/api/v1/playlists/{id}")
//                        .request().get(new GenericType<PlaylistCatalogs>(){});
//                playlists.add(current);
//            }

                List<PlaylistCatalogs> playlists = httpClient.target(basePathCatalogs.get() + "/api/v1/playlists?where=userId:EQ:" + userId)
                        .request().get(new GenericType<List<PlaylistCatalogs>>() {
                        }); //boljse?

                List<Genre> userGenres = new ArrayList<Genre>();

                for (PlaylistCatalogs p : playlists) {
                    List<Song> current_songs = p.getSongs();
                    for (Song s : current_songs) {
                        userGenres.add(s.getGenre());
                    }
                }
                Set<Genre> userGenresSet = new HashSet<Genre>(userGenres);
                int max = 0;
                Genre maxGenre = new Genre();
                for (Genre g : userGenresSet) {
                    int occurrences = Collections.frequency(userGenres, g);
                    if (occurrences > max) {
                        max = occurrences;
                        maxGenre = g;
                    }
                }
                List<Song> recommendation = httpClient.target(basePathCatalogs.get() + "/api/v1/songs?where=genre_id:EQ:" + maxGenre.getId())
                        .request().get(new GenericType<List<Song>>() {
                        }); // tu genre id nisem preprican ce prav.

                Collections.shuffle(recommendation);
                return recommendation.subList(0, 5);
            } catch (WebApplicationException | ProcessingException exception) {
                System.out.println(exception.getMessage());
                return null;
            }
        }
        return null;
    }
}