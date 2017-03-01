package sangmaneproject.kindis.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.PlayerService;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.PlayerActionHelper;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.view.holder.ItemSong;

public class AdapterSong extends RecyclerView.Adapter<ItemSong> {
    Activity context;
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    ArrayList<String> songPlaylist;
    HashMap<String, String> dataSong;
    private OnClickMenuListener onClickMenuListener;
    String type;

    public AdapterSong(Activity context, ArrayList<HashMap<String, String>> listSong, String type, ArrayList<String> songPlaylist){
        this.context = context;
        this.listSong = listSong;
        this.type = type;
        this.songPlaylist = songPlaylist;
    }

    @Override
    public ItemSong onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_song, parent, false);
        ItemSong item= new ItemSong(view);
        return item;
    }

    @Override
    public void onBindViewHolder(ItemSong holder, final int position) {
        TextView title = holder.title;
        TextView subTitle = holder.subTitle;
        RelativeLayout click = holder.click;
        final ImageButton btnMenu = holder.btnMenu;
        dataSong = listSong.get(position);

        final String uid = dataSong.get("uid");
        final String titles = dataSong.get("title");
        final String subTitles = dataSong.get("year");

        title.setText(titles);
        subTitle.setText(subTitles);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Loading . . . ", Toast.LENGTH_LONG).show();
                new PlayerSessionHelper().setPreferences(context, "uid", uid);
                if (type.equals("list")){
                    Intent intent = new Intent(context, PlayerService.class);
                    intent.setAction(PlayerActionHelper.PLAY_PLAYLIST);
                    intent.putExtra("single_id", uid);
                    intent.putExtra("position", position);
                    intent.putExtra("list_uid", songPlaylist);
                    context.startService(intent);
                }else {
                    new PlayerSessionHelper().setPreferences(context, "index", "1");
                    Intent intent = new Intent(context, PlayerService.class);
                    intent.setAction(PlayerActionHelper.UPDATE_RESOURCE);
                    intent.putExtra("single_id", uid);
                    context.startService(intent);
                }
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMenuListener.onClick(uid, btnMenu);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSong.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnClickMenuListener(OnClickMenuListener onClickMenuListener){
        this.onClickMenuListener = onClickMenuListener;
    }

    public interface OnClickMenuListener{
        void onClick(String uid, ImageButton imageButton);
    }
}
