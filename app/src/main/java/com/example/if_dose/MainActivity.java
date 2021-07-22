package com.example.if_dose;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0; 
    private Button button_login;
    private EditText password;
    private ImageView img_pass;
    private boolean show;
    private Toolbar toolbar;
    JSONArray patients;
    SharedPreferences.Editor edit;
    SharedPreferences sp;
    SharedPreferences spGlycemies;
    private static int age, c=0;
    private static double rd = 1, rp = 1, rc = 1, rdi = 1, is = 1, obj = 1;
    private static String firstName, lastName, sexe, tel;
    private static String host, port;
    private Cache cache;
    private Network network;
    private String id;
    Intent intent;
    private Context context;
    private String pass;
    private String url;
    private boolean previouslyConnected = false;
    private boolean connected = false;
    private boolean passwordError = false;
    private LanguageHelper LanguageHelper = new LanguageHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get the data and append to a list
        DatabaseHelp db = new DatabaseHelp(this);
        Cursor data = db.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
        }
        System.out.println("ffffffffffffffff"+listData);
        if(listData.isEmpty()) db.addData("me");
        LanguageHelper.loadLocale(this.getResources(),getSharedPreferences("Settings", MODE_PRIVATE));

        setContentView(R.layout.activity_main);

            // change la langue de titre
       //ActionBar actionbar =getSupportActionBar ();
       //actionbar.setTitle(getResources().getString(R.string.app_name));

