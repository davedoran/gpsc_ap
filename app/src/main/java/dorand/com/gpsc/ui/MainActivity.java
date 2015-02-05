package dorand.com.gpsc.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dorand.gpsc.ui.R;

import java.util.Locale;

import dorand.com.gpsc.ui.fragments.BackCountryTrailFragment;
import dorand.com.gpsc.ui.fragments.ClassicTrailFragment;
import dorand.com.gpsc.ui.fragments.ConditionsFragment;
import dorand.com.gpsc.ui.fragments.SkateTrailFragment;

public class MainActivity extends FragmentActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    public static String ROOT_DIR;
    public static Activity MAIN_ACTIVITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ROOT_DIR == null) {
            ROOT_DIR = getDir("gpsc", Context.MODE_PRIVATE).toString();
        }
        MAIN_ACTIVITY = this;

        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        if (title.equals(getString(R.string.ncc_link))) {
            Log.d(getClass().getName(), "NCC Link Clicked!");
            launchNCCWebSite();
        } else if (title.equals(getString(R.string.about_app))) {
            Log.d(getClass().getName(), "About Link Clicked!");
            launchAboutActivity();
        } else if ( title.equals(R.string.trail_map)) {
            Log.d(getClass().getName(), "Trail Map Link Clicked!");
            launchMapActivity();
        }
        return true;
    }

    private void launchNCCWebSite() {
        try {
            Uri uri = Uri.parse(getString(R.string.ncc_url));
            Intent launchNCCWebsite = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(launchNCCWebsite);
        } catch (Exception e) {
            Log.e(getClass().getName(), "Unable to launch NCC", e);
        }
    }

    private void launchAboutActivity() {
        Intent intent = new Intent();
        intent.setClass(this, AboutActivity.class);
        startActivity(intent);
    }

    private void launchMapActivity() {
        Intent intent = new Intent();
        intent.setClass(this, TrailMapsActivity.class);
        startActivity(intent);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment frag = null;
            switch (position) {
                case 0:
                    frag = new ConditionsFragment();
                    break;
                case 1:
                    frag = new ClassicTrailFragment();
                    break;
                case 2:
                    frag = new SkateTrailFragment();
                    break;
                case 3:
                    frag = new BackCountryTrailFragment();
                    break;
            }
            frag.setArguments(new Bundle());
            return frag;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            CharSequence ret = null;

            switch (position) {
                case 0:
                    ret = getString(R.string.title_conditions).toUpperCase(l);
                    break;
                case 1:
                    ret = getString(R.string.title_classic).toUpperCase(l);
                    break;
                case 2:
                    ret = getString(R.string.title_skate).toUpperCase(l);
                    break;
                case 3:
                    ret = getString(R.string.title_backcountry).toUpperCase(l);
                    break;
            }

            return ret;
        }
    }
}
