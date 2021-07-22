package com.example.if_dose;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.example.if_dose.Models.Rapport;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.languages.ArabicLigaturizer;
import com.itextpdf.text.pdf.languages.LanguageProcessor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import ru.slybeaver.slycalendarview.SlyCalendarDialog;
import timber.log.Timber;

public class ReportActivity extends AppCompatActivity implements SlyCalendarDialog.Callback  {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    protected static String fName = "", lName = "", alimentsPDej = "", alimentsDej = "",
           alimentsCol = "", alimentsDin = "", sexe="", tel="" ,date ="";
    protected static int age ,count;
    protected static float rd, rp, rc, rdi, is, obj;
   protected static float r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16;
    protected static String c1,c2,c3,c4;
    protected static float gluco0, gluco1, gluco2, gluco3;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final String TAG = "GenerateRapportActivity";
    private String email;

    // pour la gestion des permissions sur android 6+
    private SharedPreferences sp;
    private SharedPreferences spGlycemies;
    private RequestQueue queue;

   // private String format = "yyyy-MM-dd HH:mm:ss";
    //private String format2 = "yyyy-MM-dd";
    private String fileName;
   // private String[] a1, a2, a3, a4;
    private ImageView  imageViewBlood;
    private Button buttonLogin,buttonLogin1,buttonLogi;
    private DatePickerDialog dpd;
    private DatabaseRapports dbrapp;
    private  ArrayList<Rapport> listrapp = new ArrayList <Rapport>() ;
    Context context=this;
    private String addrsip="",host;
    Intent intentRapp;
    TextView badg;

    class Thread3 implements Runnable {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    loadMessg();


                }
            });
        }
    public void loadMessg(){
            recreate();
        }
    }
    public void verifyStoragePermissions(Activity activity,Rapport rap) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            Toast.makeText(this.getBaseContext(), getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Permission denied");
        } else {

            Log.i(TAG, getString(R.string.generateing_pdf));
            printDocumentIText(rap);


            Toast.makeText(this.getBaseContext(), getString(R.string.generateing_pdf), Toast.LENGTH_SHORT).show();
            sp.edit().putString("fileName", fileName).apply();
        }
    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Log.i(TAG, "generating pdf ...");
            printDocumentIText(rap);
            Toast.makeText(this.getBaseContext(), getString(R.string.pdf_printed), Toast.LENGTH_SHORT).show();
            sp.edit().putString("fileName", fileName).apply();

            // permission was granted, yay! Do the
            // contacts-related task you need to do.

        } else {
            Toast.makeText(this.getBaseContext(), getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Permission denied");

            // permission denied, boo! Disable the
            // functionality that depends on this permission.
        }
    }

 */
