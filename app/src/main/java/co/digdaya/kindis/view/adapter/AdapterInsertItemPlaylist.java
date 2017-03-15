package co.digdaya.kindis.view.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.util.BackgroundProses.InsertItemPlaylist;
import co.digdaya.kindis.view.holder.ItemPlaylist;

/**
 * Created by vincenttp on 2/24/2017.
 */

public class AdapterInsertItemPlaylist extends RecyclerView.Adapter<ItemPlaylist> {
    Context context;
    ArrayList<HashMap<String, String>> listPlaylist = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> dataPlaylis;
    String uidSingle;
    Dialog dialog;

    public AdapterInsertItemPlaylist (Context context, ArrayList<HashMap<String, String>> listPlaylist, String uidSingle, Dialog dialog){
        this.context = context;
        this.listPlaylist = listPlaylist;
        this.uidSingle = uidSingle;
        this.dialog = dialog;
    }

    @Override
    public ItemPlaylist onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_playlist, parent, false);
        ItemPlaylist item= new ItemPlaylist(view);
        return item;
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
                new InsertItemPlaylist(context).insertItem(uidSingle, uid);
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPlaylist.size();
    }
}
