package com.example.oscarvalcarcel.eac3_compravenda;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Afegir extends AppCompatActivity {

    TextView titol;
    TextView preu;
    ImageView imatge;
    private int REQ_CAMERA = 0;
    private String uploadImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afegir);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titol = (TextView) findViewById(R.id.title);
        preu = (TextView) findViewById(R.id.price);
        imatge = (ImageView) findViewById(R.id.image);

        //setTitle("Sell item");

        imatge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "Items");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
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

                String tempPath = getPath(selectedImageUri, getActivity());
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                imatge = (ImageView) findViewById(R.id.image);
                imatge.setImageBitmap(bm);
                uploadImagePath = tempPath;

            }
        }

    }


