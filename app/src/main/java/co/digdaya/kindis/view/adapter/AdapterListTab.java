package co.digdaya.kindis.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import co.digdaya.kindis.R;
import co.digdaya.kindis.model.DataAlbum;
import co.digdaya.kindis.model.DataPlaylist;
import co.digdaya.kindis.model.DataSingle;
import co.digdaya.kindis.model.PlaylistModel;
import co.digdaya.kindis.model.TabModel;
import co.digdaya.kindis.view.adapter.item.AdapterAlbum;
import co.digdaya.kindis.view.adapter.item.AdapterAlbumNew;
import co.digdaya.kindis.view.adapter.item.AdapterPlaylistHorizontal;
import co.digdaya.kindis.view.adapter.item.AdapterSong;
import co.digdaya.kindis.view.adapter.item.AdapterSongHorizontal;
import co.digdaya.kindis.view.fragment.bottomnavigation.playlist.Playlist;
import co.digdaya.kindis.view.holder.Item;
import co.digdaya.kindis.view.holder.ItemListTab;

/**
 * Created by DELL on 3/31/2017.
 */

public class AdapterListTab extends RecyclerView.Adapter<ItemListTab> {
    Activity context;
    TabModel tabModel;
    AdapterPlaylistHorizontal adapterPlaylistHorizontal;
    AdapterAlbumNew adapterAlbum;
    AdapterSongHorizontal adapterSong;
    Gson gson;

    public AdapterListTab(Activity context, TabModel tabModel) {
        this.context = context;
        this.tabModel = tabModel;
        gson = new Gson();
    }

    @Override
    public ItemListTab onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        ItemListTab item= new ItemListTab(view);
        return item;
    }

    @Override
    public void onBindViewHolder(ItemListTab holder, int position) {
        TextView title = holder.title;
        TextView btnMore = holder.btnMore;
        RecyclerView recyclerView = holder.list;

        title.setText(tabModel.tab1.get(position).name);

        if (tabModel.tab1.get(position).type_id.equals("1")){
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }else if (tabModel.tab1.get(position).type_id.equals("2")){
            recyclerView.setLayoutManager(new GridLayoutManager(context,2));
        }

        switch (getItemViewType(position)){
            case 2:
                parseAlbum(tabModel.tab1.get(position).data, recyclerView);
                break;
            case 3:
                parseSingle(tabModel.tab1.get(position).data, recyclerView);
            case 5:
                parsePlaylist(tabModel.tab1.get(position).data, recyclerView);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return tabModel.tab1.size();
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.parseInt(tabModel.tab1.get(position).type_content_id);
    }

    private void parsePlaylist(String s, RecyclerView recyclerView){
        String json = "{ \"data\":"+s+"}";

        DataPlaylist playlistModel = gson.fromJson(json, DataPlaylist.class);

        adapterPlaylistHorizontal = new AdapterPlaylistHorizontal(context, playlistModel);
        recyclerView.setAdapter(adapterPlaylistHorizontal);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void parseAlbum (String s, RecyclerView recyclerView){
        String json = "{ \"data\":"+s+"}";

        DataAlbum dataAlbum = gson.fromJson(json, DataAlbum.class);
        System.out.println("AdapterAlbumNew : "+dataAlbum.data.get(0).title);
        adapterAlbum = new AdapterAlbumNew(context, dataAlbum);
        recyclerView.setAdapter(adapterPlaylistHorizontal);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void parseSingle(String s, RecyclerView recyclerView){
        String json = "{ \"data\":"+s+"}";

        DataSingle dataSingle = gson.fromJson(json, DataSingle.class);

        adapterSong = new AdapterSongHorizontal(context, dataSingle);
        recyclerView.setAdapter(adapterSong);
        recyclerView.setNestedScrollingEnabled(false);
    }
}
