package org.nick.sample.bowling.dao;

import org.nick.sample.bowling.model.BowlingGame;
import org.nick.sample.bowling.exception.BowlingDaoException;

public interface BowlingDao {
    /**
     * <p>creates a new game</p>
     *
     * @return Integer, representing a new game id which uniquely identifies a game
     * @throws BowlingDaoException
     */
    public Integer createGame() throws BowlingDaoException;

    /**
     * <p>Updates the bowling game only if a bowling game with matching game id is present</p>
     *
     * @param bowlingGame uniquely identifies a game
     * @throws BowlingDaoException is thrown when there is not existing game that matches the bowlingGame input
     */
    public void updateBowlingGame(BowlingGame bowlingGame) throws BowlingDaoException;

    /**
     * <p>Deletes the game associated with given bowling game id</p>
     *
     * @param bowlingId uniquely identifies a game
     * @throws BowlingDaoException
     */
    public void deleteGame(Integer bowlingId) throws BowlingDaoException;

    /**
     * <p>get the game associated with given id</p>
     *
     * @param bowlingGameId uniquely identifies a game
     * @return an object representing a series of players and their bowling scores
     * @throws BowlingDaoException
     */
    public BowlingGame getGame(Integer bowlingGameId) throws BowlingDaoException;

    /**
     * <p>adds a new player to a given bowling game</p>
     *
     * @param bowlingGameId uniquely identifies a game
     * @param playerId      unique name chosen by the player
     * @throws BowlingDaoException
     */
    public void addPlayerToGame(Integer bowlingGameId, String playerId) throws BowlingDaoException;

    /**
     * <p>deletes a given player and all their scores from a bowling game</p>
     *
     * @param bowlingGameId uniquely identifies a game
     * @param playerId      a name chosen by the player
     * @throws BowlingDaoException
     */
    public void deletePlayerFromGame(Integer bowlingGameId, String playerId) throws BowlingDaoException;

    /**
     * <p>adds a bowling frame for a given player and game. There are 1-10 frames in the bowling game</p>
     *
     * @param bowlingGameId uniquely identifies a game
     * @param playerId,     a name chosen by the player
     * @param frameId       this is a number between 1-10, inclusive, representing a bowling frame.
     * @param frame,        an array of one to three numbers representing a bowling frame
     * @throws BowlingDaoException
     */
    public void addPlayerScore(Integer bowlingGameId, String playerId, Integer frameId, Integer[] frame) throws BowlingDaoException;

    /**
     * <p>validates and throws exception if a bowling game does not exist</p>
     *
     * @param bowlingGameId
     * @throws BowlingDaoException
     */
    public void checkGameExists(Integer bowlingGameId) throws BowlingDaoException;

    /**
     * * <p>validates and throws exception if a player does not exist in a bowling game</p>
     *
     * @param bowlingGameId
     * @param player
     * @throws BowlingDaoException
     */
    public void checkPlayerExist(Integer bowlingGameId, String player) throws BowlingDaoException;
}