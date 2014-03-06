package org.nick.sample.bowling;

import org.nick.sample.bowling.exception.BowlingInvalidDataException;

/**
 * Contains a few helper methods that help with business logic
 */
public class BowlingHelper {
    public static void validateFrameId(Integer frameId) throws BowlingInvalidDataException {
        if (frameId < 1 || frameId > 10) {
            throw new BowlingInvalidDataException("Invalid frame id  " + frameId + " provided");
        }
    }

    public static void validateFrame(Integer frameId, Integer[] frame) throws BowlingInvalidDataException {
        validateFrameId(frameId);

        if (frameId < 10 && frame.length > 2) {
            throw new BowlingInvalidDataException("Cannot add more than 2 scores in a frame");
        }
        if (frameId == 10 && frame.length > 3) {
            throw new BowlingInvalidDataException("Cannot add more than 3 scores in the 10th frame");
        }
        if (frameId < 10 && sumOfFrameScores(frame) > 10) {
            throw new BowlingInvalidDataException("Invalid frame score");
        }
        if (frameId == 10 && sumOfFrameScores(frame) > 30) {
            throw new BowlingInvalidDataException("Invalid frame score");
        }
    }

    /**
     * <p>returns the sum of the values in the given frame</p>
     *
     * @param frame int array representing a single frame
     * @return int, sum of all the scores in a given frame
     */
    public static Integer sumOfFrameScores(Integer[] frame) {
        int total = 0;
        for (Integer aFrame : frame) {
            total = total + aFrame;
        }
        return total;
    }
}
