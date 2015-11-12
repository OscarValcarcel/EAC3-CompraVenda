package com.example.oscarvalcarcel.eac3_compravenda;

import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
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

public class LlistaItems extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {


    private final static int REQ_AFEGIR = 0;                              //Constant amb el número que identifica l'activitat per afegir articles
    private final static int REQ_MOSTRAR = 1;                             //Constant amb el número que identifica l'activitat mostrar articles
    List<HashMap<String, String>> llistaArticles = new ArrayList<HashMap<String, String>>();    //ArrayList amb els articles
    SimpleAdapter adaptador = null;                                       //Adaptador que farem servir per emplenar el ListView i que treurà els articles del ArrayList
    HashMap<String, String> element = null;                               //Informació de cada article
    DBInterface db;                                                       //Objecte per gestionar la base de Dades
    ListView llista;                                                      //Widget on carregarem tots els articles
    int nombreArticles;
    Location location;


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

        //Inicialitzem la listView i li lliguem un adaptador
        llista = (ListView) findViewById(R.id.llistaItems);
        llista.setAdapter(adaptador);

        //Inicialitzem la nostra base de dades
        db = new DBInterface(this);

        //Cridem a aquest mètode per recuperar els articles de la BD i mostrar-los a la ListView
        obtenirArticlesDeDB();

        //afegim els listeners per als elements de la llista
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

    //Mètode per començar l'actiivat d'afegir articles
    public void cridaAfegir() {
        Intent intent = new Intent(this, Afegir.class);
        //startActivity(intent);
        startActivityForResult(intent, REQ_AFEGIR);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //obrim la nostra BD
        db.obre();

        //cridem el mètode per obenir un article. Li passem  com a paràmetre el nombre d'articles total menys la posició
        //del item en el qual hem fet click ja que hem invertit l'ordre quan hem afegit els articles a la llista
        String desc = db.obtenirArticle((long) nombreArticles - position).getString(3); //Obtenim la descripció de l'article
        Snackbar.make(view, desc, Snackbar.LENGTH_LONG).setAction("Action", null).show();

        //Tanquem la BD
        db.tanca();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        db.obre();

        //cridem el mètode per obenir un article. Li passem  com a paràmetre el nombre d'articles total menys la posició
        //del item en el qual hem fet click ja que hem invertit l'ordre quan hem afegit els articles a la llista
        //Obtenim la descripció i la ruta de l'article
        String titol = db.obtenirArticle((long) nombreArticles - position).getString(1);
        String preu = db.obtenirArticle((long) nombreArticles - position).getString(2);
        String descripcio = db.obtenirArticle((long) nombreArticles - position).getString(3);
        String ruta = db.obtenirArticle((long) nombreArticles - position).getString(4);

        ////////////IMPLEMENTAR PARTE DE DB DE LONGITUD Y LATITUD Y RECUPERALO AQUI
        //String posicio = db.obtenirArticle((long) nombreArticles - position).getString(5);


        //Recuperem el intent de l'activitat d'Afegir articles i els seus extres.
        Intent i = getIntent();
        Bundle extras = i.getExtras();


        //Trobem la location que es troba en els extres del intent
        // Location location = (Location) extras.get("location");

        //Creem un altre intent que utilitzarem per arrancar l'activitat de mostrar Articles
        Intent intent = new Intent(this, MostraItem.class);

        //Possem totes les dades en el intent que utilitzarem per arrancar l'activitat de mostrar el articles.
        intent.putExtra("titol", titol);
        intent.putExtra("preu", preu);
        intent.putExtra("descripcio", descripcio);
        intent.putExtra("ruta", ruta);
        intent.putExtra("id_article", (position + 1));//El id del article dintre de la bd
        //intent.putExtra("posicio", posicio);
        intent.putExtra("location", location);



        //Tanquem la BD
        db.tanca();

        return true;
    }


    //Mètode per recuperar els articles guardats a la BD
    public void obtenirArticlesDeDB() {
        //Declarem les variagles per emmagatzemmar les dades que volem de la BD
        String titol, preu, descripcio, ruta_imatge;

        //Obrim la nostre BD
        db.obre();

        //Cridem al mètode per recuperar tots els articles desats a la BD
        Cursor cursor = db.obtenirTotsArticles();

        //Recuperem quants articles tenim guardats a la BD
        nombreArticles = cursor.getCount();

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

                //Afegim l'article al nostre ArrayList d'articles en la primera posició per a que desprès es vegi al damunt del ListView
                llistaArticles.add(0, element);

            } while (cursor.moveToNext());


        } else Toast.makeText(this, "La BD està buida", Toast.LENGTH_LONG).show();

        // Tanquem la BD
        db.tanca();

        // Notifiquem al ListView que hi han hagut canvis.
        adaptador.notifyDataSetChanged();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            if (requestCode == REQ_MOSTRAR) {
                Intent refresh = new Intent(this, LlistaItems.class);
                startActivity(refresh);
                this.finish();

            }


        }


    }
}


