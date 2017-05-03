package co.digdaya.kindis.view.activity.Detail;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.model.DataAlbum;
import co.digdaya.kindis.model.DataArtist;
import co.digdaya.kindis.model.DataPlaylist;
import co.digdaya.kindis.model.DataSingle;
import co.digdaya.kindis.model.MoreModel;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerActivity;
import co.digdaya.kindis.util.SpacingItem.SpacingItemGenre;
import co.digdaya.kindis.view.adapter.more.AdapterMoreAlbum;
import co.digdaya.kindis.view.adapter.more.AdapterMoreArtist;
import co.digdaya.kindis.view.adapter.more.AdapterMorePlaylist;
import co.digdaya.kindis.view.adapter.more.AdapterMoreSingle;

public class More extends BottomPlayerActivity {
    TextView title;
    ImageButton btnBack;
    RecyclerView listViewMore;
    GridLayoutManager gridLayoutManager;
    SessionHelper sessionHelper;

    String url, param, urlMore;
    int type;
    Boolean isLastItem = false;
    Gson gson;

    AdapterMoreAlbum adapterMoreAlbum;
    AdapterMoreSingle adapterMoreSingle;
    AdapterMorePlaylist adapterMorePlaylist;
    AdapterMoreArtist adapterMoreArtist;

    MoreModel.ArtistsMore artistsMore;
    MoreModel.AlbumMore albumMore;
    MoreModel.SinggleMore singgleMore;
    MoreModel.PlaylisMore playlisMore;

    /*List<DataAlbum.Data> listAlbum = new ArrayList<>();
    Type listTypeAlbum = new TypeToken<List<DataAlbum.Data>>() {}.getType();*/

    public More() {
        layout = R.layout.activity_more;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = (TextView) findViewById(R.id.title);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        listViewMore = (RecyclerView) findViewById(R.id.list_more);
        listViewMore.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        listViewMore.addItemDecoration(new SpacingItemGenre(getApplicationContext(), "more"));

        gridLayoutManager = (GridLayoutManager) listViewMore.getLayoutManager();

        title.setText(getIntent().getStringExtra("title"));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sessionHelper = new SessionHelper();
        gson = new Gson();

        urlMore = getIntent().getStringExtra("urlMore");

        type = getIntent().getIntExtra("type", 1);
        /*param = getIntent().getStringExtra("param");
        System.out.println("paramextra: "+param);
        switch (type){
            case 1:
                url = "home/artist_more?channel_id="+getIntent().getIntExtra("menuType", 1)+"&uid="+sessionHelper.getPreferences(getApplicationContext(), "user_id")+"&page=0&limit=12"+param;
                break;
            case 2:
                url = "home/album_more?channel_id="+getIntent().getIntExtra("menuType", 1)+"&uid="+sessionHelper.getPreferences(getApplicationContext(), "user_id")+"&page=0&limit=12"+param;
                break;
            case 3:
                url = "home/single_more?channel_id="+getIntent().getIntExtra("menuType", 1)+"&uid="+sessionHelper.getPreferences(getApplicationContext(), "user_id")+"&page=0&limit=12"+param;
                break;
            case 5:
                url = "playlist/more?channel_id="+getIntent().getIntExtra("menuType", 1)+"&uid="+sessionHelper.getPreferences(getApplicationContext(), "user_id")+"&page=0&limit=12"+param;
                break;
        }*/

        getDataMore();
        listViewMore.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = listViewMore.getLayoutManager().getItemCount();
                int lastVisibleItem = ((LinearLayoutManager) listViewMore.getLayoutManager()).findLastVisibleItemPosition();

                if (!isLastItem && totalItemCount == (lastVisibleItem + 1)&&urlMore.length()>10){
                    System.out.println("addOnScrollListener: last");
                    isLastItem = true;
                    loadMore();
                }else if (totalItemCount != (lastVisibleItem + 1)){
                    isLastItem = false;
                }
            }
        });
    }

    private void getDataMore(){
        new VolleyHelper().get(urlMore, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            urlMore = object.getString("next_page");
                            switch (type){
                                case 1:
                                    artistsMore = gson.fromJson(object.toString(), MoreModel.ArtistsMore.class);
                                    adapterMoreArtist = new AdapterMoreArtist(More.this, artistsMore);
                                    listViewMore.setAdapter(adapterMoreArtist);
                                    System.out.println("itemcount: "+artistsMore.result.size());
                                    break;
                                case 2:
                                    albumMore = gson.fromJson(object.toString(), MoreModel.AlbumMore.class);
                                    adapterMoreAlbum = new AdapterMoreAlbum(albumMore, getApplicationContext());
                                    listViewMore.setAdapter(adapterMoreAlbum);
                                    System.out.println("itemcount: "+albumMore.result.size());
                                    break;
                                case 3:
                                    singgleMore = gson.fromJson(object.toString(), MoreModel.SinggleMore.class);
                                    adapterMoreSingle = new AdapterMoreSingle(More.this, singgleMore);
                                    listViewMore.setAdapter(adapterMoreSingle);
                                    System.out.println("itemcount: "+singgleMore.result.size());
                                    break;
                                case 5:
                                    playlisMore = gson.fromJson(object.toString(), MoreModel.PlaylisMore.class);
                                    adapterMorePlaylist = new AdapterMorePlaylist(More.this, playlisMore);
                                    listViewMore.setAdapter(adapterMorePlaylist);
                                    System.out.println("itemcount: "+playlisMore.result.size());
                                    break;
                            }
                        }else {
                            finish();
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }

                //loadMore();
            }
        });
    }

    private void loadMore(){
        System.out.println("loadMoreUrl: "+urlMore);
        new VolleyHelper().get(urlMore, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            urlMore = object.getString("next_page");
                            JSONArray result = object.getJSONArray("result");
                            switch (type){
                                case 1:
                                    for (int i=0; i<result.length(); i++){
                                        DataArtist.Data data = gson.fromJson(result.getJSONObject(i).toString(), DataArtist.Data.class);
                                        artistsMore.result.add(data);
                                    }
                                    adapterMoreArtist.notifyDataSetChanged();
                                    System.out.println("itemcount: "+artistsMore.result.size());
                                    break;
                                case 2:
                                    for (int i=0; i<result.length(); i++){
                                        DataAlbum.Data data = gson.fromJson(result.getJSONObject(i).toString(), DataAlbum.Data.class);
                                        albumMore.result.add(data);
                                    }
                                    adapterMoreAlbum.notifyDataSetChanged();
                                    System.out.println("itemcount: "+albumMore.result.size());
                                    break;
                                case 3:
                                    for (int i=0; i<result.length(); i++){
                                        DataSingle.Data data = gson.fromJson(result.getJSONObject(i).toString(), DataSingle.Data.class);
                                        singgleMore.result.add(data);
                                    }
                                    adapterMoreSingle.notifyDataSetChanged();
                                    System.out.println("itemcount: "+singgleMore.result.size());
                                    break;
                                case 5:
                                    for (int i=0; i<result.length(); i++){
                                        DataPlaylist.Data data = gson.fromJson(result.getJSONObject(i).toString(), DataPlaylist.Data.class);
                                        playlisMore.result.add(data);
                                    }
                                    playlisMore = gson.fromJson(object.toString(), MoreModel.PlaylisMore.class);
                                    adapterMorePlaylist.notifyDataSetChanged();
                                    System.out.println("itemcount: "+playlisMore.result.size());
                                    break;
                            }
                        }else {
                            finish();
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
