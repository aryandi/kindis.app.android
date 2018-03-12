package co.digdaya.kindis.live.view.adapter.tab;

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

import java.util.List;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.custom.ImageViewRounded;
import co.digdaya.kindis.live.helper.ViewHelper;
import co.digdaya.kindis.live.model.DataAlbum;
import co.digdaya.kindis.live.model.DataArtist;
import co.digdaya.kindis.live.model.DataGenre;
import co.digdaya.kindis.live.model.DataPlaylist;
import co.digdaya.kindis.live.model.DataSingle;
import co.digdaya.kindis.live.model.TabModel;
import co.digdaya.kindis.live.util.SpacingItem.MarginItemHorizontal;
import co.digdaya.kindis.live.util.SpacingItem.SpacingItemGenre;
import co.digdaya.kindis.live.view.activity.Detail.More;
import co.digdaya.kindis.live.view.adapter.item.AdapterAlbumNew;
import co.digdaya.kindis.live.view.adapter.item.AdapterArtistNew;
import co.digdaya.kindis.live.view.adapter.item.AdapterGenreNew;
import co.digdaya.kindis.live.view.adapter.item.AdapterPlaylistHorizontal;
import co.digdaya.kindis.live.view.adapter.item.AdapterSongHorizontal;
import co.digdaya.kindis.live.view.holder.ItemListTab;

/**
 * Created by DELL on 3/31/2017.
 */

public class AdapterListTab extends RecyclerView.Adapter<ItemListTab> {
    private boolean isHavePlaylist;
    private Activity context;
    private TabModel tabModel;
    private List<TabModel.Tab> tabs;

    private AdapterPlaylistHorizontal adapterPlaylistHorizontal;
    private AdapterAlbumNew adapterAlbum;
    private AdapterSongHorizontal adapterSong;
    private AdapterArtistNew adapterArtistNew;
    private AdapterGenreNew adapterGenre;

    private Gson gson;
    private int menuType, tabType;

    private String paramMore = "";

    public AdapterListTab(Activity context, TabModel tabModel, int tab, int menuType) {
        this.context = context;
        this.tabModel = tabModel;
        this.tabType = tab;
        this.menuType = menuType;
        switch (tab) {
            case 1:
                tabs = tabModel.tab1;
                checkHavePlaylist();
                break;
            case 2:
                tabs = tabModel.tab2;
                checkHavePlaylist();
                break;
            case 3:
                tabs = tabModel.tab3;
                break;
        }
        gson = new Gson();
    }

    private void checkHavePlaylist() {
        for (TabModel.Tab tab1 : tabs) {
            if (tab1.name.equals("Playlist") && tab1.data.length() == 10) {
                isHavePlaylist = true;
            }
        }
    }

