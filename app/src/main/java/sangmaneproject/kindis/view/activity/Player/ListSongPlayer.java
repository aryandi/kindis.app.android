package sangmaneproject.kindis.view.activity.Player;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.util.BackgroundProses.DialogPlaylist;
import sangmaneproject.kindis.util.BackgroundProses.ParseJsonPlaylist;
import sangmaneproject.kindis.view.adapter.item.AdapterSong;

public class ListSongPlayer extends AppCompatActivity implements View.OnClickListener {
    ParseJsonPlaylist parseJsonPlaylist;
    PlayerSessionHelper playerSessionHelper;

    RecyclerView listViewSong;
    RelativeLayout contSingle;
    AdapterSong adapterSong;

    String type, json;
    Dialog dialogPlaylis;

    ImageButton btnHide;
    TextView labelPlayNext;

    ImageView imgSong;
    ImageButton btnMenuPlay;
    TextView titlePlay;
    TextView subtitlePlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song_player);

        btnHide = (ImageButton) findViewById(R.id.btn_hide);
        labelPlayNext = (TextView) findViewById(R.id.label_play_next);

        contSingle = (RelativeLayout) findViewById(R.id.cont_single);
        listViewSong = (RecyclerView) findViewById(R.id.list_songs);
        listViewSong.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        imgSong = (ImageView) findViewById(R.id.image_song);
        titlePlay = (TextView) findViewById(R.id.title_play);
        subtitlePlay = (TextView) findViewById(R.id.subtitle_play);
        btnMenuPlay = (ImageButton) findViewById(R.id.btn_menu_play);

        btnHide.setOnClickListener(this);
        btnMenuPlay.setOnClickListener(this);

        parseJsonPlaylist = new ParseJsonPlaylist(getApplicationContext());
        playerSessionHelper = new PlayerSessionHelper();

        type = playerSessionHelper.getPreferences(getApplicationContext(), "type");
        json = playerSessionHelper.getPreferences(getApplicationContext(), "json");

        Glide.with(getApplicationContext())
                .load(ApiHelper.BASE_URL_IMAGE+playerSessionHelper.getPreferences(getApplicationContext(), "image"))
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imgSong);

        titlePlay.setText(playerSessionHelper.getPreferences(getApplicationContext(), "title"));
        subtitlePlay.setText(playerSessionHelper.getPreferences(getApplicationContext(), "subtitle"));

        if (new PlayerSessionHelper().getPreferences(getApplicationContext(), "index").equals("1")){
            labelPlayNext.setVisibility(View.GONE);
        }else {
            listViewSong.setVisibility(View.VISIBLE);
            adapterSong = new AdapterSong(ListSongPlayer.this, parseJsonPlaylist.getListSong(), "list", parseJsonPlaylist.getSongPlaylist());
            listViewSong.setAdapter(adapterSong);
            listViewSong.setNestedScrollingEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_hide:
                finish();
                break;
            case R.id.btn_menu_play:
                new DialogPlaylist(ListSongPlayer.this, dialogPlaylis, new PlayerSessionHelper().getPreferences(getApplicationContext(), "uid")).showDialog();
                break;
        }
    }
}
