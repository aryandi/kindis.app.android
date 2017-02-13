package sangmaneproject.kindis.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class Search extends AppCompatActivity {
    ImageButton back;
    EditText search;
    InputMethodManager imm;

    LinearLayout contResult, contAlbum, contArtist, contSingle;

    ArrayList<HashMap<String, String>> listAlbum = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listArtist = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> listSingle = new ArrayList<HashMap<String, String>>();

    TextView keywords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        back = (ImageButton) findViewById(R.id.back);
        search = (EditText) findViewById(R.id.input_search);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        contResult = (LinearLayout) findViewById(R.id.cont_result);
        contAlbum = (LinearLayout) findViewById(R.id.cont_album);
        contArtist = (LinearLayout) findViewById(R.id.cont_artist);
        contSingle = (LinearLayout) findViewById(R.id.cont_single);
        keywords = (TextView) findViewById(R.id.keyword);

        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searching(search.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private void searching(final String keyword){
        new VolleyHelper().get(ApiHelper.SEARCH+keyword, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
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
                                    listAlbum.add(map);
                                }
                                contAlbum.setVisibility(View.VISIBLE);
                            }

                            JSONArray single = result.getJSONArray("single");
                            if (single.length()>=1){
                                for (int i=0; i<single.length(); i++){
                                    JSONObject data = album.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("uid", data.getString("uid"));
                                    map.put("title", data.getString("title"));
                                    map.put("description", data.getString("description"));
                                    map.put("image", data.getString("image"));
                                    map.put("file", data.getString("file"));
                                }
                                contSingle.setVisibility(View.VISIBLE);
                            }

                            JSONArray artist = result.getJSONArray("artist");
                            if (artist.length()>=1){
                                for (int i=0; i<artist.length(); i++){
                                    JSONObject data = album.getJSONObject(i);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("uid", data.getString("uid"));
                                    map.put("name", data.getString("name"));
                                    map.put("description", data.getString("description"));
                                    map.put("image", data.getString("image"));
                                }
                                contArtist.setVisibility(View.VISIBLE);
                            }
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
}
