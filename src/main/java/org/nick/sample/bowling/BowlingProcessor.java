package org.nick.sample.bowling;

import org.nick.sample.bowling.exception.BowlingAppException;
import org.nick.sample.bowling.exception.BowlingInvalidDataException;
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
    public Response healthCheck() throws BowlingInvalidDataException {
        String success = uriInfo.getQueryParameters().getFirst("success");
        if (success != null && success.equalsIgnoreCase("false")) {
            throw new BowlingInvalidDataException("simulating failure");
        }
        return Response.ok("{\"success\": \"true\"}").build();
    }

    @PUT
    @Path("/game/{id}/player")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPlayerToGame(@PathParam("id") Integer bowlingGameId) throws BowlingInvalidDataException, BowlingAppException {
        String player = uriInfo.getQueryParameters().getFirst("username");
        if (null == player) {
            throw new BowlingInvalidDataException("No player username specified. See api for details");
        }
        BowlingController.addPlayerToGame(bowlingGameId, player);
        return Response.created(uriInfo.getAbsolutePath()).build();
    }

    @PUT
    @Path("/game/{id}/player/{playerid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePlayerFromGame(@PathParam("id") Integer bowlingGameId, @PathParam("playerid") String playerId)
            throws BowlingInvalidDataException, BowlingAppException {
        BowlingController.deletePlayerFromGame(bowlingGameId, playerId);
        return Response.ok().build();
    }

    @PUT
    @Path("/game/{id}/player/{player}/frame/{frameId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlayerScore(
            @PathParam("id") Integer bowlingGameId, @PathParam("player") String player, @PathParam("frameId") Integer frameId)
            throws BowlingInvalidDataException, BowlingAppException {
        String scores = uriInfo.getQueryParameters().getFirst("score");
        String[] playerScores = (scores != null) ? scores.split(",") : null;
        if (playerScores == null) {
            throw new BowlingInvalidDataException("Invalid frame score");
        }
        Integer len = playerScores.length;
        Integer[] playerFrame = new Integer[len];
        for (int i = 0; i < len; i++) {
            playerFrame[i] = Integer.parseInt(playerScores[i]);
        }
        BowlingController.addPlayerScore(bowlingGameId, player, frameId, playerFrame);
        return Response.created(uriInfo.getAbsolutePath()).build();
    }

    @GET
    @Path("/game/{id}/livescores")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLiveScore(@PathParam("id") Integer bowlingGameId)
            throws BowlingInvalidDataException, BowlingAppException {
        Map<String, Integer> playerScore;
        playerScore = BowlingController.getLiveScoresForGame(bowlingGameId);
        return Response.ok(playerScore).build();
    }

    @PUT
    @Path("/game")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBowlingGame(BowlingGame bowlingGame)
            throws BowlingInvalidDataException, BowlingAppException {

        BowlingController.updateBowlingGame(bowlingGame);
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
            throw new BowlingAppException("Internal Error creating game ");
            }
        return Response.ok(uri.toString()).location(uri).build();
    }

    @GET
    @Path("/game/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBowlingGame(@PathParam("id") Integer id) throws BowlingInvalidDataException, BowlingAppException {
        BowlingGame bg;
        bg = BowlingController.getGame(id);
        return Response.ok(bg).build();
    }

    @DELETE
    @Path("/game/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeBowlingGame(@PathParam("id") Integer id) throws BowlingInvalidDataException, BowlingAppException {
        BowlingController.deleteGame(id);
        return Response.noContent().build();
    }
}