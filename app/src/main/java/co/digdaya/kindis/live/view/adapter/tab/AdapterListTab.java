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

import co.digdaya.kindis.live.BuildConfig;
import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.AnalyticHelper;
import co.digdaya.kindis.live.helper.Constanta;
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

/**
 * Created by DELL on 3/31/2017.
 */

public class AdapterListTab extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int MAX_ADS = 4;
    private final AdRequest adRequest;
    private final String isPremium;
    private final String[] ads;
    private final AnalyticHelper analyticHelper;
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
    private int menuType;
    private String tabName;
    private String origin;
    private int tabType;

    private String paramMore = "";

    private static final int ADS_VIEW = 99;

    private SessionHelper sessionHelper;

    public AdapterListTab(Activity context, TabModel tabModel, int tab, int menuType, String tabName, String origin) {
        this.context = context;
        this.tabModel = tabModel;
        this.tabType = tab;
        this.menuType = menuType;
        this.tabName = tabName;
        this.origin = origin;
        MobileAds.initialize(context, context.getString(R.string.ads_app_id));
        sessionHelper = new SessionHelper();
        analyticHelper = new AnalyticHelper(context);
        isPremium = sessionHelper.getPreferences(context, "is_premium");
        if (BuildConfig.BUILD_TYPE.equals("release")){
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
        } else {
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice(sessionHelper.getPreferences(context, Constanta.PREF_DEVICE_ID))
                    .build();
        }
        ads = context.getResources().getStringArray(R.array.ads_ids);
        switch (tab) {
            case 1:
                tabs = tabModel.tab1;
                break;
            case 2:
                tabs = tabModel.tab2;
                break;
            case 3:
                tabs = tabModel.tab3;
                break;
        }
        gson = new Gson();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ADS_VIEW) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_list, parent, false);
            return new AdsViewHolder(v);
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

                if (tabs.get(position).data.length() < 10) {
                    title.setVisibility(View.GONE);
                    btnMore.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
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
            } else if (holder instanceof AdsViewHolder) {
                final AdsViewHolder vh = (AdsViewHolder) holder;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            return ADS_VIEW;
        } else if (tabs.get(position).name.equals("ads")){
            return ADS_VIEW;
        }
        return Integer.parseInt(tabs.get(position).type_content_id);
    }

    private void parsePlaylist(String s, RecyclerView recyclerView, int type) {
        String json = "{ \"data\":" + s + "}";

        final DataPlaylist playlistModel = gson.fromJson(json, DataPlaylist.class);
        adapterPlaylistHorizontal = new AdapterPlaylistHorizontal(context, playlistModel, type,
                new AdapterPlaylistHorizontal.RowClickListener() {
            @Override
            public void onRowClick(int position) {
                DataPlaylist.Data data = playlistModel.data.get(position);
                analyticHelper.contentClick(origin, data.uid, "playlist", data.name, "null", tabName);
            }
        });
        recyclerView.setAdapter(adapterPlaylistHorizontal);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void parseAlbum(String s, RecyclerView recyclerView) {
        String json = "{ \"data\":" + s + "}";

        final DataAlbum dataAlbum = gson.fromJson(json, DataAlbum.class);
        System.out.println("AdapterAlbumNew : " + dataAlbum.data.get(0).title);
        adapterAlbum = new AdapterAlbumNew(context, dataAlbum, 0, new AdapterAlbumNew.RowClickListener() {
            @Override
            public void onRowClick(int position) {
                DataAlbum.Data data = dataAlbum.data.get(position);
                analyticHelper.contentClick(origin, data.uid, "album", data.title, "null", tabName);
            }
        });
        recyclerView.setAdapter(adapterAlbum);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void parseSingle(String s, RecyclerView recyclerView) {
        String json = "{ \"data\":" + s + "}";

        final DataSingle dataSingle = gson.fromJson(json, DataSingle.class);

        adapterSong = new AdapterSongHorizontal(context, dataSingle, 0, new AdapterSongHorizontal.RowClickListener() {
            @Override
            public void onRowClick(int position) {
                DataSingle.Data data = dataSingle.data.get(position);
                analyticHelper.contentClick(origin, data.uid, "single", data.title, "null", tabName);
            }
        });
        recyclerView.setAdapter(adapterSong);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void parseArtist(String s, RecyclerView recyclerView, String subtitle) {
        String json = "{ \"data\":" + s + "}";

        final DataArtist dataArtist = gson.fromJson(json, DataArtist.class);

        adapterArtistNew = new AdapterArtistNew(context, dataArtist, subtitle, new AdapterArtistNew.RowClickListener() {
            @Override
            public void onRowClick(int pos) {
                DataArtist.Data data = dataArtist.data.get(pos);
                analyticHelper.contentClick(origin, data.uid, "artist", data.name, "null", tabName);
            }
        });
        recyclerView.setAdapter(adapterArtistNew);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void parseGenre(String s, RecyclerView recyclerView) {
        String json = "{ \"data\":" + s + "}";

        final DataGenre dataGenre = gson.fromJson(json, DataGenre.class);

        adapterGenre = new AdapterGenreNew(context, dataGenre, new AdapterGenreNew.RowClickListener() {
            @Override
            public void onRowClick(int position) {
                DataGenre.Data data = dataGenre.data.get(position);
                analyticHelper.contentClick(origin, data.uid, "genre", data.title, "null", tabName);
            }
        });
        recyclerView.setAdapter(adapterGenre);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private int getDP(int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public class AdsViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout adContainer;

        AdsViewHolder(View itemView) {
            super(itemView);

            adContainer = itemView.findViewById(R.id.image_ads);
            adContainer.setVisibility(View.GONE);

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
                }

                @Override
                public void onAdFailedToLoad(int error) {
                    adContainer.setVisibility(View.GONE);
                }

            });

            adContainer.addView(imageAds);
            Log.v("ADS", "ads id: "+ ads[adsId] + " id " + adsId);

            adsId++;
            sessionHelper.setPreferences(context, "ads_id", String.valueOf(adsId));
        }
    }

    public class ItemListTab extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView btnMore;
        public RecyclerView list;

        public ItemListTab(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_list);
            btnMore = itemView.findViewById(R.id.btn_more_list);
            list = itemView.findViewById(R.id.list);
        }
    }
}
