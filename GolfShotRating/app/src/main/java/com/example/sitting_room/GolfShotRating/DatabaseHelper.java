package com.example.sitting_room.GolfShotRating;

//Original code for DatabaseHelper obtained from a book called, 'Head first Android Development'.


import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


class DatabaseHelper extends SQLiteOpenHelper {

    private String TAG = "TEST";
    private static final String TABLE_SHOT = "shot";
    private static final String TABLE_ROUND = "round";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_USERID = "user_id";
    private static final String COLUMN_COURSE = "course";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_HIT_FROM = "hit_from";
    public static final String COLUMN_HOLE_NUMBER = "hole";
    public static final String COLUMN_SHOT_NUMBER = "shot_number";
    public static final String COLUMN_SHOT_DISTANCE = "shot_distance";
    public static final String COLUMN_SHOT_SCORE = "shot_score";
    public static final String COLUMN_ROUND_ID = "round_id";
    public static final String COLUMN_HANDICAP = "handicap";
    public static final String COLUMN_SG_DRIVING = "sg_driving";
    public static final String COLUMN_SG_LONG = "sg_long_game";
    public static final String COLUMN_SG_SHORT = "sg_short_game";
    public static final String COLUMN_SG_PUTTING = "sg_putting";
    public static final String COLUMN_AWFUL_SHOTS = "awful_shots";
    public static final String COLUMN_EXPORTED = "exported";
    public static final String COLUMN_PENALTY = "penalty";

    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "crud.db";

