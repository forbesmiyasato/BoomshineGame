package edu.pacificu.cs.group6Boomshine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import edu.pacificu.cs.group6Boomshine.edu.pacificu.cs.httprequests.HttpService;
import edu.pacificu.cs.group6Boomshine.edu.pacificu.cs.httprequests.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Defines the BoomshineGame class that contains the view logic
 * and user data fetching and updating.
 *
 * @author Forbes Miyasato
 * @version 1.0
 * @since 1.26.2019
 */

public class BoomshineGame extends AppCompatActivity
{
  private static final int MULTI_PRICE = 10;
  private static final int SUPER_PRICE = 20;
  private static final int ULTI_PRICE = 50;
  private final String sUsername = "Username";
  private CompositeDisposable mcCompositeDisposable;
  private HttpService mcService;
  private Display mcDisplay;
  private BoomshineView mcAnimatedView;
  private PowerUpView mcPowerUpView;
  private HighScoreView mcHighScoreView;
  //User data
  private JSONObject mcUserData;
  private String mcName;
  private int mHighScore = 0;
  private int mPoints = 0;
  private int mPWSuper = 0;
  private int mPWMulti = 0;
  private int mPWUlti = 0;

  /**
   * Initializes member variables to default values.
   * Gets the user data if wasn't logged in as guest.
   *
   * @param savedInstanceState A Bundle containing previously saved
   *                           Activity state (un-used)
   */
  @Override
  protected void onCreate (Bundle savedInstanceState)
  {
    super.onCreate (savedInstanceState);

    WindowManager window = getWindowManager ();
    mcDisplay = window.getDefaultDisplay ();

    mcPowerUpView = new PowerUpView (this, mcDisplay);
    mcAnimatedView = new BoomshineView (this, mcDisplay);
    mcAnimatedView.setBackgroundColor (Color.BLACK);
    mcHighScoreView = new HighScoreView (this);
    setContentView (R.layout.activity_boomshine_game);

    //Init service
    mcCompositeDisposable = new CompositeDisposable ();
    Retrofit retrofitClient = RetrofitClient.getInstance ();
    mcService = retrofitClient.create (HttpService.class);

    //Get user info
    Intent cIntent = getIntent ();

    String username = cIntent.getStringExtra (sUsername);
    String maintainUsername = cIntent.getStringExtra (sUsername);
    if (maintainUsername != null)
    {
      mcName = maintainUsername;
    } else
    {
      mcName = username;
    }
    if (mcName != null)
    {
      getUserData (mcName);
    }
  }

  /**
   * Updates the user data whenever the activity is onStop.
   */
  @Override
  protected void onStop ()
  {
    if (mcName != null)
    {
      updateUserData ();
    }
    super.onStop ();
  }

  /**
   * Changes to game view when play is clicked.
   */
  public void onPlayClicked (View cView)
  {
    setContentView (mcAnimatedView);
  }

  /**
   * Changes back to game menu when back button is clicked
   */
  public void onBackClicked ()
  {
    setContentView (R.layout.activity_boomshine_game);
  }

  /**
   * Gets the user data through http request to server
   *
   * @param username The user's username
   */
  private void getUserData (String username)
  {
    final String sHighScore = "HighScore";
    final String sPoints = "Points";
    final String sPWSuper = "PWSuper";
    final String sPWMulti = "PWMulti";
    final String sPWUltimate = "PWUltimate";
    mcCompositeDisposable.add (mcService.getUserData (username)
            .subscribeOn (Schedulers.io ())
            .observeOn (AndroidSchedulers.mainThread ())
            .subscribe (new Consumer<String> ()
            {
              @Override
              public void accept (String response) throws Exception
              {
                //Returned response has ""x"" format
                response = response.replace ("\"", "");
                mcUserData = new JSONObject (response);
                try
                {
                  mHighScore = (mcUserData.getInt (sHighScore));
                  mPoints = (mcUserData.getInt (sPoints));
                  mPWSuper = (mcUserData.getInt (sPWSuper));
                  mPWMulti = (mcUserData.getInt (sPWMulti));
                  mPWUlti = (mcUserData.getInt (sPWUltimate));
                } catch (JSONException e)
                {
                  e.printStackTrace ();
                }
              }
            }));
  }

