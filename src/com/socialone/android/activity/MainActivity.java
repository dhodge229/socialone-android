package com.socialone.android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
//import com.amazon.device.ads.AdLayout;
//import com.amazon.device.ads.AdTargetingOptions;
import com.amazon.insights.AmazonInsights;
import com.amazon.insights.InsightsCredentials;
import com.amazon.insights.InsightsOptions;
import com.crashlytics.android.Crashlytics;
import com.crittercism.app.Crittercism;
import com.flurry.android.FlurryAgent;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.parse.ParseObject;
import com.socialone.android.R;
import com.socialone.android.fragment.SocialCheckInFragment;
import com.socialone.android.fragment.SocialFragment;
import com.socialone.android.fragment.SocialShareFragment;
import com.socialone.android.utils.BlurTransformation;
import com.socialone.android.utils.OldBlurTransformation;
import com.socialone.android.utils.RoundTransformation;
import com.socialone.android.viewcomponents.NavDrawerItem;
import com.squareup.picasso.Picasso;

import roboguice.RoboGuice;
import roboguice.inject.RoboInjector;

/**
 * Created by david.hodge on 12/18/13.
 */
public class MainActivity extends RoboSherlockFragmentActivity implements DrawerLayout.DrawerListener {

    DrawerLayout mDrawerLayout;
    FrameLayout mContent;
    ImageView userImage;
    ImageView userBackground;

    ActionBarDrawerToggle mActionBarDrawerToggle;
    FragmentManager mfragmentManager;
    NavDrawerItem currentNavigationDrawerItem;
    Context mContext;
    FragmentTransaction ft;
    private AmazonInsights insights;

//    AdLayout adLayout;
//    AdTargetingOptions adTargetingOptions;

    String userShareExtra;

    public static final int NAV_SHARE = R.id.nav_item_share;
    public static final int NAV_ID_TEST = R.id.nav_item_facebook;
    public static final int NAV_ID_TEST_2 = R.id.nav_item_twitter;
    public static final int NAV_ID_TEST_3 = R.id.nav_item_myspace;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InsightsCredentials credentials = AmazonInsights.newCredentials(getString(R.string.amazon_key), getString(R.string.amazon_private_key));
        InsightsOptions options = AmazonInsights.newOptions(true, true);
        insights = AmazonInsights.newInstance(credentials, getApplicationContext(), options);

        setContentView(R.layout.main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_root);
        mContent = (FrameLayout) findViewById(R.id.fragment_container);

        userImage = (ImageView) findViewById(R.id.user_profile_image);
        userBackground = (ImageView) findViewById(R.id.user_background_image);

//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("Park", "BGW");
//        testObject.saveInBackground();

        mContext = this;
        mfragmentManager = getSupportFragmentManager();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.app_name));
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(android.R.color.transparent);

