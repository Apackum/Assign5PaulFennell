package com.example.sdafinalass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import model.GameListCurrentModel;
import model.GamesStoreModel;
import ui.GameRecyclerAdapter;
import util.LibraryViewAdapter;


public class CurrentGamesList extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<GamesStoreModel> gamesStoreList;
    private RecyclerView recyclerView;
    private GameRecyclerAdapter gameRecyclerAdapter;

    private CollectionReference collectionReference = database.collection("Achievements");

    public DatabaseReference myRef;

    public ArrayList<GameListCurrentModel> gameListCurrentModels;
    public LibraryViewAdapter libraryViewAdapter;


    public CurrentGamesList() {
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
     *This creates the view and inflates the fragment and displays all things
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_current_games_list, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        recyclerView = root.findViewById(R.id.bookView_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //Firebase reference
        myRef = FirebaseDatabase.getInstance().getReference();
        //Arraylist
        gameListCurrentModels = new ArrayList<>();

        //CLear arrayList
        ClearAll();

        //Getting the data message method
        getDataFromFirebase();
        return root;
    }

    //Methods

    //This method is used to add and request from the firebase database
    private void getDataFromFirebase() {
        //Creating a query for firebase realtime database
        Query query = myRef.child("games");

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Calling clearall method
                ClearAll();
                //for loop to add to the booklist from database
                for (DataSnapshot snapshots : dataSnapshot.getChildren()) {

                     GameListCurrentModel currentGames13 = new GameListCurrentModel();
                     currentGames13.setGimage(snapshots.child("gimage").getValue().toString());
                     currentGames13.setGtitle(snapshots.child("gtitle").getValue().toString());
                     currentGames13.setGdes(snapshots.child("gdes").getValue().toString());
                     currentGames13.setGdev(snapshots.child("gdev").getValue().toString());
                     gameListCurrentModels.add(currentGames13);
                }
                //Setting up recycler
                libraryViewAdapter = new LibraryViewAdapter(getContext(), gameListCurrentModels);
                recyclerView.setAdapter(libraryViewAdapter);
                libraryViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("HELP", "Failed to read value.", error.toException());

            }
        });
    }

    //This is my clearALL method
    @SuppressLint("NotifyDataSetChanged")
    private void ClearAll() {
        if (gameListCurrentModels != null) {
            gameListCurrentModels.clear();
            if(libraryViewAdapter != null){
                libraryViewAdapter.notifyDataSetChanged();
            }
        } else {
            gameListCurrentModels = new ArrayList<>();
        }
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
                firebaseAuth.signOut();
                Toast.makeText(getContext(), "Successfully logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

                break;

        }

        return super.onOptionsItemSelected(item);
    }


}