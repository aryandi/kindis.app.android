package co.digdaya.kindis.view.activity.Detail;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.databse.KindisDBHelper;
import co.digdaya.kindis.databse.KindisDBname;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.Constanta;
import co.digdaya.kindis.helper.PlayerActionHelper;
import co.digdaya.kindis.helper.PlayerSessionHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;
import co.digdaya.kindis.service.DownloadService;
import co.digdaya.kindis.service.PlayerService;
import co.digdaya.kindis.util.BackgroundProses.RefreshToken;
import co.digdaya.kindis.util.BackgroundProses.ResultPayment;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerActivity;
import co.digdaya.kindis.view.activity.Search;
import co.digdaya.kindis.view.adapter.item.AdapterSong;
import co.digdaya.kindis.view.dialog.DialogPayment;
import co.digdaya.kindis.view.dialog.DialogSingleMenu;

public class Detail extends BottomPlayerActivity implements View.OnClickListener {
    AppBarLayout appBarLayout;
    LinearLayout contFloatingButton;
    RelativeLayout contLabel;
    Toolbar toolbar;
    TextView titleToolbar;
    TextView titleDetail;
    TextView description;
    ImageView backDrop;
    Button btnPlayAll, btnPremium;

    RecyclerView listViewSong;
    AdapterSong adapterSong;
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    ArrayList<String> songPlaylist = new ArrayList<>();

    Dialog dialogPlaylis, dialogPay;
    DialogPayment dialogPayment;

    SessionHelper sessionHelper;
    PlayerSessionHelper playerSessionHelper;
    RefreshToken refreshToken;

    String json, types;
    int isPremium = 0;
    int premiumUser;
    boolean buyStatus;

