package org.nick.sample.bowling.dao;

import org.nick.sample.bowling.exception.BowlingInvalidDataException;
import org.nick.sample.bowling.model.BowlingGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>This singleton class is not thread safe and contains in-memory data store (array) as a backing store
 * to hold all game data. Normally, a dao will talk to a persistent data store. Since we are implementing
 * an interface here, this class is a singleton rather than a bunch of static methods
 *
 * This DAO impl does not throw any BowlingAppException because it does everything in memory - no
 * outsiede connection is made.
 *
 * Could potentially catch runtime exceptions here and then throw BowlingAppException but not doing that
 * for now because it is an overkill and because this is a fake data store anyway
 *
 * </p>
 */
public class BowlingDaoImpl implements BowlingDao {

    //all data is stores in the in memory cache here
    private static List<BowlingGame> gameData = new ArrayList<BowlingGame>();

    public static BowlingDao getInstance() {
        return DaoHolder.INSTANCE;
    }

    @Override
    public Integer createGame() {
        BowlingGame bowlingGame = new BowlingGame();
        Integer bowlingGameId;
        synchronized (this) {
            //the gamedata size gets used as the bowlinggameid inside the bowling game object
            bowlingGameId = gameData.size();
            gameData.add(bowlingGame);
        }
        //bowling game is considered created once it has the id set in it
        bowlingGame.setBowlingGameId(bowlingGameId);
        return bowlingGameId;
    }

    @Override
    public void addPlayerToGame(Integer bowlingGameId, String player) throws BowlingInvalidDataException {
        checkGameExists(bowlingGameId);
        checkPlayerDoesNotExist(bowlingGameId, player);
        BowlingGame bg = getGame(bowlingGameId);
        bg.addPlayer(player);
    }

    @Override
    public BowlingGame getGame(Integer bowlingGameId) throws BowlingInvalidDataException {
        checkGameExists(bowlingGameId);
        return gameData.get(bowlingGameId);
    }

    @Override
    public void deleteGame(Integer bowlingGameId) throws BowlingInvalidDataException {
        checkGameExists(bowlingGameId);
        BowlingGame bg = getGame(bowlingGameId);
        bg.setBowlingGameId(null);
        bg.setPlayersScoresMap(null);
    }

    @Override
    public void addPlayerScore(Integer bowlingGameId, String player, Integer frameId, Integer[] frame) throws BowlingInvalidDataException {
        checkPlayerExistInGame(bowlingGameId, player);
        BowlingGame bg = getGame(bowlingGameId);
        bg.addFrame(player, frameId, frame);
    }


    @Override
    public void updateBowlingGame(BowlingGame bowlingGame) throws BowlingInvalidDataException {
        Integer id = bowlingGame.getBowlingGameId();
        checkGameExists(id);
        if (null == bowlingGame.getPlayersScoresMap()) {
            throw new BowlingInvalidDataException("incomplete or invalid data");
        }
        gameData.add(id, bowlingGame);
    }

    @Override
    public void deletePlayerFromGame(Integer bowlingGameId, String playerId) throws BowlingInvalidDataException {
        checkPlayerExistInGame(bowlingGameId, playerId);
        BowlingGame bg = gameData.get(bowlingGameId);
        Map<String, Map<Integer, Integer[]>> playersScoresMap = bg.getPlayersScoresMap();
        playersScoresMap.remove(playerId);
    }

    public void checkGameExists(Integer bowlingGameId) throws BowlingInvalidDataException {
        if ((gameData.size() - 1) < bowlingGameId) {
            throw new BowlingInvalidDataException("Game with id: " + bowlingGameId + " not found");
        }
        // since we never remove bowlinggame object from list, we must check if it was null
        if (gameData.get(bowlingGameId).getBowlingGameId() == null) {
            throw new BowlingInvalidDataException("Game with id: " + bowlingGameId + " not found");
        }
    }

    @Override
    public void checkPlayerExistInGame(Integer bowlingGameId, String player) throws BowlingInvalidDataException {
        checkGameExists(bowlingGameId);
        BowlingGame bg = gameData.get(bowlingGameId);
        if (null != bg.getPlayersScoresMap() && !bg.getPlayersScoresMap().containsKey(player)) {
            throw new BowlingInvalidDataException("Player  " + player + " not found");
        }
    }
    private void checkPlayerDoesNotExist(Integer bowlingGameId, String player) throws BowlingInvalidDataException{
        BowlingGame bg = gameData.get(bowlingGameId);
        if (null != bg.getPlayersScoresMap() && bg.getPlayersScoresMap().containsKey(player)) {
            throw new BowlingInvalidDataException("Player  already exists: " + player);
        }

    }
    private static class DaoHolder {
        private static final BowlingDao INSTANCE = new BowlingDaoImpl();
    }

}