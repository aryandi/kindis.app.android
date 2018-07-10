package co.digdaya.kindis.live.view.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.AnalyticHelper;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.model.DataAlbum;
import co.digdaya.kindis.live.model.DataSingle;
import co.digdaya.kindis.live.model.PlaylistModelSearch;
import co.digdaya.kindis.live.util.BaseBottomPlayer.BottomPlayerActivity;
import co.digdaya.kindis.live.util.SpacingItem.MarginItemHorizontal;
import co.digdaya.kindis.live.view.adapter.item.AdapterAlbumNew;
import co.digdaya.kindis.live.view.adapter.item.AdapterPlaylistSearch;
import co.digdaya.kindis.live.view.adapter.item.AdapterSongHorizontal;
import co.digdaya.kindis.live.view.adapter.item.AdapterArtist;

public class Search extends BottomPlayerActivity {

    ImageButton back, clear;
    EditText search;
    InputMethodManager imm;

    LinearLayout contResult, contAlbum, contArtist, contSingle, contPlaylist;

    ArrayList<HashMap<String, String>> listAlbum = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listArtist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listSingle = new ArrayList<HashMap<String, String>>();

    RecyclerView listViewAlbum, listViewArtist, listViewSingle, listViewPlaylist;

    AdapterAlbumNew adapterAlbum;
    AdapterArtist adapterArtist;
    AdapterSongHorizontal adapterSong;
    AdapterPlaylistSearch adapterPlaylistHorizontal;

    TextView keywords;
    Dialog dialogPlaylis;
    ProgressDialog loading;
    Gson gson;
    private AnalyticHelper analyticHelper;

    public Search() {
        layout = R.layout.activity_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        back = (ImageButton) findViewById(R.id.back);
        clear = (ImageButton) findViewById(R.id.btn_clear);
        search = (EditText) findViewById(R.id.input_search);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        gson = new Gson();

        analyticHelper = new AnalyticHelper(this);

        contResult = (LinearLayout) findViewById(R.id.cont_result);
        contAlbum = (LinearLayout) findViewById(R.id.cont_album);
        contArtist = (LinearLayout) findViewById(R.id.cont_artist);
        contSingle = (LinearLayout) findViewById(R.id.cont_single);
        contPlaylist = (LinearLayout) findViewById(R.id.cont_playlist);
        keywords = (TextView) findViewById(R.id.keyword);

        listViewAlbum = (RecyclerView) findViewById(R.id.list_album);
        listViewArtist = (RecyclerView) findViewById(R.id.list_artist);
        listViewSingle = (RecyclerView) findViewById(R.id.list_single);
        listViewPlaylist = (RecyclerView) findViewById(R.id.list_playlist);

        listViewAlbum.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listViewArtist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listViewSingle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listViewPlaylist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        listViewAlbum.addItemDecoration(new MarginItemHorizontal(Search.this));
        listViewArtist.addItemDecoration(new MarginItemHorizontal(Search.this));
        listViewSingle.addItemDecoration(new MarginItemHorizontal(Search.this));
        listViewPlaylist.addItemDecoration(new MarginItemHorizontal(Search.this));

        loading = new ProgressDialog(Search.this, R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 1) {
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    loading.show();
                    contAlbum.setVisibility(View.GONE);
                    contArtist.setVisibility(View.GONE);
                    contSingle.setVisibility(View.GONE);

                    listAlbum.clear();
                    listArtist.clear();
                    listSingle.clear();

                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    searching(search.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    private void searching(final String keyword) {
        new VolleyHelper().get(ApiHelper.SEARCH + keyword, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                Log.d("searchresponsestatus", "" + status);
                if (status) {
                    Log.d("searchresponse", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
                            analyticHelper.searchAction(keyword, "true");
                            contResult.setVisibility(View.VISIBLE);
                            keywords.setText(keyword);
                            JSONObject result = object.getJSONObject("result");
                            String json = result.toString();

                            JSONArray playlist = result.getJSONArray("playlist");
                            if (playlist.length() >= 1) {
                                contPlaylist.setVisibility(View.VISIBLE);
                                PlaylistModelSearch dataPlaylist = gson.fromJson(json, PlaylistModelSearch.class);
                                adapterPlaylistHorizontal = new AdapterPlaylistSearch(Search.this, dataPlaylist);
                                listViewPlaylist.setAdapter(adapterPlaylistHorizontal);
                                listViewPlaylist.setNestedScrollingEnabled(true);
                            }

                            JSONArray album = result.getJSONArray("album");
                            if (album.length() >= 1) {
                                final DataAlbum dataAlbum = gson.fromJson(json, DataAlbum.class);
                                contAlbum.setVisibility(View.VISIBLE);
                                adapterAlbum = new AdapterAlbumNew(Search.this, dataAlbum, 2, new AdapterAlbumNew.RowClickListener() {
                                    @Override
                                    public void onRowClick(int position) {
                                        DataAlbum.Data data = dataAlbum.album.get(position);
                                        analyticHelper.contentClick("search", data.uid, "album",data.title, "null", "search");
                                    }
                                });
                                listViewAlbum.setAdapter(adapterAlbum);
                                listViewAlbum.setNestedScrollingEnabled(true);
                            }

                            JSONArray single = result.getJSONArray("single");
                            if (single.length() >= 1) {
                                final DataSingle dataSingle = gson.fromJson(json, DataSingle.class);

                                adapterSong = new AdapterSongHorizontal(Search.this, dataSingle, 1, new AdapterSongHorizontal.RowClickListener() {
                                    @Override
                                    public void onRowClick(int position) {
                                        DataSingle.Data data = dataSingle.single.get(position);
                                        analyticHelper.contentClick("search", data.uid, "single",data.title, "null", "search");
                                    }
                                });
                                contSingle.setVisibility(View.VISIBLE);
                                listViewSingle.setAdapter(adapterSong);
                                listViewSingle.setNestedScrollingEnabled(true);
                                //onClickMenuSong();
                            }

                            JSONArray artist = result.getJSONArray("artist");
                            if (artist.length() >= 1) {
                                for (int i = 0; i < artist.length(); i++) {
                                    JSONObject data = artist.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("uid", data.getString("uid"));
                                    map.put("name", data.getString("name"));
                                    map.put("image", data.getString("image"));
                                    listArtist.add(map);
                                }
                                contArtist.setVisibility(View.VISIBLE);
                                adapterArtist = new AdapterArtist(getApplicationContext(), listArtist);
                                listViewArtist.setAdapter(adapterArtist);
                                listViewArtist.setNestedScrollingEnabled(true);
                            }
                        } else {
                            analyticHelper.searchAction(keyword, "false");
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        analyticHelper.searchAction(keyword, "false");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    analyticHelper.searchAction(keyword, "false");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