@Override
public void onBackPressed() {
    Intent intent2 = new Intent(ReportActivity.this, WelcomeActivity.class);
    startActivity(intent2);
}
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendreport);
        Toolbar bar = findViewById(R.id.toolbar);
         badg =findViewById(R.id.comptmsg);

        bar.setLogo(R.drawable.shield);
        setSupportActionBar(bar);
        dbrapp = new DatabaseRapports(this);
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        spGlycemies = getApplicationContext().getSharedPreferences("glycemies", Context.MODE_PRIVATE);
        //getSPProperties(); add to data base
        count = sp.getInt("count" ,0);
        fName = sp.getString("nom", "Nom");
        lName = sp.getString("pren", "Prenom");
        sexe = sp.getString("sexe","-");
        age = sp.getInt("age",0);
        tel = sp.getString("tel","");
        email = sp.getString("medecin_email", getString(R.string.medecin_email));
        host = sp.getString("host_url", getString(R.string.host_adr));
        buttonLogi = findViewById(R.id.buttonLogin3r);
        buttonLogi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_3();

            }
        });
        buttonLogin = findViewById(R.id.buttonLogin2r);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SlyCalendarDialog()
                        .setLang(getString(R.string.lange))
                        .setSingle(false)
                        .setFirstMonday(false)
                        .setCallback(ReportActivity.this)
                        .show(getSupportFragmentManager(), "TAG_SLYCALENDAR");

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                /*SharedPreferences.Editor editt = sp.edit();
                editt.putInt("comp",sp.getInt("comp",0)+1);
                editt.apply();

                 */


            }
        });

        buttonLogin1 = findViewById(R.id.buttonlogin1r);
        buttonLogin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  System.out.println("**************************     " + getSPProperties().getGluco0()+"**"+ getSPProperties().getGluco1()
                        +"**"+getSPProperties().getGluco2() +"*<<<*"+getSPProperties().getAlimentsDin()+333333+dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getAlimentsDin());


               */
                System.out.println(r1+"  "+ r2+"  "+  r3+"  "+  r4+" dj "+  r5+"  "+ r6+"  "+  r7+"  "+  r8+" coloo "+

                        r9+"  "+  r10+"  "+r11+"  "+  r12+" din "+ r13+"  "+  r14+"  "+  r15+"  "+
                        r16+"  "+" ====== gluco "+
                        gluco0+"  "+  gluco1+"  "+  gluco2+"  "+  gluco3+" commentr "+  c1+"  "+  c2+"  "+
                        c3+"  "+ c4+" alment "+ alimentsPDej+"  "+ alimentsDej+"  "+
                        alimentsCol+"  "+  alimentsDin);

                if(dbrapp.getCount(Integer.parseInt(getCurrentDate("yyyyMMdd")))==0){
                    dbrapp.addData(getSPProperties());

                    System.out.println("000000000000000000000000000000000000000000000");

                }

                /*else {
                    System.out.println("**************************     " + dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getGluco0()+"**"+ dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getGluco1()
                            +"**"+dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getGluco2() +"**"+dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getGluco3() );


                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    float k=1;
                    Rapport rapport =new Rapport(34343444,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,k,  "c11111111111",  "c22222222222",
                            "c33333333",  "c4444444", " alim:ents ; PD:ej",  "alim:ents ; D:ej",
                            "alime:nts ; Co:l",  "alim:ent ; D:in",  "-06-2020");
                    dbrapp.updatAliments1(Integer.parseInt(getCurrentDate("yyyyMMdd")),rapport);
                    dbrapp.updatAliments4(Integer.parseInt(getCurrentDate("yyyyMMdd")),rapport);


                }

                 */

               else{
                    System.out.println("**************************     " + dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getGluco0()+"**"+ dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getGluco1()
                            +"**"+dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getGluco2() +"**"+dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getGluco3() );


                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    if(getSPProperties().getGluco0()!=0.0 && dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getGluco0()==0.0 && dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getAlimentsPDej().equals(""))
                    {
                        dbrapp.updatAliments1(Integer.parseInt(getCurrentDate("yyyyMMdd")),getSPProperties());
                        System.out.println("11111111111111111111111111111111111111111111111111");
                    }
                    if(getSPProperties().getGluco1()!=0.0 && dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getGluco1()==0.0  && dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getAlimentsDej().equals(""))
                    {
                        dbrapp.updatAliments2(Integer.parseInt(getCurrentDate("yyyyMMdd")), getSPProperties());
                        System.out.println("222222222222222222222222222222222222222");
                    }
                    if(getSPProperties().getGluco2()!=0.0 && dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getGluco2()==0.0  && dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getAlimentsCol().equals("") )
                    {
                        dbrapp.updatAliments3(Integer.parseInt(getCurrentDate("yyyyMMdd")),getSPProperties());
                        System.out.println("33333333333333333333333333333333333333333333333333333333");
                    }

                    if(getSPProperties().getGluco3()!=0.0 && dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getGluco3()==0.0  && dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getAlimentsDin().equals(""))
                    {
                        System.out.println(getSPProperties().getGluco3());
                        dbrapp.updatAliments4(Integer.parseInt(getCurrentDate("yyyyMMdd")), getSPProperties());
                        System.out.println("444444444444444444444444444444444444444444444");

                    }
                }




                System.out.println(dbrapp.getRapport(Integer.parseInt(getCurrentDate("yyyyMMdd"))).getAlimentsCol());
                  //dbrapp.deleterapp(Integer.parseInt(getCurrentDate("yyyyMMdd")));

                resetSPValues();
                if(dbrapp.getCount(Integer.parseInt(getCurrentDate("yyyyMMdd")))!=0) {
                    Toast.makeText(context, R.string.rappsave, Toast.LENGTH_SHORT).show();
                }else Toast.makeText(context, R.string.rappnosave, Toast.LENGTH_SHORT).show();
        }
        });



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationViewR);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
       bottomNavigationView.setSelectedItemId(R.id.nav_slideshow);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_camera:
                        Intent intent = new Intent(ReportActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_gallery:
                        Intent intent2 = new Intent(ReportActivity.this, ProfileActivity.class);
                        startActivity(intent2);
                        break;

                    case R.id.nav_slideshow:
                        Intent intent3 = new Intent(ReportActivity.this, ReportActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.nav_manage:
                        Intent intent4 = new Intent(ReportActivity.this, GlucoseActivity.class);
                        startActivity(intent4);
                        break;

                    case R.id.nav_camera2:
                        Intent intent1 = new Intent(ReportActivity.this, MealActivity.class);
                        startActivity(intent1);
                        break;

                }


                return false;
            }
        });
        System.out.println("--------------------------"+sp.getInt("comp",0)+"-------------------1---------");
        if(sp.getInt("comp",0)!=0){
            System.out.println("--------------------------"+sp.getInt("comp",0)+"-------------------1---------");
        badg.setText(String.valueOf(sp.getInt("comp", 0)));
           // badg.setText("00");
        badg.setVisibility(View.VISIBLE);}

        String[] values =host.split("//");
        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeee"+values.length);
        if (values.length>1)
            addrsip = values[1];
        System.out.println("--------------------------"+fName+" lnam "+lName+" sex "+sexe+" tle "+tel+"----------------------------");

       /* Thread3 th = new Thread3();
        try {
            th.wait(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        th.run();

        */
        //refresh();

    }
    public Rapport getSPProperties(){

            rd = sp.getFloat("rd", 0);
            rp = sp.getFloat("rp", 0);
            rc = sp.getFloat("rc", 0);
            rdi = sp.getFloat("rdi", 0);
            is = sp.getFloat("is", 0);
            obj = sp.getFloat("obj", 0);

            r1 = spGlycemies.getFloat("glucoAvantRepas0", 0);
            r2 = spGlycemies.getFloat("unitInject0", 0);
            r3 = spGlycemies.getFloat("GlycAp0", 0);
            r4 = spGlycemies.getFloat("confirmDose0", 0);
            c1 = spGlycemies.getString("comment1","");

            r5 = spGlycemies.getFloat("glucoAvantRepas1", 0);
            r6 = spGlycemies.getFloat("unitInject1", 0);
            r7 = spGlycemies.getFloat("GlycAp1", 0);
            r8 = spGlycemies.getFloat("confirmDose1", 0);
            c2 = spGlycemies.getString("comment2","");

            r9  = spGlycemies.getFloat("glucoAvantRepas2", 0);
            r10 = spGlycemies.getFloat("unitInject2", 0);
            r11 = spGlycemies.getFloat("GlycAp2", 0);
            r12 = spGlycemies.getFloat("confirmDose2", 0);
            c3  = spGlycemies.getString("comment3","");

            r13 = spGlycemies.getFloat("glucoAvantRepas3", 0);
            r14 = spGlycemies.getFloat("unitInject3", 0);
            r15 = spGlycemies.getFloat("GlycAp3", 0);
            r16 = spGlycemies.getFloat("confirmDose3", 0);
            c4  = spGlycemies.getString("comment4","");

            alimentsPDej = spGlycemies.getString("aliments0", "-");
            alimentsDej = spGlycemies.getString("aliments1", "-");
            alimentsCol = spGlycemies.getString("aliments2", "-");
            alimentsDin = spGlycemies.getString("aliments3", "-");

            gluco0 = spGlycemies.getFloat("gluco0", 0);
            gluco1 = spGlycemies.getFloat("gluco1", 0);
            gluco2 = spGlycemies.getFloat("gluco2", 0);
            gluco3 = spGlycemies.getFloat("gluco3", 0);


           Rapport rapport =new Rapport(Integer.parseInt(getCurrentDate("yyyyMMdd")),r1,  r2,  r3,  r4,  r5,  r6,  r7,  r8,
                   r9,  r10,  r11,  r12,  r13,  r14,  r15,
                   r16,  rd,  rp,  rc,  rdi,  is,  obj,
                   gluco0,  gluco1,  gluco2,  gluco3,  c1,  c2,
                   c3,  c4, alimentsPDej, alimentsDej,
                   alimentsCol,  alimentsDin,  getCurrentDate("dd-MM-yyyy"));
           System.out.println(alimentsPDej+ alimentsDej+ alimentsCol  +alimentsDin+ "=============================");

           return rapport;

        }

        private void resetSPValues() {

            SharedPreferences.Editor edit = spGlycemies.edit();

            edit.putString("aliments0", "");
            edit.putString("aliments1", "");
            edit.putString("aliments2", "");
            edit.putString("aliments3", "");
            edit.remove("date");

            edit.remove("gluco0");
            edit.remove("gluco1");
            edit.remove("gluco2");
            edit.remove("gluco3");
            edit.remove("comment4");
            edit.remove("GlycAp3");
            edit.remove("unitInject3");
            edit.remove("glucoAvantRepas3");
            edit.remove("confirmDose3");
            edit.remove("comment3");
            edit.remove("GlycAp2");
            edit.remove("unitInject2");
            edit.remove("glucoAvantRepas2");
            edit.remove("confirmDose2");
            edit.remove("comment2");
            edit.remove("GlycAp1");
            edit.remove("unitInject1");
            edit.remove("glucoAvantRepas1");
            edit.remove("confirmDose1");
            edit.remove("comment1");
            edit.remove("GlycAp0");
            edit.remove("unitInject0");
            edit.remove("glucoAvantRepas0");
            edit.remove("confirmDose0");

            edit.apply();

        }
        public void generatePdfV2(Rapport rap) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                verifyStoragePermissions(this,rap);
            }
            else {
                Timber.i(TAG, "bellow M");
                Timber.i(TAG, "generating pdf ...");
                printDocumentIText(rap);
                 Toast.makeText(this.getBaseContext(), "Printed", Toast.LENGTH_SHORT).show();
                sp.edit().putString("fileName", fileName).apply();
            }

        }
        public void printDocumentIText(Rapport rap) {

            //Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL, new BaseColor(33,33,33));
            Font darkBoldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL, new BaseColor(33,33,33));
            Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, new BaseColor(1,57,102));
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD ,new BaseColor(1,87,155));
            Font smallTitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL, new BaseColor(33,33,33));
            Font smallTitleFontB = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, new BaseColor(33,33,33));

            BaseFont myBaseFont = null;
            try {
                myBaseFont = BaseFont.createFont("assets/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            } catch (Exception e) {
                Timber.e("" + e.getMessage());
            }
            Font normalFont = new Font(myBaseFont, 12, Font.NORMAL, new BaseColor(33,33,33));
//        Font normalFont = new Font("Cairo-Regular.ttf", 12, Font.NORMAL);


            FontFactory.getFontImp().defaultEncoding = BaseFont.IDENTITY_H;
            LineSeparator lineSep = new LineSeparator();
            LanguageProcessor al = new ArabicLigaturizer();

            try {

                File pdfFolder = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS);

//            File pdfFolder = new File(Environment.DIRECTORY_DOWNLOADS, "rapports");
//            if (!pdfFolder.exists()) {
//                pdfFolder.mkdir();
//                Log.i(TAG, "Pdf Directory created");
//            }

                fileName = createFileName(rap);
                File pdfFile = new File(pdfFolder, fileName);
//            File pdfFile = new File(Environment.DIRECTORY_DOWNLOADS,fileName);
                boolean created = true;
                if (!pdfFile.exists()) {
                    created = pdfFile.createNewFile();
                }

                if (!created) {

                    Toast.makeText(getBaseContext(), "file not created", Toast.LENGTH_SHORT).show();
                    Timber.i("file not created");
                }
//            File pdfFile = new File(Environment.DIRECTORY_DOWNLOADS, createFileName());
                OutputStream output = new FileOutputStream(pdfFile);
                Document document = new Document(PageSize.A4.rotate());
                PdfWriter.getInstance(document, output);
                document.open();

                // load image
                try {
                    // get input stream
                    InputStream ims = getAssets().open("header_2.png");
                    Bitmap bmp = BitmapFactory.decodeStream(ims);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image image = Image.getInstance(stream.toByteArray());
                    image.setAlignment(Image.ALIGN_CENTER);
//                image.scaleAbsolute(75f, 75f);
                    document.add(image);
                } catch (Exception ex) {
                    Timber.e(ex.getMessage());
                }
                Paragraph title = new Paragraph("Rapport du jour " +rap.getDate(), titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add( Chunk.NEWLINE );

            /*document.add(new Paragraph("Nom : " + fName + " " + lName.toUpperCase(), darkFont));
            document.add(new Paragraph("Sexe :"+ sexe ,darkFont));
            document.add(new Paragraph("Age :"+ age ,darkFont));
            document.add(new Paragraph("Telephone :"+ tel ,darkFont));*/

                PdfPTable profilTable =new PdfPTable(2);
                profilTable.setWidthPercentage(100);
                Paragraph p;

                p = new Paragraph("Nom : " + fName + " " + lName.toUpperCase(), smallTitleFontB);
                profilTable.addCell(getCell(p, PdfPCell.ALIGN_LEFT));
                p = new Paragraph("Ratio de petit dejeuner : " + rap.getRp(), smallTitleFont);
                profilTable.addCell(getCell(p,PdfPCell.ALIGN_RIGHT));
                p = new Paragraph("Sexe :"+ sexe ,smallTitleFontB);
                profilTable.addCell(getCell(p,PdfPCell.ALIGN_LEFT));
                p = new Paragraph("Ratio de dejeuner         : " + rap.getRd(), smallTitleFont);
                profilTable.addCell(getCell(p,PdfPCell.ALIGN_RIGHT));
                p = new Paragraph("Age :"+ age ,smallTitleFontB);
                profilTable.addCell(getCell(p,PdfPCell.ALIGN_LEFT));
                p = new Paragraph("Ratio de collation         : " + rap.getRc(), smallTitleFont);
                profilTable.addCell(getCell(p,PdfPCell.ALIGN_RIGHT));
                p = new Paragraph("Telephone :"+ tel ,smallTitleFontB);
                profilTable.addCell(getCell(p,PdfPCell.ALIGN_LEFT));
                p = new Paragraph("Ratio de dinner             : " + rap.getRdi(), smallTitleFont);
                profilTable.addCell(getCell(p,PdfPCell.ALIGN_RIGHT));
                p = new Paragraph("");
                profilTable.addCell(getCell(p,PdfPCell.ALIGN_LEFT));
                p = new Paragraph("Indice de sensibilite      : " + rap.getIs(), smallTitleFont);
                profilTable.addCell(getCell(p,PdfPCell.ALIGN_RIGHT));
                p = new Paragraph("");
                profilTable.addCell(getCell(p,PdfPCell.ALIGN_LEFT));
                p = new Paragraph("Objectif                         : " + rap.getObj(), smallTitleFont);
                profilTable.addCell(getCell(p,PdfPCell.ALIGN_RIGHT));

                document.add(profilTable);
                // load image
           /*try {
                // get input stream
                InputStream ims = getAssets().open("logo2.png");
                Bitmap bmp = BitmapFactory.decodeStream(ims);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
//                image.scaleAbsolute(75f, 75f);
                document.add(image);
            } catch (Exception ex) {
                Timber.e(ex.getMessage());
            }*/

//            Paragraph title2 = new Paragraph("This is Chapter 2", FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC, new Color(0, 0, 255)));
//            Chapter chapter2 = new Chapter(title2, 2);
//            chapter2.setNumberDepth(0);
//            Paragraph someText = new Paragraph("This is some text");
//            chapter2.add(someText);
//            Paragraph title21 = new Paragraph("This is Section 1 in Chapter 2", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(255, 0, 0)));
//            Section section1 = chapter2.addSection(title21);
//            Paragraph someSectionText = new Paragraph("This is some silly paragraph in a chapter and/or section. It contains some text to test the functionality of Chapters and Section.");
//            section1.add(someSectionText);
//            document.add(Chunk.NEWLINE);

//            document.add(new Chunk(lineSep));

            /*document.add(new Paragraph("Ratio de petit dejeuner : " + rp));
            document.add(new Paragraph("Ratio de dejeuner         : " + rd));
            document.add(new Paragraph("Ratio de collation          : " + rc));
            document.add(new Paragraph("Ratio de dinner             : " + rdi));
            document.add(new Paragraph("Indice de sensibilite      : " + is));
            document.add(new Paragraph("Objective                    : " + obj));*/

                //           document.add(new Chunk(lineSep));
                document.add(Chunk.NEWLINE);

                PdfPTable table = new PdfPTable(8);
                table.setWidths(new float[] { 1, 2, 1,1,1,1,1,2 });
                table.setHeaderRows(1);
                //--------------------------------------------------------------
                // 1st row (header)
                PdfPCell cell;
//            cell = new PdfPCell(new Phrase("Table du jour",bigFont));
//            cell.setColspan(6);
//            cell.setFixedHeight(30);
//            cell.setBorder(Rectangle.NO_BORDER);
//            cell.setVerticalAlignment(Element.ALIGN_CENTER);
//            table.addCell(cell);
                //--------------------------------------------------------------
                // 2nd row
                cell = new PdfPCell(new Phrase("Repas", boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Aliments", boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
//            cell = new PdfPCell(new Phrase("Ratio", boldFont));
                cell = new PdfPCell(new Phrase("Glucide", boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Glycemie avant repas", boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Unitée a injecter", boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Glycemie apres 4 heures", boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Dose confirmé", boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("Remarques", boldFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                //--------------------------------------------------------------
                // 3rd row
                cell = new PdfPCell(new Phrase("Petit dejeuner", darkBoldFont));
                table.addCell(cell);

//            PdfPTable t1 = new PdfPTable(3);
//            t1.setWidths(new float[] { 2, 1 });
                PdfPTable t1 = new PdfPTable(2);
                if (rap.getA1().length >= 1) {
                    PdfPCell ci;
                    for (String c : rap.getA1()) {
                        if (c.isEmpty() || c.equals(" ") || !c.contains(":"))
                            continue;

                        String[] t = c.split(":");

                        ci = new PdfPCell(new Phrase(t[0], normalFont));
                        ci.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        t1.addCell(ci);

                        ci = new PdfPCell(new Phrase(t[1], normalFont));
                        ci.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        t1.addCell(ci);
                    }
                    cell = new PdfPCell(t1);
                } else {
                    cell = new PdfPCell(new Phrase(rap.getAlimentsPDej(), normalFont));
                }
                table.addCell(cell);
//            cell = new PdfPCell(new Phrase("" + rp));
                cell = new PdfPCell(new Phrase("" + rap.getGluco0()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR1()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR2()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR3()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR4()));
                table.addCell(cell);
                (cell = new PdfPCell(new Phrase("" + rap.getC1(), normalFont))).setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                table.addCell(cell);

                //--------------------------------------------------------------
                // 4th row
                cell = new PdfPCell(new Phrase("Dejeuner",darkBoldFont));
                table.addCell(cell);

                PdfPTable t2 = new PdfPTable(2);
                if (rap.getA2().length >= 1) {
                    PdfPCell ci;
                    for (String c : rap.getA2()) {
                        if (c.isEmpty() || c.equals(" ") || !c.contains(":"))
                            continue;

                        String[] t = c.split(":");

                        ci = new PdfPCell(new Phrase(t[0], normalFont));
                        ci.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        t2.addCell(ci);

                        ci = new PdfPCell(new Phrase(t[1], normalFont));
                        ci.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        t2.addCell(ci);
                    }
                    cell = new PdfPCell(t2);
                } else {
                    cell = new PdfPCell(new Phrase(rap.getAlimentsDej(), normalFont));
                }
                table.addCell(cell);
//            cell = new PdfPCell(new Phrase("" + rd));
                cell = new PdfPCell(new Phrase("" + rap.getGluco1()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR5()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR6()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR7()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR8()));
                table.addCell(cell);
                (cell = new PdfPCell(new Phrase("" + rap.getC2(), normalFont))).setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                table.addCell(cell);
                //--------------------------------------------------------------
                // 5th row
                cell = new PdfPCell(new Phrase("Collation", darkBoldFont));
                table.addCell(cell);

                PdfPTable t3 = new PdfPTable(2);
                if (rap.getA3().length >= 1) {
                    PdfPCell ci;
                    for (String c : rap.getA3()) {
                        if (c.isEmpty() || c.equals(" ") || !c.contains(":"))
                            continue;

                        String[] t = c.split(":");

                        ci = new PdfPCell(new Phrase(t[0], normalFont));
                        ci.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        t3.addCell(ci);

                        ci = new PdfPCell(new Phrase(t[1], normalFont));
                        ci.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        t3.addCell(ci);
                    }
                    cell = new PdfPCell(t3);
                } else {
                    cell = new PdfPCell(new Phrase(rap.getAlimentsCol(), normalFont));
                }
                table.addCell(cell);
//            cell = new PdfPCell(new Phrase("" + rc));
                cell = new PdfPCell(new Phrase("" + rap.getGluco2()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR9()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR10()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR11()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR12()));
                table.addCell(cell);
                (cell = new PdfPCell(new Phrase("" + rap.getC3(), normalFont))).setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                table.addCell(cell);
                //--------------------------------------------------------------
                // 6th row
                cell = new PdfPCell(new Phrase("Dinner", darkBoldFont));
                table.addCell(cell);
                PdfPTable t4 = new PdfPTable(2);
                if (rap.getA4().length >= 1) {
                    PdfPCell ci;
                    for (String c : rap.getA4()) {
                        if (c.isEmpty() || c.equals(" ") || !c.contains(":"))
                            continue;

                        String[] t = c.split(":");

                        ci = new PdfPCell(new Phrase(t[0], normalFont));
                        ci.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        t4.addCell(ci);

                        ci = new PdfPCell(new Phrase(t[1], normalFont));
                        ci.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                        t4.addCell(ci);
                    }
                    cell = new PdfPCell(t4);
                } else {
                    cell = new PdfPCell(new Phrase(rap.getAlimentsDin(), normalFont));
                }
                table.addCell(cell);
//            cell = new PdfPCell(new Phrase("" + rdi));
                cell = new PdfPCell(new Phrase("" + rap.getGluco3()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR13()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR14()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR15()));
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("" + rap.getR16()));
                table.addCell(cell);
                (cell = new PdfPCell(new Phrase("" +rap.getC4(), normalFont))).setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
                table.addCell(cell);
                //--------------------------------------------------------------
                table.setTotalWidth(
                        PageSize.A4.rotate().getWidth() - (document.leftMargin() + document.rightMargin()));
                table.setLockedWidth(true);
                document.add(table);
                document.close();

            } catch (Exception e) {
                Timber.e("Exception : " + e.getMessage());
            }

        }


        public String createFileName(Rapport rap) {

            String fileName = "";
            String ext = ".pdf";

            // patient name
            String patientName = fName + " " + lName;

            fileName = patientName + " _ " + rap.getDate() + ext;

            return fileName;
        }

        public String getCurrentDate(String format) {
            //            return DateFormat.getDateTimeInstance().format(new Date())
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.format(c.getTime());
        }
        // a fonction to get a personalized cell to represent the patient infos
        public PdfPCell getCell(Paragraph paragraph, int alignment) {
            PdfPCell cell = new PdfPCell(paragraph);
            cell.setPadding(0);
            cell.setHorizontalAlignment(alignment);
            cell.setBorder(PdfPCell.NO_BORDER);
            return cell;
        }

        public void onClick_3() {
            Intent i = new Intent(this, MessageListActivity.class);
            startActivity(i);
        }

/*
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

        int star =year*10000+(monthOfYear+1)*100+dayOfMonth;
        int end =yearEnd*10000+(monthOfYearEnd+1)*100+dayOfMonthEnd;

        if(star>end){
            star =yearEnd*10000+(monthOfYearEnd+1)*100+dayOfMonthEnd;
            end=year*10000+(monthOfYear+1)*100+dayOfMonth;}

        if (dbrapp.getCountall(star,end) != 0) {
            //ArrayList<Rapport> listrapp = new ArrayList <Rapport>() ;
            listrapp = dbrapp.getAllRapport(star,end);
            int lastrappotr =0;
            for ( int i=0; i<listrapp.size();i++){
                if(i==listrapp.size()-1)
                    lastrappotr=1;

                generatePdfV2(listrapp.get(i));
                System.out.println(listrapp.get(i).getAlimentsCol());
                System.out.println(listrapp.get(i).getA3()[0]);
               //new MyAsyncTask().execute();


               /* Timber.i(alimentsPDej);
                Timber.i(alimentsDej);
                Timber.i(alimentsCol);
                Timber.i(alimentsDin);

                */ /*
            }
        }else Toast.makeText(this,R.string.pasdrapport, Toast.LENGTH_SHORT).show();
    }
    */

    @Override
    public void onCancelled() {

    }

    @Override
    public void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes) {
       if (firstDate != null) {
            String d1 = new SimpleDateFormat("yyyyMMdd",new Locale(getString(R.string.lange))).format(firstDate.getTime());
            long star = Long.parseLong(d1);
            long end;
            if (secondDate == null) {
                end =star;
            } else {
                String d2 = new SimpleDateFormat("yyyyMMdd",new Locale(getString(R.string.lange))).format(secondDate.getTime());
                end = Long.parseLong(d2);
            }





        if (dbrapp.getCountall(star,end) != 0) {
            //ArrayList<Rapport> listrapp = new ArrayList <Rapport>() ;
            listrapp = dbrapp.getAllRapport(star,end);
            int lastrappotr =0;
            for ( int i=0; i<listrapp.size();i++){
                if(i==listrapp.size()-1)
                    lastrappotr=1;

                generatePdfV2(listrapp.get(i));
                System.out.println(listrapp.get(i).getAlimentsCol());
                System.out.println(listrapp.get(i).getA3()[0]);
                //new MyAsyncTask().execute();


               /* Timber.i(alimentsPDej);
                Timber.i(alimentsDej);
                Timber.i(alimentsCol);
                Timber.i(alimentsDin);

                */


             }
             }else Toast.makeText(this,R.string.pasdrapport, Toast.LENGTH_SHORT).show();





        }else Toast.makeText(this, R.string.svpentredat, Toast.LENGTH_SHORT).show();

    }

   /* private void refresh() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        recreate();
                    }
                });
            }
        };
        timer.schedule(doTask,9000);
    }


    */

}
