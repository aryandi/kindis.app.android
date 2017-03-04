package sangmaneproject.kindis.view.activity.Account;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import sangmaneproject.kindis.R;

public class ChangeEmail extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    ImageButton btnDrawer;
    ImageButton btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        btnDrawer = (ImageButton) findViewById(R.id.btn_drawer);
        btnMenu = (ImageButton) findViewById(R.id.btn_menu);

        btnDrawer.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_drawer:
                finish();
                break;
            case R.id.btn_menu:
                PopupMenu popup = new PopupMenu(this, btnMenu);
                popup.getMenuInflater().inflate(R.menu.profile, popup.getMenu());
                popup.setOnMenuItemClickListener(this);
                popup.show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
