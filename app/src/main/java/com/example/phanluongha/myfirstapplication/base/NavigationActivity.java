package com.example.phanluongha.myfirstapplication.base;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phanluongha.myfirstapplication.MyCalendarActivity;
import com.example.phanluongha.myfirstapplication.MyFavouritesActivity;
import com.example.phanluongha.myfirstapplication.MyInboxActivity;
import com.example.phanluongha.myfirstapplication.NotepadActivity;
import com.example.phanluongha.myfirstapplication.R;
import com.example.phanluongha.myfirstapplication.SignInActivity;
import com.example.phanluongha.myfirstapplication.customview.BadgeView;
import com.example.phanluongha.myfirstapplication.request.JsonParser;
import com.example.phanluongha.myfirstapplication.utils.AnimationShowHideView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by haphan on 3/13/2017.
 */
public class NavigationActivity extends DefaultActivity {
    private TextView txtUsername;
    private ImageView imgShowHidePlant;
    private LinearLayout layoutChildMyPlant;
    private DrawerLayout drawer;
    LinearLayout btnSignIn;
    View imgCountNotification;
    private FrameLayout layoutNoti;
    private TextView txtInbox;
    private boolean isCollapse = false;
    BadgeView badgeView;
    private ImageView flagEN;
    private ImageView flagVN;


    protected void initNavigation() {
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        layoutChildMyPlant = (LinearLayout) findViewById(R.id.layoutChildMyPlant);
        imgShowHidePlant = (ImageView) findViewById(R.id.imgShowHidePlant);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        btnSignIn = (LinearLayout) findViewById(R.id.btnSignIn);

        LinearLayout btnSuggest = (LinearLayout) findViewById(R.id.btnSuggest);
        LinearLayout btnHotdeals = (LinearLayout) findViewById(R.id.btnHotdeals);
        LinearLayout btnMyPlant = (LinearLayout) findViewById(R.id.btnMyPlant);

        LinearLayout btnMyCalendar = (LinearLayout) findViewById(R.id.btnMyCalendar);
        LinearLayout btnMyFavorites = (LinearLayout) findViewById(R.id.btnMyFavorites);
        LinearLayout btnMyInbox = (LinearLayout) findViewById(R.id.btnMyInbox);
        LinearLayout btnMyNote = (LinearLayout) findViewById(R.id.btnMyNote);

        txtInbox = (TextView) findViewById(R.id.txtInbox);
        layoutNoti = (FrameLayout) findViewById(R.id.layoutNoti);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(NavigationActivity.this, SignInActivity.class), 1000);
            }
        });

        btnSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NavigationActivity.this, "btnSuggest", Toast.LENGTH_SHORT).show();
            }
        });
        btnHotdeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NavigationActivity.this, "hot deals", Toast.LENGTH_SHORT).show();
            }
        });
        btnMyPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCollapse = !isCollapse;
                if (isCollapse) {
                    imgShowHidePlant.setImageResource(R.drawable.ic_down);
                    AnimationShowHideView.collapse(layoutChildMyPlant);
                } else {
                    imgShowHidePlant.setImageResource(R.drawable.ic_up);
                    AnimationShowHideView.expand(layoutChildMyPlant);
                }
            }
        });

        btnMyCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavigationActivity.this, MyCalendarActivity.class));
            }
        });
        btnMyFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavigationActivity.this, MyFavouritesActivity.class));
            }
        });
        btnMyInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavigationActivity.this, MyInboxActivity.class));
            }
        });
        btnMyNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavigationActivity.this, NotepadActivity.class));
            }
        });
        flagEN = (ImageView) findViewById(R.id.flagEN);
        flagVN = (ImageView) findViewById(R.id.flagVN);
        String laguage = sharedpreferences.getString("language", "en");
        final AlphaAnimation alphaDimly = new AlphaAnimation(0.3F, 0.3F);
        alphaDimly.setDuration(0);
        alphaDimly.setFillAfter(true);

        final AlphaAnimation alphaClearly = new AlphaAnimation(1F, 1F);
        alphaClearly.setDuration(0);
        alphaClearly.setFillAfter(true);
        if (laguage.equalsIgnoreCase("vi")) {
            flagEN.setEnabled(true);
            flagEN.startAnimation(alphaClearly);
            flagVN.setEnabled(false);
            flagVN.startAnimation(alphaDimly);
        } else {
            flagEN.setEnabled(false);
            flagEN.startAnimation(alphaDimly);
            flagVN.setEnabled(true);
            flagVN.startAnimation(alphaClearly);
        }


        flagEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration config = new Configuration(getResources().getConfiguration());
                config.locale = new Locale("en");
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                flagEN.setEnabled(false);
                flagEN.startAnimation(alphaDimly);
                flagVN.setEnabled(true);
                flagVN.startAnimation(alphaClearly);
                sharedpreferences.edit().putString("language", "en").commit();
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });
        flagVN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration config = new Configuration(getResources().getConfiguration());
                config.locale = new Locale("vi");
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                flagEN.setEnabled(true);
                flagEN.startAnimation(alphaClearly);
                flagVN.setEnabled(false);
                flagVN.startAnimation(alphaDimly);
                sharedpreferences.edit().putString("language", "vi").commit();
                Intent intent = getIntent();
                overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        imgCountNotification = menu.findItem(R.id.actionNotifications).getActionView();
        imgCountNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.END);
            }
        });

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetNumberNoti().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                if (btnSignIn != null) {
                    btnSignIn.setVisibility(View.GONE);
                }
                if (txtUsername != null) {
                    txtUsername.setText(data.getStringExtra("username"));
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    private class GetNumberNoti extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JsonParser jParser = new JsonParser(NavigationActivity.this);
            JSONObject json = jParser.getJSONFromUrl("http://188.166.241.242/api/getnumberofnotificationnotread" + "?idDevice=" + NavigationActivity.this.idDevice + "&token=" + NavigationActivity.this.token);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (json.has("error")) {
                try {
                    Toast.makeText(NavigationActivity.this, json.getJSONObject("error").getString("msg"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    int number = json.getJSONObject("data").getInt("numberNotRead");
                    if (badgeView == null) {
                        badgeView = new BadgeView(NavigationActivity.this, imgCountNotification);
                        badgeView.setBadgePosition(BadgeView.POSITION_TOP_LEFT);

                    }
                    if (number > 0) {
                        badgeView.setText(String.valueOf(number));
                        txtInbox.setText(String.valueOf(number));
                        badgeView.show();
                        layoutNoti.setVisibility(View.VISIBLE);
                    } else {
                        badgeView.hide();
                        layoutNoti.setVisibility(View.INVISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
