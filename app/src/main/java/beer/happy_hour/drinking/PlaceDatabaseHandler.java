package beer.happy_hour.drinking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import beer.happy_hour.drinking.model.DeliveryPlace;

/**
 * Created by brcon on 13/03/2017.
 */

public class PlaceDatabaseHandler extends SQLiteOpenHelper {
    //Database
    private static final String DATABASE_NAME = "places.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABELA = "places";

    //Data
    private static final String KEY_ID = "id";
    private static final String KEY_ADRESS = "endereco";
    private static final String KEY_CITYSTATE = "cidadeestado";
    private static final String KEY_COUNTRY = "pais";
    private static final String KEY_ZIPCODE = "cep";
    private static final String KEY_KNOWNAME = "nome local";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";


    public PlaceDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABELA + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ADRESS + " TEXT,"
                + KEY_CITYSTATE + " TEXT,"
                + KEY_COUNTRY + " TEXT,"
                + KEY_ZIPCODE + " TEXT,"
                + KEY_KNOWNAME + " TEXT"
                + KEY_LATITUDE + " TEXT"
                + KEY_LONGITUDE + "TEXT"
                + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABELA);

        // Create tables again
        onCreate(db);
    }

    public void addDeliveryPlace(DeliveryPlace place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ADRESS, place.getAdress());
        values.put(KEY_CITYSTATE, place.getCityState());
        values.put(KEY_COUNTRY, place.getCountry());
        values.put(KEY_ZIPCODE, place.getZipCode());
        values.put(KEY_KNOWNAME, place.getKnownName());
        values.put(KEY_LATITUDE, place.getLatitude());
        values.put(KEY_LONGITUDE, place.getLongitude());

        // Inserting Row
        db.insert(TABELA, null, values);
        db.close(); // Closing database connection
    }

    // Getting single DeliveryPlace
    public DeliveryPlace getDeliveryPlace(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABELA, new String[]{
                        KEY_ID,
                        KEY_ADRESS,
                        KEY_CITYSTATE,
                        KEY_COUNTRY,
                        KEY_ZIPCODE,
                        KEY_KNOWNAME,
                        KEY_LATITUDE,
                        KEY_LONGITUDE
                },
                KEY_ID +
                        "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DeliveryPlace place = new DeliveryPlace(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                Integer.parseInt(cursor.getString(6)),
                Integer.parseInt(cursor.getString(7))
        );

        return place;
    }

    // Getting All DeliveryPlaces
    public List<DeliveryPlace> getAllDeliveryPlaces() {
        List<DeliveryPlace> placesList = new ArrayList<DeliveryPlace>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABELA;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DeliveryPlace place = new DeliveryPlace(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        Integer.parseInt(cursor.getString(6)),
                        Integer.parseInt(cursor.getString(7))
                );

                // Adding place to list
                placesList.add(place);
            } while (cursor.moveToNext());
        }

        // return contact list
        return placesList;
    }

    // Getting DeliveryPlaces Count
    public int getDeliveryPlacesCount() {
        String countQuery = "SELECT  * FROM " + TABELA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single place
    public int updateContact(DeliveryPlace place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ADRESS, place.getAdress());
        values.put(KEY_CITYSTATE, place.getCityState());
        values.put(KEY_COUNTRY, place.getCountry());
        values.put(KEY_ZIPCODE, place.getZipCode());
        values.put(KEY_KNOWNAME, place.getKnownName());
        values.put(KEY_LATITUDE, place.getLatitude());
        values.put(KEY_LONGITUDE, place.getLatitude());

        // updating row
        return db.update(TABELA, values, KEY_ID + " = ?",
                new String[]{String.valueOf(place.getId())});
    }

    // Deleting single place
    public void deleteContact(DeliveryPlace place) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABELA, KEY_ID + " = ?",
                new String[]{String.valueOf(place.getId())});
        db.close();
    }
}
