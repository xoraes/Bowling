package org.nick.sample.bowling;

import java.util.*;

/**
 * Created by ndhupia on 2/22/14.
 */

// not thread safe
public class BowlingDaoImpl implements BowlingDao {

    private static List<BowlingGame> gameData = new ArrayList<BowlingGame>();

    public static BowlingDao getInstance() {
        return DaoHolder.INSTANCE;
    }

    /**
     * <p>returns true of the frame was a strike</p>
     * @param frame int array representing a single frame
     * @return true iff the frame is a strike, else false
     */
    private static boolean isStrike(Integer[] frame) {
        return (frame.length == 1);
    }

    /**
     * <p>returns true of the frame was a spare</p>
     * @param frame int array representing a single frame
     * @return true iff the frame is a spare, else false
     */
    private static boolean isSpare(Integer[] frame) {
        return (! isStrike(frame) && sumOfFrameScores(frame) == 10);
    }

    /**
     * <p>returns the sum of the values in the given frame</p>
     * @param frame int array representing a single frame
     * @return int, sum of all the scores in a given frame
     */
    private static Integer sumOfFrameScores(Integer[] frame) {
        int total = 0;
        for (Integer aFrame : frame) {
            total = total + aFrame;
        }
        return total;
    }

    @Override
    public int createGame(BowlingGame bowlingGame) throws BowlingDaoException {
        if (null == bowlingGame) {
            throw new BowlingDaoException("Could not create bowling game due to invalid data");
        }
        int bowlingGameId;

        synchronized (this) {
            bowlingGameId = gameData.size();
            gameData.add(bowlingGame);
        }
        bowlingGame.setBowlingGameId(bowlingGameId);
        return bowlingGameId;
    }

    @Override
    public boolean addPlayerToGame(Integer bowlingGameId, String player) throws BowlingDaoException {
        validateBowlingId(bowlingGameId);
        BowlingGame bg = getGame(bowlingGameId);
        if (null != bg.getPlayersScoresMap() && bg.getPlayersScoresMap().containsKey(player)) {
            throw new BowlingDaoException("Player with username: " + player + " already exists. Please choose another username");
        }
        return bg.addPlayer(player);
    }

    @Override
    public BowlingGame getGame(Integer bowlingGameId) throws BowlingDaoException{
        validateBowlingId(bowlingGameId);
        return gameData.get(bowlingGameId);
    }

    @Override
    public void deleteGame(Integer bowlingGameId) throws BowlingDaoException {
        validateBowlingId(bowlingGameId);
        BowlingGame bg = getGame(bowlingGameId);
        bg.setBowlingGameId(null);
        bg.setPlayersScoresMap(null);
    }

    @Override
    public void addPlayerScore(Integer bowlingGameId, String player, Integer frameId, Integer[] frame) throws BowlingDaoException {
        validatePlayer(bowlingGameId, player);
        validateFrame(frameId, frame);
        BowlingGame bg = getGame(bowlingGameId);
        bg.addFrame(player,frameId, frame);
    }

    private void validateFrame(Integer frameId, Integer[] frame) throws BowlingDaoException {
        validateFrameId(frameId);
        if (frameId < 10 && frame.length > 2) {
            throw new BowlingDaoException("Cannot add more than 2 scores in a frame");
        }
        if (frameId == 10 && frame.length > 3) {
            throw new BowlingDaoException("Cannot add more than 3 scores in the 10th frame");
        }
        if (frameId < 10 && sumOfFrameScores(frame) > 10) {
            throw new BowlingDaoException("Invalid frame score");
        }
        if (frameId == 10 && sumOfFrameScores(frame) > 30) {
            throw new BowlingDaoException("Invalid frame score");
        }

    }

    @Override
    public Map<String, Integer> getLiveScoresForGame(Integer bowlingGameId) throws BowlingDaoException {
        validateBowlingId(bowlingGameId);
        Map<String, Map<Integer, Integer[]>> scoreMap = getGame(bowlingGameId).getPlayersScoresMap();

        Map<String, Integer> liveScoreMap = new HashMap<String, Integer>();
        if (null != scoreMap) {
            for (Map.Entry<String, Map<Integer, Integer[]>> entry : scoreMap.entrySet()) {
                Integer liveScore = 0;
                Map<Integer, Integer[]> frameList = entry.getValue();
                if (frameList == null) {
                    continue;
                }
                List<Integer[]> frames = new ArrayList<Integer[]>(frameList.values());

                int frameSize = frames.size();
                for (int i = 0; i < frameSize; i++) {
                    Integer[] frame = frames.get(i);
                    int sumFrameValues = sumOfFrameScores(frame);
                    // if its a strike - then add the next two scores if available yet. Handle case where next score is also strike
                    if (isStrike(frame) && i < 9) {
                        if (frameSize > i+1) {
                            sumFrameValues+= frames.get(i+1)[0];
                        }
                        if ((frameSize > i+1) && isStrike(frames.get(i+1))) {
                            if (frameSize > i + 2) {
                                sumFrameValues+= frames.get(i+2)[0];
                            }
                        }
                        else if (frameSize > i+1) {
                            sumFrameValues+= frames.get(i+1)[1];
                        }
                    }
                    else if (i < 9 && isSpare(frame) && (frameSize > i+1)) {
                        sumFrameValues+= frames.get(i+1)[0];
                    }
                    liveScore += sumFrameValues;
                }
                liveScoreMap.put(entry.getKey(),liveScore);

            }
        }
        return liveScoreMap;
    }

    private void validateBowlingId(Integer bowlingGameId) throws BowlingDaoException {
        if ((gameData.size() - 1) < bowlingGameId) {
            throw new BowlingDaoException("Game with id: " + bowlingGameId + " not found");
        }
        if (gameData.get(bowlingGameId).getBowlingGameId() == null) {
            throw new BowlingDaoException("Game with id: " + bowlingGameId + " not found");
        }
    }

    private void validatePlayer(Integer bowlingGameId, String player) throws BowlingDaoException {
        BowlingGame bg = gameData.get(bowlingGameId);
        if (null != bg.getPlayersScoresMap() && !bg.getPlayersScoresMap().containsKey(player)) {
            throw new BowlingDaoException("Player  " + player + " not found");
        }
    }

    private void validateFrameId(Integer frameId) throws BowlingDaoException {
        if (frameId < 1 || frameId > 10) {
            throw new BowlingDaoException("Invalid frame id  " + frameId + " provided");
        }
    }

    private static class DaoHolder {
        private static final BowlingDao INSTANCE = new BowlingDaoImpl();
    }

}