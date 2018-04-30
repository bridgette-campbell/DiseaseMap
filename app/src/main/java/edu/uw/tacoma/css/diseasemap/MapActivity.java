package edu.uw.tacoma.css.diseasemap;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapActivity extends AppCompatActivity {

    private static final int RC_DISEASE = 0;

    private Button mButtonDisease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mButtonDisease = findViewById(R.id.disease_button);
    }

    // "Select Disease" onClick - launches DiseaseActivity
    public void launchDiseases(View v) {
        Intent diseases = new Intent(this, DiseaseActivity.class);
        startActivityForResult(diseases, RC_DISEASE);
    }

    // Gets the chosen Disease
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == RC_DISEASE && data != null) {
            String chosenDisease = DiseaseActivity.getChosenDisease(data);
            mButtonDisease.setText(getString(R.string.disease_selected, chosenDisease));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out:
                signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

        GoogleSignIn.getClient(this, gso).signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),
                                R.string.signed_out, Toast.LENGTH_SHORT).show();
                    }
                });

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
