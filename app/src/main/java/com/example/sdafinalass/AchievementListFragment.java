package com.example.sdafinalass;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import model.GamesStoreModel;
import ui.GameRecyclerAdapter;
import util.GamesApi;


public class AchievementListFragment extends Fragment {

    private TextView getNoAchievementEntry;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<GamesStoreModel> gamesStoreList;
    private RecyclerView recyclerView;
    private GameRecyclerAdapter gameRecyclerAdapter;

    private CollectionReference collectionReference = database.collection("Achievements");
    private TextView noAchievementEntry;

    public AchievementListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_achievement_list, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        noAchievementEntry = view.findViewById(R.id.nothing_here);
        gamesStoreList = new ArrayList<>();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add:
                //Take users to add journal
                if(firebaseUser != null && firebaseAuth != null) {
                    Intent intent = new Intent(getActivity(), PostLoginActivityAddAchievement.class);
                    startActivity(intent);
                }else {

                }

                break;

            case R.id.sign_out:
                //This will sign the user aut
                firebaseAuth.signOut();
                Toast.makeText(getContext(), "Successfully logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

        break;

    }

        return super.onOptionsItemSelected(item);
}


    //This is used to retrieve all of the journal from firestore
    @Override
    public void onStart() {
        super.onStart();
        collectionReference.whereEqualTo("userId", GamesApi.getInstance()
                .getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot achievements : queryDocumentSnapshots) {
                                GamesStoreModel gamesStore = achievements.toObject(GamesStoreModel.class);
                                gamesStoreList.add(gamesStore);
                            }
                            //This invokes the recyclerView
                            gameRecyclerAdapter = new GameRecyclerAdapter(getContext(), gamesStoreList);
                            recyclerView.setAdapter(gameRecyclerAdapter);
                            gameRecyclerAdapter.notifyDataSetChanged();

                        }else {

                            noAchievementEntry.setVisibility(View.VISIBLE);

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

}