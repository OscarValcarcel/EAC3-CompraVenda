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

public class LlistaItems extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


    private final static int REQ_AFEGIR = 0;                              //Constant amb el número que identifica l'activitat per afegir articles
    private final static int REQ_MOSTRAR = 1;                             //Constant amb el número que identifica l'activitat mostrar articles
    List<HashMap<String, String>> llistaArticles = new ArrayList<HashMap<String, String>>();    //ArrayList amb els articles
    SimpleAdapter adaptador = null;                                       //Adaptador que farem servir per emplenar el ListView i que treurà els articles del ArrayList
    HashMap<String, String> element = null;                               //Informació de cada article
    DBInterface db;                                                       //Objecte per gestionar la base de Dades
    ListView llista;                                                      //Widget on carregarem tots els articles
    int nombreArticles;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] clausOrigen = {"ruta", "detalls", "descripcio"};

        // Identificadors dels elements de layout_llista corresponents per visualitzar-los
        int[] vistesDesti = {R.id.foto, R.id.detalls};


        adaptador = new SimpleAdapter(getBaseContext(), llistaArticles, R.layout.layout_llista, clausOrigen, vistesDesti);

        //Inicialitzem la listView i li lliguem un adaptador
        llista = (ListView) findViewById(R.id.llistaItems);
        llista.setAdapter(adaptador);

        //Inicialitzem la nostra base de dades
        db = new DBInterface(this);

        obtenirArticlesDeDB();


        //afegim els listeners per als items de la llista
        llista.setOnItemClickListener(this);
        llista.setOnItemLongClickListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cridem al mètode que crida a l'activitat per afegir articles
                cridaAfegir();
            }

        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Recuperem el nostre article
        element = llistaArticles.get(position);

        //Mostrem la descripcio al snackbar
        Snackbar.make(view, element.get("descripcio"), Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        //Recuperem el nostre article
        HashMap<String, String> element = llistaArticles.get(position);

      /*  //I tots els seus atributs
        String titol = element.get("titol");
        String preu = element.get("preu");
        String descripcio = element.get("descripcio");
        String ruta = element.get("ruta");
        String longitud = element.get("longitud");
        String latitud = element.get("latitud");
*/

        //Creem un altre intent que utilitzarem per arrancar l'activitat de mostrar Articles
        Intent intent = new Intent(this, MostraItem.class);

        //Possem totes les dades que recuperem del element en el intent que utilitzarem per arrancar l'activitat de mostrar el articles.
        intent.putExtra("titol", element.get("titol"));
        intent.putExtra("preu", element.get("preu"));
        intent.putExtra("descripcio", element.get("descripcio"));
        intent.putExtra("ruta", element.get("ruta"));
        intent.putExtra("id_article", element.get("id_article"));//El id del article dintre de la bd
        intent.putExtra("longitud", element.get("longitud"));
        intent.putExtra("latitud", element.get("latitud"));

        //Iniciem l'activitat per Mostrar els articles
        startActivityForResult(intent, REQ_MOSTRAR);


        return true;
    }


    //Mètode per recuperar els articles guardats a la BD
    public void obtenirArticlesDeDB() {
        //Declarem les variagles per emmagatzemmar les dades que volem de la BD
        String id_article, titol, preu, descripcio, ruta_imatge, latitud, longitud;

        //Obrim la nostre BD
        db.obre();

        //Cridem al mètode per recuperar tots els articles desats a la BD
        Cursor cursor = db.obtenirTotsArticles();

        /*//Recuperem quants articles tenim guardats a la BD
        nombreArticles = cursor.getCount();*/

        //Si hi han dades i ens podem moure a la primera posició del cursor
        if (cursor.moveToFirst()) {

            //Anem recorrent el Cursor i desant les dades
            do {

                id_article = Integer.toString(cursor.getInt(0));
                titol = cursor.getString(1);
                preu = cursor.getString(2);
                descripcio = cursor.getString(3);
                ruta_imatge = cursor.getString(4);
                longitud = cursor.getString(5);
                latitud = cursor.getString(6);

                //Inicialitzem el Hashmap que ens servirà per anar afegint totes les dades que hem anat recuperant de la BD
                element = new HashMap<String, String>();

                //Anem possant les claus i valors al HashMap
                element.put("id_article", id_article);
                element.put("ruta", ruta_imatge);
                element.put("titol",titol);
                element.put("preu",preu);
                element.put("detalls", titol + " - " + preu + " €");
                element.put("descripcio", descripcio);
                element.put("latitud", latitud);
                element.put("longitud", longitud);


                //Afegim l'article al nostre ArrayList d'articles en la primera posició per a que desprès es vegi al damunt del ListView
                llistaArticles.add(0, element);

            } while (cursor.moveToNext());


        } else Toast.makeText(this, "La BD està buida", Toast.LENGTH_LONG).show();

        // Tanquem la BD
        db.tanca();

        // Notifiquem al ListView que hi han hagut canvis.
        adaptador.notifyDataSetChanged();



        //return nombreArticles;


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Si tenim un resultat correcte tornant de qualsevol de les 2 activitats, reiniciem aquesta activitat
        //perque volem refrescar la GUI
        if (RESULT_OK == resultCode) {
            Intent refresh = new Intent(this, LlistaItems.class);
            startActivity(refresh);
            this.finish();

        }
    }

    //Mètode per començar l'actiivat d'afegir articles
    public void cridaAfegir() {
        Intent intent = new Intent(this, Afegir.class);
        //startActivity(intent);
        startActivityForResult(intent, REQ_AFEGIR);
    }

}


