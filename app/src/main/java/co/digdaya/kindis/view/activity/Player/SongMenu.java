package co.digdaya.kindis.view.activity.Player;

import android.app.Dialog;
import android.content.Intent;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.Constanta;
import co.digdaya.kindis.helper.PlayerSessionHelper;
import co.digdaya.kindis.view.activity.Detail.Detail;
import co.digdaya.kindis.view.activity.Detail.DetailArtist;
import co.digdaya.kindis.view.dialog.DialogSingleMenu;

public class SongMenu extends AppCompatActivity implements View.OnClickListener {
    ImageView image;
    TextView title, subtitle;
    ImageButton btnBack;
    LinearLayout btnAddToPlaylist, btnSaveOffline, btnShare, btnGoToArtist, btnGoToAlbum;

    PlayerSessionHelper playerSessionHelper;
    DialogSingleMenu dialogSingleMenu;
    Dialog dialogPlaylis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_menu);

        image = (ImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnAddToPlaylist = (LinearLayout) findViewById(R.id.btn_add_to_playlist);
        btnSaveOffline = (LinearLayout) findViewById(R.id.btn_save_offline);
        btnShare = (LinearLayout) findViewById(R.id.btn_share);
        btnGoToArtist = (LinearLayout) findViewById(R.id.btn_go_to_artist);
        btnGoToAlbum = (LinearLayout) findViewById(R.id.btn_go_to_album);

        playerSessionHelper = new PlayerSessionHelper();

        initView();

        btnBack.setOnClickListener(this);
        btnAddToPlaylist.setOnClickListener(this);
        btnSaveOffline.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnGoToArtist.setOnClickListener(this);
        btnGoToAlbum.setOnClickListener(this);
    }

    private void initView(){
        Glide.with(getApplicationContext())
                .load(ApiHelper.BASE_URL_IMAGE+getIntent().getStringExtra(Constanta.INTENT_EXTRA_IMAGE))
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(image);
        title.setText(getIntent().getStringExtra(Constanta.INTENT_EXTRA_TITLE));
        subtitle.setText(getIntent().getStringExtra(Constanta.INTENT_EXTRA_SUBTITLE));

        dialogSingleMenu = new DialogSingleMenu(this, dialogPlaylis, getIntent().getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_SINGLE_ID), null, null, true, true);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_add_to_playlist:
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
                break;
            case R.id.btn_go_to_artist:
                intent = new Intent(this, DetailArtist.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uid", playerSessionHelper.getPreferences(getApplicationContext(), "artist_id"));
                intent.putExtra("type", "artist");
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
