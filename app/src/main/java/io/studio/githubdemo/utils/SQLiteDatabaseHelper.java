package io.studio.githubdemo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import io.studio.githubdemo.retrofit.response.CommonResponse;
import needle.Needle;

/**
 * Helper class for local database
 *
 * @author Yash
 * @since 07-11-2017
 */
public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    /**
     * Interface to handle db Callbacks
     */
    public interface SQLiteCallBack {
        void onSuccess();

        void onError();
    }

    /**
     * Interface to handle db Callback for CompleteProfile
     */
    public interface ArtisanDataCallBack {
        void onSuccess(CommonResponse CommonResponse);

        void onError();
    }

    /**
     * Interface to handle db Callback for Country List
     */
    public interface CountryDataCallBack {
        void onSuccess(List<CommonResponse> countryList);

        void onError();
    }

    /**
     * Interface to handle db Callback for the Unit Data List (Weight Unit List and Length Unit List)
     */
    public interface CommonResponseCallBack {
        void onSuccess(List<CommonResponse> unitDataList);

        void onError();
    }

    /**
     * Interface to handle db Callback for State List
     */
    public interface StateDataCallBack {
        void onSuccess(List<CommonResponse> stateList);

        void onError();
    }

    /**
     * Interface to handle db Callback for the Analytics Graph Data
     */
    public interface AnalyticsGraphCallBack {
        void onSuccess(String pageProduct, String addToCart, String purchased);

        void onError();
    }

    // Database Info
    private static final String DATABASE_NAME = "gitUsersDatabase";
    private static final int DATABASE_VERSION = 1;

    // Data Tables
    private static final String TABLE_ARTISAN_PROFILE = "artisan_profile";
    private static final String TABLE_COUNTRY = "table_country";


    // Country Table Columns
    private static final String NAME = "name";
    private static final String ID = "id";



    // Singleton Instance
    private static SQLiteDatabaseHelper dbInstance;

    public static synchronized SQLiteDatabaseHelper getInstance(Context context) {
        /*
            Use the application context, which will ensure that you
            don't accidentally leak an Activity's context.
        */
        if (dbInstance == null) {
            dbInstance = new SQLiteDatabaseHelper(context.getApplicationContext());
        }
        return dbInstance;
    }

    public static void removeDatabaseInstance() {
        if (dbInstance != null) {
            dbInstance = null;
        }
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private SQLiteDatabaseHelper(Context context) {
        //super(context, DATABASE_NAME, null, DATABASE_VERSION);
        super(context, DATABASE_NAME + "_" +"GITHUB", null, DATABASE_VERSION);
    }

    /**
     * Called when the database connection is being configured.
     * Configure database settings for things like foreign key support, write-ahead logging, etc.
     */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    /*
           Called when the database is created for the FIRST time.
           If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
        */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        /*
             Creating Country Table
         */
        String CREATE_COUNTRY = "CREATE TABLE " + TABLE_COUNTRY +
                "(" +
                NAME + " TEXT," +
                ID + " INTEGER" +
                ");";





        /*
            Executing SQL Query to Create Tables
         */
        sqLiteDatabase.execSQL(CREATE_COUNTRY);
    }

    /*
       Called when the database needs to be upgraded.
       This method will only be called if a database already exists on disk with the same DATABASE_NAME,
       but the DATABASE_VERSION is different than the version of the database that exists on disk.
    */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTISAN_PROFILE);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRY);
        }
    }

    /*
        ========================================== CRUD OPERATIONS PROFILE DATA =================================
     */

    /**
     * Method to add Artisan Profile Data
     *
     * @param CommonResponse Complete Profile Data
     * @param callBack             SQLiteCallBack to handle error and success
     */
    public void addArtisanProfileData(final CommonResponse CommonResponse, final String produceType, final SQLiteCallBack callBack) {
        Needle.onBackgroundThread().execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = getWritableDatabase();
                db.beginTransaction();

                try {

                    db.delete(TABLE_ARTISAN_PROFILE, null, null);

                    ContentValues values = new ContentValues();
                    db.insertOrThrow(TABLE_ARTISAN_PROFILE, null, values);
                    db.setTransactionSuccessful();

                    Needle.onMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Needle.onMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onError();
                        }
                    });

                  Log.d("","Error while trying to add data to database");
                } finally {
                    db.endTransaction();
                }
            }
        });
    }

    /**
     * Method to get all the data of an Artisan
     */
    public void getArtisanProfileData(final Context context, final ArtisanDataCallBack callBack) {
        Needle.onBackgroundThread().execute(new Runnable() {
            @Override
            public void run() {
                final CommonResponse CommonResponse = new CommonResponse();
                String selectQuery = "SELECT * FROM " + TABLE_ARTISAN_PROFILE;
                Cursor cursor;
                SQLiteDatabase db = getReadableDatabase();
                cursor = db.rawQuery(selectQuery, null);

                try {
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            //CommonResponse.setArtisanId(cursor.getInt(cursor.getColumnIndex(ARTISAN_ID)));
                            //PrefsHelper.putValue(context, Constant.PRODUCE_TYPE, cursor.getString(cursor.getColumnIndex(PRODUCE_TYPE)));
                        } while (cursor.moveToNext());
                    }

                    Needle.onMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(CommonResponse);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Needle.onMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onError();
                        }
                    });
                    Log.d("","Error while trying to get data from database");
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            }
        });
    }


    /**
     * Method to delete all the records from the Artisan Table
     *
     * @param callBack SQLiteCallBack to handle error and success
     */
    public void deleteAllProfileData(final SQLiteCallBack callBack) {
        Needle.onBackgroundThread().execute(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = getWritableDatabase();
                db.beginTransaction();
                try {
                    db.delete(TABLE_ARTISAN_PROFILE, null, null);
                    db.setTransactionSuccessful();
                    if (callBack != null)
                        callBack.onSuccess();
                } catch (Exception e) {
                    e.printStackTrace();
                    Needle.onMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            if (callBack != null)
                                callBack.onError();
                        }
                    });
                    Log.d("","Error while trying to delete all data");
                } finally {
                    db.endTransaction();
                }
            }
        });
    }

    /*
        ========================================== CRUD OPERATIONS COUNTRY DATA =================================
     */

}