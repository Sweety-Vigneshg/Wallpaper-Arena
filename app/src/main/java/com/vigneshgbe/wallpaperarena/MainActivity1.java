package com.vigneshgbe.wallpaperarena;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class MainActivity1 extends AppCompatActivity {
    ChipNavigationBar nav;
    FragmentManager fragmentManager;
    RelativeLayout relativeLayout,container;
    SQLiteDatabase sqLiteDatabase;
    private long backpressedtime;
    int temp=0;
    Button exit;
    ArrayList<Fragment> fraglist = new ArrayList<Fragment>(3);
    home h = new home();
    trending t = new trending();
    SearchFragment s = new SearchFragment();
    @Override
    public void onBackPressed() {
        if (backpressedtime + 300 > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }
        else {
            Toast.makeText(getBaseContext(), "Press Back Again To Exit", Toast.LENGTH_SHORT).show();
        }
        backpressedtime = System.currentTimeMillis();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        nav = findViewById(R.id.navbar);
        relativeLayout = findViewById(R.id.concont);
        container = findViewById(R.id.cont);
        sqLiteDatabase = getApplicationContext().openOrCreateDatabase("LikedDatabase", Context.MODE_PRIVATE,null);
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Favourites(Id INTEGER PRIMARY KEY AUTOINCREMENT,UrlMedium VARCHAR(255) , Urlhd VARCHAR(255));");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS Refresh(Id INTEGER PRIMARY KEY AUTOINCREMENT,State VARCHAR(255));");
        exit = findViewById(R.id.retry);
        exit = findViewById(R.id.retry);
        if (ConnectionManager.checkConnection(getBaseContext())){
            relativeLayout.setVisibility(View.GONE);
            if (savedInstanceState == null){
                fraglist.add(h);
                fraglist.add(t);
                fraglist.add(s);
                nav.setItemSelected(R.id.homee,true);
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.cont,h)
                        .add(R.id.cont,t)
                        .add(R.id.cont,s)
                        .show(fraglist.get(0))
                        .hide(fraglist.get(1))
                        .hide(fraglist.get(2))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();

            }
            nav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
                @Override
                public void onItemSelected(int id) {
                    if (id == R.id.homee) {
                        fraglist.clear();
                        fraglist.add(h);
                        fraglist.add(t);
                        fraglist.add(s);
                        temp = 1;
                    } else if (id == R.id.trend) {
                        fraglist.clear();
                        fraglist.add(t);
                        fraglist.add(h);
                        fraglist.add(s);
                        temp = 1;
                    } else if (id == R.id.category) {
                        fraglist.clear();
                        fraglist.add(s);
                        fraglist.add(t);
                        fraglist.add(h);
                        temp = 0;
                    }
                    if (temp==1){
                        fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .show(fraglist.get(0))
                                .hide(fraglist.get(1))
                                .hide(fraglist.get(2))
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                    }else if (temp==0){
                        fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .detach(fraglist.get(0))
                                .attach(fraglist.get(0))
                                .show(fraglist.get(0))
                                .hide(fraglist.get(1))
                                .hide(fraglist.get(2))
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                    }

                }
            });
        }
        else {
            relativeLayout.setVisibility(View.VISIBLE);
            nav.setVisibility(View.GONE);
            exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    System.exit(0);
                }
            });
        }
    }
}


