package sangmaneproject.kindis.view.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;
import sangmaneproject.kindis.helper.VolleyHelper;
import sangmaneproject.kindis.util.DialogPlaylist;
import sangmaneproject.kindis.view.adapter.AdapterAlbum;
import sangmaneproject.kindis.view.adapter.AdapterArtist;
import sangmaneproject.kindis.view.adapter.AdapterSong;

public class Search extends AppCompatActivity {
    ImageButton back, clear;
    EditText search;
    InputMethodManager imm;

    LinearLayout contResult, contAlbum, contArtist, contSingle;

    ArrayList<HashMap<String, String>> listAlbum = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listArtist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listSingle = new ArrayList<HashMap<String, String>>();

    RecyclerView listViewAlbum, listViewArtist, listViewSingle;

    AdapterAlbum adapterAlbum;
    AdapterArtist adapterArtist;
    AdapterSong adapterSong;

    TextView keywords;
    Dialog dialogPlaylis;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        back = (ImageButton) findViewById(R.id.back);
        clear = (ImageButton) findViewById(R.id.btn_clear);
        search = (EditText) findViewById(R.id.input_search);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        contResult = (LinearLayout) findViewById(R.id.cont_result);
        contAlbum = (LinearLayout) findViewById(R.id.cont_album);
        contArtist = (LinearLayout) findViewById(R.id.cont_artist);
        contSingle = (LinearLayout) findViewById(R.id.cont_single);
        keywords = (TextView) findViewById(R.id.keyword);

        listViewAlbum = (RecyclerView) findViewById(R.id.list_album);
        listViewArtist = (RecyclerView) findViewById(R.id.list_artist);
        listViewSingle = (RecyclerView) findViewById(R.id.list_single);

        listViewAlbum.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listViewArtist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listViewSingle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

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
                if (charSequence.length()>=1){
                    clear.setVisibility(View.VISIBLE);
                }else {
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

    private void searching(final String keyword){
        new VolleyHelper().get(ApiHelper.SEARCH+keyword, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                loading.dismiss();
                Log.d("searchresponsestatus", ""+status);
                if (status){
                    Log.d("searchresponse", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            contResult.setVisibility(View.VISIBLE);
                            keywords.setText(keyword);
                            JSONObject result = object.getJSONObject("result");

                            JSONArray album = result.getJSONArray("album");
                            if (album.length()>=1){
                                for (int i=0; i<album.length(); i++){
                                    JSONObject data = album.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("uid", data.getString("uid"));
                                    map.put("title", data.getString("title"));
                                    map.put("description", data.getString("description"));
                                    map.put("image", data.getString("image"));
                                    map.put("year", data.getString("year"));
                                    listAlbum.add(map);
                                }
                                contAlbum.setVisibility(View.VISIBLE);
                                adapterAlbum = new AdapterAlbum(getApplicationContext(), listAlbum);
                                listViewAlbum.setAdapter(adapterAlbum);
                                listViewAlbum.setNestedScrollingEnabled(true);
                            }

                            JSONArray single = result.getJSONArray("single");
                            if (single.length()>=1){
                                for (int i=0; i<single.length(); i++){
                                    JSONObject data = single.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("uid", data.getString("uid"));
                                    map.put("title", data.getString("title"));
                                    map.put("image", data.getString("image"));
                                    map.put("file", data.getString("file"));
                                    listSingle.add(map);
                                }
                                contSingle.setVisibility(View.VISIBLE);
                                adapterSong = new AdapterSong(Search.this, listSingle);
                                listViewSingle.setAdapter(adapterSong);
                                listViewSingle.setNestedScrollingEnabled(true);
                                onClickMenuSong();
                            }

                            JSONArray artist = result.getJSONArray("artist");
                            if (artist.length()>=1){
                                for (int i=0; i<artist.length(); i++){
                                    JSONObject data = artist.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("uid", data.getString("uid"));
                                    map.put("name", data.getString("name"));
                                    map.put("description", data.getString("description"));
                                    map.put("image", data.getString("image"));
                                    listArtist.add(map);
                                }
                                contArtist.setVisibility(View.VISIBLE);
                                adapterArtist = new AdapterArtist(getApplicationContext(), listArtist);
                                listViewArtist.setAdapter(adapterArtist);
                                listViewArtist.setNestedScrollingEnabled(true);
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onClickMenuSong(){
        adapterSong.setOnClickMenuListener(new AdapterSong.OnClickMenuListener() {
            @Override
            public void onClick(String uid, ImageButton imageButton) {
                new DialogPlaylist(Search.this, dialogPlaylis, uid).showDialog();
            }
        });
    }
}