//

        Toolbar bar =findViewById(R.id.toolbar);
        bar.setLogo(R.drawable.shield);
        context = getApplicationContext();
        password = findViewById(R.id.editTextPassword);
        button_login = findViewById(R.id.buttonLogin);
        spGlycemies = context.getSharedPreferences("glycemies", Context.MODE_PRIVATE);
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        edit = sp.edit();
        img_pass = findViewById(R.id.imageView);
        show = false;
        img_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(show){
                    password.setTransformationMethod(null);
                    img_pass.setImageResource(R.drawable.ic_visibility_black_24dp);
                }else{
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    img_pass.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                }
                show = !show;
            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* stoper le login*/
                login();
                /* passer directement vers WelcomeActivity*/
                //startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            }
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        network = new BasicNetwork(new HurlStack());
        host =  sp.getString("host_url", getString(R.string.host_adr));
        port = sp.getString("host_port", getString(R.string.host_port));
        url = host + ":" + port + getString(R.string.urlLogin);

    }

    private void login() {


            Timber.i(getString(R.string.sign));
            passwordError = false;
            intent = new Intent(this, WelcomeActivity.class);
            pass = password.getText().toString();
            RequestQueue queue = new RequestQueue(cache, network);
            queue.start();
            System.out.println(url+pass);
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url + pass, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                patients = response.getJSONArray("patients");

                                if (patients != null) {
                                    Timber.i("JSON patients object length : " + patients.length());
                                }

                                if (patients.length() >= 1 && patients.getJSONObject(0) != null) {
                                    JSONObject patient = patients.getJSONObject(0);
                                    connected = true;
                                    edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);

                                    id = patient.getString("id");
                                    firstName = patient.getString("nom");
                                    lastName = patient.getString("prenom");
                                    if (!patient.isNull("sexe"))
                                        sexe = patient.getString("sexe");
                                    if (!patient.isNull("age"))
                                        age = patient.getInt("age");
                                    if (!patient.isNull("telephone"))
                                        tel = patient.getString("telephone");

                                    if (!patient.isNull("ratioPetitDej"))
                                        rp = patient.getDouble("ratioPetitDej");
                                    if (!patient.isNull("ratioDej"))
                                        rd = patient.getDouble("ratioDej");
                                    if (!patient.isNull("ratioColl"))
                                        rc = patient.getDouble("ratioColl");
                                    if (!patient.isNull("ratioDinnez"))
                                        rdi = patient.getDouble("ratioDinnez");
                                    if (!patient.isNull("IndiceSensibilite"))
                                        is = patient.getDouble("IndiceSensibilite");
                                    if (!patient.isNull("Objectif"))
                                        obj = patient.getDouble("Objectif");

                                    edit.putInt("count",c);
                                    edit.putString("nom", firstName);
                                    edit.putString("pren", lastName);
                                    edit.putString("sexe", sexe);
                                    edit.putInt("age", age);
                                    edit.putString("tel", tel);
                                    edit.putFloat("rp", (float) rp);
                                    edit.putFloat("rd", (float) rd);
                                    edit.putFloat("rc", (float) rc);
                                    edit.putFloat("rdi", (float) rdi);
                                    edit.putFloat("rp", (float) rp);
                                    edit.putFloat("is", (float) is);
                                    edit.putFloat("obj", (float) obj);
                                    edit.putString("id", id);
                                    edit.putString("pass", pass);

                                    edit.commit();
                                    startActivity(intent);
                                } else {
                                    edit.putBoolean(getString(R.string.pref_previously_started), Boolean.FALSE);
                                    edit.commit();
                                }

                            } catch (JSONException e) {
                                Timber.e("JSON Error : " + e.getMessage());
                            } catch (Exception e) {

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Timber.e("Volly Error : " + error.toString());

                            edit.putBoolean(getString(R.string.pref_previously_started), Boolean.FALSE);
                            edit.commit();

                            String message = null;

                            if (error instanceof NetworkError) {
                                message = "Pas de connexion internet...veuillez verifier votre connexion!";
                            } else if (error instanceof ServerError) {
                                message = "Serveur introuvable. veuillez reessayer après!!";
                            } else if (error instanceof AuthFailureError) {
                                message = "Erreur d'authentification!";
                            } else if (error instanceof ParseError) {
                                message = "Mot de passe incorrect!";
                                passwordError = true;
                            } else if (error instanceof NoConnectionError) {
                                message = "Pas de connexion internet...veuillez verifier votre connexion!";
                            } else if (error instanceof TimeoutError) {
                                message = "Délai de connection dépassé!...veuillez verifier votre connexion!";
                            }

//                        NetworkResponse networkResponse = error.networkResponse;
//                        if (networkResponse != null) {
//                            Log.e("Status code", String.valueOf(networkResponse.statusCode));
//                            CharSequence msg = networkResponse.toString();

                            CharSequence msg = message + "" + error.getMessage();
//                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                            showErrorAlert(MainActivity.this, message);

                            Timber.e(msg.toString());
//                        }
                        }

                    });

            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

            if (!validate()) {
                onNotValidInput();
                return;
            }

            button_login.setEnabled(false);

            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.confirme));
            progressDialog.show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            if (connected)
                                onLoginSuccess();
                            else
                                onLoginFail();
                            progressDialog.dismiss();
                        }
                    }, 30000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        Toast.makeText(getBaseContext(), R.string.y_singin, Toast.LENGTH_LONG).show();
        button_login.setEnabled(true);
    }

    public void onLoginFail() {
        button_login.setEnabled(true);
        password.setError(getString(R.string.motpss_inco));
    }

    public void onNotValidInput() {
        Toast.makeText(getBaseContext(), R.string.no_singin, Toast.LENGTH_LONG).show();
        button_login.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String passwd = password.getText().toString();

        if (passwd.isEmpty() || passwd.length() != 8) {
           password.setError(getString(R.string.nmbr_MPaS));
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }

    private void showErrorAlert(Context con, String msg) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(con).create();

            alertDialog.setTitle(getString(R.string.alert_msg));
            alertDialog.setMessage(msg);
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setButton(Dialog.BUTTON_NEUTRAL, getString(R.string.alert_btn_ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            if (!passwordError)
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, getString(R.string.alert_btn_parametres),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                            }
                        });

            alertDialog.show();
        } catch (Exception e) {
            Timber.e("Show Dialog: " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("csdkncnsjsvjsdbjvbjsbjvbs");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (item.getItemId() == R.id.action_langue) {

            Toast.makeText(this ,"langue sélectionnée" ,Toast.LENGTH_SHORT);
            return true;
        }
        switch (item.getItemId()){
            case R.id.langue_AR:

                LanguageHelper.setLocale (this.getResources()," ",getSharedPreferences("Settings", MODE_PRIVATE));
                recreate();
                break;
            case R.id.langue_FR:

                LanguageHelper.setLocale (this.getResources(),"fr",getSharedPreferences("Settings", MODE_PRIVATE));
                recreate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
