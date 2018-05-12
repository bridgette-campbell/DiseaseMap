package edu.uw.tacoma.css.diseasemap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;

/**
 * A simple {@link Fragment} subclass.
 */
public class LogInFragment extends DialogFragment {

    public static final String VERIFY_ACCOUNT_URL =
            "http://diseasemapapp.000webhostapp.com/login.php?";

    private VerifyAccountListener mListener;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_log_in, null);
        mEmailEditText = view.findViewById(R.id.log_in_email);
        mPasswordEditText = view.findViewById(R.id.log_in_password);

        builder.setView(view)
                .setPositiveButton(R.string.log_in, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

                        // Attempt to verify the account and sign in the user
                        String url = buildLogInURL();
                        mListener.verifyAccount(url);
                    }
                })

                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CreateAccountFragment.CreateAccountListener) {
            mListener = (VerifyAccountListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement CreateAccountListener");
        }
    }

    public interface VerifyAccountListener {
        void verifyAccount(String url);
    }

    private String buildLogInURL() {
        StringBuilder sb = new StringBuilder(VERIFY_ACCOUNT_URL);

        try {
            String email = mEmailEditText.getText().toString();
            sb.append("email=");
            sb.append(URLEncoder.encode(email, "UTF-8"));

            String password = mPasswordEditText.getText().toString();
            sb.append("&password=");
            sb.append(URLEncoder.encode(password, "UTF-8"));
        }
        catch (Exception e) {
            Toast.makeText(getContext(), "Something wrong with the url: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        Log.v("MapActivity", sb.toString());
        return sb.toString();
    }
}
