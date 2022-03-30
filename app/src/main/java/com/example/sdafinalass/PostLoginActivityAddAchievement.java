package com.example.sdafinalass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.Objects;

import model.GamesStoreModel;
import util.GamesApi;

public class PostLoginActivityAddAchievement extends AppCompatActivity implements View.OnClickListener{

    private static final int GALLERY_CODE = 1;
    private static final String TAG = "PostLoginActivity";
    private Button saveButton;
    private ProgressBar progressBar;
    private ImageView addPhotoButton;
    private EditText gameTitle;
    private EditText gameDescription;
    private TextView currentUserText;
    private ImageView imageView;

    private String currentUserId;
    private String currentUserName;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    //Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private CollectionReference collectionReference = db.collection("Achievements");
    private Uri imageUri;

    /**
     *  on create for the post login activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login_add_achievement);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://sdafinalprojectpaulfennell.appspot.com");

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        firebaseAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.post_progressBar);
        gameTitle = findViewById(R.id.post_title_et);
        gameDescription = findViewById(R.id.post_description_et);
        currentUserText = findViewById(R.id.post_username_textview);

        imageView = findViewById(R.id.post_imageView);
        saveButton = findViewById(R.id.post_save_button);
        saveButton.setOnClickListener(this);
        addPhotoButton = findViewById(R.id.postCameraButton);
        addPhotoButton.setOnClickListener(this);

        progressBar.setVisibility(View.INVISIBLE);

        if (GamesApi.getInstance() != null) {
            currentUserId = GamesApi.getInstance().getUserId();
            currentUserName = GamesApi.getInstance().getUsername();

            currentUserText.setText(currentUserName);
        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {

                } else {

                }

            }
        };


    }

    /**
     * This is the view for the two buttons that save and add a picture from the users gallery
     * @param v
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_save_button:
                //saveJournal
                saveJournal();
                break;
            case R.id.postCameraButton:
                //get image from gallery/phone
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_CODE);
                break;
        }

    }

    private void saveJournal() {
        final String gameTitles = gameTitle.getText().toString().trim();
        final String descGame = gameDescription.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(gameTitles) &&
                !TextUtils.isEmpty(descGame)
                && imageUri != null) {

            final StorageReference filepath = storageReference
                    //This Creates a photo bucket within the app cloud storage with the given names and add a timestamp
                    .child("achievement_images")
                    .child("my_achievement_" + Timestamp.now().getSeconds());

            filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String imageUrl = uri.toString();
                                    //this adds the entries to the firebase collection
                                    GamesStoreModel gamesStore = new GamesStoreModel();
                                    gamesStore.setGameTitle(gameTitles);
                                    gamesStore.setGameDescription(descGame);
                                    gamesStore.setImageUrl(imageUrl);
                                    gamesStore.setTimeAdded(new Timestamp(new Date()));
                                    gamesStore.setUserName(currentUserName);
                                    gamesStore.setUserId(currentUserId);

                                    //Invoking the collectionReference
                                    collectionReference.add(gamesStore)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {

                                                    progressBar.setVisibility(View.INVISIBLE);
                                                    startActivity(new Intent(PostLoginActivityAddAchievement.this,
                                                            MainActivity.class));
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "onFailure: " + e.getMessage());

                                                }
                                            });
                                }
                            });


                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    });


        } else {

            progressBar.setVisibility(View.INVISIBLE);

        }
    }

    /**
     * set the image method
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData(); // we have the actual path to the image
                imageView.setImageURI(imageUri);//show image

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
