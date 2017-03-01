package sangmaneproject.kindis.view.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.PlayerSessionHelper;
import sangmaneproject.kindis.util.DialogPlaylist;
import sangmaneproject.kindis.util.ParseJsonPlaylist;
import sangmaneproject.kindis.view.adapter.AdapterSong;

public class ListSongPlayer extends AppCompatActivity {
    RecyclerView listViewSong;
    RelativeLayout contSingle;
    AdapterSong adapterSong;
    ParseJsonPlaylist parseJsonPlaylist;
    ArrayList<HashMap<String, String>> listSong = new ArrayList<HashMap<String, String>>();
    ArrayList<String> songPlaylist = new ArrayList<>();

    TextView title;
    TextView subTitle;
    RelativeLayout click;
    ImageButton btnMenu;

    String type, json;
    Dialog dialogPlaylis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_song_player);

        title = (TextView) findViewById(R.id.title);
        subTitle = (TextView) findViewById(R.id.subtitle);
        click = (RelativeLayout) findViewById(R.id.adapter_song);
        btnMenu = (ImageButton) findViewById(R.id.btn_menu);

        contSingle = (RelativeLayout) findViewById(R.id.cont_single);
        listViewSong = (RecyclerView) findViewById(R.id.list_songs);
        listViewSong.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        parseJsonPlaylist = new ParseJsonPlaylist(getApplicationContext());
        type = new PlayerSessionHelper().getPreferences(getApplicationContext(), "type");
        json = new PlayerSessionHelper().getPreferences(getApplicationContext(), "json");

        if (new PlayerSessionHelper().getPreferences(getApplicationContext(), "index").equals("1")){
            contSingle.setVisibility(View.VISIBLE);
            title.setText(new PlayerSessionHelper().getPreferences(getApplicationContext(), "title"));
            subTitle.setText(new PlayerSessionHelper().getPreferences(getApplicationContext(), "subtitle"));
            btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DialogPlaylist(ListSongPlayer.this, dialogPlaylis, new PlayerSessionHelper().getPreferences(getApplicationContext(), "uid")).showDialog();
                }
            });
        }else {
            Log.d("kontoljson", json);
            listViewSong.setVisibility(View.VISIBLE);
            listSong = parseJsonPlaylist.getListSong();
            songPlaylist = parseJsonPlaylist.getSongPlaylist();
            adapterSong = new AdapterSong(ListSongPlayer.this, listSong, "list", songPlaylist);
            listViewSong.setAdapter(adapterSong);
            listViewSong.setNestedScrollingEnabled(true);
        }
    }
}