//        // Get the intent that started this activity
//        Intent intent = getIntent();
//        Uri data = intent.getData();
//
//        // Figure out what to do based on the intent type
//        if (intent.getType().indexOf("image/") != -1) {
//            // Handle intents with image data ...
//        } else if (intent.getType().equals("text/plain")) {
//            setContentFragment(NAV_SHARE);
//            userShareExtra = data.getUserInfo();
//        }

        initDrawerLayout();
        getUserInfo();
        if (savedInstanceState == null) {
            setContentFragment(NAV_SHARE);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initDrawerLayout() {
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer,
                R.string.drawer_open, R.string.drawer_close);
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    private void getUserInfo() {
        Picasso.with(mContext)
                .load("http://scontent-a-iad.xx.fbcdn.net/hphotos-prn1/549518_1404352919783769_1484587627_n.jpg")
                .resize(200, 200)
                .centerCrop()
                .transform(new RoundTransformation())
                .into(userImage);

        if (Build.VERSION.SDK_INT >= 14) {
            Picasso.with(mContext)
                    .load("https://scontent-b-iad.xx.fbcdn.net/hphotos-ash3/1001714_1403689593183435_988938356_n.jpg")
                    .resize(400, 400)
                    .centerCrop()
                    .transform(new BlurTransformation(mContext))
                    .into(userBackground);
        } else {
            Picasso.with(mContext)
                    .load("https://scontent-b-iad.xx.fbcdn.net/hphotos-ash3/1001714_1403689593183435_988938356_n.jpg")
                    .resize(400, 400)
                    .centerCrop()
                    .transform(new OldBlurTransformation())
                    .into(userBackground);
        }
    }


    public void navigationDrawerItemClick(View v) {
        setSupportProgressBarIndeterminateVisibility(false);
        switch (v.getId()) {
            default:
                if (currentNavigationDrawerItem != v) {
                    mDrawerLayout.closeDrawers();
                    setContentFragment(v.getId());
                }
        }
    }

    private void setContentFragment(int fragID) {
        RoboInjector injector = RoboGuice.getInjector(this);
        Fragment fragment;
        Bundle args = new Bundle();
        switch (fragID) {
            case NAV_SHARE:
                fragment = injector.getInstance(SocialFragment.class);
                break;
            case NAV_ID_TEST:
                fragment = injector.getInstance(SocialShareFragment.class);
                break;
            case NAV_ID_TEST_2:
                fragment = injector.getInstance(SocialCheckInFragment.class);
                break;
            case NAV_ID_TEST_3:
                fragment = injector.getInstance(SocialFragment.class);
                break;
            default:
                return;
        }

        if (fragment != null) {
            fragment.setArguments(args);
            fragment.setRetainInstance(true);
        }

        ft = mfragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fragment_container, fragment).addToBackStack("tag");
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
//        getFragmentManager().getBackStackEntryCount();
        if (mfragmentManager.getBackStackEntryCount() == 1) {
            this.finish();
        }
        mfragmentManager.popBackStack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(getMenuItem(item))) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
//        mActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
//        mActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private android.view.MenuItem getMenuItem(final MenuItem item) {
        return new android.view.MenuItem() {
            @Override
            public int getItemId() {
                return item.getItemId();
            }

            public boolean isEnabled() {
                return true;
            }

            @Override
            public boolean collapseActionView() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean expandActionView() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public ActionProvider getActionProvider() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public View getActionView() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public char getAlphabeticShortcut() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int getGroupId() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public Drawable getIcon() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Intent getIntent() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public ContextMenu.ContextMenuInfo getMenuInfo() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public char getNumericShortcut() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public int getOrder() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public SubMenu getSubMenu() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public CharSequence getTitle() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public CharSequence getTitleCondensed() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean hasSubMenu() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isActionViewExpanded() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isCheckable() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isChecked() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isVisible() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public android.view.MenuItem setActionProvider(ActionProvider actionProvider) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setActionView(View view) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setActionView(int resId) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setAlphabeticShortcut(char alphaChar) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setCheckable(boolean checkable) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setChecked(boolean checked) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setEnabled(boolean enabled) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setIcon(Drawable icon) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setIcon(int iconRes) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setIntent(Intent intent) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setNumericShortcut(char numericChar) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setShortcut(char numericChar, char alphaChar) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void setShowAsAction(int actionEnum) {
                // TODO Auto-generated method stub

            }

            @Override
            public android.view.MenuItem setShowAsActionFlags(int actionEnum) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setTitle(CharSequence title) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setTitle(int title) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setTitleCondensed(CharSequence title) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public android.view.MenuItem setVisible(boolean visible) {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance().activityStart(this); // Add this method.
        FlurryAgent.onStartSession(this, getString(R.string.furry_id));
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance().activityStop(this); // Add this method.
        FlurryAgent.onEndSession(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.insights.getSessionClient().resumeSession();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.insights.getSessionClient().pauseSession();
        this.insights.getEventClient().submitEvents();
    }

    @Override
    public void onDrawerSlide(View view, float v) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDrawerOpened(View view) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDrawerClosed(View view) {
        navigationDrawerItemClick(view);
    }

    @Override
    public void onDrawerStateChanged(int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}