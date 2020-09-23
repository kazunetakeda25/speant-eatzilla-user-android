package com.speant.user.ui;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.speant.user.Common.activities.BaseActivity;
import com.speant.user.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
