package org.nick.sample.bowling;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ndhupia
 * Date: 2/22/14
 * Time: 8:50 AM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public final class BowlingGame {

    private Integer bowlingGameId;

    public Map<String, Map<Integer, Integer[]>> getPlayersScoresMap() {
        return playersScoresMap;
    }

    public void setPlayersScoresMap(Map<String, Map<Integer, Integer[]>> playersScoresMap) {
        this.playersScoresMap = playersScoresMap;
    }

    @XmlElement(nillable = true)
    private Map<String, Map<Integer, Integer[]>> playersScoresMap;


    public BowlingGame() {
        playersScoresMap = new HashMap<String, Map<Integer, Integer[]>>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BowlingGame that = (BowlingGame) o;

        return bowlingGameId.equals(that.bowlingGameId);

    }

    @Override
    public int hashCode() {
        return bowlingGameId.hashCode();
    }

    public Integer getBowlingGameId() {
        return bowlingGameId;
    }

    public void setBowlingGameId(Integer bowlingGameId) {
        this.bowlingGameId = bowlingGameId;
    }

    public void addFrame(String player, Integer frameId, Integer[] frame) {
        if (null != playersScoresMap) {
            Map<Integer, Integer[]> scoreValues = playersScoresMap.get(player);
            if (scoreValues != null) {
                scoreValues.put(frameId, frame);
            } else {
                scoreValues = new HashMap<Integer, Integer[]>();
                scoreValues.put(frameId, frame);
                playersScoresMap.put(player, scoreValues);
            }
        }
    }

    @Override
    public String toString() {
        return "BowlingGame{" +
                "bowlingGameId=" + bowlingGameId +
                ", playersScoresMap=" + playersScoresMap +
                '}';
    }

    public boolean addPlayer(String player) {
        if (!playersScoresMap.containsKey(player)) {
            playersScoresMap.put(player, null);
            return true;
        }
        return false;
    }
}
