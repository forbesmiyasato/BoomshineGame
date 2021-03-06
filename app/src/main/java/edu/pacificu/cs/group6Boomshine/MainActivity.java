package edu.pacificu.cs.group6Boomshine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import edu.pacificu.cs.group6Boomshine.edu.pacificu.cs.httprequests.HttpService;
import edu.pacificu.cs.group6Boomshine.edu.pacificu.cs.httprequests.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Defines the GameOverActivity class that contains display and
 * interaction logic for the Game Over screen.
 *
 * @author Forbes Miyasato
 * @version 1.0
 * @since 1.26.2019
 */

public class MainActivity extends AppCompatActivity
{
  private final String sUSERNAME = "Username";
  private EditText mcLoginUsername;
  private EditText mcLoginPassword;
  private String msName;
  private TextView mcCreateAccount;
  private CompositeDisposable mcCompositeDisposable;
  private HttpService mcService;

  @Override
  protected void onCreate (Bundle savedInstanceState)
  {
    super.onCreate (savedInstanceState);

    setContentView (R.layout.activity_main);

    //Init service
    mcCompositeDisposable = new CompositeDisposable ();
    Retrofit retrofitClient = RetrofitClient.getInstance ();
    mcService = retrofitClient.create (HttpService.class);

    //Init view
    mcLoginPassword = findViewById (R.id.login_password);
    mcLoginUsername = findViewById (R.id.login_username);
    mcCreateAccount = findViewById (R.id.create_account);

    mcCreateAccount.setOnClickListener (new View.OnClickListener ()
    {
      @Override
      public void onClick (View view)
      {
        createAccountClicked ();
      }
    });
  }

  @Override
  protected void onStop ()
  {
    mcCompositeDisposable.clear ();
    super.onStop ();
  }

  /**
   * Overrides the superball.onCreateOptionsMenu method.Inflates the
   * options menu contained in res/menu/menu.
   *
   * @param menu The menu to inflate into. The items and
   *             submenus in the XML will be added to this menu.
   */

  @Override
  public boolean onCreateOptionsMenu (Menu menu)
  {
    MenuInflater inflater = getMenuInflater ();
    inflater.inflate (R.menu.menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item)
  {
    if (item.getItemId () == R.id.menuAbout)
    {
      onAbout ();
    }
    return true;
  }

  /**
   * Starts a new About Activity when the user presses the about button.
   */
  public void onAbout ()
  {
    startActivity (new Intent (this,
      About.class));
  }

  /**
   * Inflates the register layout when create account is clicked
   */
  public void createAccountClicked ()
  {
    final View registerLayout = LayoutInflater.from
            (MainActivity.this).inflate (R.layout.activity_register,
            null);

    new MaterialStyledDialog.Builder (MainActivity.this)
      .setIcon (R.drawable.ic_account)
      .setTitle ("REGISTRATION")
      .setDescription ("Please fill all fields")
      .setCustomView (registerLayout)
      .setNegativeText ("CANCEL")
      .onNegative (new MaterialDialog.SingleButtonCallback ()
      {
        @Override
        public void onClick (@NonNull MaterialDialog dialog,
                             @NonNull DialogAction which)
        {
          dialog.dismiss ();
        }
      })
      .setPositiveText ("REGISTER")
      .onPositive (new MaterialDialog.SingleButtonCallback ()
      {
        @Override
        public void onClick (@NonNull MaterialDialog dialog,
                             @NonNull DialogAction which)
        {
          MaterialEditText editRegisterName = registerLayout.findViewById
                  (R.id.register_username);
          MaterialEditText editRegisterPassword = registerLayout.findViewById
                  (R.id.register_password);

          if (TextUtils.isEmpty (editRegisterName.getText ().toString ()))
          {
            Toast.makeText (MainActivity.this,
                    "Name cannot be empty",
              Toast.LENGTH_SHORT).show ();
            return;
          }
          if (TextUtils.isEmpty (editRegisterPassword.getText ().toString ()))
          {
            Toast.makeText (MainActivity.this,
                    "Password cannot be empty",
              Toast.LENGTH_SHORT).show ();
            return;
          }

          registerUser (editRegisterName.getText ().toString (),
            editRegisterPassword.getText ().toString ());
        }
      }).show ();
  }

  /**
   * Makes an http request to the server to register the user, if successful
   * redirect the user to the game menu activity
   *
   * @param username The user's username
   * @param password The user's password
   */
  private void registerUser (String username, String password)
  {
    final String sSUCCESS = "User Added";
    msName = username;
    mcCompositeDisposable.add (mcService.registerUser (username, password)
      .subscribeOn (Schedulers.io ())
      .observeOn (AndroidSchedulers.mainThread ())
      .subscribe (new Consumer<String> ()
      {
        @Override
        public void accept (String response) throws Exception
        {
          //Returned response has ""x"" format
          Toast.makeText (MainActivity.this, "" + response,
            Toast.LENGTH_SHORT).show ();
          response = response.replace ("\"", "");
          if (response.equals (sSUCCESS))
          {
            Intent cIntent = new Intent (MainActivity.this,
              BoomshineGame.class);
            cIntent.putExtra (sUSERNAME, msName);
            startActivity (cIntent);
          }
        }
      }));
  }

  /**
   * Starts a new About Activity when the user presses the
   * play as guest button.
   *
   * @param cView The view clicked
   */
  public void playAsGuestClicked (View cView)
  {
    startActivity (new Intent (this,
      BoomshineGame.class));
  }

  /**
   * Logs in the user when the login button is clicked
   *
   * @param cView The view clicked
   */
  public void loginClicked (View cView)
  {
    loginUser (mcLoginUsername.getText ().toString (),
      mcLoginPassword.getText ().toString ());
  }

  /**
   * Makes an http request to the server to login the user, if successful
   * redirect the user to the game menu activity
   *
   * @param username The user's username
   * @param password The user's password
   */
  private void loginUser (String username, String password)
  {
    final String sSUCCESS = "Login Success";
    if (TextUtils.isEmpty (username))
    {
      Toast.makeText (this, "Name cannot be empty",
              Toast.LENGTH_SHORT).show ();
      return;
    }
    if (TextUtils.isEmpty (password))
    {
      Toast.makeText (this, "Password cannot be empty",
              Toast.LENGTH_SHORT).show ();
      return;
    }
    msName = username;
    mcCompositeDisposable.add (mcService.loginUser (username, password)
      .subscribeOn (Schedulers.io ())
      .observeOn (AndroidSchedulers.mainThread ())
      .subscribe (new Consumer<String> ()
      {
        @Override
        public void accept (String response) throws Exception
        {
          //Returned response has ""x"" format
          response = response.replace ("\"", "");
          Toast.makeText (MainActivity.this, "" + response,
            Toast.LENGTH_SHORT).show ();
          if (response.equals (sSUCCESS))
          {
            Intent cIntent = new Intent (MainActivity.this,
              BoomshineGame.class);
            cIntent.putExtra (sUSERNAME, msName);
            startActivity (cIntent);
          }
        }
      }));
  }
}