    @Override
    public ItemListTab onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ItemListTab(view);
    }

    @Override
    public void onBindViewHolder(ItemListTab holder, final int position) {
        TextView title = holder.title;
        TextView btnMore = holder.btnMore;
        final RecyclerView recyclerView = holder.list;
        ImageViewRounded imageAds = holder.imageAds;
        TextView textAds = holder.textAds;

        if (menuType == 1) {
            musiqAdsHandler(position, imageAds, textAds);
        } else if (menuType == 9) {
            taklimAdsHandler(position, imageAds, textAds);
        }

        if (tabs.get(position).data.length() < 10) {
            title.setVisibility(View.GONE);
            btnMore.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            imageAds.setVisibility(View.GONE);
            textAds.setVisibility(View.GONE);
        }

        title.setText(tabs.get(position).name);

        if (tabs.get(position).is_more.equals("1")) {
            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tabType == 2 && menuType == 1) {
                        paramMore = "&ob=3";
                    }
                    Intent intent = new Intent(context, More.class);
                    intent.putExtra("title", tabs.get(position).name);
                    intent.putExtra("type", getItemViewType(position));
                    intent.putExtra("menuType", menuType);
                    intent.putExtra("param", paramMore);
                    intent.putExtra("urlMore", tabs.get(position).is_more_endpoint);
                    context.startActivity(intent);
                }
            });
        } else {
            btnMore.setVisibility(View.GONE);
        }

        if (tabs.get(position).type_id.equals("1")) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.addItemDecoration(new MarginItemHorizontal(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
            recyclerView.addItemDecoration(new SpacingItemGenre(context, ""));
        }/*else if (tabs.get(position).type_id.equals("2")){
            recyclerView.setLayoutManager(new GridLayoutManager(context,3));
            recyclerView.addItemDecoration(new SpacingItemMore(context));
        }else if (tabs.get(position).type_id.equals("3")){
            recyclerView.setLayoutManager(new GridLayoutManager(context,3));
            recyclerView.addItemDecoration(new SpacingItemGenre(context));
        }*/

        switch (getItemViewType(position)) {
            case 1:
                parseArtist(tabs.get(position).data, recyclerView, tabs.get(position).name);
                break;
            case 2:
                parseAlbum(tabs.get(position).data, recyclerView);
                break;
            case 3:
                parseSingle(tabs.get(position).data, recyclerView);
                break;
            case 4:
                parseGenre(tabs.get(position).data, recyclerView);
                break;
            case 5:
                parsePlaylist(tabs.get(position).data, recyclerView, Integer.parseInt(tabs.get(position).type_id));
                break;
        }
    }

    private void musiqAdsHandler(int position, ImageViewRounded imageAds, TextView textAds) {
        switch (tabType) {
            // discover
            case 1:
                if (isHavePlaylist) {
                    adsHandler(position, imageAds, textAds, "Album");
                } else {
                    adsHandler(position, imageAds, textAds, "Artist");
                }
                break;
            // recently
            case 2:
                if (isHavePlaylist) {
                    adsHandler(position, imageAds, textAds, "Single");
                } else {
                    adsHandler(position, imageAds, textAds, "Played");
                }
                break;
            // genre
            case 3:
                if (position == tabs.size() - 1) {
                    showAds(position, imageAds, textAds);
                }
                break;
        }
    }

    private void taklimAdsHandler(int position, ImageViewRounded imageAds, TextView textAds) {
        switch (tabType) {
            // syiar
            case 1:
                if (isHavePlaylist) {
                    adsHandler(position, imageAds, textAds, "Dai");
                } else {
                    adsHandler(position, imageAds, textAds, "Discover");
                }
                break;
            // kisah
            case 2:
                if (isHavePlaylist) {
                    adsHandler(position, imageAds, textAds, "Storyteller");
                } else {
                    adsHandler(position, imageAds, textAds, "Discover");
                }
                break;
            //  murrotal
            case 3:
                if (position == tabs.size() - 1) {
                    showAds(position, imageAds, textAds);
                }
                break;
        }
    }

    private void adsHandler(int position, ImageViewRounded imageAds, TextView textAds, String album) {
        if (tabs.get(position).name.equals(album)) {
            showAds(position, imageAds, textAds);
        } else if (position == tabs.size() - 1) {
            showAds(position, imageAds, textAds);
        } else {
            imageAds.setVisibility(View.GONE);
            textAds.setVisibility(View.GONE);
        }
    }

    private void showAds(int position, ImageViewRounded imageAds, TextView textAds) {
        imageAds.setVisibility(View.VISIBLE);
        textAds.setVisibility(View.VISIBLE);
        if (tabs.size() == position + 1) {
            ViewHelper.setMargins(imageAds, 16, 0, 16, getDP(16));
        }
    }

    @Override
    public int getItemCount() {
        return tabs.size();
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.parseInt(tabs.get(position).type_content_id);
    }

    private void parsePlaylist(String s, RecyclerView recyclerView, int type) {
        String json = "{ \"data\":" + s + "}";

        DataPlaylist playlistModel = gson.fromJson(json, DataPlaylist.class);

        adapterPlaylistHorizontal = new AdapterPlaylistHorizontal(context, playlistModel, type);
        recyclerView.setAdapter(adapterPlaylistHorizontal);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void parseAlbum(String s, RecyclerView recyclerView) {
        String json = "{ \"data\":" + s + "}";

        DataAlbum dataAlbum = gson.fromJson(json, DataAlbum.class);
        System.out.println("AdapterAlbumNew : " + dataAlbum.data.get(0).title);
        adapterAlbum = new AdapterAlbumNew(context, dataAlbum, 0);
        recyclerView.setAdapter(adapterAlbum);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void parseSingle(String s, RecyclerView recyclerView) {
        String json = "{ \"data\":" + s + "}";

        DataSingle dataSingle = gson.fromJson(json, DataSingle.class);

        adapterSong = new AdapterSongHorizontal(context, dataSingle, 0);
        recyclerView.setAdapter(adapterSong);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void parseArtist(String s, RecyclerView recyclerView, String subtitle) {
        String json = "{ \"data\":" + s + "}";

        DataArtist dataArtist = gson.fromJson(json, DataArtist.class);

        adapterArtistNew = new AdapterArtistNew(context, dataArtist, subtitle);
        recyclerView.setAdapter(adapterArtistNew);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void parseGenre(String s, RecyclerView recyclerView) {
        String json = "{ \"data\":" + s + "}";

        DataGenre dataGenre = gson.fromJson(json, DataGenre.class);

        adapterGenre = new AdapterGenreNew(context, dataGenre);
        recyclerView.setAdapter(adapterGenre);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private int getDP(int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
