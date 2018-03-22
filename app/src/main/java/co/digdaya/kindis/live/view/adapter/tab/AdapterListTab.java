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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
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
        return new ItemListTab(view);
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
                imageAds.setVisibility(View.GONE);

                if (isPremium.equals("0")) {
                    switch (menuType) {
                        case 1:
                            musiqAdsHandler(position, imageAds);
                            break;
                        case 9:
                            taklimAdsHandler(position, imageAds);
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
                vh.imageAds.setVisibility(View.GONE);
                showAds(0, vh.imageAds);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void musiqAdsHandler(int position, View imageAds) {
        switch (tabType) {
            // discover
            case 1:
                if (isHavePlaylist) {
                    adsHandler(position, imageAds, "Album");
                } else {
                    adsHandler(position, imageAds, "Artist");
                }
                break;
            // recently
            case 2:
                if (isHavePlaylist) {
                    adsHandler(position, imageAds, "Single");
                } else {
                    adsHandler(position, imageAds, "Played");
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

    private void taklimAdsHandler(int position, View imageAds) {
        switch (tabType) {
            // syiar
            case 1:
                if (isHavePlaylist) {
                    adsHandler(position, imageAds, "Dai");
                } else {
                    adsHandler(position, imageAds, "Discover");
                }
                break;
            // kisah
            case 2:
                if (isHavePlaylist) {
                    adsHandler(position, imageAds, "Storyteller");
                } else {
                    adsHandler(position, imageAds, "Discover");
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

    private void adsHandler(int position, View imageAds, String album) {
        if (tabs.get(position).name.equals(album)) {
            showAds(position, imageAds);
        }
//        else if (position == tabs.size() - 1) {
//            showAds(position, imageAds);
//        }
        else {
            imageAds.setVisibility(View.GONE);
        }
    }

    private void showAds(int position, final View adContainer) {

        String ads_id = sessionHelper.getPreferences(context, "ads_id");

        int adsId = 0;
        if (!ads_id.equals("")){
            adsId = Integer.parseInt(ads_id);
        }
        if (adsId >= MAX_ADS){
            adsId = 0;
        }

        final AdView imageAds = new AdView(context);

        imageAds.setAdSize(AdSize.LARGE_BANNER);
        imageAds.setAdUnitId(ads[adsId]);
        imageAds.loadAd(adRequest);
        imageAds.setAdListener(new AdListener() {

                @Override
                public void onAdLoaded() {
                    adContainer.setVisibility(View.VISIBLE);
                    imageAds.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdFailedToLoad(int error) {
                    adContainer.setVisibility(View.VISIBLE);
                    imageAds.setVisibility(View.GONE);
                }

            });

        ((RelativeLayout)adContainer).addView(imageAds);
        Log.v("ADS", "ads id: "+ ads[adsId] + "id " + adsId);

        adsId++;
        sessionHelper.setPreferences(context, "ads_id", String.valueOf(adsId));
//        if (tabs.size() == position + 1) {
//            ViewHelper.setMargins(imageAds, getDP(10), 0, getDP(10), getDP(26));
//        }
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

        View imageAds;

        FooterViewHolder(View itemView) {
            super(itemView);
            imageAds = itemView.findViewById(R.id.image_ads);
//            imageAds.setAdListener(new AdListener() {
//
//                @Override
//                public void onAdLoaded() {
//                    imageAds.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onAdFailedToLoad(int error) {
//                    imageAds.setVisibility(View.GONE);
//                }
//
//            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Do whatever you want on clicking the item
                }
            });
        }
    }
}
