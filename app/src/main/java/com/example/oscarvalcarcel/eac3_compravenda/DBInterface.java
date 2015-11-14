package com.example.oscarvalcarcel.eac3_compravenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class DBInterface {
    //Constants
    public static final String CLAU_ID = "_id";
    public static final String CLAU_TITOL = "titol";
    public static final String CLAU_PREU = "preu";
    public static final String CLAU_DESCRIPCIO = "descripcio";
    public static final String CLAU_IMATGE = "imatge";
    public static final String CLAU_POSICIO = "posicio";
    public static final String CLAU_LONGITUD = "longitud";
    public static final String CLAU_LATITUD = "latitud";


    public static final String TAG = "DBInterface";

    public static final String BD_NOM = "BDCompravenda";
    public static final String BD_TAULA = "Articles";
    public static final int VERSIO = 1;

    //Constant amb la sentència SQL que crearà la taula
    /*public static final String BD_CREATE =
            "create table " + BD_TAULA + "( " + CLAU_ID + " integer primary key autoincrement, " + CLAU_TITOL + " text not null, " +
                    CLAU_PREU + " text not null, " + CLAU_DESCRIPCIO + " text not null, " + CLAU_IMATGE + " text not null, " +
                    CLAU_POSICIO + " text not null);";*/

    public static final String BD_CREATE =
            "create table " + BD_TAULA + "( " + CLAU_ID + " integer primary key autoincrement, " + CLAU_TITOL + " text not null, " +
                    CLAU_PREU + " text not null, " + CLAU_DESCRIPCIO + " text not null, " + CLAU_IMATGE + " text not null, " +
                    CLAU_LONGITUD + " text not null, " + CLAU_LATITUD + " text not null);";


    private final Context context;
    public final AjudaBD ajuda;
    private SQLiteDatabase bd;

    //Constructor
    public DBInterface(Context con) {
        this.context = con;
        ajuda = new AjudaBD(con);
    }

    //Obre la BD
    public DBInterface obre() throws SQLException {
        bd = ajuda.getWritableDatabase();
        return this;
    }

    //Tanca la BD
    public void tanca()
    {
        ajuda.close();
    }

    //Insereix l'article a la Base de Dades
    public long insereixArticle(String titol, String preu, String descripcio, String imatge,
                                String longitud, String latitud) {

        //Creem un objecte ContentValues que ens ajudarà a insertar els valors
        ContentValues initialValues = new ContentValues();
        initialValues.put(CLAU_TITOL, titol);
        initialValues.put(CLAU_PREU, preu);
        initialValues.put(CLAU_DESCRIPCIO, descripcio);
        initialValues.put(CLAU_IMATGE, imatge);
        //initialValues.put(CLAU_POSICIO, posicio);
        initialValues.put(CLAU_LONGITUD, longitud);
        initialValues.put(CLAU_LATITUD, latitud);




        return bd.insert(BD_TAULA, null, initialValues);


    }

    //Mètode que esborra l'article de la BD
    public boolean esborraArticle(Long IDFila)
    {
        return bd.delete(BD_TAULA, CLAU_ID + " = " + IDFila, null) > 0;
    }


    //Obté una article
    public Cursor obtenirArticle(Long IDFila) throws SQLException {
            Cursor mCursor = bd.query(true, BD_TAULA, new String[] {CLAU_ID, CLAU_TITOL, CLAU_PREU,
                            CLAU_DESCRIPCIO, CLAU_IMATGE, CLAU_LONGITUD, CLAU_LATITUD}, CLAU_ID + " = " + IDFila,
                    null, null, null, null, null);

        if(mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;

    }

    //Obté totes les noticies de la Base de Dades
    public Cursor obtenirTotsArticles() {
        return bd.query(BD_TAULA, new String[]{CLAU_ID, CLAU_TITOL, CLAU_PREU,
                        CLAU_DESCRIPCIO, CLAU_IMATGE, CLAU_LONGITUD, CLAU_LATITUD}, null,
                null, null, null, null);
    }


    // Esborra la Base de Dades
    public void esborraTaula() {
        bd.execSQL("DROP TABLE IF EXISTS " + BD_TAULA);
        try {
            bd.execSQL(BD_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }




    //Creem aquesta classe que hereta de la classe SQLiteOpenHelper per simplificar la creació i l'accès a la BD
    public static class AjudaBD extends SQLiteOpenHelper {

        AjudaBD(Context con) {
            super(con, BD_NOM, null, VERSIO);
        }


        @Override
        public void onCreate(SQLiteDatabase bd) {
            try {
                bd.execSQL(BD_CREATE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Actualitzant Base de dades de la versió" + oldVersion + " a " + newVersion + ". Destruirà totes les dades");
            db.execSQL("DROP TABLE IF EXISTS " + BD_TAULA);

            onCreate(db);
        }

        public ArrayList<Cursor> getData(String Query){
            //get writable database
            SQLiteDatabase sqlDB = this.getWritableDatabase();
            String[] columns = new String[] { "mesage" };
            //an array list of cursor to save two cursors one has results from the query
            //other cursor stores error message if any errors are triggered
            ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
            MatrixCursor Cursor2= new MatrixCursor(columns);
            alc.add(null);
            alc.add(null);


            try{
                String maxQuery = Query ;
                //execute the query results will be save in Cursor c
                Cursor c = sqlDB.rawQuery(maxQuery, null);


                //add value to cursor2
                Cursor2.addRow(new Object[] { "Success" });

                alc.set(1,Cursor2);
                if (null != c && c.getCount() > 0) {


                    alc.set(0,c);
                    c.moveToFirst();

                    return alc ;
                }
                return alc;
            } catch(SQLException sqlEx){
                Log.d("printing exception", sqlEx.getMessage());
                //if any exceptions are triggered save the error message to cursor an return the arraylist
                Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
                alc.set(1,Cursor2);
                return alc;
            } catch(Exception ex){

                Log.d("printing exception", ex.getMessage());

                //if any exceptions are triggered save the error message to cursor an return the arraylist
                Cursor2.addRow(new Object[] { ""+ex.getMessage() });
                alc.set(1,Cursor2);
                return alc;
            }


        }
    }


}