    boolean isGetPrice = false;
    int price;
    String googleCode;
    String transID;
    String playlistID;
    public Detail(){
        layout = R.layout.activity_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        btnPremium = (Button) findViewById(R.id.btn_premium);
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

        sessionHelper = new SessionHelper();
        playerSessionHelper = new PlayerSessionHelper();
        refreshToken = new RefreshToken(getApplicationContext());
        btnPremium.setOnClickListener(this);

        listViewSong.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        premiumUser = Integer.parseInt(sessionHelper.getPreferences(getApplicationContext(), "is_premium"));

        backDrop.setColorFilter(Color.parseColor("#70000000"));

        types = getIntent().getStringExtra("type");
        if (types.equals("album")){
            if (premiumUser==0){
                btnPremium.setVisibility(View.GONE);
            }else {
                btnPremium.setVisibility(View.VISIBLE);
            }
            String url = ApiHelper.ITEM_ALBUM+getIntent().getStringExtra("uid");
            getDetail(url);
        }else if (types.equals("playlist")){
            getDetailPlaylist();
        }else if (types.equals("premium")){
            btnPremium.setVisibility(View.VISIBLE);
            getListPremium();
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
                System.out.println("isPremium= "+isPremium+"\npremiumUser= "+premiumUser);
                playerSessionHelper.setPreferences(getApplicationContext(), "isShuffle", "false");
                playerSessionHelper.setPreferences(getApplicationContext(), "setShuffle", "true");
                if (isPremium==1 && premiumUser==0){
                    dialogPayment.showDialog();
                }else {
                    Toast.makeText(getApplicationContext(), "Loading . . . ", Toast.LENGTH_LONG).show();
                    new PlayerSessionHelper().setPreferences(getApplicationContext(), "index", String.valueOf(songPlaylist.size()));
                    new PlayerSessionHelper().setPreferences(getApplicationContext(), "json", json);
                    new PlayerSessionHelper().setPreferences(getApplicationContext(), "type", types);
                    Intent intent = new Intent(Detail.this, PlayerService.class);
                    intent.setAction(PlayerActionHelper.PLAY_MULTYSOURCE);
                    intent.putExtra("single_id", songPlaylist.get(0));
                    intent.putExtra("list_uid", songPlaylist);
                    startService(intent);
                }
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
                            playerSessionHelper.setPreferences(getApplicationContext(), "subtitle_player", summary.getString("title"));


                            Glide.with(getApplicationContext())
                                    .load(ApiHelper.BASE_URL_IMAGE+summary.getString("banner_image"))
                                    .thumbnail( 0.1f )
                                    .placeholder(R.drawable.ic_default_img)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .centerCrop()
                                    .into(backDrop);

                            if (types.equals("genre")){
                                getSong();
                            }else if (types.equals("album")){
                                json = result.getJSONArray("single").toString();
                                getListSong(result.getJSONArray("single").toString(), summary.getString("title"));
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

    //from playlist
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
                            playerSessionHelper.setPreferences(getApplicationContext(), "subtitle_player", playlist.getString("playlist_name"));

                            JSONArray singles = playlist.getJSONArray("singles");
                            json = singles.toString();
                            for (int i=0; i<singles.length(); i++){
                                JSONObject data = singles.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("uid", data.optString("single_id"));
                                map.put("title", data.optString("title"));
                                map.put("subtitle", data.optString("artist"));
                                map.put("is_premium", data.optString("is_premium"));
                                map.put("artist_id", data.getString("artist_id"));
                                map.put("share_link", data.optString("share_link"));
                                listSong.add(map);
                                songPlaylist.add(data.optString("single_id"));
                            }

                            adapterSong = new AdapterSong(Detail.this, listSong, "", null);
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
        new VolleyHelper().get(ApiHelper.SINGLE_GENRE + getIntent().getStringExtra("uid")+"&page=&uid="+sessionHelper.getPreferences(getApplicationContext(), "user_id"), new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    Log.d("DetailgetSong", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray result = object.getJSONArray("result");
                        json = result.toString();
                        for (int i=0; i<result.length(); i++){
                            JSONObject data = result.getJSONObject(i);
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("uid", data.optString("uid"));
                            map.put("title", data.optString("title"));
                            map.put("subtitle", data.optString("artist"));
                            map.put("is_premium", data.optString("is_premium"));
                            map.put("artist_id", data.getString("artist_id"));
                            map.put("share_link", data.optString("share_link"));
                            listSong.add(map);
                            songPlaylist.add(data.optString("uid"));
                        }

                        adapterSong = new AdapterSong(Detail.this, listSong, "", null);
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

    //from premium
    private void getListPremium(){
        new GetPrice().execute();
        HashMap<String, String> param = new HashMap<>();
        param.put("uid", sessionHelper.getPreferences(getApplicationContext(), "user_id"));
        param.put("token_access", sessionHelper.getPreferences(getApplicationContext(), "token_access"));
        param.put("playlist_id", getIntent().getStringExtra("uid"));

        new VolleyHelper().post(ApiHelper.DETAIL_PLAYLIST_PREMIUM, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    try {
                        System.out.println("playlistpremium"+ response);
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");
                            JSONObject playlist = result.getJSONObject("playlist");
                            titleToolbar.setText(playlist.getString("playlist_name"));
                            titleDetail.setText(playlist.getString("playlist_name"));
                            playerSessionHelper.setPreferences(getApplicationContext(), "subtitle_player", playlist.getString("playlist_name"));
                            playlistID = playlist.getString("uid");
                            transID = playlist.getString("order_id");

                            sessionHelper.setPreferences(getApplicationContext(), "playlistID", playlistID);
                            sessionHelper.setPreferences(getApplicationContext(), "transID", transID);

                            dialogPayment = new DialogPayment(dialogPay,
                                    Detail.this,
                                    transID,
                                    price,
                                    "Playlist : "+playlist.getString("playlist_name"),
                                    googleCode,
                                    playlist.getString("order_id"),
                                    playlist.getString("uid"),
                                    "2",
                                    playlistID);

                            isPremium = Integer.parseInt(playlist.getString("is_premium"));

                            checkPlaylistExist(playlist.getString("uid"));
                            buyStatus = playlist.getBoolean("buy_status");
                            if (isPremium == 1 && premiumUser==1){
                                btnPremium.setText("SAVE");
                            }else if(premiumUser==0 && isPremium==1){
                                if (buyStatus){
                                    btnPremium.setText("SAVE");
                                }else {
                                    btnPremium.setText("RENT");
                                }
                            }else if (isPremium == 0 && premiumUser==0){
                                btnPremium.setVisibility(View.GONE);
                            }

                            Glide.with(getApplicationContext())
                                    .load(ApiHelper.BASE_URL_IMAGE+playlist.getString("banner_image"))
                                    .thumbnail( 0.1f )
                                    .placeholder(R.drawable.ic_default_img)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .centerCrop()
                                    .into(backDrop);
                            JSONArray single = playlist.getJSONArray("singles");
                            json = single.toString();
                            for (int i=0; i<single.length(); i++){
                                JSONObject data = single.getJSONObject(i);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("uid", data.getString("single_id"));
                                map.put("title", data.optString("title"));
                                map.put("subtitle", data.optString("artist"));
                                map.put("is_premium", data.optString("is_premium"));
                                map.put("artist_id", data.getString("artist_id"));
                                map.put("share_link", data.optString("share_link"));
                                listSong.add(map);
                                songPlaylist.add(data.optString("single_id"));
                            }

                            if (isPremium==1 && premiumUser==0){
                                adapterSong = new AdapterSong(Detail.this, listSong, "premium", null);
                            }else {
                                adapterSong = new AdapterSong(Detail.this, listSong, "", null);
                            }
                            listViewSong.setAdapter(adapterSong);
                            listViewSong.setNestedScrollingEnabled(true);
                            onClickMenuSong();
                        }else {
                            if (refreshToken.refreshToken()){
                                getListPremium();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    //from album
    private void getListSong(String json, String subtitle){
        try {
            JSONArray single = new JSONArray(json);
            for (int i=0; i < single.length(); i++){
                JSONObject data = single.getJSONObject(i);
                JSONObject summary = data.getJSONObject("summary");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("uid", summary.optString("uid"));
                map.put("title", summary.optString("title"));
                map.put("subtitle", subtitle);
                map.put("is_premium", summary.getString("is_premium"));
                map.put("artist_id", summary.getString("artist_id"));
                map.put("share_link", summary.optString("share_link"));
                listSong.add(map);
                songPlaylist.add(summary.optString("uid"));
            }
            adapterSong = new AdapterSong(Detail.this, listSong, "", null);
            listViewSong.setAdapter(adapterSong);
            listViewSong.setNestedScrollingEnabled(true);
            onClickMenuSong();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onClickMenuSong(){
        String isMyPlaylist = ""+getIntent().getStringExtra("isMyPlaylist");
        if (isMyPlaylist.equals("true")){
            adapterSong.setOnClickMenuListener(new AdapterSong.OnClickMenuListener() {

                @Override
                public void onClick(final String uid, ImageButton imageButton, String artistID, String shareLink) {
                    PopupMenu popup = new PopupMenu(getApplicationContext(), imageButton);
                    popup.getMenuInflater().inflate(R.menu.list_playlist, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId()==R.id.delete){
                                removeFromPlaylist(uid);
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            });
        }else {
            adapterSong.setOnClickMenuListener(new AdapterSong.OnClickMenuListener() {
                @Override
                public void onClick(String uid, ImageButton imageButton, String artistID, String shareLink) {
                    new DialogSingleMenu(Detail.this, dialogPlaylis, uid, artistID, shareLink, false).showDialog();
                }
            });
        }
    }

    private void removeFromPlaylist(String uid){
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", new SessionHelper().getPreferences(getApplicationContext(), "user_id"));
        param.put("playlist_id", getIntent().getStringExtra("uid"));
        param.put("songs", "["+uid+"]");

        new VolleyHelper().post(ApiHelper.REMOVE_ITEM_PLAYLIST, param, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                            listSong.clear();
                            getDetailPlaylist();
                        }else {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_premium:
                if (types.equals("album")){
                    Intent intent = new Intent(this, DownloadService.class);
                    intent.setAction(Constanta.INTENT_ACTION_DOWNLOAD_ALBUM);
                    intent.putExtra(Constanta.INTENT_ACTION_DOWNLOAD_ALBUM_ID, getIntent().getStringExtra("uid"));
                    startService(intent);
                }else if (types.equals("premium")){
                    if (btnPremium.getText().equals("RENT")){
                        if (isGetPrice){
                            dialogPayment.showDialog();
                        }
                    }else {
                        Intent intent = new Intent(this, DownloadService.class);
                        intent.setAction(Constanta.INTENT_ACTION_DOWNLOAD_PLAYLIST);
                        intent.putExtra(Constanta.INTENT_ACTION_DOWNLOAD_PLAYLIST_ID, getIntent().getStringExtra("uid"));
                        startService(intent);
                    }
                    /*if (buyStatus){
                        dialogPayment.showDialog();
                    }else {
                        Intent intent = new Intent(this, DownloadService.class);
                        intent.setAction(Constanta.INTENT_ACTION_DOWNLOAD_PLAYLIST);
                        intent.putExtra(Constanta.INTENT_ACTION_DOWNLOAD_PLAYLIST_ID, getIntent().getStringExtra("uid"));
                        startService(intent);
                    }*/
                }
                break;
        }
    }

    private void checkPlaylistExist(String id){
        KindisDBHelper kindisDBHelper = new KindisDBHelper(getApplicationContext());
        SQLiteDatabase db = kindisDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ KindisDBname.TABLE_PLAYLIST +" WHERE "+KindisDBname.COLUMN_PLAYLIST_ID+" = "+id,null);
        if (cursor.moveToFirst()){
            while (cursor.isAfterLast()==false){
                btnPremium.setVisibility(View.GONE);
                cursor.moveToNext();
            }
        }
        db.close();
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm:ss");
        String formattedDate = sdf.format(date);

        if (requestCode == 10001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            System.out.println("onActivityResult: "+responseCode);
            System.out.println("onActivityResult: "+purchaseData);
            System.out.println("onActivityResult: "+dataSignature);

            /*String transIDr = data.getStringExtra("transID");
            String playlistIDr = data.getStringExtra("playlistID");
            String pricer = data.getStringExtra("price");*/

            if (resultCode == RESULT_OK) {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("uid", sessionHelper.getPreferences(getApplicationContext(), "user_id"));
                param.put("token_access", sessionHelper.getPreferences(getApplicationContext(), "token_access"));
                param.put("dev_id", "2");
                param.put("client_id", "xBc3w11");
                param.put("package", "2");
                param.put("trans_id", sessionHelper.getPreferences(getApplicationContext(), "transID"));
                param.put("order_id", sessionHelper.getPreferences(getApplicationContext(), "transID"));
                param.put("order", "["+sessionHelper.getPreferences(getApplicationContext(), "playlistID")+"]");
                param.put("payment_type", "google_play");
                param.put("payment_status", "200");
                param.put("payment_status_msg", "success");
                param.put("price", sessionHelper.getPreferences(getApplicationContext(), "price"));
                param.put("trans_time", formattedDate);

                System.out.println("Response payment: "+param);
                new VolleyHelper().post(ApiHelper.PAYMENT, param, new VolleyHelper.HttpListener<String>() {
                    @Override
                    public void onReceive(boolean status, String message, String response) {
                        System.out.println("Response payment: "+response);
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        if (status){
                            new ResultPayment(Detail.this).execute(response);
                        }
                    }
                });
            }else {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetPrice extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = sessionHelper.getPreferences(getApplicationContext(), "user_id") +
                    "&token_access="+sessionHelper.getPreferences(getApplicationContext(), "token_access") +
                    "&dev_id=2" +
                    "&client_id=xBc3w11"+
                    "&type=playlist7";
            new VolleyHelper().get(ApiHelper.PRICE + url, new VolleyHelper.HttpListener<String>() {
                @Override
                public void onReceive(boolean status, String message, String response) {
                    System.out.println("GetPrice: "+response);
                    if (status){
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("status")){
                                JSONObject result = object.getJSONObject("result");
                                sessionHelper.setPreferences(getApplicationContext(), "price", result.getString("price"));
                                price = result.getInt("price");
                                googleCode = result.getString("google_code");
                                isGetPrice = true;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            return null;
        }
    }
}
