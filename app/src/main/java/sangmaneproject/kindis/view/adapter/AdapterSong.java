package sangmaneproject.kindis.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.PlayerService;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.controller.SongPlay;
import sangmaneproject.kindis.helper.PlayerActionHelper;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.view.holder.ItemSong;

public class AdapterSong extends RecyclerView.Adapter<ItemSong> {
    Context context;
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> dataSong;

    public AdapterSong (Context context, ArrayList<HashMap<String, String>> listSong){
        this.context = context;
        this.listSong = listSong;
    }

    @Override
    public ItemSong onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_song, parent, false);
        ItemSong item= new ItemSong(view);
        return item;
    }

    @Override
    public void onBindViewHolder(ItemSong holder, int position) {
        TextView title = holder.title;
        TextView subTitle = holder.subTitle;
        RelativeLayout click = holder.click;
        dataSong = listSong.get(position);

        final String uid = dataSong.get("uid");
        final String titles = dataSong.get("title");
        final String subTitles = dataSong.get("year");

        title.setText(titles);
        subTitle.setText(subTitles);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*new PlayerSessionHelper().setPreferences(context, "title", titles);
                new PlayerSessionHelper().setPreferences(context, "year", subTitles);
                Intent intent = new Intent(context, PlayerService.class);
                intent.setAction(PlayerActionHelper.UPDATE_RESOURCE);
                context.startService(intent);*/

                //new SongPlay(context).execute(uid);

                Intent intent = new Intent(context, PlayerService.class);
                intent.setAction(PlayerActionHelper.UPDATE_RESOURCE);
                intent.putExtra("single_id", uid);
                context.startService(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSong.size();
    }
}
