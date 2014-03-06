package org.nick.sample.bowling.dao;

import org.nick.sample.bowling.exception.BowlingAppException;
import org.nick.sample.bowling.exception.BowlingInvalidDataException;
import org.nick.sample.bowling.model.BowlingGame;

public interface BowlingDao {
    /**
     * <p>creates a new game</p>
     *
     * @return Integer, representing a new game id which uniquely identifies a game
     * @throws BowlingAppException is thrown when there is a problem with processing request due to server/backend issues
     *         like connection failures, read timeouts etc
     */
    public Integer createGame() throws BowlingAppException;

    /**
     * <p>Updates the bowling game only if a bowling game with matching game id is present</p>
     *
     * @param bowlingGame uniquely identifies a game
     * @throws BowlingInvalidDataException is thrown when there is not existing game that matches the bowlingGame input
     * @throws BowlingAppException is thrown when there is a problem with processing request due to server/backend issues
     */
    public void updateBowlingGame(BowlingGame bowlingGame) throws BowlingInvalidDataException,BowlingAppException;

    /**
     * <p>Deletes the game associated with given bowling game id</p>
     *
     * @param bowlingId uniquely identifies a game
     * @throws BowlingInvalidDataException
     * @throws BowlingAppException is thrown when there is a problem with processing request due to server/backend issues
     */
    public void deleteGame(Integer bowlingId) throws BowlingInvalidDataException,BowlingAppException;

    /**
     * <p>get the game associated with given id</p>
     *
     * @param bowlingGameId uniquely identifies a game
     * @return an object representing a series of players and their bowling scores
     * @throws BowlingAppException is thrown when there is a problem with processing request due to server/backend issues
     * @throws BowlingInvalidDataException
     */
    public BowlingGame getGame(Integer bowlingGameId) throws BowlingInvalidDataException,BowlingAppException;

    /**
     * <p>adds a new player to a given bowling game</p>
     *
     * @param bowlingGameId uniquely identifies a game
     * @param playerId      unique name chosen by the player
     * @throws BowlingInvalidDataException
     * @throws BowlingAppException is thrown when there is a problem with processing request due to server/backend issues
     */
    public void addPlayerToGame(Integer bowlingGameId, String playerId) throws BowlingInvalidDataException,BowlingAppException;

    /**
     * <p>deletes a given player and all their scores from a bowling game</p>
     *
     * @param bowlingGameId uniquely identifies a game
     * @param playerId      a name chosen by the player
     * @throws BowlingInvalidDataException
     * @throws BowlingAppException is thrown when there is a problem with processing request due to server/backend issues
     */
    public void deletePlayerFromGame(Integer bowlingGameId, String playerId) throws BowlingInvalidDataException,BowlingAppException;

    /**
     * <p>adds a bowling frame for a given player and game. There are 1-10 frames in the bowling game</p>
     *
     * @param bowlingGameId uniquely identifies a game
     * @param playerId,     a name chosen by the player
     * @param frameId       this is a number between 1-10, inclusive, representing a bowling frame.
     * @param frame,        an array of one to three numbers representing a bowling frame
     * @throws BowlingInvalidDataException
     * @throws BowlingAppException is thrown when there is a problem with processing request due to server/backend issues
     */
    public void addPlayerScore(Integer bowlingGameId, String playerId, Integer frameId, Integer[] frame) throws BowlingInvalidDataException,BowlingAppException;

    /**
     * <p>validates and throws exception if a bowling game does not exist</p>
     *
     * @param bowlingGameId
     * @throws BowlingInvalidDataException
     * @throws BowlingAppException is thrown when there is a problem with processing request due to server/backend issues
     */
    public void checkGameExists(Integer bowlingGameId) throws BowlingInvalidDataException,BowlingAppException;

    /**
     * * <p>validates and throws exception if a player does not exist in a bowling game</p>
     *
     * @param bowlingGameId
     * @param player
     * @throws BowlingInvalidDataException
     * @throws BowlingAppException is thrown when there is a problem with processing request due to server/backend issues
     */
    public void checkPlayerExistInGame(Integer bowlingGameId, String player) throws BowlingInvalidDataException,BowlingAppException;
}