package com.example.atharva.ocr;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;

public class StartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Button button1;
    private Button button2;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#1DA1F2"));
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_header);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.manual:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_manual()).commit();
                break;

            case R.id.upload:
                startActivity(new Intent(StartActivity.this,UploadActivity.class));
               // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_upload()).commit();
                break;

            case R.id.Scan:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_scan()).commit();
                break;

            case R.id.nav_help:
                startActivity(new Intent(StartActivity.this,Activity_help.class));
                break;

            case R.id.nav_share:
                break;
        }
        drawer.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.activity_upload);
        fragment.onActivityResult(requestCode, resultCode, data);
    }*/
}
