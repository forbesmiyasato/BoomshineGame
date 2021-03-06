package edu.pacificu.cs.group6Boomshine;

import android.content.Context;
import android.view.Display;

/**
 * Creates a BoundedMovingSprite class that extends a FixedSprite.
 * Defines a moving sprite that is contained within set limits (typically
 * the size of the device screen) by resetting movement direction when
 * contact is made with the defined limit.
 *
 * @author Forbes Miyasato & Thomas Robasciotti
 * @version 1.0
 * @since 1.24.2019
 */


public class BoundedMovingSprite extends MovingSprite
{
  private int mTopBound;
  private int mBottomBound;
  private int mLeftBound;
  private int mRightBound;

  /**
   * Initializes bounding member variables with passed-in values
   * after calling superclass constructor.
   *
   * @param context     The calling context object
   * @param display     The calling display object
   * @param drawable    The drawable id value
   * @param topCoord    The top coordinate of created circle
   * @param leftCoord   The left coordinate of created circle
   * @param speed       The initial speed of the created circle
   * @param topBound    The top bound for the created circle
   * @param bottomBound The bottom bound for the created circle
   * @param leftBound   The left bound for the created circle
   * @param rightBound  The right bound for the created circle
   */

  BoundedMovingSprite (Context context, Display display, int drawable,
                       int topCoord, int leftCoord, int speed, int topBound,
                       int bottomBound, int leftBound, int rightBound)
  {
    super (context, display, drawable, topCoord, leftCoord, speed);
    mTopBound = topBound;
    mBottomBound = bottomBound;
    mLeftBound = leftBound;
    mRightBound = rightBound;
  }

  /**
   * Changes moving sprite direction based on current direction and
   * which bound limit was contacted.
   */

  public void hitBound ()
  {
    final int CHANGE_DIRECTION = -1;
    final int MULTIPLY_BY_TWO = 2;
    if (getTopCoordinate () <= mTopBound)
    {
      mDy = mDy * CHANGE_DIRECTION;
    }
    if (getLeftCoordinate () <= mLeftBound)
    {
      mDx = mDx * CHANGE_DIRECTION;
    }
    if (mRadius * MULTIPLY_BY_TWO + getTopCoordinate () >= mBottomBound)
    {
      mDy = mDy * CHANGE_DIRECTION;
    }
    if (mRadius * MULTIPLY_BY_TWO + getLeftCoordinate () >= mRightBound)
    {
      mDx = mDx * CHANGE_DIRECTION;
    }
  }
}