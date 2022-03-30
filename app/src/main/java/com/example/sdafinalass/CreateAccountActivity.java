package com.example.sdafinalass;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.GamesApi;

public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    //Firestore connections
    private final FirebaseFirestore database = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = database.collection("Users");

    private EditText emailTextEdit, passwordTextEdit, userNameTextEdit;
    private ProgressBar progressBar;
    private Button creatAccountButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        firebaseAuth = FirebaseAuth.getInstance();

        creatAccountButton = findViewById(R.id.create_account_button);
        progressBar = findViewById(R.id.create_account_progress);
        emailTextEdit = findViewById(R.id.email_account);
        passwordTextEdit = findViewById(R.id.password_account);
        userNameTextEdit = findViewById(R.id.username_account_info);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();

                if(firebaseUser != null) {
                    //The firebase user is currently logged in


                }else {
                    //No user logged in yet
                }
            }
        };

        creatAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(emailTextEdit.getText().toString())
                        && !TextUtils.isEmpty(passwordTextEdit.getText().toString())
                        && !TextUtils.isEmpty(userNameTextEdit.getText().toString())) {

                    String email = emailTextEdit.getText().toString().trim();
                    String password = passwordTextEdit.getText().toString().trim();
                    String username = userNameTextEdit.getText().toString().trim();


                    createUserEmailAccount(email, password, username);

                }else {
                    Toast.makeText(CreateAccountActivity.this, "Please do not leave blank entries", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    private void createUserEmailAccount(String email, String password, String username){

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {


            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()) {
                                //This is going to take user to next  page
                                Log.d("createUser", "createUserWithEmail:success");
                                firebaseUser = firebaseAuth.getCurrentUser();
                                assert firebaseUser != null;
                                String currentFirebaseUser = firebaseUser.getUid();

                                //Create a user map to create save a user within the user collection
                                Map<String, String> userObject = new HashMap<>();
                                userObject.put("userId", currentFirebaseUser);
                                userObject.put("username", username);

                                //this used to save to firebase database
                                collectionReference.add(userObject)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if(task.getResult().exists()) {
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    String name = task.getResult()
                                                                            .getString("username");

                                                                    GamesApi gamesApi = GamesApi.getInstance();// This is the global api
                                                                    gamesApi.setUserId(currentFirebaseUser);
                                                                    gamesApi.setUsername(name);

                                                                    Intent intent = new Intent(CreateAccountActivity.this, PostLoginActivityAddAchievement.class);
                                                                    intent.putExtra("username", name);
                                                                    intent.putExtra("userId", currentFirebaseUser);
                                                                    startActivity(intent);


                                                                }else {
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                }
                                                            }
                                                        });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

                            } //Something went wrong


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d("Firebase", "onFailure: " + e.toString());
                    if( password.length() <= 5) {

                        progressBar.setVisibility(View.INVISIBLE);

                        Toast.makeText(CreateAccountActivity.this, "Password must have 6 or more characters", Toast.LENGTH_LONG).show();
                    }
                    if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                        Toast.makeText(CreateAccountActivity.this, "Email Is valid", Toast.LENGTH_SHORT).show();

                    }else {

                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(CreateAccountActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
        if(firebaseUser != null) {
            firebaseUser.reload();
        }
    }
}