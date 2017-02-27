package sangmaneproject.kindis.view.fragment.detail;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import de.hdodenhof.circleimageview.CircleImageView;
import sangmaneproject.kindis.R;
import sangmaneproject.kindis.helper.ApiHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailMain extends Fragment {
    CircleImageView image;
    TextView title, subTitle;

    String img, ttl, sttl;
    public DetailMain(String img, String ttl, String sttl) {
        this.img = img;
        this.ttl = ttl;
        this.sttl = sttl;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        image = (CircleImageView) view.findViewById(R.id.image);
        title = (TextView) view.findViewById(R.id.title);
        subTitle = (TextView) view.findViewById(R.id.subtitle);

        Glide.with(getContext())
                .load(ApiHelper.BASE_URL_IMAGE+img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(image);
        title.setText(ttl);
        subTitle.setText(sttl);
    }
}
