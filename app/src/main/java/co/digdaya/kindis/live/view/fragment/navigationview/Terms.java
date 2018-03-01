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
public class Terms extends BottomPlayerStaticFragment{
    DrawerLayout drawer;
    ImageButton btnDrawer;
    WebView viewTerms;

    public Terms() {
    }

    public Terms(DrawerLayout drawer) {
        this.drawer = drawer;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terms, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDrawer = (ImageButton) view.findViewById(R.id.btn_drawer);
        viewTerms = (WebView) view.findViewById(R.id.view_terms);

        btnDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        getDetaiTerms();

        viewTerms.setBackgroundColor(Color.TRANSPARENT);
        viewTerms.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
    }

    private void getDetaiTerms(){
        new VolleyHelper().get(ApiHelper.TERMS, new VolleyHelper.HttpListener<String>() {
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
                                    viewTerms.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
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
