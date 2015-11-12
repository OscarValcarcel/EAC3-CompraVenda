package com.example.oscarvalcarcel.eac3_compravenda;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MostraItem extends AppCompatActivity {

    ImageView imageview;
    TextView textview;
    DBInterface db;
    int id_article;


    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageview = (ImageView)findViewById(R.id.show_image);
        textview=  (TextView)findViewById(R.id.descripcio);
        db = new DBInterface(this);

        mMap = ((MapFragment)getFragmentManager().findFragmentById(
                R.id.mapaShowFragment)).getMap();


        Intent intent = getIntent();
        String titol = intent.getStringExtra("titol");
        String preu = intent.getStringExtra("preu");
        String descripcio = intent.getStringExtra("descripcio");
        String ruta = intent.getStringExtra("ruta");
        String posicio = intent.getStringExtra("posicio");
        id_article = intent.getIntExtra("id_article", 0);



        //String[] parts = posicio.split(",");
       /* double[] parsed = new double[3];
        for (int i = 0; i < 3; i++) {
            parsed[i] = Double.parseDouble(parts[i + 1]);
        }
*/
        //Location loc = new Location("");

        //LatLng latlng = new LatLng(Double.parseDouble(parts[1]),Double.parseDouble(parts[2]));


        //Afegim la camera amb nivell de zoom i la desplacem a la posició que hem creat
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 19));


        //Canviem el titol de l'activitat
        setTitle(titol + "-" + preu);

        textview.setText(descripcio);


        Uri uri=Uri.parse(ruta);

        ContentResolver contRes = getContentResolver();
        contRes.notifyChange(uri, null);

        try {
          Bitmap  bitmap = android.provider.MediaStore.Images.Media.getBitmap(contRes, uri);

            imageview.setImageBitmap(bitmap);

        } catch (Exception e) {
            Toast.makeText(this, "No es pot carregar la imatge" +
                            uri.toString(),
                    Toast.LENGTH_SHORT).show();
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.obre();
                db.esborraArticle(Long.valueOf(id_article));

                setResult(RESULT_OK, null);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
