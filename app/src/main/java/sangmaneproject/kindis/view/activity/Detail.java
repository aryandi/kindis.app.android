package sangmaneproject.kindis.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.PlayerService;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.PlayerActionHelper;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.util.DialogPlaylist;
import sangmaneproject.kindis.view.adapter.AdapterSong;

public class Detail extends AppCompatActivity {
    AppBarLayout appBarLayout;
    LinearLayout contFloatingButton;
    RelativeLayout contLabel;
    Toolbar toolbar;
    TextView titleToolbar;
    TextView titleDetail;
    TextView description;
    ImageView backDrop;
    Button btnPlayAll;

    RecyclerView listViewSong;
    AdapterSong adapterSong;
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    ArrayList<String> songPlaylist = new ArrayList<>();
    Dialog dialogPlaylis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);
        contFloatingButton = (LinearLayout) findViewById(R.id.cont_floating_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contLabel = (RelativeLayout) findViewById(R.id.cont_label);
        titleToolbar = (TextView) toolbar.findViewById(R.id.title_toolbar);
        titleDetail = (TextView) findViewById(R.id.title_detail);
        description = (TextView) findViewById(R.id.description);
        backDrop = (ImageView) findViewById(R.id.backdrop);
        btnPlayAll = (Button) findViewById(R.id.btn_play_all);
        listViewSong = (RecyclerView) findViewById(R.id.list_songs);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listViewSong.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        if (getIntent().getStringExtra("type").equals("genre")){
            String url = ApiHelper.DETAIL_GENRE+getIntent().getStringExtra("uid");
            getDetail(url);
        }else if (getIntent().getStringExtra("type").equals("album")){
            String url = ApiHelper.ITEM_ALBUM+getIntent().getStringExtra("uid");
            getDetail(url);
        }else if (getIntent().getStringExtra("type").equals("playlist")){
            /*String url = ApiHelper.ITEM_PLAYLIST+getIntent().getStringExtra("uid");
            getDetail(url);*/
            getDetailPlaylist();
        }

        titleToolbar.setVisibility(View.INVISIBLE);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;
            boolean isShow = false;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    contLabel.setVisibility(View.INVISIBLE);
                    contFloatingButton.setVisibility(View.INVISIBLE);
                    titleToolbar.setVisibility(View.VISIBLE);
                    isShow = true;
                } else if (isShow) {
                    contLabel.setVisibility(View.VISIBLE);
                    contFloatingButton.setVisibility(View.VISIBLE);
                    titleToolbar.setVisibility(View.INVISIBLE);
                    isShow = false;
                }
            }
        });

        btnPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("single_id", songPlaylist.get(0));
                Intent intent = new Intent(Detail.this, PlayerService.class);
                intent.setAction(PlayerActionHelper.PLAY_MULTYSOURCE);
                intent.putExtra("single_id", songPlaylist.get(0));
                intent.putExtra("list_uid", songPlaylist);
                startService(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search){
            Intent intent = new Intent(this, Search.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDetail(String url){
        new VolleyHelper().get(url, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    try {
                        Log.d("DetailResponse", response);
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");
                            JSONObject summary = result.getJSONObject("summary");
                            titleToolbar.setText(summary.getString("title"));
                            titleDetail.setText(summary.getString("title"));
                            description.setText(summary.getString("description"));

                            Glide.with(getApplicationContext())
                                    .load(ApiHelper.BASE_URL_IMAGE+summary.getString("image"))
                                    .thumbnail( 0.1f )
                                    .placeholder(R.drawable.ic_default_img)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .centerCrop()
                                    .into(backDrop);

                            if (getIntent().getStringExtra("type").equals("genre")){
                                getSong();
                            }else if (getIntent().getStringExtra("type").equals("album")){
                                getListSong(result.getJSONArray("single").toString());
                            }
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

    private void getDetailPlaylist(){
        String url = ApiHelper.ITEM_PLAYLIST+getIntent().getStringExtra("uid");
        new VolleyHelper().get(url, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    Log.d("DetailResponse", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");
                            JSONObject playlist = result.getJSONObject("playlist");
                            titleToolbar.setText(playlist.getString("playlist_name"));
                            titleDetail.setText(playlist.getString("playlist_name"));

                            JSONArray singles = playlist.getJSONArray("singles");
                            for (int i=0; i<singles.length(); i++){
                                JSONObject data = singles.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("uid", data.optString("single_id"));
                                map.put("title", data.optString("title"));
                                listSong.add(map);
                                songPlaylist.add(data.optString("single_id"));
                            }

                            adapterSong = new AdapterSong(getApplicationContext(), listSong);
                            listViewSong.setAdapter(adapterSong);
                            listViewSong.setNestedScrollingEnabled(true);

                            onClickMenuSong();
                        }else {
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //from genre
    private void getSong(){
        new VolleyHelper().get(ApiHelper.SINGLE_GENRE + getIntent().getStringExtra("uid"), new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    Log.d("DetailgetSong", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray result = object.getJSONArray("result");
                        for (int i=0; i<result.length(); i++){
                            JSONObject data = result.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("uid", data.optString("uid"));
                            map.put("title", data.optString("title"));
                            listSong.add(map);
                            songPlaylist.add(data.optString("uid"));
                        }

                        adapterSong = new AdapterSong(getApplicationContext(), listSong);
                        listViewSong.setAdapter(adapterSong);
                        listViewSong.setNestedScrollingEnabled(true);

                        onClickMenuSong();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //from album
    private void getListSong(String json){
        try {
            JSONArray single = new JSONArray(json);
            for (int i=0; i < single.length(); i++){
                JSONObject data = single.getJSONObject(i);
                JSONObject summary = data.getJSONObject("summary");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("uid", summary.optString("uid"));
                map.put("title", summary.optString("title"));
                listSong.add(map);
                songPlaylist.add(summary.optString("uid"));
            }
            adapterSong = new AdapterSong(getApplicationContext(), listSong);
            listViewSong.setAdapter(adapterSong);
            listViewSong.setNestedScrollingEnabled(true);

            onClickMenuSong();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onClickMenuSong(){
        adapterSong.setOnClickMenuListener(new AdapterSong.OnClickMenuListener() {
            @Override
            public void onClick(String uid) {
                new DialogPlaylist(Detail.this, dialogPlaylis).showDialog();
            }
        });
    }
}
