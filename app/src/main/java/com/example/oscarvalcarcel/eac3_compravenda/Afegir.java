package com.example.oscarvalcarcel.eac3_compravenda;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Afegir extends AppCompatActivity implements LocationListener {

    ImageView imatge;
    TextView titol;
    TextView preu;
    ImageButton posicio;

    private int REQ_CAMERA = 0;
    private String uploadImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afegir);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inicialitzem els widgets
        titol = (TextView) findViewById(R.id.titol);
        preu = (TextView) findViewById(R.id.preu);
        imatge = (ImageView) findViewById(R.id.imatge);
        posicio = (ImageButton) findViewById(R.id.posicio);
        posicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cridaMapa();
                posicio.setBackgroundColor(Color.parseColor("#A1EFB4"));

            }
        });


       // try {
            LocationManager gestorLoc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //gestorLoc.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, this);
            //gestorLoc.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);

       // }catch (SecurityException s){
       //     Log.e("Error", s.getMessage());
       // }
        //setTitle("Sell item");

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        gestorLoc.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, this);

        imatge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imageNum = 0;

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File items = new File(android.os.Environment.getExternalStorageDirectory(), "Items");
                if (!items.exists()) {
                    items.mkdir();
                }


                String nom_foto = "imatge.jpg";

                File directori_foto = new File(items, nom_foto);
                Log.d("Ruta", items.getAbsolutePath());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(directori_foto));
                //String path =   items.getAbsolutePath();
                startActivityForResult(intent, REQ_CAMERA);

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_afegir_items, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.done) {
            Toast.makeText(this, "Completa totes les dades!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    //Si tot ha sortit bé
    if (RESULT_OK == resultCode) {
        if (requestCode == REQ_CAMERA) {
            //Bitmap bmp = new Bit

            File f = new File(Environment.getExternalStorageDirectory()
                    .toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("Items")) {
                    f = temp;
                    break;
                }
            }

           // Uri u = intent.getData(); // this gonna give you the pic's uri
            // you should convert this to a path (see the link below the code section

        /*
        // Obtenim els extras
        Bundle extras = data.getExtras();
        // Obtenim la imatge del extra
        Bitmap bmp = (Bitmap) extras.get("data");

        //La establim en el widget
        imatge = (ImageView) findViewById(R.id.image);
        imatge.setImageBitmap(bmp);*/


        //-----------------------------------------------------------
        //super.onActivityResult(requestCode, resultCode, data);
        //if (resultCode == Activity.RESULT_OK) {
        //    if (requestCode == REQUEST_CAMERA) {
        //        File f = new File(Environment.getExternalStorageDirectory()
        //                .toString());
        //        for (File temp : f.listFiles()) {
        //            if (temp.getName().equals("temp.jpg")) {
        //                f = temp;
         //               break;
         //           }
         //       }
                try {
                    Bitmap bm;
                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();

                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);

                    bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
                    imatge.setImageBitmap(bm);
                    uploadImagePath = f.getAbsolutePath();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }// else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();

       // When you capture image, in onActivityResult() use that URI to obtain file path:

        String[] projection = { MediaStore.Images.Media.DATA};
        /*try (Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null)) {
            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String capturedImageFilePath = cursor.getString(column_index_data);
        }*/
/*
                String tempPath = getPath(selectedImageUri, getActivity());
                String ruta = getA
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                imatge = (ImageView) findViewById(R.id.image);
                imatge.setImageBitmap(bm);
                uploadImagePath = tempPath;*/

            }
        }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void cridaMapa(){
        Intent intent = new Intent(this,MapsActivity.class);
        //startActivityForResult(intent,0);
        startActivity(intent);
    }

}


