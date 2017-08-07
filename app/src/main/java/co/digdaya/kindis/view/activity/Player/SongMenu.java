package co.digdaya.kindis.view.activity.Player;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import co.digdaya.kindis.R;
import co.digdaya.kindis.helper.ApiHelper;
import co.digdaya.kindis.helper.Constanta;

public class SongMenu extends AppCompatActivity {
    ImageView image;
    TextView title, subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_menu);

        image = (ImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);

        Glide.with(getApplicationContext())
                .load(ApiHelper.BASE_URL_IMAGE+getIntent().getStringExtra(Constanta.INTENT_EXTRA_IMAGE))
                .thumbnail( 0.1f )
                .placeholder(R.drawable.ic_default_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(image);
        title.setText(getIntent().getStringExtra(Constanta.INTENT_EXTRA_TITLE));
        subtitle.setText(getIntent().getStringExtra(Constanta.INTENT_EXTRA_SUBTITLE));
    }
}
