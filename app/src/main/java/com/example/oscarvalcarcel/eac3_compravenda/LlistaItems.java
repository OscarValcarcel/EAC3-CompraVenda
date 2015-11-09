package com.example.oscarvalcarcel.eac3_compravenda;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LlistaItems extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private final static int REQ_AFEGIR = 0;                              //Constant amb el número que identifica l'activitat per afegir articles
    List<HashMap<String, String>> llistaArticles = new ArrayList<HashMap<String, String>>();    //ArrayList amb els articles
    SimpleAdapter adaptador = null;                                       //Adaptador que farem servir per emplenar el ListView i que treurà els articles del ArrayList
    HashMap<String, String> element = null;                               //Informació de cada article
    DBInterface db;                                                       //Objecte per gestionar la base de Dades
    ListView llista;                                                      //Widget on carregarem tots els articles


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] clausOrigen = {"foto", "detalls", "descripcio"};

        // Identificadors dels elements de layout_llista corresponents per visualitzar-los
        int[] vistesDesti = {R.id.foto, R.id.detalls};


        adaptador = new SimpleAdapter(getBaseContext(), llistaArticles, R.layout.layout_llista, clausOrigen, vistesDesti);

        //Inicialitzem la listView
        llista = (ListView) findViewById(R.id.llistaItems);
        llista.setAdapter(adaptador);

        //Inicialitzem la nostra base de dades
        db = new DBInterface(this);

        //Cridem a aquest mètode per recuperar els articles de la BD i mostrar-los a la ListView
        obtenirArticlesDeDB();

        //afegim un listener a la llista
        llista.setOnItemClickListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cridem al mètode que crida a l'activitat per afegir articles
                cridaAfegir();
            }


        });

    }

    public void cridaAfegir() {
        Intent intent = new Intent(this, Afegir.class);
        //startActivityForResult(intent,0);
        startActivityForResult(intent, REQ_AFEGIR);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        db.obre();
        //HashMap<String,String> el = llistaArticles.get(parent.getSelectedItemPosition());
        String desc = db.obtenirArticle((long) position + 1).getString(3);
        Snackbar.make(view, desc, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        //db.tanca();
    }

    //Mètode per recuperar els articles guardats a la BD
    public void obtenirArticlesDeDB() {
        //Declarem les variagles per emmagatzemmar les dades que volem de la BD
        String titol, preu, descripcio, ruta_imatge;

        //Obrim la nostre BD
        db.obre();

        //Cridem al mètode per recuperar tots els articles desats a la BD
        Cursor cursor = db.obtenirTotsArticles();

        //Si hi han dades i ens podem moure a la primera posició del cursor
        if (cursor.moveToFirst()) {

            //Anem recorrent el Cursor
            do {

                titol = cursor.getString(1);
                preu = cursor.getString(2);
                descripcio = cursor.getString(3);
                ruta_imatge = cursor.getString(4);

                //Inicialitzem el Hashmap que ens servirà per anar afegint totes les dades que hem anat recuperant de la BD
                element = new HashMap<String, String>();

                //Anem possant les claus i valors al HashMap
                element.put("foto", ruta_imatge);
                element.put("detalls", titol + " - " + preu + " €");
                element.put("descripcio", descripcio);

                //Afegim el HashMap al nostre ArrayList
                llistaArticles.add(element);

            } while (cursor.moveToNext());


        } else Toast.makeText(this, "La BD està buida", Toast.LENGTH_LONG).show();

        // Tanquem la BD
        db.tanca();

        // Notifiquem al ListView que hi ha hagut canvis.
        adaptador.notifyDataSetChanged();


    }
}


