package org.nick.sample.bowling;

import org.nick.sample.bowling.dao.BowlingDao;
import org.nick.sample.bowling.dao.BowlingDaoFactory;
import org.nick.sample.bowling.exception.BowlingAppException;
import org.nick.sample.bowling.exception.BowlingInvalidDataException;
import org.nick.sample.bowling.model.BowlingGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>This class contains business logic and process requests from the rest calls.
 * It has access to the dao to perform CRUD operations</p>
 */
public class BowlingController {
    //TODO: ideally should use dependency injection (Spring or Guice) here instead of factory
    private static BowlingDao dao = BowlingDaoFactory.getDao();

    public static void addPlayerToGame(Integer bowlingGameId, String player)
            throws BowlingInvalidDataException, BowlingAppException {
        dao.addPlayerToGame(bowlingGameId, player);
    }

    public static void deletePlayerFromGame(Integer bowlingGameId, String playerId)
            throws BowlingInvalidDataException, BowlingAppException {
        dao.deletePlayerFromGame(bowlingGameId, playerId);
    }

    public static void addPlayerScore(Integer bowlingGameId, String player, Integer frameId, Integer[] playerFrame)
            throws BowlingInvalidDataException, BowlingAppException {
        BowlingHelper.validateFrame(frameId, playerFrame);
        dao.addPlayerScore(bowlingGameId, player, frameId, playerFrame);
    }

    public static void updateBowlingGame(BowlingGame bowlingGame) throws BowlingInvalidDataException, BowlingAppException {
        dao.updateBowlingGame(bowlingGame);
    }

    public static int createGame() throws BowlingAppException {
        return dao.createGame();
    }

    public static BowlingGame getGame(Integer id) throws BowlingInvalidDataException, BowlingAppException {
        return dao.getGame(id);
    }

    public static void deleteGame(Integer id) throws BowlingInvalidDataException, BowlingAppException {
        dao.deleteGame(id);
    }

    public static Map<String, Integer> getLiveScoresForGame(Integer bowlingGameId) throws BowlingInvalidDataException, BowlingAppException {
        dao.checkGameExists(bowlingGameId);
        Map<String, Map<Integer, Integer[]>> scoreMap = dao.getGame(bowlingGameId).getPlayersScoresMap();

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
                    int sumFrameValues = BowlingHelper.sumOfFrameScores(frame);
                    // if its a strike - then add the next two scores if available yet. Handle case where next score is also strike
                    if (isStrike(frame) && i < 9) {
                        if (frameSize > i + 1) {
                            sumFrameValues += frames.get(i + 1)[0];
                        }
                        if ((frameSize > i + 1) && isStrike(frames.get(i + 1))) {
                            if (frameSize > i + 2) {
                                sumFrameValues += frames.get(i + 2)[0];
                            }
                        } else if (frameSize > i + 1) {
                            sumFrameValues += frames.get(i + 1)[1];
                        }
                    } else if (i < 9 && isSpare(frame) && (frameSize > i + 1)) {
                        sumFrameValues += frames.get(i + 1)[0];
                    }
                    liveScore += sumFrameValues;
                }
                liveScoreMap.put(entry.getKey(), liveScore);

            }
        }
        return liveScoreMap;
    }

    /**
     * <p>returns true of the frame was a strike</p>
     *
     * @param frame int array representing a single frame
     * @return true iff the frame is a strike, else false
     */
    private static boolean isStrike(Integer[] frame) {
        return (frame != null && frame.length > 1 && frame[0] == 10);
    }

    /**
     * <p>returns true of the frame was a spare</p>
     *
     * @param frame int array representing a single frame
     * @return true iff the frame is a spare, else false
     */
    private static boolean isSpare(Integer[] frame) {
        return (!isStrike(frame) && BowlingHelper.sumOfFrameScores(frame) == 10);
    }
}