  /**
   * Updates the user data to database through http request to server
   */
  private void updateUserData ()
  {
    mcCompositeDisposable.add (mcService.updateUser (mcName,
            mHighScore, mPoints, mPWMulti, mPWSuper, mPWUlti)
            .subscribeOn (Schedulers.io ())
            .observeOn (AndroidSchedulers.mainThread ())
            .subscribe ());
  }

  /**
   * Changes to the power up view when the power up button is clicked
   *
   * @param cView The view clicked
   */
  public void onPowerupsClicked (View cView)
  {
    setContentView (mcPowerUpView);
  }

  /**
   * Changes to the high score view when the high scores button is clicked
   *
   * @param cView The view clicked
   */
  public void onHighScoresClicked (View cView)
  {
    setContentView (mcHighScoreView);
  }

  /**
   * Increase the users multi ball power up and decrease their points
   * accordingly when user clicks to buy multi ball power up
   *
   * @return
   */
  public void onMultiBuy ()
  {
    mPWMulti++;
    mPoints -= MULTI_PRICE;
  }

  /**
   * Increase the users super ball power up and decrease their points
   * accordingly when user clicks to buy super ball power up
   */
  public void onSuperBuy ()
  {
    mPWSuper++;
    mPoints -= SUPER_PRICE;
  }

  /**
   * Increase the users ulti ball power up and decrease their points
   * accordingly when user clicks to buy ulti ball power up
   */
  public void onUltiBuy ()
  {
    mPWUlti++;
    mPoints -= ULTI_PRICE;
  }

  /**
   * Gets the user'ss high score
   *
   * @return The user's high score
   */
  public int getHighScore ()
  {
    return mHighScore;
  }

  /**
   * Gets the user's points
   *
   * @return The user's points
   */
  public int getPoints ()
  {
    return mPoints;
  }

  /**
   * Gets the user's multi ball power up amount
   *
   * @return The user's multi ball power up amount
   */
  public int getPWMulti ()
  {
    return mPWMulti;
  }

  /**
   * Gets the user's super ball power up amount
   *
   * @return The user's super ball power up amount
   */
  public int getPWSuper ()
  {
    return mPWSuper;
  }

  /**
   * Gets the user's ulti ball power up amount
   *
   * @return The user's ulti ball power up amount
   */
  public int getPWUlti ()
  {
    return mPWUlti;
  }

  /**
   * Sets the user's high score
   */
  public void setHighScore (int highScore)
  {
    mHighScore = highScore;
  }

  /**
   * Sets the user's points
   */
  public void setPoints (int points)
  {
    mPoints = points;
  }

  /**
   * Sets the user's multi ball power up amount
   */
  public void setPWMulti (int pwMulti)
  {
    mPWMulti = pwMulti;
  }

  /**
   * Sets the user's super ball power up amount
   */
  public void setPWSuper (int pwSuper)
  {
    mPWSuper = pwSuper;
  }

  /**
   * Sets the user's ulti ball power up amount
   */
  public void setPWUlti (int pwUlti)
  {
    mPWUlti = pwUlti;
  }

  /**
   * Checks if the user has enough points to buy a multi ball power up
   *
   * @return true if they have enough points to buy a multiball power up,
   * false if not
   */
  public boolean canBuyMulti ()
  {
    return mPoints >= MULTI_PRICE;
  }

  /**
   * Checks if the user has enough points to buy a super ball power up
   *
   * @return true if they have enough points to buy a super ball power up,
   * false if not
   */
  public boolean canBuySuper ()
  {
    return mPoints >= SUPER_PRICE;
  }

  /**
   * Checks if the user has enough points to buy a super ball power up
   *
   * @return true if they have enough points to buy a super ball power up,
   * false if not
   */
  public boolean canBuyUlti ()
  {
    return mPoints >= ULTI_PRICE;
  }

  /**
   * Handles the game over logic.
   * Updates user data and opens game over activity
   */
  public void onGameOver (int totalScore, int userMultiPowerups,
                          int userSuperPowerups, int userUltraPowerups)
  {
    final String sPlayerScore = "player_score";
    Intent gameOverIntent = new Intent (BoomshineGame.this,
            GameOverActivity.class);
    gameOverIntent.putExtra (sPlayerScore, totalScore);
    if (mcName != null)
    {
      gameOverIntent.putExtra (sUsername, mcName);
    }

    if (getHighScore () < totalScore)
    {
      setHighScore (totalScore);
    }

    setPWMulti (userMultiPowerups);
    setPWSuper (userSuperPowerups);
    setPWUlti (userUltraPowerups);

    startActivity (gameOverIntent);
  }
}