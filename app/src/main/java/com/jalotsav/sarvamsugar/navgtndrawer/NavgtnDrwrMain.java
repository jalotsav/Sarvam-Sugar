/*
 * Copyright 2016 Jalotsav
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.jalotsav.sarvamsugar.navgtndrawer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jalotsav.sarvamsugar.R;
import com.jalotsav.sarvamsugar.common.AppConstants;
import com.jalotsav.sarvamsugar.common.UserSessionManager;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

/**
 * Created by JALOTSAV Dev. on 30/7/16.
 */
public class NavgtnDrwrMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    UserSessionManager session;
    NavigationView mNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lo_drwrlyot_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        session = new UserSessionManager(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setNavigationMenuItemsVisibility();
        mNavigationView.setNavigationItemSelectedListener(this);

        // Select First option on Launch
//        mNavigationView.getMenu().getItem(0).setChecked(true);
//        onNavigationItemSelected(mNavigationView.getMenu().getItem(0));

//        setTitle(getResources().getString(R.string.outstanding_sml));
    }

    private void setNavigationMenuItemsVisibility() {

        if(!session.getUserType().equals(AppConstants.USERTYPE_ADMIN)) {

            mNavigationView.getMenu().getItem(0).getSubMenu().getItem(0).setVisible(false);
            mNavigationView.getMenu().getItem(1).setVisible(false);
            mNavigationView.getMenu().getItem(2).setVisible(false);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        CharSequence toolbarTitle = getString(R.string.app_name);

        switch (item.getItemId()) {
            case R.id.nav_outstanding:

                fragment = new FrgmntOutstandingRprt();
                toolbarTitle = getString(R.string.outstanding_sml);
                break;
            case R.id.nav_pendngsauda:

                fragment = new FrgmntPendingSauda();
                toolbarTitle = getString(R.string.pendngsauda_sml);
                break;
            case R.id.nav_allmasterdtls:

                fragment = new FrgmntAllMasterDtls();
                toolbarTitle = getString(R.string.all_master_dtls_sml);
                break;
            case R.id.nav_godownwisestck:

                fragment = new FrgmntGodownwiseStock();
                toolbarTitle = getString(R.string.godownwise_stock_sml);
                break;
            case R.id.nav_dalalwisesales:

                fragment = new FrgmntDalalwiseSales();
                toolbarTitle = getString(R.string.dalalwisesales_sml);
                break;
            case R.id.nav_dalalwisesauda:

                fragment = new FrgmntDalalwiseSauda();
                toolbarTitle = getString(R.string.dalalwisesauda_sml);
                break;
            case R.id.nav_settings_logout:

                // Show AlertDialog for confirm to Logout
                confirmLogoutAlertDialog();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.framlyot_container, fragment).commit();

            setTitle(toolbarTitle);
        }
        return true;
    }

    // Show AlertDialog for confirm to Logout
    private void confirmLogoutAlertDialog() {

        AlertDialog.Builder alrtDlg = new AlertDialog.Builder(this);
        alrtDlg.setTitle(getString(R.string.logout_sml));
        alrtDlg.setMessage(getString(R.string.logout_alrtdlg_msg));
        alrtDlg.setNegativeButton(getString(R.string.no_sml).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alrtDlg.setPositiveButton(getString(R.string.logout_sml).toUpperCase(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                session.logoutUser();
            }
        });

        alrtDlg.show();
    }

    @Override
    public void setTitle(CharSequence title) {
        mToolbar.setTitle(title);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filters) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
