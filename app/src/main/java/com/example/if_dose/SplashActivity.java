package com.example.if_dose;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.if_dose.Models.CatMsg;
import com.example.if_dose.Models.User;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import me.leolin.shortcutbadger.ShortcutBadger;
import timber.log.Timber;

import static com.example.if_dose.App.CHANNEL_1_ID;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 1000;
    private static String host, port;
    private final String TAG = "SplashActivity";
    User user;
    private String pass;
    private String url;
    private Context context;
    private Cache cache;
    private Network network;
    private JSONArray patients;
    private SharedPreferences sp;
    private SharedPreferences spGlycemies;
    private SharedPreferences.Editor edit;
    private boolean previouslyStarted;
    private Context msg_context;
    private DatabaseHelper db;
    private JSONObject ratio, demande;
    private CatMsg messag;
    private String pid;
    Intent resultintent;
    private String addrsip = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        msg_context = this;
        db = new DatabaseHelper(this);
        url = host + ":" + port + getString(R.string.urlLogin);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url2 ="http://f9735e658073.ngrok.io/api/checkBd";
        System.out.println(url2);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url2,new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if(response.contains("KO")){

       sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        edit = sp.edit();

        resultintent = new Intent(getApplicationContext(),MessageListActivity.class);
        previouslyStarted = sp.getBoolean(getString(R.string.pref_previously_started), false);
        host = sp.getString("host_url", getString(R.string.host_adr));
        port = sp.getString("host_port", getString(R.string.host_port));
        url = host + ":" + port + getString(R.string.urlLogin);
        pass = sp.getString("pass", "");
        pid=sp.getString("id","0");
        new ATask().execute();
        if(db.getMsgIdserv("mobile")!=0 ||db.getMsgIdserv("web")!=0){
            int l=Math.max(db.getMsgIdserv("mobile"),db.getMsgIdserv("web"));
            getMsgDemand("retio",l);
        }
        if(db.getMsgIdserv("demande")!=0 ){
            getMsgDemand("LastDemande",db.getMsgIdserv("demande"));
        }
        String[] values =host.split("//");
        if (values.length>1)
            addrsip = values[1];
        getRatiofrompusher(resultintent);

                        }if(response.contains("KO")){
                            Intent intent = new Intent(SplashActivity.this, ServerErrorActivity.class);
                            SplashActivity.this.startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent(SplashActivity.this, ServerErrorActivity.class);
                SplashActivity.this.startActivity(intent);
            }
        });
        queue.add(stringRequest);



      /*  sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        edit = sp.edit();
        msg_context = this;
        db = new DatabaseHelper(this);
        resultintent = new Intent(getApplicationContext(), MessageListActivity.class);
        previouslyStarted = sp.getBoolean(getString(R.string.pref_previously_started), false);
        host = sp.getString("host_url", getString(R.string.host_adr));
        port = sp.getString("host_port", getString(R.string.host_port));
        url = host + ":" + port + getString(R.string.urlLogin);
        pass = sp.getString("pass", "");
        pid = sp.getString("id", "0");
        new ATask().execute();
        if (db.getMsgIdserv("mobile") != 0 || db.getMsgIdserv("web") != 0) {
            int l = Math.max(db.getMsgIdserv("mobile"), db.getMsgIdserv("web"));
            getMsgDemand("retio", l);
        }
        if (db.getMsgIdserv("demande") != 0) {
            getMsgDemand("LastDemande", db.getMsgIdserv("demande"));
        }
        String[] values = host.split("//");
        if (values.length > 1)
            addrsip = values[1];
        getRatiofrompusher(resultintent);*/
    }

    private class ATask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (previouslyStarted) {
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
                            url + pass, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                patients = response.getJSONArray("patients");
                                if (patients != null) {
                                    Timber.i("JSON patients object length : " + patients.length());
                                }
                                if (patients.length() >= 1 && patients.getJSONObject(0) != null) {
                                    JSONObject patient = patients.getJSONObject(0);
                                    String id = patient.getString("id");
                                    String firstName = patient.getString("nom");
                                    String lastName = patient.getString("prenom");
                                    double rp = patient.getDouble("ratioPetitDej");
                                    double rd = patient.getDouble("ratioDej");
                                    double rc = patient.getDouble("ratioColl");
                                    double rdi = patient.getDouble("ratioDinnez");
                                    double is = patient.getDouble("IndiceSensibilite");
                                    double obj = patient.getDouble("Objectif");
                                    edit.putString("nom", firstName);
                                    edit.putString("pren", lastName);
                                    edit.putFloat("rp", (float) rp);
                                    edit.putFloat("rd", (float) rd);
                                    edit.putFloat("rc", (float) rc);
                                    edit.putFloat("rdi", (float) rdi);
                                    edit.putFloat("rp", (float) rp);
                                    edit.putFloat("is", (float) is);
                                    edit.putFloat("obj", (float) obj);
                                    edit.putString("id", id);
                                    edit.apply();
                                    Timber.i("Updated patient infos");
                                }

                            } catch (Exception e) {
                                Timber.e(e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Timber.e(error.toString());
                        }
                    });
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
                }
            } catch (Exception e) {
                Timber.e(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Timber.i("previouslyStarted : " + previouslyStarted);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!previouslyStarted) {
                        edit.putString("host_adr", getString(R.string.host_adr));
                        edit.putString("host_port", getString(R.string.host_port));
                        edit.apply();
                        Timber.i("not logged in ):");
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        SplashActivity.this.startActivity(intent);
                        finish();
                    } else {
                        Timber.i("already logged in (:");
                        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                        SplashActivity.this.startActivity(intent);
                        finish();
                    }
                }
            }, SPLASH_DURATION);
        }
    }

    public void getRatiofrompusher(final Intent resultintent) {
        PusherOptions options = new PusherOptions()
                .setUseTLS(false)
                .setCluster("eu")
                .setWsPort(6001)
                .setHost(addrsip);
        com.pusher.client.Pusher pusher = new com.pusher.client.Pusher("882060899073b1574e7e", options);
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);
        Channel channel = pusher.subscribe("ratio." + pid);
        channel.bind("App\\Events\\sendRatio", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                System.out.println("Received event with data:  " + event.toString());
                try {
                    ratio = new JSONObject(event.getData());
                    JSONObject data = ratio.getJSONObject("commentaire");
                    String message = data.getString("contenue");
                    JSONObject RatioS = ratio.getJSONObject("Ratio");
                    String device = RatioS.getString("device");
                    float new_rpd = (float) RatioS.getDouble("ratioPetitDej");
                    float new_rdj = (float) RatioS.getDouble("ratioDej");
                    float new_rc = (float) RatioS.getDouble("ratioColl");
                    float new_rdin = (float) RatioS.getDouble("ratioDinnez");
                    int id = RatioS.getInt("id");
                    boolean nevomsg = false;
                    System.out.println(id + "========================== dratioo idserv");
                    edit = sp.edit();
                    if (new_rpd > sp.getFloat("rp", 0)) {
                        String msg1 = getString(R.string.rpdO);
                        messag = new CatMsg(msg1 + new_rpd, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                        db.addData(messag);
                        edit.putFloat("rp", new_rpd);
                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                        edit.apply();
                        nevomsg = true;
                    } else if (new_rpd < sp.getFloat("rp", 0)) {
                        String msg2 = getString(R.string.rpdR);
                        messag = new CatMsg(getString(R.string.rpdR) + new_rpd, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                        db.addData(messag);
                        edit.putFloat("rp", new_rpd);
                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                        edit.apply();
                        nevomsg = true;
                    }
                    if (new_rdj > sp.getFloat("rd", 0)) {
                        String msg3 = getString(R.string.rdO);
                        ;
                        messag = new CatMsg(msg3 + new_rdj, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                        db.addData(messag);
                        edit.putFloat("rd", new_rdj);
                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                        edit.apply();
                        nevomsg = true;
                    } else if (new_rdj < sp.getFloat("rd", 0)) {
                        String msg4 = getString(R.string.rdjR);
                        messag = new CatMsg(msg4 + new_rdj, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                        db.addData(messag);
                        edit.putFloat("rd", new_rdj);
                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                        edit.apply();
                        nevomsg = true;
                    }
                    if (new_rc > sp.getFloat("rc", 0)) {
                        String msg5 = getString(R.string.rcO);
                        messag = new CatMsg(msg5 + new_rc, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                        db.addData(messag);
                        edit.putFloat("rc", new_rc);
                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                        edit.apply();
                        nevomsg = true;
                    } else if (new_rc < sp.getFloat("rc", 0)) {
                        String msg6 = getString(R.string.rcR);
                        messag = new CatMsg(msg6 + new_rc, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                        db.addData(messag);
                        edit.putFloat("rc", new_rc);
                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                        edit.apply();
                        nevomsg = true;
                    }
                    if (new_rdin > sp.getFloat("rdi", 0)) {
                        String msg7 = getString(R.string.rdinO);
                        messag = new CatMsg(msg7 + new_rdin, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                        db.addData(messag);
                        edit.putFloat("rdi", new_rdin);
                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                        edit.apply();
                        nevomsg = true;
                    } else if (new_rdin < sp.getFloat("rdi", 0)) {
                        String msg8 = getString(R.string.rdinR);
                        messag = new CatMsg(msg8 + new_rdin, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                        db.addData(messag);
                        edit.putFloat("rdi", new_rdin);
                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                        edit.apply();
                        nevomsg = true;
                    }
                    ShortcutBadger.applyCount(getApplicationContext(), sp.getInt("comp", 0)); //for 1.1.4+
                    if (nevomsg) {
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(msg_context);
                        PendingIntent resultPendingintent = PendingIntent.getActivity(msg_context, 1, resultintent, PendingIntent.FLAG_UPDATE_CURRENT);
                        Notification notification = new NotificationCompat.Builder(msg_context, CHANNEL_1_ID)
                                .setSmallIcon(R.drawable.shield)
                                .setContentTitle("Notification")
                                .setContentText(message)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .setAutoCancel(true)
                                .setContentIntent(resultPendingintent)
                                .build();
                        notificationManager.notify(1, notification);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Channel channel2 = pusher.subscribe("demande." + pid);
        channel2.bind("App\\Events\\Demande", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                System.out.println("Received event with data:  " + event.toString());
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(msg_context);
                try {
                    demande = new JSONObject(event.getData());
                    JSONObject data = demande.getJSONObject("demande");
                    String period = data.getString("p\u00e9riode");
                    int id = data.getInt("id");
                    System.out.println(id + "========================== demand idserv");
                    String[] values = period.split("/");
                    String[] d1 = values[0].split("-");
                    long date1 = Integer.parseInt(d1[2]) * 10000 + Integer.parseInt(d1[1]) * 100 + Integer.parseInt(d1[0]);
                    long date2 = 0;
                    String ms = values[0];
                    System.out.println("#######################" + values.length);
                    if (values.length < 2) {
                        date2 = date1;
                    } else {
                        String[] d2 = values[1].split("-");
                        date2 = Integer.parseInt(d2[2]) * 10000 + Integer.parseInt(d2[1]) * 100 + Integer.parseInt(d2[0]);
                        ms = ms + getString(R.string.a) + values[1];
                        if (date1 > date2) {
                            date2 = date1;
                            date1 = Integer.parseInt(d2[2]) * 10000 + Integer.parseInt(d2[1]) * 100 + Integer.parseInt(d2[0]);
                        }
                    }
                    CatMsg demn = new CatMsg(getString(R.string.msgdemand) + ms, getCurrentDate("dd-MM-yyyy HH:mm"), "demande", date1, date2, id);
                    db.addData(demn);
                    edit.putInt("comp", sp.getInt("comp", 0) + 1);
                    edit.apply();
                    ShortcutBadger.applyCount(getApplicationContext(), sp.getInt("comp", 0)); //for 1.1.4+
                    System.out.println("#######################" + date1 + "    " + date2);
                    PendingIntent resultPendingintent = PendingIntent.getActivity(msg_context, 1, resultintent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification notification = new NotificationCompat.Builder(msg_context, CHANNEL_1_ID)
                            .setSmallIcon(R.drawable.shield)
                            .setContentTitle("Notification")
                            .setContentText(getString(R.string.msgdemand) + ms)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setAutoCancel(true)
                            .setContentIntent(resultPendingintent)
                            .build();
                    notificationManager.notify(1, notification);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public String getCurrentDate(String dat) {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat(dat);
        return df.format(c.getTime());
    }

    private void getMsgDemand(final String lastRD, int lastid) {
        String url = host + ":" + port + "/api/" + lastRD + "/" + lastid + "/" + pid;
        //String url ="http://192.168.43.36:8000/api/LastDemande retio/8/1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(msg_context);
                            if (lastRD.equals("retio")) {
                                JSONArray ar = response.getJSONArray("ratio");
                                for (int i = 0; i < ar.length(); i++) {
                                    JSONObject obj = ar.getJSONObject(i);
                                    System.out.println(obj.toString());
                                    String device = obj.getString("device");
                                    float new_rpd = (float) obj.getDouble("ratioPetitDej");
                                    float new_rdj = (float) obj.getDouble("ratioDej");
                                    float new_rc = (float) obj.getDouble("ratioColl");
                                    float new_rdin = (float) obj.getDouble("ratioDinnez");
                                    int id = obj.getInt("id");
                                    System.out.println(id + "  ID  ===================== RATIO   " + new_rpd + new_rdj + new_rc + new_rdin);
                                    edit = sp.edit();
                                    boolean nevomsg = false;
                                    if (new_rpd > sp.getFloat("rp", 0)) {
                                        String msg1 = getString(R.string.rpdO);
                                        messag = new CatMsg(msg1 + new_rpd, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                                        db.addData(messag);
                                        edit.putFloat("rp", new_rpd);
                                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                                        edit.apply();
                                        nevomsg = true;
                                    } else if (new_rpd < sp.getFloat("rp", 0)) {
                                        String msg2 = getString(R.string.rpdR);
                                        messag = new CatMsg(getString(R.string.rpdR) + new_rpd, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                                        db.addData(messag);
                                        edit.putFloat("rp", new_rpd);
                                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                                        edit.apply();
                                        nevomsg = true;
                                    }
                                    if (new_rdj > sp.getFloat("rd", 0)) {
                                        String msg3 = getString(R.string.rdO);
                                        ;
                                        messag = new CatMsg(msg3 + new_rdj, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                                        db.addData(messag);
                                        edit.putFloat("rd", new_rdj);
                                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                                        edit.apply();
                                        nevomsg = true;
                                    } else if (new_rdj < sp.getFloat("rd", 0)) {
                                        String msg4 = getString(R.string.rdjR);
                                        messag = new CatMsg(msg4 + new_rdj, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                                        db.addData(messag);
                                        edit.putFloat("rd", new_rdj);
                                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                                        edit.apply();
                                        nevomsg = true;
                                    }
                                    if (new_rc > sp.getFloat("rc", 0)) {
                                        String msg5 = getString(R.string.rcO);
                                        messag = new CatMsg(msg5 + new_rc, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                                        db.addData(messag);
                                        edit.putFloat("rc", new_rc);
                                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                                        edit.apply();
                                        nevomsg = true;
                                    } else if (new_rc < sp.getFloat("rc", 0)) {
                                        String msg6 = getString(R.string.rcR);
                                        messag = new CatMsg(msg6 + new_rc, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                                        db.addData(messag);
                                        edit.putFloat("rc", new_rc);
                                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                                        edit.apply();
                                        nevomsg = true;
                                    }
                                    if (new_rdin > sp.getFloat("rdi", 0)) {
                                        String msg7 = getString(R.string.rdinO);
                                        messag = new CatMsg(msg7 + new_rdin, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                                        db.addData(messag);
                                        edit.putFloat("rdi", new_rdin);
                                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                                        edit.apply();
                                        nevomsg = true;
                                    } else if (new_rdin < sp.getFloat("rdi", 0)) {
                                        String msg8 = getString(R.string.rdinR);
                                        messag = new CatMsg(msg8 + new_rdin, getCurrentDate("dd-MM-yyyy HH:mm"), device, id);
                                        db.addData(messag);
                                        edit.putFloat("rdi", new_rdin);
                                        edit.putInt("comp", sp.getInt("comp", 0) + 1);
                                        edit.apply();
                                        nevomsg = true;
                                    }
                                    if (nevomsg) {
                                        System.out.println("*********************    NOTI 11111111111111111111111111111    **********************");
                                        //Intent resultintent = new Intent(getApplicationContext(),MessageListActivity.class);
                                        PendingIntent resultPendingintent = PendingIntent.getActivity(msg_context, 1, resultintent, PendingIntent.FLAG_UPDATE_CURRENT);
                                        Notification notification = new NotificationCompat.Builder(msg_context, CHANNEL_1_ID)
                                                .setSmallIcon(R.drawable.shield)
                                                .setContentTitle("Notification")
                                                .setContentText("Changement de ratio")
                                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                                .setAutoCancel(true)
                                                .setContentIntent(resultPendingintent)
                                                .build();
                                        notificationManager.notify(1, notification);
                                    }
                                }
                            }
                            if (lastRD.equals("LastDemande")) {
                                JSONArray arr = response.getJSONArray("demande");
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);
                                    System.out.println("*******************************************");
                                    System.out.println(obj.toString());
                                    String period = obj.getString("p\u00e9riode");
                                    int id = obj.getInt("id");
                                    System.out.println(id + "  ID  ===================== PIRIOD " + period);
                                    String[] values = period.split("/");
                                    String[] d1 = values[0].split("-");
                                    long date1 = Integer.parseInt(d1[2]) * 10000 + Integer.parseInt(d1[1]) * 100 + Integer.parseInt(d1[0]);
                                    long date2 = 0;
                                    String ms = values[0];
                                    System.out.println("#######################" + values.length);
                                    if (values.length < 2) {
                                        date2 = date1;
                                    } else {
                                        String[] d2 = values[1].split("-");
                                        date2 = Integer.parseInt(d2[2]) * 10000 + Integer.parseInt(d2[1]) * 100 + Integer.parseInt(d2[0]);
                                        ms = ms + " a " + values[1];
                                        if (date1 > date2) {
                                            date2 = date1;
                                            date1 = Integer.parseInt(d2[2]) * 10000 + Integer.parseInt(d2[1]) * 100 + Integer.parseInt(d2[0]);
                                        }
                                    }
                                    CatMsg demn = new CatMsg(getString(R.string.msgdemand) + ms, getCurrentDate("dd-MM-yyyy HH:mm"), "demande", date1, date2, id);
                                    db.addData(demn);
                                    edit.putInt("comp", sp.getInt("comp", 0) + 1);
                                    edit.apply();
                                    System.out.println("#######################" + date1 + "    " + date2);
                                    System.out.println("*********************    ONOTI 22222222222222222222222222222 **********************");
                                    PendingIntent resultPendingintent = PendingIntent.getActivity(msg_context, 1, resultintent, PendingIntent.FLAG_UPDATE_CURRENT);
                                    Notification notification = new NotificationCompat.Builder(msg_context, CHANNEL_1_ID)
                                            .setSmallIcon(R.drawable.shield)
                                            .setContentTitle("Notification")
                                            .setContentText(getString(R.string.msgdemand) + ms)
                                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                            .setAutoCancel(true)
                                            .setContentIntent(resultPendingintent)
                                            .build();
                                    notificationManager.notify(1, notification);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ShortcutBadger.applyCount(getApplicationContext(), sp.getInt("comp", 0)); //for 1.1.4+
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println("ggeeeeeeeeeeeeeeeeeeeeeeegggg");
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }


    private void getTestDb1(final String checkBd) {
        String url1 = host + ":" + port + "/api/" + checkBd;
        //String url ="http://192.168.43.36:8000/api/LastDemande retio/8/1";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(msg_context);
                    JSONArray ar = response.getJSONArray("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                    for (int i = 0; i < ar.length(); i++) {
                        JSONObject obj = ar.getJSONObject(i);
                        String device = obj.getString("status");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }


}
