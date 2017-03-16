package co.digdaya.kindis.view.adapter.item;

import android.app.Activity;
import android.app.Dialog;
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

import co.digdaya.kindis.PlayerService;
import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.PlayerActionHelper;
import co.digdaya.kindis.helper.PlayerSessionHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.view.dialog.GetPremium;
import co.digdaya.kindis.view.holder.ItemSong;

public class AdapterSong extends RecyclerView.Adapter<ItemSong> {
    Activity context;
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    ArrayList<String> songPlaylist;
    HashMap<String, String> dataSong;
    private OnClickMenuListener onClickMenuListener;
    String type;

    Dialog dialogPremium;
    GetPremium getPremium;

    public AdapterSong(Activity context, ArrayList<HashMap<String, String>> listSong, String type, ArrayList<String> songPlaylist){
        this.context = context;
        this.listSong = listSong;
        this.type = type;
        this.songPlaylist = songPlaylist;
        getPremium = new GetPremium(context, dialogPremium);
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
        final String subTitles = dataSong.get("subtitle");

        title.setText(titles);
        subTitle.setText(subTitles);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PlayerSessionHelper().setPreferences(context, "uid", uid);
                if (type.equals("list")){
                    Toast.makeText(context, "Loading . . . ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, PlayerService.class);
                    intent.setAction(PlayerActionHelper.PLAY_PLAYLIST);
                    intent.putExtra("single_id", uid);
                    intent.putExtra("position", position);
                    intent.putExtra("list_uid", songPlaylist);
                    context.startService(intent);
                }else {
                    if (new SessionHelper().getPreferences(context, "is_premium").equals(dataSong.get("is_premium")) || new SessionHelper().getPreferences(context, "is_premium").equals("1")) {
                        Toast.makeText(context, "Loading . . . ", Toast.LENGTH_LONG).show();
                        new PlayerSessionHelper().setPreferences(context, "index", "1");
                        Intent intent = new Intent(context, PlayerService.class);
                        intent.setAction(PlayerActionHelper.UPDATE_RESOURCE);
                        intent.putExtra("single_id", uid);
                        context.startService(intent);
                    }else {
                        getPremium.showDialog();
                    }
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
