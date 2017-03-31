package co.digdaya.kindis.view.activity.Detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.SessionHelper;
import co.digdaya.kindis.helper.VolleyHelper;

public class More extends AppCompatActivity {
    TextView title;
    ImageButton btnBack;
    RecyclerView listViewMore;

    String url;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        title = (TextView) findViewById(R.id.title);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        listViewMore = (RecyclerView) findViewById(R.id.list_more);

        title.setText(getIntent().getStringExtra("title"));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        type = getIntent().getIntExtra("type", 1);
        switch (type){
            case 2:
                url = "home/album_more?channel_id=1&uid="+new SessionHelper().getPreferences(getApplicationContext(), "user_id")+"&page=0&limit=12";
                break;
        }

        getDataMore();
    }

    private void getDataMore(){
        new VolleyHelper().get(ApiHelper.BASE_URL + url, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                System.out.println("getDataMore : "+response);
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray result = object.getJSONArray("result");

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
