package co.digdaya.kindis.live.view.activity.Player;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.AnalyticHelper;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.Constanta;
import co.digdaya.kindis.live.helper.PlayerSessionHelper;
import co.digdaya.kindis.live.view.activity.Detail.Detail;
import co.digdaya.kindis.live.view.activity.Detail.DetailArtist;
import co.digdaya.kindis.live.view.dialog.DialogSingleMenu;

public class SongMenu extends AppCompatActivity implements View.OnClickListener {
    ImageView image;
    TextView title, subtitle;
    ImageButton btnBack;
    LinearLayout btnAddToPlaylist, btnSaveOffline, btnShare, btnGoToArtist, btnGoToAlbum;

    PlayerSessionHelper playerSessionHelper;
    DialogSingleMenu dialogSingleMenu;
    Dialog dialogPlaylis;
    private AnalyticHelper analyticHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_menu);

        image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);
        btnBack = findViewById(R.id.btn_back);
        btnAddToPlaylist = findViewById(R.id.btn_add_to_playlist);
        btnSaveOffline = findViewById(R.id.btn_save_offline);
        btnShare = findViewById(R.id.btn_share);
        btnGoToArtist = findViewById(R.id.btn_go_to_artist);
        btnGoToAlbum = findViewById(R.id.btn_go_to_album);

        playerSessionHelper = new PlayerSessionHelper();
        analyticHelper = new AnalyticHelper(this);

        initView();

        btnBack.setOnClickListener(this);
        btnAddToPlaylist.setOnClickListener(this);
        btnSaveOffline.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnGoToArtist.setOnClickListener(this);
        btnGoToAlbum.setOnClickListener(this);
    }

    private void initView() {
        Glide.with(getApplicationContext())
                .load(ApiHelper.BASE_URL_IMAGE + getIntent().getStringExtra(Constanta.INTENT_EXTRA_IMAGE))
                .thumbnail(0.1f)
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(image);
        title.setText(getIntent().getStringExtra(Constanta.INTENT_EXTRA_TITLE));
        subtitle.setText(getIntent().getStringExtra(Constanta.INTENT_EXTRA_SUBTITLE));

        dialogSingleMenu = new DialogSingleMenu(this, dialogPlaylis,
                getIntent().getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_SINGLE_ID),
                null, null, true, true);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_add_to_playlist:
                analyticHelper.playerAction(getIntent().getStringExtra(Constanta.INTENT_EXTRA_ORIGIN),
                        "", getIntent().getStringExtra(Constanta.INTENT_EXTRA_CONTENT_ID),
                        playerSessionHelper.getPreferences(getApplicationContext(), "title"),
                        "null");
                dialogSingleMenu.showDialog();
                break;
            case R.id.btn_save_offline:
                break;
            case R.id.btn_share:
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, playerSessionHelper.getPreferences(getApplicationContext(), "share_link"));
                intent.setType("text/plain");
                startActivity(intent);
                analyticHelper.shareAction(getIntent().getStringExtra(Constanta.INTENT_EXTRA_ORIGIN),
                        getIntent().getStringExtra(Constanta.INTENT_EXTRA_CONTENT_ID),
                        playerSessionHelper.getPreferences(getApplicationContext(), "title"),
                        "");
                break;
            case R.id.btn_go_to_artist:
                intent = new Intent(this, DetailArtist.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", playerSessionHelper.getPreferences(getApplicationContext(), "artist_id"));
                intent.putExtra(Constanta.INTENT_EXTRA_TYPE, "artist");
                startActivity(intent);
                break;
            case R.id.btn_go_to_album:
                intent = new Intent(this, Detail.class);
                intent.putExtra(Constanta.INTENT_EXTRA_TYPE, Constanta.INTENT_EXTRA_VALUE_ALBUM);
                intent.putExtra("uid", playerSessionHelper.getPreferences(getApplicationContext(), Constanta.PLAYERSESSION_ALBUM_ID));
                startActivity(intent);
                break;
        }
    }
}