    public DatabaseHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "DB_HELPER onCreate started");
        //All necessary tables you like to create will create here

        db.execSQL("CREATE TABLE shot ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "latitude REAL, "
                + "longitude REAL, "
                + "hit_from TEXT, "
                + "shot_score REAL, "
                + "hole INTEGER, "
                + "shot_number INTEGER, "
                + "shot_distance Real,"
                + "round_id INTEGER,"
                + "penalty INTEGER DEFAULT 0);");

        db.execSQL("CREATE TABLE round ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "date NUMERIC, "
                + "sg_driving REAL, "
                + "sg_long_game REAL, "
                + "sg_putting REAL, "
                + "sg_short_game REAL, "
                + "sg_total REAL, "
                + "handicap INTEGER, "
                + "awful_shots INTEGER, "
                + "course TEXT, "
                + "user_id REAL, "
                + "exported INTEGER DEFAULT 0);");

        db.execSQL("CREATE TABLE stroke_baseline ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "distance int, "
                + "tee_shot_driver REAL, "
                + "tee_shot_iron REAL, "
                + "fairway REAL, "
                + "rough REAL, "
                + "bunker REAL, "
                + "recovery REAL );");

        insertstrokebaseline(db, 20, 2.52, 2.52, 2.40, 2.59, 2.53, 2.59);
        insertstrokebaseline(db, 40, 2.72, 2.72, 2.60, 2.78, 2.82, 2.82);
        insertstrokebaseline(db, 60, 2.82, 2.82, 2.70, 2.91, 3.15, 3.15);
        insertstrokebaseline(db, 80, 2.87, 2.87, 2.75, 2.96, 3.24, 3.24);
        insertstrokebaseline(db, 100, 2.92, 2.92, 2.80, 3.02, 3.23, 3.80);
        insertstrokebaseline(db, 120, 2.99, 2.99, 2.85, 3.08, 3.21, 3.78);
        insertstrokebaseline(db, 140, 2.97, 2.97, 2.91, 3.15, 3.22, 3.80);
        insertstrokebaseline(db, 160, 2.99, 2.99, 2.98, 3.23, 3.28, 3.81);
        insertstrokebaseline(db, 180, 3.05, 3.05, 3.08, 3.31, 3.40, 3.82);
        insertstrokebaseline(db, 200, 3.12, 3.12, 3.19, 3.42, 3.55, 3.87);
        insertstrokebaseline(db, 220, 3.17, 3.17, 3.32, 3.53, 3.70, 3.92);
        insertstrokebaseline(db, 240, 3.25, 3.25, 3.45, 3.64, 3.84, 3.97);
        insertstrokebaseline(db, 260, 3.45, 3.45, 3.58, 3.74, 3.93, 4.03);
        insertstrokebaseline(db, 280, 3.65, 3.65, 3.69, 3.83, 4.00, 4.10);
        insertstrokebaseline(db, 300, 3.71, 3.71, 3.78, 3.90, 4.04, 4.20);
        insertstrokebaseline(db, 320, 3.79, 3.79, 3.84, 3.95, 4.12, 4.31);
        insertstrokebaseline(db, 340, 3.86, 3.86, 3.88, 4.02, 4.26, 4.44);
        insertstrokebaseline(db, 360, 3.92, 3.92, 3.95, 4.11, 4.41, 4.56);
        insertstrokebaseline(db, 380, 3.96, 3.96, 4.03, 4.21, 4.55, 4.66);
        insertstrokebaseline(db, 400, 3.99, 3.99, 4.11, 4.30, 4.69, 4.75);
        insertstrokebaseline(db, 420, 4.02, 4.02, 4.15, 4.34, 4.73, 4.79);
        insertstrokebaseline(db, 440, 4.08, 4.08, 4.20, 4.39, 4.78, 4.84);
        insertstrokebaseline(db, 460, 4.17, 4.17, 4.29, 4.48, 4.87, 4.93);
        insertstrokebaseline(db, 480, 4.28, 4.28, 4.40, 4.59, 4.98, 5.04);
        insertstrokebaseline(db, 500, 4.41, 4.41, 4.53, 4.72, 5.11, 5.17);
        insertstrokebaseline(db, 520, 4.54, 4.54, 4.66, 4.85, 5.24, 5.30);
        insertstrokebaseline(db, 540, 4.65, 4.65, 4.78, 4.97, 5.36, 5.42);
        insertstrokebaseline(db, 560, 4.74, 4.74, 4.86, 5.05, 5.44, 5.50);
        insertstrokebaseline(db, 580, 4.79, 4.79, 4.91, 5.10, 5.49, 5.55);
        insertstrokebaseline(db, 600, 4.82, 4.82, 4.94, 5.13, 5.52, 5.58);

        db.execSQL("CREATE TABLE putt_baseline ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "distance INTEGER, "
                + "ave_putts REAL);");

        insertputtbaseline(db, 2, 1.01);
        insertputtbaseline(db, 3, 1.04);
        insertputtbaseline(db, 4, 1.13);
        insertputtbaseline(db, 5, 1.23);
        insertputtbaseline(db, 6, 1.34);
        insertputtbaseline(db, 7, 1.42);
        insertputtbaseline(db, 8, 1.50);
        insertputtbaseline(db, 9, 1.56);
        insertputtbaseline(db, 10, 1.61);
        insertputtbaseline(db, 15, 1.78);
        insertputtbaseline(db, 20, 1.87);
        insertputtbaseline(db, 30, 1.98);
        insertputtbaseline(db, 40, 2.06);
        insertputtbaseline(db, 50, 2.14);
        insertputtbaseline(db, 60, 2.21);
        insertputtbaseline(db, 90, 2.40);


    }//onCreate

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS shot");

        // Create tables again
        onCreate(db);

    }

    private static void insertstrokebaseline(SQLiteDatabase db, Integer dist, Double driver, Double iron,
                                             Double fairway, Double rough, Double bunker, Double recovery) {


        ContentValues strokeValues = new ContentValues();
        strokeValues.put("distance", dist);
        strokeValues.put("tee_shot_driver", driver);
        strokeValues.put("tee_shot_iron", iron);
        strokeValues.put("fairway", fairway);
        strokeValues.put("rough", rough);
        strokeValues.put("bunker", bunker);
        strokeValues.put("recovery", recovery);
        db.insert("stroke_baseline", null, strokeValues);//insert into database the stroke values
    }


    private static void insertputtbaseline(SQLiteDatabase db, Integer dist, Double putts) {
        ContentValues puttValues = new ContentValues();
        puttValues.put("distance", dist);
        puttValues.put("ave_putts", putts);
        db.insert("putt_baseline", null, puttValues);//insert into database the putt values
    }


    public void addShotToDB(Double latitude_coord, Double longitude_coord, String place, int hole_counter, int shot_counter, int roundID) {
        Log.i(TAG, "addShotToDB started");
        ContentValues values = new ContentValues();
        values.put(COLUMN_LATITUDE, latitude_coord);
        values.put(COLUMN_LONGITUDE, longitude_coord);
        values.put(COLUMN_HIT_FROM, place);
        values.put(COLUMN_HOLE_NUMBER, hole_counter);
        values.put(COLUMN_SHOT_NUMBER, shot_counter);
        values.put(COLUMN_ROUND_ID, roundID);

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_SHOT, null, values);
        db.close();

    }//addShotToDB

    public void addFinalRatingToDB(Integer Id, Double rating){
        Log.i(TAG, "addFinalRatingToDB started");
        Log.i(TAG, "Id is: " + Id);
        Log.i(TAG, "Rating is: " + rating);

        ContentValues values = new ContentValues();
        values.put(COLUMN_SHOT_SCORE, rating);
        SQLiteDatabase db = this.getWritableDatabase();

        //Update DB by inserting distance into the relevant row.
        db.update(TABLE_SHOT,
                values,
                "_id = ?",
                new String[]{Integer.toString(Id)});
        db.close();

    }


    public void addDistancesToDB(Double distance, Integer row) {
        //Log.i(TAG, "addDistancesToDB started");
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHOT_DISTANCE, distance);

        SQLiteDatabase db = this.getWritableDatabase();

        //Update DB by inserting distance into the relevant row.
        db.update(TABLE_SHOT,
                values,
                "_id = ?",
                new String[] {Integer.toString(row)});
        db.close();
    }


    public void addNewRoundToDB(String date, String coursename, String handicap, String userID) {
        //Log.i(TAG, "addNewRoundToDB started");
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_COURSE, coursename);
        values.put(COLUMN_HANDICAP, handicap);
        values.put(COLUMN_USERID, userID);


        SQLiteDatabase db = this.getWritableDatabase();

        //Update DB by inserting date into db.
        db.insert(TABLE_ROUND, null, values);
        db.close();
    }

    public void addPenaltyShotToDB(Integer row, Integer penalty){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PENALTY, penalty);

        SQLiteDatabase db = this.getWritableDatabase();
        //Update DB by inserting distance into the relevant row.
        db.update(TABLE_SHOT,
                values,
                "_id = ?",
                new String[] {Integer.toString(row)});
        db.close();
    }


    public void addScoresToDb(Integer roundId, Double drivingRating, Double longGameRating, Double puttingRating, Double shortGameRating, Integer awfulShots){
        Log.i(TAG, "addScoresToDb started");
        ContentValues values = new ContentValues();
        values.put(COLUMN_SG_DRIVING, drivingRating);
        values.put(COLUMN_SG_LONG, longGameRating);
        values.put(COLUMN_SG_SHORT, shortGameRating);
        values.put(COLUMN_SG_PUTTING, puttingRating);
        values.put(COLUMN_AWFUL_SHOTS, awfulShots);

        SQLiteDatabase db = this.getWritableDatabase();

        //Update DB by inserting distance into the relevant row.
        db.update(TABLE_ROUND,
                values,
                "_id = ?",
                new String[]{Integer.toString(roundId)});
        db.close();
    }

    public void updateExport(Integer roundID)
    {
        Log.i(TAG, "updateExport started");
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPORTED, 1);

        SQLiteDatabase db = this.getWritableDatabase();
        //Update DB by inserting distance into the relevant row.
        db.update(TABLE_ROUND,
                values,
                "_id = ?",
                new String[]{Integer.toString(roundID)});
        db.close();
    }



}//class
