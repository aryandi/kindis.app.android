package co.digdaya.kindis.live.view.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.util.BackgroundProses.InsertItemPlaylist;
import co.digdaya.kindis.live.view.holder.ItemPlaylist;

/**
 * Created by vincenttp on 2/24/2017.
 */

public class AdapterInsertItemPlaylist extends RecyclerView.Adapter<ItemPlaylist> {
    private Activity activity;
    private ArrayList<HashMap<String, String>> listPlaylist = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> dataPlaylis;
    private String uidSingle;
    private Dialog dialog;

    public AdapterInsertItemPlaylist (Activity activity, ArrayList<HashMap<String,
            String>> listPlaylist, String uidSingle, Dialog dialog){
        this.activity = activity;
        this.listPlaylist = listPlaylist;
        this.uidSingle = uidSingle;
        this.dialog = dialog;
    }

    @Override
    public ItemPlaylist onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_playlist, parent, false);
        return new ItemPlaylist(view);
    }

    @Override
    public void onBindViewHolder(ItemPlaylist holder, int position) {
        TextView title = holder.title;
        RelativeLayout click = holder.click;
        dataPlaylis = listPlaylist.get(position);
        title.setText(dataPlaylis.get("title"));

        final String uid = dataPlaylis.get("playlist_id");
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InsertItemPlaylist(activity).insertItem(uidSingle, uid);
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPlaylist.size();
    }
}
