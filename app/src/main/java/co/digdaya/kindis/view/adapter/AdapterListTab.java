package co.digdaya.kindis.view.adapter;

import android.app.Activity;
import android.content.Intent;
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
import co.digdaya.kindis.model.DataArtist;
import co.digdaya.kindis.model.DataPlaylist;
import co.digdaya.kindis.model.DataSingle;
import co.digdaya.kindis.model.TabModel;
import co.digdaya.kindis.util.MarginItemHorizontal;
import co.digdaya.kindis.util.SpacingItemHome;
import co.digdaya.kindis.view.activity.Detail.More;
import co.digdaya.kindis.view.adapter.item.AdapterAlbumNew;
import co.digdaya.kindis.view.adapter.item.AdapterArtistNew;
import co.digdaya.kindis.view.adapter.item.AdapterPlaylistHorizontal;
import co.digdaya.kindis.view.adapter.item.AdapterSongHorizontal;
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
    AdapterArtistNew adapterArtistNew;
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
    public void onBindViewHolder(ItemListTab holder, final int position) {
        TextView title = holder.title;
        TextView btnMore = holder.btnMore;
        final RecyclerView recyclerView = holder.list;

        if (tabModel.tab1.get(position).data.length() < 10){
            title.setVisibility(View.GONE);
            btnMore.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }

        title.setText(tabModel.tab1.get(position).name);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, More.class);
                intent.putExtra("title", tabModel.tab1.get(position).name);
                intent.putExtra("type", getItemViewType(position));
                context.startActivity(intent);
            }
        });

        if (tabModel.tab1.get(position).type_id.equals("1")){
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.addItemDecoration(new MarginItemHorizontal(context));
        }else if (tabModel.tab1.get(position).type_id.equals("2")){
            recyclerView.setLayoutManager(new GridLayoutManager(context,2));
            recyclerView.addItemDecoration(new SpacingItemHome(context.getApplicationContext()));
        }

        switch (getItemViewType(position)){
            case 1:
                parseArtist(tabModel.tab1.get(position).data, recyclerView);
                break;
            case 2:
                parseAlbum(tabModel.tab1.get(position).data, recyclerView);
                break;
            case 3:
                parseSingle(tabModel.tab1.get(position).data, recyclerView);
                break;
            case 5:
                parsePlaylist(tabModel.tab1.get(position).data, recyclerView, Integer.parseInt(tabModel.tab1.get(position).type_id));
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

    private void parsePlaylist(String s, RecyclerView recyclerView, int type){
        String json = "{ \"data\":"+s+"}";

        DataPlaylist playlistModel = gson.fromJson(json, DataPlaylist.class);

        adapterPlaylistHorizontal = new AdapterPlaylistHorizontal(context, playlistModel, type);
        recyclerView.setAdapter(adapterPlaylistHorizontal);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void parseAlbum (String s, RecyclerView recyclerView){
        String json = "{ \"data\":"+s+"}";

        DataAlbum dataAlbum = gson.fromJson(json, DataAlbum.class);
        System.out.println("AdapterAlbumNew : "+dataAlbum.data.get(0).title);
        adapterAlbum = new AdapterAlbumNew(context, dataAlbum);
        recyclerView.setAdapter(adapterAlbum);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void parseSingle(String s, RecyclerView recyclerView){
        String json = "{ \"data\":"+s+"}";

        DataSingle dataSingle = gson.fromJson(json, DataSingle.class);

        adapterSong = new AdapterSongHorizontal(context, dataSingle);
        recyclerView.setAdapter(adapterSong);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void parseArtist (String s, RecyclerView recyclerView){
        String json = "{ \"data\":"+s+"}";

        DataArtist dataArtist = gson.fromJson(json, DataArtist.class);

        adapterArtistNew = new AdapterArtistNew(context, dataArtist);
        recyclerView.setAdapter(adapterArtistNew);
        recyclerView.setNestedScrollingEnabled(false);
    }
}
