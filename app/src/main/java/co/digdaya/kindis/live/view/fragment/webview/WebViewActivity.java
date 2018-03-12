package co.digdaya.kindis.live.view.fragment.webview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.digdaya.kindis.live.R;
import co.digdaya.kindis.live.custom.TextViewSemiBold;
import co.digdaya.kindis.live.helper.ApiHelper;
import co.digdaya.kindis.live.helper.ParseHtml;
import co.digdaya.kindis.live.helper.VolleyHelper;

/**
 * Created by ryandzhunter on 12/03/18.
 */

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.btn_drawer)
    ImageButton btnDrawer;
    @BindView(R.id.title)
    TextViewSemiBold textTitle;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.container)
    LinearLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        String title = getIntent().getStringExtra("title");
        textTitle.setText(title);
        if (title.equals("Terms and Agreements")){
            getDetaiPrivacy(ApiHelper.TERMS);
        } else if (title.equals("Privacy Policy")){
            getDetaiPrivacy(ApiHelper.PRIVACY);
        }

    }

    private void getDetaiPrivacy(String url) {
        new VolleyHelper().get(url, new VolleyHelper.HttpListener<String>() {
            @Override
            public void onReceive(boolean status, String message, String response) {
                if (status) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if (object.getBoolean("status")) {
                            JSONObject result = object.getJSONObject("result");

                            new ParseHtml().parse(result.getString("content"), new ParseHtml.ResultListener<String>() {
                                @Override
                                public void onResult(String html) {
                                    webView.loadDataWithBaseURL("", html, "text/html", "UTF-8", "");
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(WebViewActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
