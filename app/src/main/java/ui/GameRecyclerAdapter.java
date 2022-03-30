package ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sdafinalass.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.GamesStoreModel;

public class GameRecyclerAdapter extends RecyclerView.Adapter<GameRecyclerAdapter.ViewHolder>{
    private Context context;
    private List<GamesStoreModel> gamesStore;

    public GameRecyclerAdapter(Context context, List<GamesStoreModel> gamesStores) {
        this.context = context;
        this.gamesStore = gamesStores;
    }

    /**
     * creates viewholder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public GameRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.games_rom, parent, false);
        return new ViewHolder(view, context);
    }

    /**
     * adds the lists and creates a holder for them
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull GameRecyclerAdapter.ViewHolder holder, int position) {

        GamesStoreModel gamesStore = this.gamesStore.get(position);
        String imageUrl;
        holder.aTitle.setText(gamesStore.getGameTitle());
        holder.aDescription.setText(gamesStore.getGameDescription());
        holder.aUser.setText(gamesStore.getUserName());
        imageUrl = gamesStore.getImageUrl();
        //This adds the time since uploaded to the adateadded variable
        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(gamesStore
                .getTimeAdded()
                .getSeconds() * 1000);
        holder.aDateAdded.setText(timeAgo);


        //I will use the picasso to download and show image
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.phfpro)
                .fit()
                .into(holder.aImage);
    }

    /**
     * set item size
     * @return
     */
    @Override
    public int getItemCount() {
        return gamesStore.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView aTitle, aDescription, aDateAdded, aUser;
        public ImageView aImage;
        public ImageButton shareButton;
        String userId, username;

        /**
         * finds the items within the xml file
         * @param itemView
         * @param ctx
         */
        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            aTitle = itemView.findViewById(R.id.games_title_list);
            aDescription = itemView.findViewById(R.id.games_Description_list);
            aImage = itemView.findViewById(R.id.games_image_list);
            aDateAdded = itemView.findViewById(R.id.games_time_list);
            aUser = itemView.findViewById(R.id.games_row_name);

            shareButton =itemView.findViewById(R.id.games_row_share_button);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //context.startActivity();
                }
            });
        }
    }
}