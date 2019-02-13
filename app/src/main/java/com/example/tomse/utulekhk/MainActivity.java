package com.example.tomse.utulekhk;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private String mPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        } else {
            mUsername = mFirebaseUser.getDisplayName();

            String admin = "Tomas Selecky";
            if (mUsername.equals(admin)){
                startActivity(new Intent(MainActivity.this, MainActivityAdmin.class));
            }

            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
                Toast.makeText(this, "user:" + mUsername, Toast.LENGTH_LONG).show();
                Toast.makeText(this, "photo:" + mPhotoUrl, Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                //adminArray = Array of admins from firestore
                //if adminArray.contains (mFirebaseUser) >> set admin UI
                String admin = "Tomas Selecky";
                if (mUsername.equals(admin)){
                    startActivity(new Intent(MainActivity.this, MainActivityAdmin.class));
                }


                mUsername = mFirebaseUser.getDisplayName();
                Toast.makeText(this, "Signed in! Your username is:" + mUsername, Toast.LENGTH_LONG).show();

                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Toast.makeText(this, "Problem signing in!", Toast.LENGTH_SHORT).show();
                // response.getError().getErrorCode()
                Log.d("Tom:","signing error code:" + response.getError().getErrorCode());
            }
        }
    }
}
