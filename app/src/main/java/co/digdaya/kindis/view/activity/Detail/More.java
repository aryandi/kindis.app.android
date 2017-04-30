package co.digdaya.kindis.view.activity.Detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.model.DataAlbum;
import co.digdaya.kindis.model.MoreModel;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerActivity;
import co.digdaya.kindis.util.SpacingItem.SpacingItemGenre;
import co.digdaya.kindis.util.SpacingItem.SpacingItemMore;
import co.digdaya.kindis.view.adapter.item.AdapterPlaylistHorizontal;
import co.digdaya.kindis.view.adapter.more.AdapterMoreAlbum;
import co.digdaya.kindis.view.adapter.more.AdapterMoreArtist;
import co.digdaya.kindis.view.adapter.more.AdapterMorePlaylist;
import co.digdaya.kindis.view.adapter.more.AdapterMoreSingle;

public class More extends BottomPlayerActivity {
    TextView title;
    ImageButton btnBack;
    RecyclerView listViewMore;
    SessionHelper sessionHelper;

    String url, param, urlMore;
    int type;
    Gson gson;

    AdapterMoreAlbum adapterMoreAlbum;
    AdapterMoreSingle adapterMoreSingle;
    AdapterMorePlaylist adapterMorePlaylist;
    AdapterMoreArtist adapterMoreArtist;

    MoreModel.ArtistsMore artistsMore;
    MoreModel.AlbumMore albumMore;
    MoreModel.SinggleMore singgleMore;
    MoreModel.PlaylisMore playlisMore;

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

        title.setText(getIntent().getStringExtra("title"));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sessionHelper = new SessionHelper();
        gson = new Gson();

        type = getIntent().getIntExtra("type", 1);
        param = getIntent().getStringExtra("param");
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
        }

        getDataMore();
    }

    private void getDataMore(){
        new VolleyHelper().get(ApiHelper.BASE_URL + url, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        urlMore = object.getString("next_page");
                        if (object.getBoolean("status")){
                            switch (type){
                                case 1:
                                    artistsMore = gson.fromJson(object.toString(), MoreModel.ArtistsMore.class);
                                    adapterMoreArtist = new AdapterMoreArtist(More.this, artistsMore);
                                    listViewMore.setAdapter(adapterMoreArtist);
                                    break;
                                case 2:
                                    albumMore = gson.fromJson(object.toString(), MoreModel.AlbumMore.class);
                                    adapterMoreAlbum = new AdapterMoreAlbum(albumMore, getApplicationContext());
                                    listViewMore.setAdapter(adapterMoreAlbum);
                                    break;
                                case 3:
                                    singgleMore = gson.fromJson(object.toString(), MoreModel.SinggleMore.class);
                                    adapterMoreSingle = new AdapterMoreSingle(More.this, singgleMore);
                                    listViewMore.setAdapter(adapterMoreSingle);
                                    break;
                                case 5:
                                    playlisMore = gson.fromJson(object.toString(), MoreModel.PlaylisMore.class);
                                    adapterMorePlaylist = new AdapterMorePlaylist(More.this, playlisMore);
                                    listViewMore.setAdapter(adapterMorePlaylist);
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
