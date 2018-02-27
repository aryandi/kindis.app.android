package co.digdaya.kindis.live.view.fragment.navigationview;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.ParseHtml;
import co.digdaya.kindis.live.helper.VolleyHelper;
import co.digdaya.kindis.live.util.BaseBottomPlayer.BottomPlayerStaticFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class Privacy extends BottomPlayerStaticFragment{
    DrawerLayout drawer;
    ImageButton btnDrawer;
    WebView viewPrivacy;

    public Privacy(DrawerLayout drawer) {
        this.drawer = drawer;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_privacy, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDrawer = (ImageButton) view.findViewById(R.id.btn_drawer);
        viewPrivacy = (WebView) view.findViewById(R.id.view_privacy);

        btnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        getDetaiPrivacy();

        viewPrivacy.setBackgroundColor(Color.TRANSPARENT);
        viewPrivacy.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
    }

    private void getDetaiPrivacy(){
        new VolleyHelper().get(ApiHelper.PRIVACY, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status){
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")){
                            JSONObject result = object.getJSONObject("result");

                            new ParseHtml().parse(result.getString("content"), new ParseHtml.ResultListener<String>() {
                                @Override
                                public void onResult(String html) {
                                    viewPrivacy.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
