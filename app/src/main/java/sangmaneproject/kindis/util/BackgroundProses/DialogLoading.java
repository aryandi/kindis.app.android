package sangmaneproject.kindis.util.BackgroundProses;

import android.app.Activity;
import android.app.ProgressDialog;

import sangmaneproject.kindis.R;

/**
 * Created by vincenttp on 3/5/2017.
 */

public class DialogLoading {
    Activity context;
    ProgressDialog loading;

    public DialogLoading(Activity context){
        this.context = context;
        loading = new ProgressDialog(context, R.style.MyTheme);
        loading.setProgressStyle(android.R.style.Widget_Material_Light_ProgressBar_Large_Inverse);
        loading.setCancelable(false);
    }

    public void showLoading(){
        loading.show();
    }

    public void dismisLoading(){
        loading.dismiss();
    }
}
