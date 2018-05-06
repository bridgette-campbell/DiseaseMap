package edu.uw.tacoma.css.diseasemap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    /**
     * Request code
     */
    private static final int RC_SIGN_IN = 1;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic profile (included in
        // DEFAULT_SIGN_IN)
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Google Sign-In button handler (XML onClick doesn't work with it)
        SignInButton btnGoogleSignIn = findViewById(R.id.google_sign_in_button);
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check for existing Google Sign-In account (if the user is already signed in, the
        // GoogleSignInAccount will be non-null)
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from googleSignIn()
        if (requestCode == RC_SIGN_IN) {

            // The Task returned from this call is always completed; no need to attach a listener
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    /**
     * Handles the result of the user attempting to sign in with a Google Account.
     *
     * @param completedTask The GoogleSignInAccount (handled by Google's API)
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        }
        catch (ApiException e) {

            // Toast to inform the user
            Toast.makeText(this, R.string.sign_in_failed, Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    /**
     * Starts MapActivity if the user is signed in.
     *
     * @param account A valid account means the user is signed in; null means they are not
     */
    private void updateUI(GoogleSignInAccount account) {

        // Non-null account means the user is signed in
        if (account != null) {

            // Launch MapActivity
            startActivity(new Intent(this, MapActivity.class));
            finish();
        }
    }
}
