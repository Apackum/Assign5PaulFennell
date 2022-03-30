package com.example.sdafinalass;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class RssFeedFragment extends Fragment {

    ListView lsRss;
    ArrayList<String> titles;
    ArrayList<String> links;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = database.collection("Users");

    public RssFeedFragment() {
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
     * this infaltes, contains saves the instance state of the create view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * returns view
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rss_feed, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        lsRss = view.findViewById(R.id.lvRSS);
        titles = new ArrayList<String>();
        links = new ArrayList<String>();

        lsRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(links.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        new ProcessInBackground().execute();

        return view;
    }

    /**
     * uses the input stream api to gather data for the rss feed
     * @param url
     * @return
     */
    public InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class ProcessInBackground extends AsyncTask<Integer, Void, Exception> {

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Currently Loading Information.....Please Wait...");
            progressDialog.show();
        }

        /**
         * finds the feed and sets it the variables within the fragment
         * @param integers
         * @return
         */
        @Override
        protected Exception doInBackground(Integer... integers) {
            try {

                URL url = new URL("https://www.gamespot.com/feeds/mashup");
                URL url1 = new URL("https://blog.playstation.com/feed/");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParserFactory factory1 = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                XmlPullParser xpp1 = factory1.newPullParser();

                xpp.setInput(getInputStream(url), "UTF_8");
                xpp1.setInput(getInputStream(url1), "UTF_8");

                boolean insideItem = false;

                int eventType = xpp.getEventType();
                int eventType1 = xpp1.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                titles.add(xpp.nextText());
                            }
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            if (insideItem) {
                                links.add(xpp.nextText());
                            }
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                    }

                    eventType = xpp.next();
                }
                while (eventType1 != XmlPullParser.END_DOCUMENT) {
                    if (eventType1 == XmlPullParser.START_TAG) {
                        if (xpp1.getName().equalsIgnoreCase("item")) {
                            insideItem = true;
                        } else if (xpp1.getName().equalsIgnoreCase("title")) {
                            if (insideItem) {
                                titles.add(xpp1.nextText());
                            }
                        } else if (xpp1.getName().equalsIgnoreCase("link")) {
                            if (insideItem) {
                                links.add(xpp1.nextText());
                            }
                        }
                    } else if (eventType1 == XmlPullParser.END_TAG && xpp1.getName().equalsIgnoreCase("item")) {
                        insideItem = false;
                    }

                    eventType1 = xpp1.next();
                }
            } catch (XmlPullParserException | IOException e) {
                exception = e;
            }
            return exception;
        }

        /**
         * set the layout for the list view in the cml file
         * @param s
         */
        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, titles);

            lsRss.setAdapter(adapter);

            progressDialog.dismiss();
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