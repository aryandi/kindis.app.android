package sangmaneproject.kindis.view.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.view.holder.ItemPlaylist;
import sangmaneproject.kindis.view.holder.ItemSong;

public class AdapterPlaylist extends RecyclerView.Adapter<ItemPlaylist> {
    Context context;
    ArrayList<HashMap<String, String>> listPlaylist = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> dataPlaylis;

    public AdapterPlaylist (Context context, ArrayList<HashMap<String, String>> listPlaylist){
        this.context = context;
        this.listPlaylist = listPlaylist;
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
        dataPlaylis = listPlaylist.get(position);
        title.setText(dataPlaylis.get("title"));
    }

    @Override
    public int getItemCount() {
        return listPlaylist.size();
    }
}
