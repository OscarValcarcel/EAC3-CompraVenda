package com.example.oscarvalcarcel.eac3_compravenda;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class Afegir extends AppCompatActivity implements LocationListener {


    private ImageView imatge;                                      //ImageView per visualitzar la foto que fem. També activarà la càmera
    private EditText titol;                                        //EditText per possar el titol del article que es vol vendre
    private EditText preu;                                         //EditText per possar el preu del article que es vol vendre
    private EditText descripcio;
    private ImageButton posicio;                                   //ImageButton que polsarem per trobar la nostra geolocalització
    private static final int REQ_CAMERA = 0;               //Constant amb el número que identifica l'activitat de l'aplicació de fotos
    private static final int REQ_MAP = 1;                  //Constant amb el número que identifica l'activitat de Google Maps
    private Location location;                                     //Variable que desarà la nostra localització.
    private Uri identificadorFoto = null;                                 //Variable del tipus URI que guardarà l'identificador del arxiu
    private Bitmap bitmap;
    private DBInterface db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afegir);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inicialitzem els widgets i la DB
        titol = (EditText) findViewById(R.id.titol);
        preu = (EditText) findViewById(R.id.preu);
        descripcio = (EditText) findViewById(R.id.descripcio);
        imatge = (ImageView) findViewById(R.id.imatge);
        posicio = (ImageButton) findViewById(R.id.posicio);
        db = new DBInterface(this);

        //ELIMINAR TRAS APP OK!!!!//////////////////////////////////////////////
        final Button comprobar = (Button) findViewById(R.id.comprobar);
        comprobar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
             comprobaDB();
             }
        });


                //Establim un listener per al botó per quan fem click
                posicio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //cridem al mètode que obra l'activitat de maps i canviem el color de fons del botó
                        cridaMapa();


                    }
                });


        imatge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int imageNum = 0;

                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File items = new File(android.os.Environment.getExternalStorageDirectory().toString() + "/Items/");

                //Si la carpeta no existeix la creem
                if (!items.exists()) {
                    items.mkdir();
                }

                String nomFoto = UUID.randomUUID().toString() + ".jpg";
                //Creem el fitxer per a la foto que farem. Passem com a paràmetre el directori on volem desar-la i el nom
                File fitxerFoto = new File(items, nomFoto);

                //Passem a l'Intent el fitxer per a que sàpiga on ha de desar la foto
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fitxerFoto));

                identificadorFoto = Uri.fromFile(fitxerFoto);
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


    ///ELIMINAR TRAS APP OK!!!!!!!!!!//////////////////////////////////////
    public void comprobaDB() {
        Intent dbmanager = new Intent(this, AndroidDatabaseManager.class);
        startActivity(dbmanager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //Intent dbmanager = new Intent(this,AndroidDatabaseManager.class);
        //startActivity(dbmanager);
        int id = item.getItemId();

        String textTitol = titol.getText().toString();
        String textPreu = preu.getText().toString();
        String textDescripcio = descripcio.getText().toString();

        //Si pulsem el icon per validar que tota l'entrada del item és correcta
        if (id == R.id.done) {

            //Si no tenim totes les dades complertes
            if ((location == null) || (textTitol.equals("")) || (textPreu.equals("")) || (textDescripcio.equals("")) || (bitmap == null)) {

                //Avisem amb un Toast personalitzat
                Toast toast = Toast.makeText(this, "Completa totes les dades!", Toast.LENGTH_SHORT);
                View toastview = toast.getView();
                TextView toastMessage = (TextView) toastview.findViewById(android.R.id.message);
                toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.shop_mini, 0, 0, 0);
                toastMessage.setGravity(Gravity.LEFT);
                toastMessage.setCompoundDrawablePadding(15);
                toast.show();


                return true;

            } else {
                //LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());

                db.obre();
                //db.esborraTaula();
                db.insereixArticle(textTitol, textPreu, textDescripcio, identificadorFoto.toString(), String.valueOf(location.getLongitude()),String.valueOf(location.getLatitude()));
                Toast.makeText(this, "Article inserit correctament", Toast.LENGTH_SHORT).show();
                db.tanca();

                //Establim que el resultat és OK
                setResult(RESULT_OK, null);

                //Finalitzem l'activitat
                finish();

            }

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // imatge = (ImageView) findViewById(R.id.imatge);

        //Si tot ha sortit bé
        if (RESULT_OK == resultCode) {
            if (requestCode == REQ_CAMERA) {

                ContentResolver contRes = getContentResolver();
                contRes.notifyChange(identificadorFoto, null);

                try {
                    bitmap = android.provider.MediaStore.Images.Media.getBitmap(contRes, identificadorFoto);

                    // Reduïm la imatge per no tenir problemes de visualització.
                    int height = (bitmap.getHeight() * 800 / bitmap.getWidth());
                    //Bitmap resized = Bitmap.createScaledBitmap(bitmap, 800, height, true);
                    Bitmap resized = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
                    //Bitmap resized = Bitmap.createScaledBitmap(bitmap, 350, 700, true);

                    // Guardem el Bitmap generat
                    FileOutputStream stream = new FileOutputStream(identificadorFoto.toString().replace("file://", ""));
                    resized.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                    stream.flush();
                    stream.close();

                    /*Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap rotated = Bitmap.createBitmap(resized, 0, 0,
                            resized.getWidth(), resized.getHeight(),
                            matrix, true);*/

                    // L'assignem a l'ImageView
                    imatge.setImageBitmap(resized);
                    //imatge.setRotation(90);
                } catch (Exception e) {
                    Toast.makeText(this, "No es pot carregar la imatge" +
                                    identificadorFoto.toString(),
                            Toast.LENGTH_SHORT).show();
                }

            } else {
                if (requestCode == REQ_MAP) {
                    Bundle extras = data.getExtras();
                    location = (Location) extras.get("location");

                    //Si tenim posició cambiém el color del fons del botó
                    if (location != null) {
                        posicio.getBackground().setColorFilter(Color.rgb(204, 255, 204), PorterDuff.Mode.MULTIPLY);
                    }

                    /*String text = "Posició actual:\n" +
                            "Latitud " + location.getLatitude() + "\n" +
                            "Longitud " + location.getLongitude();
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();*/


                }

            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
       /* this.location = location;
        String text = "Posició actual:\n" +
                "Latitud " + location.getLatitude() + "\n" +
                "Longitud " + location.getLongitude();
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();*/

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

    //Mètode per cridar a l'activitat de tipus Google Maps
    public void cridaMapa() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, REQ_MAP);
    }


}


