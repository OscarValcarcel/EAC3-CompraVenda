package com.example.oscarvalcarcel.eac3_compravenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

public class LlistaItems extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cridem al mètode que crida a l'activitat per afegir articles
                cridaAfegir();
            }


        });

        listView = (ListView) findViewById(R.id.listView);
    }

    public void cridaAfegir(){
        Intent intent = new Intent(this,Afegir.class);
        //startActivityForResult(intent,0);
        startActivity(intent);
    }
}


