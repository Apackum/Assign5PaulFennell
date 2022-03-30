package com.example.sdafinalass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import util.GamesApi;

public class LoginActivity extends AppCompatActivity {

    //Declared variables
    private Button loginButton, createButton;
    private AutoCompleteTextView emailEntry;
    private EditText passwordEntry;
    private ProgressBar progressBar;

    //Firebase declarations
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    //connection for firebase database
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        progressBar = findViewById(R.id.login_progress);

        firebaseAuth = FirebaseAuth.getInstance();

        emailEntry = findViewById(R.id.email_text);
        passwordEntry = findViewById(R.id.password);
        loginButton = findViewById(R.id.email_sign_in_button);
        createButton = findViewById(R.id.create_account_button_login);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emailPasswordUserLogin(emailEntry.getText().toString().trim(),
                        passwordEntry.getText().toString().trim());


            }
        });

    }

    private void emailPasswordUserLogin(String email, String password) {

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password)) {

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            String currentUserId = user.getUid();

                            collectionReference
                                    .whereEqualTo("userId", currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if (error != null) {

                                            }
                                            assert value != null;
                                            if (!value.isEmpty()) {

                                                progressBar.setVisibility(View.INVISIBLE);
                                                for (QueryDocumentSnapshot snapshot : value) {

                                                    GamesApi gamesApi = GamesApi.getInstance();
                                                    gamesApi.setUsername(snapshot.getString("username"));
                                                    gamesApi.setUserId(snapshot.getString("userId"));
                                                    //Starts the ListActivity
                                                    startActivity(new Intent(LoginActivity.this,
                                                            MainActivity.class));


                                                }

                                            }
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.VISIBLE);
                            Log.d("Login Activity", "onFailure: " + e.toString());
                        }
                    });


        } else {
            loginButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this, "Please Enter An email and a Password", Toast.LENGTH_SHORT).show();
        }
    }
}
