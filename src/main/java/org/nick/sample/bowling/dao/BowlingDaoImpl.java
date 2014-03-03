package org.nick.sample.bowling.dao;

import org.nick.sample.bowling.model.BowlingGame;
import org.nick.sample.bowling.exception.BowlingDaoException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>This singleton class is not thread safe and contains in-memory data store (array) as a backing store
 * to hold all game data. Normally, a dao will talk to a persistent data store</p>
 */
public class BowlingDaoImpl implements BowlingDao {

    private static List<BowlingGame> gameData = new ArrayList<BowlingGame>();

    public static BowlingDao getInstance() {
        return DaoHolder.INSTANCE;
    }

    @Override
    public Integer createGame() throws BowlingDaoException {
        BowlingGame bowlingGame = new BowlingGame();
        Integer bowlingGameId;
        synchronized (this) {
            bowlingGameId = gameData.size();
            bowlingGame.setBowlingGameId(bowlingGameId);
            gameData.add(bowlingGame);
        }
        return bowlingGameId;
    }

    @Override
    public void addPlayerToGame(Integer bowlingGameId, String player) throws BowlingDaoException {
        checkGameExists(bowlingGameId);
        checkPlayerDoesNotExist(bowlingGameId, player);
        BowlingGame bg = getGame(bowlingGameId);
        bg.addPlayer(player);
    }

    private void checkPlayerDoesNotExist(Integer bowlingGameId, String player) throws BowlingDaoException{
        BowlingGame bg = gameData.get(bowlingGameId);
        if (null != bg.getPlayersScoresMap() && bg.getPlayersScoresMap().containsKey(player)) {
            throw new BowlingDaoException("Player  already exists: " + player);
        }

    }

    @Override
    public BowlingGame getGame(Integer bowlingGameId) throws BowlingDaoException {
        checkGameExists(bowlingGameId);
        return gameData.get(bowlingGameId);
    }

    @Override
    public void deleteGame(Integer bowlingGameId) throws BowlingDaoException {
        checkGameExists(bowlingGameId);
        BowlingGame bg = getGame(bowlingGameId);
        bg.setBowlingGameId(null);
        bg.setPlayersScoresMap(null);
    }

    @Override
    public void addPlayerScore(Integer bowlingGameId, String player, Integer frameId, Integer[] frame) throws BowlingDaoException {
        checkPlayerExist(bowlingGameId, player);
        BowlingGame bg = getGame(bowlingGameId);
        bg.addFrame(player, frameId, frame);
    }


    @Override
    public void updateBowlingGame(BowlingGame bowlingGame) throws BowlingDaoException {
        Integer id = bowlingGame.getBowlingGameId();
        checkGameExists(id);
        if (null == bowlingGame.getPlayersScoresMap()) {
            throw new BowlingDaoException("incomplete or invalid data");
        }
        gameData.add(id, bowlingGame);
    }

    @Override
    public void deletePlayerFromGame(Integer bowlingGameId, String playerId) throws BowlingDaoException {
        checkGameExists(bowlingGameId);
        checkPlayerExist(bowlingGameId, playerId);
        BowlingGame bg = gameData.get(bowlingGameId);
        Map<String, Map<Integer, Integer[]>> playersScoresMap = bg.getPlayersScoresMap();
        playersScoresMap.remove(playerId);
    }

    public void checkGameExists(Integer bowlingGameId) throws BowlingDaoException {
        if ((gameData.size() - 1) < bowlingGameId) {
            throw new BowlingDaoException("Game with id: " + bowlingGameId + " not found");
        }
        if (gameData.get(bowlingGameId).getBowlingGameId() == null) {
            throw new BowlingDaoException("Game with id: " + bowlingGameId + " not found");
        }
    }

    @Override
    public void checkPlayerExist(Integer bowlingGameId, String player) throws BowlingDaoException {
        BowlingGame bg = gameData.get(bowlingGameId);
        if (null != bg.getPlayersScoresMap() && !bg.getPlayersScoresMap().containsKey(player)) {
            throw new BowlingDaoException("Player  " + player + " not found");
        }
    }

    private static class DaoHolder {
        private static final BowlingDao INSTANCE = new BowlingDaoImpl();
    }

}