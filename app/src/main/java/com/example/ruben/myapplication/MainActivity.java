package com.example.ruben.myapplication;

import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.ruben.myapplication.vocabularycard.CardFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private SchemaDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDbHelper = new SchemaDbHelper(this.getApplicationContext());

//        setDB();

        setContentView(R.layout.activity_main);
        initializeStuff();

        // since, NoActionBar was defined in theme, we set toolbar as our action bar.
        setSupportActionBar(toolbar);

        //this basically defines on click on each menu item.
        setUpNavigationView(navigationView);

        //This is for the Hamburger icon.
        drawerToggle = setupDrawerToggle();
        drawerLayout.addDrawerListener(drawerToggle);

        //Inflate the first fragment,this is like home fragment before user selects anything.
        FragmentManager fragmentManager = getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putString("frgText", "Stark");
        Fragment fragment = new CardFragment();
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction().replace(R.id.frameContent,fragment).commit();
        navigationView.setCheckedItem(R.id.nav_houseStark);
        setTitle("House Stark");
    }

    private void setDB(){
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(SchemaContract.KanjiEntry.COLUMN_NAME_CHARACTER, "絵");
        values.put(SchemaContract.KanjiEntry.COLUMN_NAME_HIRAGANA, "え");
        values.put(SchemaContract.KanjiEntry.COLUMN_NAME_KATAKANA, "エ");
        values.put(SchemaContract.KanjiEntry.COLUMN_NAME_ROMAJI, "e");

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(SchemaContract.KanjiEntry.TABLE_NAME, null, values);
    }

    private String getKanji(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                SchemaContract.KanjiEntry._ID,
                SchemaContract.KanjiEntry.COLUMN_NAME_CHARACTER,
                SchemaContract.KanjiEntry.COLUMN_NAME_HIRAGANA,
                SchemaContract.KanjiEntry.COLUMN_NAME_KATAKANA,
                SchemaContract.KanjiEntry.COLUMN_NAME_ROMAJI
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = SchemaContract.KanjiEntry.COLUMN_NAME_CHARACTER + " = ?";
        String[] selectionArgs = { "My Title" };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                SchemaContract.KanjiEntry.COLUMN_NAME_CHARACTER + " DESC";

        Cursor cursor = db.query(
                SchemaContract.KanjiEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null, //selection,                                // The columns for the WHERE clause
                null, //selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null //sortOrder                                 // The sort order
        );

        String kanjis = "";
        while(cursor.moveToNext()) {

            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(SchemaContract.KanjiEntry._ID));
            String itemCharacter = cursor.getString(
                    cursor.getColumnIndexOrThrow(SchemaContract.KanjiEntry.COLUMN_NAME_CHARACTER));
            String itemHiragana = cursor.getString(
                    cursor.getColumnIndexOrThrow(SchemaContract.KanjiEntry.COLUMN_NAME_HIRAGANA));
            String itemKatakana = cursor.getString(
                    cursor.getColumnIndexOrThrow(SchemaContract.KanjiEntry.COLUMN_NAME_KATAKANA));
            String itemRomaji = cursor.getString(
                    cursor.getColumnIndexOrThrow(SchemaContract.KanjiEntry.COLUMN_NAME_ROMAJI));

            kanjis = kanjis +"["+ itemCharacter +" "+ itemHiragana +" "+ itemKatakana +" "+ itemRomaji +"]";
        }
        cursor.close();

        return kanjis;
    }

    void initializeStuff(){
        drawerLayout =(DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationDrawer);
    }

    /**
     * Inflate the fragment according to item clicked in navigation drawer.
     */
    private void setUpNavigationView(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        //replace the current fragment with the new fragment.
                        Fragment selectedFragment = selectDrawerItem(menuItem);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frameContent, selectedFragment).commit();
                        // the current menu item is highlighted in navigation tray.
                        navigationView.setCheckedItem(menuItem.getItemId());
                        setTitle(menuItem.getTitle());
                        //close the drawer when user selects a nav item.
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    /**
     * This method returns the fragment according to navigation item selected.
     */
    public Fragment selectDrawerItem(MenuItem menuItem){
        Fragment fragment = null;
        Bundle bundle = new Bundle();

        switch(menuItem.getItemId()) {
            case R.id.nav_houseStark:

                bundle.putString("frgText", "Stark");
                fragment = new CardFragment();
                fragment.setArguments(bundle);
                break;
            case R.id.nav_houseLannister:

                bundle.putString("frgText", getKanji());
                fragment = new CardFragment();
                fragment.setArguments(bundle);
                break;
            case R.id.nav_houseTargaryen:
                bundle.putString("frgText", "Targaryeb");
                fragment = new CardFragment();
                fragment.setArguments(bundle);
                break;
        }
        return fragment;
    }

    /**
     * This is to setup our Toggle icon. The strings R.string.drawer_open and R.string.drawer close, are for accessibility (generally audio for visually impaired)
     * use only. It is now showed on the screen. While the remaining parameters are required initialize the toggle.
     */
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.drawer_open,R.string.drawer_close);
    }

    /**
     * This makes sure that the action bar home button that is the toggle button, opens or closes the drawer when tapped.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This synchronizes the drawer icon that rotates when the drawer is swiped left or right.
     * Called inside onPostCreate so that it can synchronize the animation again when the Activity is restored.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    /**
     * This is to handle generally orientation changes of your device. It is mandatory to include
     * android:configChanges="keyboardHidden|orientation|screenSize" in your activity tag of the manifest for this to work.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }
}
