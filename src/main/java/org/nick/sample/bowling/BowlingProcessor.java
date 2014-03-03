package org.nick.sample.bowling;

import org.nick.sample.bowling.exception.BowlingAppException;
import org.nick.sample.bowling.exception.BowlingDaoException;
import org.nick.sample.bowling.model.BowlingGame;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Path("/")

/**
 * <p>This is the class the process rest requests. It calls methods on BowlingController
 * to process the requests</p>
 *
 */
public class BowlingProcessor {

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
    public Response addPlayerToGame(@PathParam("id") Integer bowlingGameId) throws BowlingAppException {
        String player = uriInfo.getQueryParameters().getFirst("username");
        if (null == player) {
            throw new BowlingAppException(400, "No player username specified. See api for details");
        }
        try {
            BowlingController.addPlayerToGame(bowlingGameId, player);
        } catch (BowlingDaoException e) {
            throw new BowlingAppException(400, e.getMessage());
        }

        return Response.created(uriInfo.getAbsolutePath()).build();
    }

    @PUT
    @Path("/game/{id}/player/{playerid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePlayerFromGame(@PathParam("id") Integer bowlingGameId, @PathParam("playerid") String playerId) throws BowlingAppException {
        try {
            BowlingController.deletePlayerFromGame(bowlingGameId, playerId);
        } catch (BowlingDaoException e) {
            throw new BowlingAppException(400, e.getMessage());
        }

        return Response.ok().build();
    }

    @PUT
    @Path("/game/{id}/player/{player}/frame/{frameId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlayerScore(@PathParam("id") Integer bowlingGameId, @PathParam("player") String player, @PathParam("frameId") Integer frameId) throws BowlingAppException {
        String scores = uriInfo.getQueryParameters().getFirst("score");
        String[] playerScores = (scores != null) ? scores.split(",") : null;
        if (playerScores == null) {
            throw new BowlingAppException(400, "Invalid frame score");
        }
        Integer len = playerScores.length;
        Integer[] playerFrame = new Integer[len];
        for (int i = 0; i < len; i++) {
            playerFrame[i] = Integer.parseInt(playerScores[i]);
        }
        try {
            BowlingController.addPlayerScore(bowlingGameId, player, frameId, playerFrame);
        } catch (BowlingDaoException e) {
            throw new BowlingAppException(400, e.getMessage());
        }
        return Response.created(uriInfo.getAbsolutePath()).build();
    }

    @GET
    @Path("/game/{id}/livescores")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLiveScore(@PathParam("id") Integer bowlingGameId) throws BowlingAppException {
        Map<String, Integer> playerScore;
        try {
            playerScore = BowlingController.getLiveScoresForGame(bowlingGameId);
        } catch (BowlingDaoException e) {
            throw new BowlingAppException(400, e.getMessage());
        }
        return Response.ok(playerScore).build();
    }

    @PUT
    @Path("/game")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBowlingGame(BowlingGame bowlingGame) throws BowlingAppException {
        try {
            BowlingController.updateBowlingGame(bowlingGame);
        } catch (BowlingDaoException e) {
            throw new BowlingAppException(400, e.getMessage());
        }
        return Response.noContent().location(uriInfo.getAbsolutePath()).build();
    }

    @POST
    @Path("/game")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBowlingGame() throws BowlingAppException {
        int bowlingId;
        URI uri;
        try {
            bowlingId = BowlingController.createGame();
            uri = new URI(uriInfo.getAbsolutePath() + "/" + bowlingId);
        } catch (URISyntaxException e) {
            throw new BowlingAppException(500, "Error creating game ");
        } catch (BowlingDaoException e) {
            throw new BowlingAppException(400, e.getMessage());
        }
        return Response.created(uri).link(uri, "created").build();
    }

    @GET
    @Path("/game/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBowlingGame(@PathParam("id") Integer id) throws BowlingAppException {
        BowlingGame bg;
        try {
            bg = BowlingController.getGame(id);
        } catch (BowlingDaoException e) {
            throw new BowlingAppException(404, e.getMessage());
        }
        return Response.ok(bg).build();
    }

    @DELETE
    @Path("/game/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeBowlingGame(@PathParam("id") Integer id) throws BowlingAppException {
        try {
            BowlingController.deleteGame(id);
        } catch (BowlingDaoException e) {
            throw new BowlingAppException(404, e.getMessage());
        }
        return Response.noContent().build();
    }
}