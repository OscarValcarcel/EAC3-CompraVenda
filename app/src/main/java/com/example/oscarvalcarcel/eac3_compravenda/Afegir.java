package com.example.oscarvalcarcel.eac3_compravenda;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Afegir extends AppCompatActivity {

    TextView titol;
    TextView preu;
    ImageView imatge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afegir);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titol = (TextView) findViewById(R.id.titol);
        preu = (TextView) findViewById(R.id.preu);
        imatge = (ImageView) findViewById(R.id.imatge);

        setTitle("Sell item");

        imatge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);

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
        // Obtenim els extras
        Bundle extras = data.getExtras();
        // Obtenim la imatge del extra
        Bitmap bmp = (Bitmap) extras.get("data");

        //La establim en el widget
        imatge = (ImageView) findViewById(R.id.imatge);
        imatge.setImageBitmap(bmp);
    }
}
}
