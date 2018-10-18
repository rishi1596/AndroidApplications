package rj.bkinfotech;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jimeet29 on 19-12-2017.
 */

public class DBController extends SQLiteOpenHelper {
    public DBController(Context applicationcontext) {
        super(applicationcontext, "complaint.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("Create table localcomplaintdetails (id integer  primary key,ticketid integer, name varchar(25),companyname varchar(60),usertype varchar(15), problemtype varchar(15),registeredno varchar(10),alternateno varchar(12),address varchar(100),description varchar(200),engineerappointed varchar(20),ticketstatus varchar(12),clientsidestatus varchar(12),engineersidestatus varchar(12));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS localcomplaintdetails");
    }


    public void insertComplaint(HashMap<String, String> complaint_details) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ticketid", complaint_details.get("ticketid"));
        cv.put("name", complaint_details.get("name"));
        cv.put("companyname", complaint_details.get("companyname"));
        cv.put("usertype", complaint_details.get("usertype"));
        cv.put("problemtype", complaint_details.get("problemtype"));
        cv.put("registeredno", complaint_details.get("registeredno"));
        cv.put("alternateno", complaint_details.get("alternateno"));
        cv.put("address", complaint_details.get("address"));
        cv.put("description", complaint_details.get("description"));
        cv.put("engineerappointed", complaint_details.get("engineerappointed"));
        cv.put("ticketstatus", complaint_details.get("ticketstatus"));
        cv.put("clientsidestatus", complaint_details.get("clientsidestatus"));
        cv.put("engineersidestatus", complaint_details.get("engineersidestatus"));

        db.insertWithOnConflict("localcomplaintdetails", null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }

    public ArrayList<HashMap<String, String>> getComplaints() {
        ArrayList<HashMap<String, String>> complaintsList = new ArrayList<HashMap<String, String>>();
        String get_complaint_query = "Select * from localcomplaintdetails order by id desc";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(get_complaint_query, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", cursor.getString(0));
                map.put("ticketid", cursor.getString(1));
                map.put("name", cursor.getString(2));
                map.put("companyname", cursor.getString(3));
                map.put("usertype", cursor.getString(4));
                map.put("problemtype", cursor.getString(5));
                map.put("registeredno", cursor.getString(6));
                map.put("alternateno", cursor.getString(7));
                map.put("address", cursor.getString(8));
                map.put("description", cursor.getString(9));
                map.put("engineerappointed", cursor.getString(10));
                map.put("ticketstatus", cursor.getString(11));
                map.put("clientsidestatus", cursor.getString(12));
                map.put("engineersidestatus", cursor.getString(13));

                complaintsList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return complaintsList;
    }
}
