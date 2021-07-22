package com.example.if_dose;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.if_dose.db.Aliment;
import com.example.if_dose.db.AlimentDao;
import com.example.if_dose.db.DaoSession;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class MealActivity extends AppCompatActivity {
    private ImageView imageViewBlood;
    private TextView subtitle_header;
    private Button button;
    private EditText name_edit_text,foodQtytype_edit_text,foodQty_edit_text,name_edit_text1;
    private AutoCompleteTextView food_edit_text;
    private TextInputLayout name_text_input,name_text_input1;
    public static final String JSON_ARRAY = "categories";
    public static final String KEY_NAME = "name";
    private final static String TAG = "AddMealActivity";
    private static final String KEY_ID = "id";
    private static final int GALLERY_REQUEST_CODE = 1;
    HashMap<String, Integer> map;
    DaoSession daoSession;
    AlimentDao alimentDao;
    private Spinner categoriesSP;

    private Spinner spn;
    private EditText glucoQtET;
    private JSONArray result;
    private String JsonCatURL;
    private ArrayList<String> categories;
    private String category, name;
    private double quantite, glucide;
    //    private String url ;
    private RequestQueue queue;
    //    private String res ;
    private String host;
    private String port;
    private SharedPreferences sp;
    private ImageView imageView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmeal);
        Toolbar bar =findViewById(R.id.toolbar);
        setSupportActionBar(bar);
        bar.setLogo(R.drawable.shield);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        host= getString(R.string.host_adr);
        imageViewBlood = findViewById(R.id.imageViewBlood);
        subtitle_header = findViewById(R.id.subtitle_header);
        button = findViewById(R.id.buttonLogin);
        port = sp.getString("host_port", getString(R.string.host_port));
        name_edit_text = findViewById(R.id.name_edit_text);
        imageView = findViewById(R.id.imgi);
        food_edit_text = findViewById(R.id.autoCompleteTextView2);
        categories = new ArrayList<String>();
        map=new HashMap<String, Integer>();
        categoriesSP = findViewById(R.id.spinner3);
        daoSession = ((App) getApplication()).getDaoSession();
        alimentDao = daoSession.getAlimentDao();

        ArrayAdapter<String> adaptera = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1,categories);
        food_edit_text.setAdapter(adaptera);
        food_edit_text.setThreshold(1);
        final PopupMenu popupMenu=new PopupMenu(this, imageView);
        getCategories();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(categories);
                popupMenu.getMenu().clear();
                for (int i =0; i < categories.size(); i++) {
                    popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, categories.get(i));
                }


                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        food_edit_text.setText(item.getTitle());

                        System.out.println(item.getTitle() +"++++++++++ttyt+++++++++++++++++++");
                        return true;
                    }
                });



                popupMenu.show();
            }
        });


        foodQty_edit_text = findViewById(R.id.foodQty_edit_text);
        // foodQtytype_edit_text = findViewById(R.id.foodQty_edit_text);
        name_edit_text1 = findViewById(R.id.name_edit_text1);
        name_text_input = findViewById(R.id.name_text_input);
        name_text_input1 = findViewById(R.id.name_text_input1);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.nav_camera2);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_camera:
                        Intent intent = new Intent(MealActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_gallery:
                        Intent intent1 = new Intent(MealActivity.this, ProfileActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.nav_slideshow:
                        Intent intent2 = new Intent(MealActivity.this, ReportActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.nav_manage:
                        Intent intent3 = new Intent(MealActivity.this, GlucoseActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.nav_camera2:
                        Intent intent4 = new Intent(MealActivity.this, MealActivity.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });
    }
    public void onClickAdd(View view) {
        try {
            // retrieve input values
            category = food_edit_text.getText().toString();
            quantite = Double.parseDouble(String.valueOf(foodQty_edit_text.getText()));
            glucide = Double.parseDouble(String.valueOf(name_edit_text1.getText()));
            name = String.valueOf(name_edit_text.getText()).trim();

            // verify if Aliment exist with the same name
            Aliment aliment = alimentDao.queryBuilder()
                    .where(AlimentDao.Properties.Name.eq(name))
                    .unique();

            if (aliment == null) {
                aliment = new Aliment();
            }

            aliment.setName(name);
            aliment.setGlucide(glucide);
            aliment.setQuantite(quantite);
            System.out.println("====>"+category+" "+map.get(category));
            aliment.setCategory_id((long) map.get(category));
            alimentDao.insertOrReplace(aliment);
            Log.i(TAG, "Aliment inserted");
            update();
            addMealSuccess();

        } catch (Exception e) {
//            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            Timber.e(e.getMessage());
            addMealFail();
        }
    }

    private void update() {
        /*// Instantiate the RequestQueue.

        RequestQueue queue = Volley.newRequestQueue(this);


        String url = host+"/api/aliment/" +
                "name=" + name + "&category=" +
                map.get(category) + "&quantite=" + quantite + "&glucide=" + glucide + "&file="+null+"";




// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        System.out.println("SUCCEEEEESSSS");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("FAILURE");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);*/
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://if-diabete.ump.ma/api/postAlimentAndroid";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("nameArabe", name);
            jsonBody.put("nameFr", name);
            jsonBody.put("category_id", map.get(category));
            jsonBody.put("quantite", quantite);
            jsonBody.put("glucide", glucide);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void addMealSuccess() {
        Toast.makeText(getBaseContext(), getString(R.string.add_meal_success)
                , Toast.LENGTH_SHORT).show();

        // clear inputs
        name_edit_text.setText("");
        foodQty_edit_text.setText("");
    }

    public void addMealFail() {
        Toast.makeText(getBaseContext(), getString(R.string.add_meal_fail)
                , Toast.LENGTH_SHORT).show();
    }

    private void getCategories() {

        String url =host + ":" + port +"/api/categories";
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                //Add category name to the array of categories
                                categories.add(obj.get("name").toString());
                                //Add a pair of key (id) value (category) into categories map
                                map.put(obj.get("name").toString(), i + 1);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println("gggggg");


                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

    }

    private void getCategoriesList(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                map.put(json.getString(KEY_NAME), json.getInt(KEY_ID));
                categories.add(json.getString(KEY_NAME));
            } catch (JSONException e) {
//                e.printStackTrace();
                Timber.e(e.getMessage());
            }
        }
        //Setting adapter to show the items in the spinner
        categoriesSP.setAdapter(new ArrayAdapter<String>(MealActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                categories));
    }





    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        startActivityForResult(intent, GALLERY_REQUEST_CODE);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case GALLERY_REQUEST_CODE:
                    //data.getData returns the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    imageView.setImageURI(selectedImage);
                    break;
            }

    }

}



/*String url = host+ ":" + port+"/api/aliment/" + "nameArabe=" + name + "&category_id=" + map.get(category) + "&quantite="
        + quantite + "&glucide=" + glucide + "&nameFr="+ name +"&image="+null+"";

        String ur = host+ ":" + port+"/api/aliment/" + "nameArabe=" + name + "&category_id=" + map.get(category) + "&quantite="
        + quantite + "&glucide=" + glucide + "&nameFr="+ name +"&image="+null+"";

 */