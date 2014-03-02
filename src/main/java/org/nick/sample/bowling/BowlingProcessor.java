package org.nick.sample.bowling;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ndhupia on 2/22/14.
 */
@Path("/")

public class BowlingProcessor
{

    @Context
    private UriInfo uriInfo;


    @Path("/healthcheck")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response healthCheck() throws BowlingAppException {
        String success = uriInfo.getQueryParameters().getFirst("success");
        if (success != null && success.equalsIgnoreCase("false")) {
            throw new BowlingAppException(500, "simulating failure");
        }
        return Response.ok("{\"success\": \"true\"}").build();
    }

    @PUT
    @Path("/game/{id}/player")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPlayerToGame(@PathParam("id")Integer bowlingGameId) throws BowlingAppException {
        BowlingDao dao = BowlingDaoImpl.getInstance();
        boolean newPlayer = false;
        String player = uriInfo.getQueryParameters().getFirst("username");
        if (null == player) {
            throw new BowlingAppException(400, "No player username specified. See api for details");
        }
        try {
            newPlayer = dao.addPlayerToGame(bowlingGameId, player);
        } catch (BowlingDaoException e) {
            throw new BowlingAppException(400, e.getMessage());
        }

        return Response.created(uriInfo.getAbsolutePath()).build();
    }

    @PUT
    @Path("/game/{id}/player/{player}/frame/{frameId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlayerScore(@PathParam("id")Integer bowlingGameId, @PathParam("player")String player, @PathParam("frameId") Integer frameId) throws BowlingAppException {
        BowlingDao dao = BowlingDaoImpl.getInstance();
        String scores = uriInfo.getQueryParameters().getFirst("score");
        String[] playerScores = (scores != null) ? scores.split(",") : null;
        if (playerScores == null) {
            throw new BowlingAppException(400, "Invalid frame score");
        }
        Integer len = playerScores.length;
        if (len > 3) {
            throw new BowlingAppException(400, "Invalid frame score");
        }

        Integer[] playerFrame = new Integer[len];
        for (int i=0;i<len;i++) {
            playerFrame[i] = Integer.parseInt(playerScores[i]);
        }
        try {
            dao.addPlayerScore(bowlingGameId,player,frameId, playerFrame);
        } catch (BowlingDaoException e) {
            throw new BowlingAppException(400, e.getMessage());
        }
        return Response.created(uriInfo.getAbsolutePath()).build();
    }

    @GET
    @Path("/game/{id}/livescores")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLiveScore(@PathParam("id")Integer bowlingGameId) throws BowlingAppException {
        Map<String,Integer> playerScore;
        BowlingDao dao = BowlingDaoImpl.getInstance();
        try {
            playerScore = dao.getLiveScoresForGame(bowlingGameId);
        } catch (BowlingDaoException e) {
            throw new BowlingAppException(400, e.getMessage());
        }
        return Response.ok(playerScore).build();
    }

    @POST
    @Path("/game")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBowlingGame(BowlingGame bowlingGame) throws BowlingAppException {
        BowlingDao dao = BowlingDaoImpl.getInstance();
        int bowlingId;
        URI uri;
        try {
            bowlingId = dao.createGame(bowlingGame);
            uri = new URI(uriInfo.getAbsolutePath() + "/" + bowlingId);
        } catch (URISyntaxException e) {
            throw new BowlingAppException(500, "Error creating game " + bowlingGame.toString());
        } catch (BowlingDaoException e) {
            throw new BowlingAppException(400, e.getMessage());
        }
        return Response.created(uri).link(uri, "created").build();
    }

    @GET
    @Path("/game/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBowlingGame(@PathParam("id") Integer id) throws BowlingAppException {
        BowlingDao dao = BowlingDaoImpl.getInstance();
        BowlingGame bg;
        try {
            bg = dao.getGame(id);
        } catch (BowlingDaoException e) {
            throw new BowlingAppException(404, e.getMessage());
        }
        return Response.ok(bg).build();
    }
    @DELETE
    @Path("/game/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeBowlingGame(@PathParam("id") Integer id) throws BowlingAppException {
        BowlingDao dao = BowlingDaoImpl.getInstance();
        try {
            dao.deleteGame(id);
        } catch (BowlingDaoException e) {
            throw new BowlingAppException(404, e.getMessage());
        }
        return Response.noContent().build();
    }
}