package co.digdaya.kindis.live.view.adapter.tab;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;

import java.util.List;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.SessionHelper;
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

public class AdapterListTab extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int MAX_ADS = 4;
    private final AdRequest adRequest;
    private final String isPremium;
    private final String[] ads;
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

    private static final int FOOTER_VIEW = 99;

    SessionHelper sessionHelper;

    public AdapterListTab(Activity context, TabModel tabModel, int tab, int menuType) {

        this.context = context;
        this.tabModel = tabModel;
        this.tabType = tab;
        this.menuType = menuType;
        MobileAds.initialize(context, context.getString(R.string.ads_app_id));
        sessionHelper = new SessionHelper();
        isPremium = sessionHelper.getPreferences(context, "is_premium");
        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(sessionHelper.getPreferences(context, "android_id"))
                .build();
        ads = context.getResources().getStringArray(R.array.ads_ids);
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_VIEW) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer_ads_list, parent, false);
            return new FooterViewHolder(v);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ItemListTab(view, sessionHelper, ads);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            if (holder instanceof ItemListTab) {
                ItemListTab itemListTab = (ItemListTab) holder;
                TextView title = itemListTab.title;
                TextView btnMore = itemListTab.btnMore;
                final RecyclerView recyclerView = itemListTab.list;
                View imageAds = itemListTab.imageAds;
                AdView imageAds1 = itemListTab.imageAds1;
                AdView imageAds2 = itemListTab.imageAds2;
                AdView imageAds3 = itemListTab.imageAds3;
                AdView imageAds4 = itemListTab.imageAds4;

                imageAds.setVisibility(View.GONE);

                if (isPremium.equals("0")) {
                    switch (menuType) {
                        case 1:
                            musiqAdsHandler(position, imageAds1, imageAds2, imageAds3, imageAds4);
                            break;
                        case 9:
                            taklimAdsHandler(position, imageAds1, imageAds2, imageAds3, imageAds4);
                            break;
                    }
                }

                if (tabs.get(position).data.length() < 10) {
                    title.setVisibility(View.GONE);
                    btnMore.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    imageAds.setVisibility(View.GONE);
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
            } else if (holder instanceof FooterViewHolder) {
                final FooterViewHolder vh = (FooterViewHolder) holder;
                showAds(0, vh.imageAds1, vh.imageAds2, vh.imageAds3, vh.imageAds4);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void musiqAdsHandler(int position, AdView imageAds1, AdView imageAds2, AdView imageAds3, AdView imageAds4) {
        switch (tabType) {
            // discover
            case 1:
                if (isHavePlaylist) {
                    adsHandler(position, imageAds1, imageAds2, imageAds3, imageAds4, "Album");
                } else {
                    adsHandler(position, imageAds1, imageAds2, imageAds3, imageAds4, "Artist");
                }
                break;
            // recently
            case 2:
                if (isHavePlaylist) {
                    adsHandler(position, imageAds1, imageAds2, imageAds3, imageAds4, "Single");
                } else {
                    adsHandler(position, imageAds1, imageAds2, imageAds3, imageAds4, "Played");
                }
                break;
            // genre
            case 3:
//                if (position == tabs.size() - 1) {
//                    showAds(position, imageAds);
//                }
                break;
        }
    }

    private void taklimAdsHandler(int position, AdView imageAds1, AdView imageAds2, AdView imageAds3, AdView imageAds4) {
        switch (tabType) {
            // syiar
            case 1:
                if (isHavePlaylist) {
                    adsHandler(position, imageAds1, imageAds2, imageAds3, imageAds4, "Dai");
                } else {
                    adsHandler(position, imageAds1, imageAds2, imageAds3, imageAds4, "Discover");
                }
                break;
            // kisah
            case 2:
                if (isHavePlaylist) {
                    adsHandler(position, imageAds1, imageAds2, imageAds3, imageAds4, "Storyteller");
                } else {
                    adsHandler(position, imageAds1, imageAds2, imageAds3, imageAds4, "Discover");
                }
                break;
            //  murrotal
            case 3:
//                if (position == tabs.size() - 1) {
//                    showAds(position, imageAds);
//                }
                break;
        }
    }

    private void adsHandler(int position, AdView imageAds1, AdView imageAds2, AdView imageAds3, AdView imageAds4, String album) {
        if (tabs.get(position).name.equals(album)) {
            showAds(position, imageAds1, imageAds2, imageAds3, imageAds4);
        }
//        else if (position == tabs.size() - 1) {
//            showAds(position, imageAds);
//        }
        else {
            imageAds1.setVisibility(View.GONE);
            imageAds2.setVisibility(View.GONE);
            imageAds3.setVisibility(View.GONE);
            imageAds4.setVisibility(View.GONE);
        }
    }

    private void showAds(int position, AdView imageAds1, AdView imageAds2, AdView imageAds3, AdView imageAds4) {

        String ads_id = sessionHelper.getPreferences(context, "ads_id");

        int adsId = 0;
        if (!ads_id.equals("")) {
            adsId = Integer.parseInt(ads_id);
        }
        if (adsId >= MAX_ADS) {
            adsId = 0;
        }

        Log.v("ADS", "showAds: " + adsId);

        switch (adsId){
            case 0 :
                imageAds1.loadAd(adRequest);
                imageAds1.setVisibility(View.VISIBLE);
                imageAds2.setVisibility(View.GONE);
                imageAds3.setVisibility(View.GONE);
                imageAds4.setVisibility(View.GONE);
                break;

            case 1 :
                imageAds2.loadAd(adRequest);
                imageAds2.setVisibility(View.VISIBLE);
                imageAds1.setVisibility(View.GONE);
                imageAds3.setVisibility(View.GONE);
                imageAds4.setVisibility(View.GONE);
                break;

            case 2 :
                imageAds3.loadAd(adRequest);
                imageAds3.setVisibility(View.VISIBLE);
                imageAds1.setVisibility(View.GONE);
                imageAds2.setVisibility(View.GONE);
                imageAds4.setVisibility(View.GONE);
                break;

            case 3 :
                imageAds4.loadAd(adRequest);
                imageAds4.setVisibility(View.VISIBLE);
                imageAds1.setVisibility(View.GONE);
                imageAds2.setVisibility(View.GONE);
                imageAds3.setVisibility(View.GONE);
                break;
        }

        adsId++;
        sessionHelper.setPreferences(context, "ads_id", String.valueOf(adsId));
    }

    @Override
    public int getItemCount() {
        if (isPremium.equals("1")) return tabs.size();

        if (tabs == null) {
            return 0;
        }

        if (tabs.size() == 0) {
            //Return 1 here to show nothing
            return 1;
        }

        // Add extra view to show the footer view
        return tabs.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPremium.equals("0") && position == tabs.size()) {
            // This is where we'll add footer.
            return FOOTER_VIEW;
        }
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

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        private final AdView imageAds1;
        private final AdView imageAds2;
        private final AdView imageAds3;
        private final AdView imageAds4;

        FooterViewHolder(View itemView) {
            super(itemView);
            imageAds1 = (AdView) itemView.findViewById(R.id.image_ads_1);
            imageAds2 = (AdView) itemView.findViewById(R.id.image_ads_2);
            imageAds3 = (AdView) itemView.findViewById(R.id.image_ads_3);
            imageAds4 = (AdView) itemView.findViewById(R.id.image_ads_4);
        }
    }
}
