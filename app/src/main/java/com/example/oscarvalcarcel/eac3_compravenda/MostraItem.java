package com.example.oscarvalcarcel.eac3_compravenda;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MostraItem extends AppCompatActivity {

    ImageView imageview;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageview = (ImageView)findViewById(R.id.show_image);

        mMap = ((MapFragment)getFragmentManager().findFragmentById(
                R.id.mapaShowFragment)).getMap();

/*
        Intent intent = getIntent();
        String ruta = intent.getStringExtra("ruta");
        String action = intent.getAction();
        String type = intent.getType();
*/

        Bitmap thumnail= (Bitmap) getIntent().getExtras().getParcelable("ruta");
        imageview.setImageBitmap(thumnail);

        /*if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {
                    YourImageView.setImageUri(imageUri);
                }
            }*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
