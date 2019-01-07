package api.v1.resources;
import beans.RecommendationsBean;
import dtos.Song;
import com.kumuluz.ee.logs.cdi.Log;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Log
@ApplicationScoped
@Path("/recommendations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecommendationsResource {

    @Inject
    private RecommendationsBean recommendationsBean;

    @GET
    @Path("{id}")
    public Response getRecommendation(@PathParam("id") int userId) {
        List<Song> songs = recommendationsBean.getRecommendations(userId);
        if (songs == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(songs).build();
    }
}
