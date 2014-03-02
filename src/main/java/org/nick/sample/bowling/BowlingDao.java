package org.nick.sample.bowling;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by ndhupia on 2/22/14.
 */
public interface BowlingDao {
    public int createGame(BowlingGame bowlingGame) throws BowlingDaoException;
    public boolean addPlayerToGame(Integer bowlingGameId, String username) throws BowlingDaoException;
    public BowlingGame getGame(Integer bowlingGame) throws BowlingDaoException;
    public void deleteGame(Integer bowlingId) throws BowlingDaoException;
    public void addPlayerScore(Integer bowlingGameId, String player, Integer frameId, Integer[] frame) throws BowlingDaoException;
    public Map<String, Integer> getLiveScoresForGame(Integer bowlingGameId) throws BowlingDaoException;
}