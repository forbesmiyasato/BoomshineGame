package edu.pacificu.cs.group6Boomshine;

/**
 * Defines the Level class that maintains information regarding
 * the current level. This class determines the total number of circles
 * and a level score that is added to the overall game score.
 *
 * @author Thomas Robasciotti
 *
 * @version 1.0
 */

public class Level {

    private final int CIRCLE_MULTIPLIER = 2;
    private final int SCORE_MULTIPLIER = 10;
    private int mHitsRequired;
    private int mTotalCircles;
    private int mLevelNumber;
    private int mLevelScore;
    private int mCirclesHit;

    /**
     * Instantiates class variables to default values based
     * on passed-in level number.
     *
     * @param levelNumber The desired level number
     */

    Level (int levelNumber)
    {
        this.mLevelNumber = levelNumber;
        this.mLevelScore = 0;
        this.mCirclesHit = 0;
        this.mHitsRequired = mLevelNumber;
        this.mTotalCircles = mLevelNumber * CIRCLE_MULTIPLIER;
    }

    /**
     * Signals the current level is over. Calculates score
     * and returns a boolean indicating level pass or fail
     *
     * @return True if the level was passed, otherwise false.
     */

    public boolean levelOver ()
    {
        calculateLevelScore();
        return this.mHitsRequired <= this.mCirclesHit;
    }

    /**
     * Gets the calculated level score
     *
     * @return The current level score.
     */

    public int getLevelScore ()
    {
        return this.mLevelScore;
    }

    /**
     * Sets the current level score to a value based on
     * the total number of circles hit multiplied by the
     * score modifier.
     *
     */

    private void calculateLevelScore ()
    {
        this.mLevelScore = this.mCirclesHit * SCORE_MULTIPLIER;
    }

    /**
     * Increases the count of total circles hit by one
     *
     */

    public void incrememtCirclesHit ()
    {
        this.mCirclesHit++;
    }

}