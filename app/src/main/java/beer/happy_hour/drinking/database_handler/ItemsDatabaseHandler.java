package beer.happy_hour.drinking.database_handler;

/**
 * Created by brcon on 02/04/2017.
 */

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import beer.happy_hour.drinking.model.Item;

public class ItemsDatabaseHandler extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "itemsManager";

    // Contacts table name
    private static final String TABLE_ITEMS = "items";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_MANUFACTURER = "manufacturer";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_IMAGE = "image";

    public ItemsDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        db = getWritableDatabase();
        Log.d("constructor", "ItemsDatabaseHandler");
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT, "
                + KEY_BRAND + " TEXT, "
                + KEY_MANUFACTURER + " TEXT, "
                + KEY_IMAGE_URL + " TEXT, "
                + KEY_IMAGE + " BLOB"
                + ")";
        Log.d("onCreateItemsDatabase", CREATE_ITEMS_TABLE);

        db.execSQL(CREATE_ITEMS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("onUpgradeItemsDatabase", "Upgrade");

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new item
    public void addItem(Item item) throws SQLiteException {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getProduct().getName()); // Product Name
        values.put(KEY_BRAND, item.getProduct().getBrand()); // Product Brand
        values.put(KEY_MANUFACTURER, item.getProduct().getManufacturer()); // Product Manufacturer
        values.put(KEY_IMAGE_URL, item.getProduct().getImage_url()); // Product Image URL
        values.put(KEY_IMAGE, new byte[0]); // Product Image

        // Inserting Row
        try {
            db.insert(TABLE_ITEMS, null, values);
        }finally {
            db.close(); // Closing database connection
        }

    }

    //Obs: Provavelmente este método jamais poderá ser utilizado, pois cria ítens vazios na tabela
//    public void addBitmapImage(Bitmap bmpImage) throws SQLiteException {
//        db = this.getWritableDatabase();
//
//        byte[] byteImage = getByteArrayFromBitmap(bmpImage);
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_IMAGE, byteImage); // Product Image
//
//        db.insert(TABLE_ITEMS, null, values);
//        db.close(); // Closing database connection
//    }

    private static byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public Bitmap getImage(Item item){
            db = this.getWritableDatabase();

//        String query = "select img from table where "+ KEY_NAME + "=" + item.getProduct().getName() ;
            String query = "SELECT " + KEY_IMAGE + " FROM " + TABLE_ITEMS + " WHERE "+ KEY_NAME + "=" + "\'"+ item.getProduct().getName() + "\'";

            Log.d("jnsdfinsdudf",query);
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()){
                byte[] imgByte = cursor.getBlob(0);
                cursor.close();

                if(imgByte != null) {
                    Log.d("EntrouAqui", "EntrouAqui");
                    return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                }
                else
                    return null;
            }

            if (!cursor.isClosed()) {
                cursor.close();
            }

            db.close();

            return null ;
    }

    // Retrieve image from database
    public Bitmap getImage(int i){
        db = this.getWritableDatabase();

        String query = "select img  from table where feedid=" + i ;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            byte[] imgByte = cursor.getBlob(0);
            cursor.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        db.close();

        return null ;
    }

    public boolean hasItem(Item item){
        db = this.getWritableDatabase();

//        String query = "select img from table where "+ KEY_NAME + "=" + item.getProduct().getName() ;
        String query = "SELECT " + KEY_ID + " FROM " + TABLE_ITEMS + " WHERE "+ KEY_NAME + "=" + "\""+ item.getProduct().getName() + "\"";

        Log.d("DatabaseQuery",query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            String string = cursor.getString(0);
            cursor.close();

            if(string != null)
                return true;
            else
                return false;
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        db.close();

        return false;
    }

    // Updating single item
    public void updateItem(Item item) {
        db = this.getWritableDatabase();
        int id = 0;

        String query = "SELECT " + KEY_ID + " FROM " + TABLE_ITEMS + " WHERE "+ KEY_NAME + "=" + "\""+ item.getProduct().getName() + "\"";

        Log.d("DatabaseQuery",query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            id = cursor.getInt(0);
            cursor.close();
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getProduct().getName()); // Product Name
        values.put(KEY_BRAND, item.getProduct().getBrand()); // Product Brand
        values.put(KEY_MANUFACTURER, item.getProduct().getManufacturer()); // Product Manufacturer
        values.put(KEY_IMAGE_URL, item.getProduct().getImage_url()); // Product Image URL
        values.put(KEY_IMAGE, new byte[0]); // Product Image

        // updating row
        if(id>0)
            try {
                db.update(TABLE_ITEMS, values, KEY_ID + " = ?", new String[]{Integer.toString(id)});
            }
            finally {
                db.close();
            }
    }

    // Updating single item image
    public void updateItemImage(Item item, Bitmap bmpImage) {
        Log.d("EntrandoUpdateImage", "Entrando");
        db = this.getWritableDatabase();

        int id = 0;

        String query = "SELECT " + KEY_ID + " FROM " + TABLE_ITEMS + " WHERE "+ KEY_NAME + "=" + "\""+ item.getProduct().getName() + "\"";

        Log.d("DatabaseQuery",query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            id = cursor.getInt(0);
            cursor.close();
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }


        byte[] byteImage = getByteArrayFromBitmap(bmpImage);

        Log.d("EntrandoUpdateImage", byteImage.toString());

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getProduct().getName()); // Product Name
        values.put(KEY_BRAND, item.getProduct().getBrand()); // Product Brand
        values.put(KEY_MANUFACTURER, item.getProduct().getManufacturer()); // Product Manufacturer
        values.put(KEY_IMAGE_URL, item.getProduct().getImage_url()); // Product Image URL
        values.put(KEY_IMAGE, byteImage); // Product Image

        // updating row
        db.update(TABLE_ITEMS, values, KEY_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }

    // Deleting single item
    public void deleteItem(Item item) {
        int id = 0;

        String query = "SELECT " + KEY_ID + " FROM " + TABLE_ITEMS + " WHERE "+ KEY_NAME + "=" + "\""+ item.getProduct().getName() + "\"";

        Log.d("DatabaseQuery",query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            id = cursor.getInt(0);
            cursor.close();
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }


    // Getting items Count
    public int getItemsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
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
