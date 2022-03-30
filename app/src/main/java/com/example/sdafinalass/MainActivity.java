package com.example.sdafinalass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import ui.ViewPageAdapter;
import util.GamesApi;

/**
 * Main activity
 */
public class MainActivity extends AppCompatActivity   {
    private static final String TAG = "MainActivity";

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Users");

    public static final int BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1;

    ViewPager viewPager;

    /**
     * This is the onCreate method for my main activity.class
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        // Obtain the FirebaseAnalytics instance.
        viewPager = findViewById(R.id.pager);
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, getApplicationContext());
        viewPager.setAdapter(adapter);


        authStateListener = new FirebaseAuth.AuthStateListener() {
            /**
             * This listens for the firebase authentication
             * @param firebaseAuth
             */
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    String currentUserId = firebaseUser.getUid();

                    collectionReference
                            .whereEqualTo("userId", currentUserId)

                            .addSnapshotListener(new EventListener<QuerySnapshot>() {

                                /**
                                 * This add snapshot to the variables
                                 * @param value
                                 * @param error
                                 */
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (error != null) {
                                        return;
                                    }
                                    String name;
                                    assert value != null;
                                    if (!value.isEmpty()) {
                                        for (QueryDocumentSnapshot snapshot : value) {
                                            GamesApi gamesApi = GamesApi.getInstance();
                                            gamesApi.setUserId(snapshot.getString("userId"));
                                            gamesApi.setUsername(snapshot.getString("username"));




                                        }
                                    }
                                }
                            });
                }   else {

                }

            }
        };
    }



    @Override

    protected void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}