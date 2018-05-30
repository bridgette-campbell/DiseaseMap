package edu.uw.tacoma.css.diseasemap.account;

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

import edu.uw.tacoma.css.diseasemap.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountFragment extends DialogFragment {

    /**
     * The URL for adding a user to the web server
     */
    private static final String CREATE_ACCOUNT_URL =
            "http://diseasemapapp.000webhostapp.com/addUser.php?";

    private CreateAccountListener mListener;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mVerifyPasswordEditText;

    /**
     * Mandatory empty constructor
     */
    public CreateAccountFragment() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_create_account, null);
        mEmailEditText = view.findViewById(R.id.create_account_email);
        mPasswordEditText = view.findViewById(R.id.create_account_password);
        mVerifyPasswordEditText = view.findViewById(R.id.verify_password);

        builder.setView(view)
                .setPositiveButton(R.string.create_account, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

                        // If the passwords match
                        if (mPasswordEditText.getText().toString().equals(
                                mVerifyPasswordEditText.getText().toString())) {

                            // Attempt to create the account and sign in the user
                            String url = buildCreateAccountURL();
                            mListener.addAccount(url);
                        }
                        else {
                            Toast.makeText(getContext(), "Passwords do not match",
                                    Toast.LENGTH_SHORT).show();
                        }
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

        if (context instanceof CreateAccountListener) {
            mListener = (CreateAccountListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement CreateAccountListener");
        }
    }

    public interface CreateAccountListener {
        void addAccount(String url);
    }

    /*
     * Builds the URL for creating accounts
     */
    private String buildCreateAccountURL() {
        StringBuilder sb = new StringBuilder(CREATE_ACCOUNT_URL);

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

        Log.v("CreateAccountFragment", sb.toString());
        return sb.toString();
    }
}
