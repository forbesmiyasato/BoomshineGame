package edu.pacificu.cs.group6Boomshine;

import android.content.Context;
import android.view.Display;

import java.util.ArrayList;

/**
 * Defines the ExplodingCircleFactory class that creates
 * expanding circles with special behavior/attributes (power-ups).
 *
 * @author Thomas Robasciotti
 * @version 1.0
 */

public class ExplodingCircleFactory
{

  private ArrayList<ExplodingBoundedMovingCircle> mcCircles;

  /**
   * Creates and returns a collection of ExplodingBoundedMovingCircles
   * based on individual strategy behavior. Requires all information for
   * creating a normal ExplodingBoundedMovingCircle.
   *
   * @return A collection of ExplodingBoundedMovingCircles
   */

  public ArrayList<ExplodingBoundedMovingCircle> create (ExplodingType eType,
                                                         Context context,
                                                         Display display,
                                                         int drawable,
                                                         int topCoord,
                                                         int leftCoord,
                                                         int speed,
                                                         int topBound,
                                                         int bottomBound,
                                                         int leftBound,
                                                         int rightBound,
                                                         int radius)
  {
    mcCircles = new ArrayList<> ();

    switch (eType)
    {
      case NORMAL:
        mcCircles.add (new ExplodingBoundedMovingCircle (eType, context,
          display, drawable, topCoord, leftCoord, speed, topBound, bottomBound,
          leftBound, rightBound, radius));
        break;
      case MULTI:
        MultiballExplode cMulti = new MultiballExplode ();
        mcCircles.addAll (cMulti.createMulti (eType, context, display,
          drawable,topCoord, leftCoord, speed, topBound, bottomBound,
          leftBound, rightBound, radius));
      case SUPER:
        SuperballExplode cSuper = new SuperballExplode ();
        mcCircles.addAll (cSuper.createSuper (eType, context, display,
          drawable, topCoord, leftCoord, speed, topBound, bottomBound,
          leftBound, rightBound, radius));
      case ULTIMATE:
        UltraballExplode cUltimate = new UltraballExplode ();
        mcCircles.addAll (cUltimate.createUltra (eType, context, display,
          drawable, topCoord, leftCoord, speed, topBound, bottomBound,
          leftBound, rightBound, radius));
      default:
        break;
    }

    return mcCircles;
  }
}
