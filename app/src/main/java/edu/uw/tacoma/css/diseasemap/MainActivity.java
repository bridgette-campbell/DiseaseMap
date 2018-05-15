package edu.uw.tacoma.css.diseasemap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.uw.tacoma.css.diseasemap.account.CreateAccountFragment;
import edu.uw.tacoma.css.diseasemap.account.SignInFragment;

/**
 * The {@link AppCompatActivity} that handles signing into our app to use it.
 *
 * @author Bridgette Campbell, Daniel McBride, Matt Qunell
 */
public class MainActivity extends AppCompatActivity
        implements CreateAccountFragment.CreateAccountListener,
        SignInFragment.VerifyAccountListener {

    /**
     * SharedPreferences key for determining if the user is signed in
     */
    public static final String SIGNED_IN = "signed_in";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Launch MapActivity if the user is already signed in
        if (getPreferences(Context.MODE_PRIVATE).getInt(SIGNED_IN, -1) == 1) {
            startActivity(new Intent(this, MapActivity.class));
            finish();
        }
    }

    /**
     * Button onClick handler for "Create Account"
     *
     * @param v The parent View
     */
    public void launchCreateAccount(View v) {
        DialogFragment fragment = new CreateAccountFragment();
        fragment.show(getSupportFragmentManager(), "Create Account");
    }

    /**
     * Button onClick handler for "Sign In"
     *
     * @param v The parent View
     */
    public void launchLogIn(View v) {
        DialogFragment fragment = new SignInFragment();
        fragment.show(getSupportFragmentManager(), "Sign In");
    }

    /**
     * Signs the user in
     */
    public void signIn() {
        //int signedIn = getPreferences(Context.MODE_PRIVATE).getInt(SIGNED_IN, -1);

        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        editor.putInt(SIGNED_IN, 1);
        editor.apply();

        startActivity(new Intent(this, MapActivity.class));
        finish();
    }

    @Override
    public void addAccount(String url) {
        new AddAccountTask().execute(new String[]{url});

        // Takes you back to the previous screen by popping the current fragment
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void verifyAccount(String url) {
        new VerifyAccountTask().execute(new String[]{url});

        // Takes you back to the previous screen by popping the current fragment
        getSupportFragmentManager().popBackStackImmediate();
    }


    /**
     * Inner class that handles adding accounts to the website's database
     */
    private class AddAccountTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder response = new StringBuilder();
            HttpURLConnection urlConnection = null;

            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s;
                    while ((s = buffer.readLine()) != null) {
                        response.append(s);
                    }

                }
                catch (Exception e) {
                    response = new StringBuilder("Unable to add account. "
                            + e.getMessage());
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");

                if (status.equals("success")) {
                    Log.v("MapActivity", "here");
                    Toast.makeText(getApplicationContext(), "Account successfully added!"
                            , Toast.LENGTH_LONG)
                            .show();

                    signIn();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Failed to add: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            }
            catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data: " +
                        e.getMessage(), Toast.LENGTH_LONG).show();
                Log.v("MapActivity", e.getMessage());
            }
        }
    }


    /**
     * Inner class that handles account verification
     */
    private class VerifyAccountTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder response = new StringBuilder();
            HttpURLConnection urlConnection = null;

            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s;
                    while ((s = buffer.readLine()) != null) {
                        response.append(s);
                    }
                }
                catch (Exception e) {
                    response = new StringBuilder("Unable to verify the account. " +
                            e.getMessage());
                }
                finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            Log.v("MapActivity", "onPostExecute (verify)");

            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");

                if (status.equals("success")) {
                    // todo: move this toast to MapActivity?
                    Toast.makeText(getApplicationContext(), "User successfully signed in!"
                            , Toast.LENGTH_LONG)
                            .show();

                    signIn();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Failed to signed in: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            }
            catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data: " +
                        e.getMessage(), Toast.LENGTH_LONG).show();

                Log.v("MapActivity", e.getMessage());
            }
        }
    }
}
