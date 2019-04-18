package com.example.nikhil.Shortnews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class CategoriesActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        setUpToolbar();


        getSupportFragmentManager().beginTransaction().replace(R.id.categories_container,new CategoriesFragment()).commit();


    }

    private void setUpToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.mainToolbar); setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Short News");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }

        }
        return super.onOptionsItemSelected(item);


    }

    public static class CategoriesFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {


                addPreferencesFromResource(R.xml.pref_categories);

        }
    }

}
