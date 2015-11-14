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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MostraItem extends AppCompatActivity {

    ImageView imageview;       //ImageView que contindrà la imatge del article
    TextView textview;         //TextView que contindrà la descripció del article
    DBInterface db;            //Objecte que contindrà la nostra Base de Dades
    String id_article;         //El identificador del article


    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inicialitzem els widgets, la Base de dades i el Fragment que contindrà el mapa
        imageview = (ImageView) findViewById(R.id.show_image);
        textview = (TextView) findViewById(R.id.descripcio);
        db = new DBInterface(this);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapaShowFragment)).getMap();

        //Recuperem el Intent
        Intent intent = getIntent();

        //I tots els seus Extres
        id_article = intent.getStringExtra("id_article");
        String titol = intent.getStringExtra("titol");
        String preu = intent.getStringExtra("preu");
        String descripcio = intent.getStringExtra("descripcio");
        String ruta = intent.getStringExtra("ruta");
        String latitud = intent.getStringExtra("latitud");
        String longitud = intent.getStringExtra("longitud");

        //Creem un LatLong amb la latitud i longitud del nostre article
        LatLng latlng = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));


        //Afegim la camera amb nivell de zoom i la desplacem a la posició que hem creat
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));


        //Canviem el titol de l'activitat
        setTitle(titol + "-" + preu + " €");

        //Posem la descripció al TextView
        textview.setText(descripcio);

        //Recuperem el Uri a travès de la ruta
        Uri uri = Uri.parse(ruta);

        //Obtenim un ContentResolver i li notifiquem la uri obtinguda
        ContentResolver contRes = getContentResolver();
        contRes.notifyChange(uri, null);

        try {
            //Obtenim un bitmap a travès del ContentResolver i el Uri
            Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(contRes, uri);

            //Establim la nostra imatge
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

                //Obrim la nostra BD i eliminem el article en questió
                db.obre();
                db.esborraArticle(Long.valueOf(id_article));
                db.tanca();

                //Establim el resultat com a OK i finalitzem l'Activity
                setResult(RESULT_OK, null);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
