package com.example.sdafinalass;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import util.GamesApi;

public class WelcomeFragment extends Fragment {

    private Button loginButton;
    Button button;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Users");



    public WelcomeFragment() {
        // Required empty public constructor
    }

    /**
     * This add the sign out and add buttons
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /**
     * This is the create view for the welcome fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * returns view
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);



        //Finding connection in xml
        loginButton = view.findViewById(R.id.Login);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        authStateListener = new FirebaseAuth.AuthStateListener() {

            /**
             * listener for firebase changes and such
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
                    loginButton.setVisibility(View.GONE);
                }

            }
        };
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginButton.setVisibility(View.INVISIBLE);
                //This is for my login activity
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);



            }
        });

        return view;


    }

    /**
     * This add the sign out and add buttons
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    /**
     * This add the sign out and add buttons
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add:
                //Take users to add journal
                if(firebaseUser != null && firebaseAuth != null) {
                    Intent intent = new Intent(getContext(), PostLoginActivityAddAchievement.class);
                    startActivity(intent);
                }

                break;

            case R.id.sign_out:
                //This will sign the user aut
                button = loginButton;
                button.setVisibility(View.VISIBLE);
                firebaseAuth.signOut();
                Toast.makeText(getContext(), "Successfully logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), LoginActivity.class);




                break;

        }

        return super.onOptionsItemSelected(item);
    }

}