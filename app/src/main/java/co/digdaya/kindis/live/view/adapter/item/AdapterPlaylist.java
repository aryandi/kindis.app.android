package co.digdaya.kindis.live.view.adapter.item;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.Constanta;
import co.digdaya.kindis.live.view.activity.Detail.Detail;
import co.digdaya.kindis.live.view.holder.ItemPlaylist;

public class AdapterPlaylist extends RecyclerView.Adapter<ItemPlaylist> {
    Context context;
    ArrayList<HashMap<String, String>> listPlaylist = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> dataPlaylis;
    String isMyPlaylist;
    private OnClickMenuListener onClickMenuListener;

    public AdapterPlaylist (Context context, ArrayList<HashMap<String, String>> listPlaylist, String isMyPlaylist){
        this.context = context;
        this.listPlaylist = listPlaylist;
        this.isMyPlaylist = isMyPlaylist;
    }

    @Override
    public ItemPlaylist onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_playlist, parent, false);
        ItemPlaylist item= new ItemPlaylist(view);
        return item;
    }

    @Override
    public void onBindViewHolder(ItemPlaylist holder, int position) {
        TextView txtTitle = holder.title;
        RelativeLayout click = holder.click;
        final ImageButton menu = holder.menu;
        dataPlaylis = listPlaylist.get(position);

        final String uid = dataPlaylis.get("playlist_id");
        final String title = dataPlaylis.get("title");
        txtTitle.setText(title);

        if (isMyPlaylist.equals("true")){
            menu.setVisibility(View.VISIBLE);
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickMenuListener.onClick(uid, title, menu);
                }
            });
        }

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Detail.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", uid);
                intent.putExtra(Constanta.INTENT_EXTRA_TYPE, "playlist");
                intent.putExtra("isMyPlaylist", isMyPlaylist);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPlaylist.size();
    }

    public void setOnClickMenuListener(OnClickMenuListener onClickMenuListener){
        this.onClickMenuListener = onClickMenuListener;
    }

    public interface OnClickMenuListener{
        void onClick(String uid, String title, ImageButton button);
    }
}
