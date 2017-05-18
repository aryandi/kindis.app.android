package co.digdaya.kindis.view.activity.Detail;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.digdaya.kindis.R;
import co.digdaya.kindis.databse.KindisDBHelper;
import co.digdaya.kindis.databse.KindisDBname;
import co.digdaya.kindis.helper.Constanta;
import co.digdaya.kindis.model.DataSingleOffline;
import co.digdaya.kindis.util.BaseBottomPlayer.BottomPlayerActivity;
import co.digdaya.kindis.view.adapter.item.AdapterDetailSongOffline;

/**
 * Created by DELL on 5/18/2017.
 */

public class DetailOffline extends BottomPlayerActivity {
    TextView titleDetail, titleToolbar, desc;
    AppBarLayout appBarLayout;
    LinearLayout contFloatingButton;
    RelativeLayout contLabel;
    Toolbar toolbar;
    RecyclerView listViewSong;
    AdapterDetailSongOffline adapterDetailSongOffline;
    List<DataSingleOffline> dataSingleOfflines = new ArrayList<>();

    public DetailOffline() {
        layout = R.layout.activity_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleDetail = (TextView) findViewById(R.id.title_detail);
        titleToolbar = (TextView) findViewById(R.id.title_toolbar);
        desc = (TextView) findViewById(R.id.description);
        appBarLayout = (AppBarLayout) findViewById(R.id.htab_appbar);
        contFloatingButton = (LinearLayout) findViewById(R.id.cont_floating_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contLabel = (RelativeLayout) findViewById(R.id.cont_label);
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
        listViewSong.setNestedScrollingEnabled(false);
        listViewSong.setFocusable(false);

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

        titleToolbar.setText(getIntent().getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_ALBUM));
        titleDetail.setText(getIntent().getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_ALBUM));
        desc.setText(getIntent().getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_DESC));

        getSong(getIntent().getStringExtra(Constanta.INTENT_ACTION_DOWNLOAD_ALBUM_ID));
    }

    private void getSong(String fkID){
        KindisDBHelper kindisDBHelper = new KindisDBHelper(getApplicationContext());
        SQLiteDatabase db = kindisDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "+ KindisDBname.TABLE_SINGLE +" WHERE "+KindisDBname.COLUMN_FK+" = "+fkID,null);
        if (cursor.moveToFirst()){
            while (cursor.isAfterLast()==false){
                System.out.println("DBData: "+cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_TITLE)));
                System.out.println("DBData: "+cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_PATH)));
                DataSingleOffline dataSingleOffline = new DataSingleOffline();
                dataSingleOffline.setUid(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ID)));
                dataSingleOffline.setTitle(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_TITLE)));
                dataSingleOffline.setPath(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_PATH)));
                dataSingleOffline.setImage(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_IMAGE)));
                dataSingleOffline.setAlbum(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ALBUM)));
                dataSingleOffline.setArtist(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ARTIST)));
                dataSingleOffline.setArtist_id(cursor.getString(cursor.getColumnIndex(KindisDBname.COLUMN_ARTIST_ID)));
                dataSingleOfflines.add(dataSingleOffline);
                cursor.moveToNext();
            }
        }
        adapterDetailSongOffline = new AdapterDetailSongOffline(this, dataSingleOfflines);
        listViewSong.setAdapter(adapterDetailSongOffline);
    }
}
