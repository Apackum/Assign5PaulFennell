package util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.sdafinalass.R;

import java.util.ArrayList;

import model.GameListCurrentModel;

public class LibraryViewAdapter extends RecyclerView.Adapter<LibraryViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView gameImage;
        TextView gameTitle;
        TextView gameDeveloper;
        TextView gameDescription;
        ImageButton currentFavouriteButton;

        /**
         * find the fields and attaches them
         * @param itemView
         */
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            //grab the image, the text and the layout id's
            gameImage = itemView.findViewById(R.id.games_image_list);
            gameTitle = itemView.findViewById(R.id.games_title_list);
            gameDescription = itemView.findViewById(R.id.games_Description_list);
            gameDeveloper = itemView.findViewById(R.id.games_time_list);
            currentFavouriteButton = itemView.findViewById(R.id.current_row_favourite_button);

            currentFavouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //context.startActivity();
                }
            });


        }
    }

    private final Context mNewContext;
    private final ArrayList<GameListCurrentModel> currentGames;

    /**
     * creates arraylist and attaches to adapter
     * @param mNewContext
     * @param currentGames
     */
    public LibraryViewAdapter(Context mNewContext, ArrayList<GameListCurrentModel> currentGames) {
        this.mNewContext = mNewContext;
        this.currentGames = currentGames;

    }

    /**
     * attaches the layout file
     * @param parent
     * @param viewType
     * @return
     */
    //declare methods
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * sets the variable to the holder and images
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: was called");
        //TextViews
        viewHolder.gameTitle.setText(currentGames.get(position).getGtitle());
        viewHolder.gameDeveloper.setText(currentGames.get(position).getGdev());
        viewHolder.gameDescription.setText(currentGames.get(position).getGdes());
        //Using glide to store images
        Glide.with(mNewContext)
                .load(currentGames.get(position).getGimage())
                .into(viewHolder.gameImage);

    }

    /**
     * get the size of the items
     * @return
     */
    //Set item count to the number of book list items
    @Override
    public int getItemCount() {
        return currentGames.size();
    }
}
