package nadol.nadol;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jimeet29 on 13-05-2017.
 */
public class DBController extends SQLiteOpenHelper {

    public DBController(Context applicationcontext)
    {
        super(applicationcontext,"entries.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE news ( id INTEGER ,title VARCHAR(100), content VARCHAR(500), image VARCHAR(500), time VARCHAR(20));");
        db.execSQL("CREATE TABLE score ( id INTEGER ,matchno INTEGER, teama VARCHAR(50), teamalogoid INTEGER, teamb VARCHAR(50), teamblogoid INTEGER, over FLOAT, score INTEGER, wicket INTEGER, target INTEGER, result VARCHAR(200));");
        db.execSQL("INSERT INTO score (id, matchno, teama, teamalogoid, teamb, teamblogoid,over,score,wicket,target,result) VALUES (10, 1, 'Team A', 0, 'Team B',0,0.0,0,0,0,'N/A')");
        db.execSQL("CREATE TABLE sponsor ( id INTEGER ,thing VARCHAR(200), name VARCHAR(500));");
        db.execSQL("CREATE TABLE fixtures ( fixtureid INTEGER, matchno INTEGER, teama VARCHAR(50), teamalogoid INTEGER,vs Varchar(3), teamb VARCHAR(50), teamblogoid INTEGER);");
        db.execSQL("CREATE TABLE team ( id INTEGER, name VARCHAR(40), owner VARCHAR(50), teamlogoid INTEGER);");
        db.execSQL("CREATE TABLE results ( id INTEGER, mainresult VARCHAR(100), mom VARCHAR(100));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS news");
        db.execSQL("DROP TABLE IF EXISTS score");
        db.execSQL("DROP TABLE IF EXISTS sponsor");
        db.execSQL("DROP TABLE IF EXISTS fixtures");
        db.execSQL("DROP TABLE IF EXISTS team");
        db.execSQL("DROP TABLE IF EXISTS results");
        onCreate(db);
    }

    public void insertnews(HashMap<String,String> newsfromserver) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", newsfromserver.get("newsid"));
        values.put("title", newsfromserver.get("newstitle"));
        values.put("content", newsfromserver.get("newscontent"));
        values.put("image", newsfromserver.get("newsimage"));
        values.put("time", newsfromserver.get("newstime"));

        database.insertWithOnConflict("news", null,
                values, SQLiteDatabase.CONFLICT_IGNORE);
        database.close();
    }

    public ArrayList<HashMap<String, String>> getnews() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM news order by id DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> map = new HashMap<>();
                map.put("newsid", cursor.getString(0));
                map.put("newstitle", cursor.getString(1));
                map.put("newscontent", cursor.getString(2));
                map.put("newsimage", cursor.getString(3));
                map.put("newstime", cursor.getString(4));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return usersList;
    }

    public void deletenews()
    {   SQLiteDatabase database = this.getWritableDatabase();
        String delquery = "delete from news";
        database.execSQL(delquery);
        database.close();
    }

    public ArrayList<HashMap<String, String>> getspecificnews(int nid) {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM news where id = '"+nid+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> map = new HashMap<>();

                map.put("newstitle", cursor.getString(1));
                map.put("newscontent", cursor.getString(2));
                map.put("newsimage", cursor.getString(3));
                map.put("newstime", cursor.getString(4));
                System.out.println("newstitle"+cursor.getString(1));
                usersList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return usersList;
    }

    public void insertscore(HashMap<String,String> scorefromserver) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", scorefromserver.get("id"));
        values.put("matchno", scorefromserver.get("matchno"));
        values.put("teama", scorefromserver.get("teama"));
        values.put("teamalogoid", scorefromserver.get("teamalogoid"));
        values.put("teamb", scorefromserver.get("teamb"));
        values.put("teamblogoid", scorefromserver.get("teamblogoid"));
        values.put("over", scorefromserver.get("over"));
        values.put("score", scorefromserver.get("score"));
        values.put("wicket", scorefromserver.get("wicket"));
        values.put("target", scorefromserver.get("target"));
        values.put("result", scorefromserver.get("result"));

        database.insertWithOnConflict("score", null,
                values, SQLiteDatabase.CONFLICT_IGNORE);
        database.close();
    }

    public ArrayList<HashMap<String, String>> getscore() {
        ArrayList<HashMap<String, String>> usersList;
        int[] teamlogos = new int[]{
                R.drawable.teams,
                R.drawable.a,
                R.drawable.b,
                R.drawable.c,
                R.drawable.d,
                R.drawable.e,
                R.drawable.f
        };
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM score ";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> map = new HashMap<>();
                map.put("scoreid", cursor.getString(0));
                map.put("matchno", cursor.getString(1));
                map.put("teama", cursor.getString(2));

                int teama = Integer.parseInt(cursor.getString(3));
                switch(teama)
                {
                    case 0:
                        map.put("teamalogoid", Integer.toString(teamlogos[0]));
                        break;
                    case 1:
                        map.put("teamalogoid", Integer.toString(teamlogos[1]));
                        break;
                    case 2:
                        map.put("teamalogoid", Integer.toString(teamlogos[2]));
                        break;
                    case 3:
                        map.put("teamalogoid", Integer.toString(teamlogos[3]));
                        break;
                    case 4:
                        map.put("teamalogoid", Integer.toString(teamlogos[4]));
                        break;
                    case 5:
                        map.put("teamalogoid", Integer.toString(teamlogos[5]));
                        break;
                    case 6:
                        map.put("teamalogoid", Integer.toString(teamlogos[6]));
                        break;
                }
                map.put("teamb", cursor.getString(4));

                int teamb = Integer.parseInt(cursor.getString(5));
                switch(teamb)
                {
                    case 0:
                        map.put("teamblogoid", Integer.toString(teamlogos[0]));
                        break;
                    case 1:
                        map.put("teamblogoid", Integer.toString(teamlogos[1]));
                        break;
                    case 2:
                        map.put("teamblogoid", Integer.toString(teamlogos[2]));
                        break;
                    case 3:
                        map.put("teamblogoid", Integer.toString(teamlogos[3]));
                        break;
                    case 4:
                        map.put("teamblogoid", Integer.toString(teamlogos[4]));
                        break;
                    case 5:
                        map.put("teamblogoid", Integer.toString(teamlogos[5]));
                        break;
                    case 6:
                        map.put("teamblogoid", Integer.toString(teamlogos[6]));
                        break;

                }
                map.put("over", cursor.getString(6));
                map.put("score", cursor.getString(7));
                map.put("wicket", cursor.getString(8));
                map.put("target", cursor.getString(9));
                map.put("result", cursor.getString(10));

                usersList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return usersList;
    }

    public void deletescore()
    {   SQLiteDatabase database = this.getWritableDatabase();
        String delquery = "delete from score";
        database.execSQL(delquery);
        database.close();
    }

    public void insertsponsor(HashMap<String,String> sponsorfromserver) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", sponsorfromserver.get("sponsorid"));
        values.put("thing", sponsorfromserver.get("sponsorthing"));
        values.put("name", sponsorfromserver.get("sponsorname"));


        database.insertWithOnConflict("sponsor", null,
                values, SQLiteDatabase.CONFLICT_IGNORE);
        database.close();
    }

    public ArrayList<HashMap<String, String>> getsponsor() {
        ArrayList<HashMap<String, String>> usersList;
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM sponsor";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> map = new HashMap<>();
                map.put("sponsorid", cursor.getString(0));
                map.put("sponsorthing", cursor.getString(1));
                map.put("sponsorname", cursor.getString(2));

                usersList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return usersList;
    }

    public void deletesponsor()
    {   SQLiteDatabase database = this.getWritableDatabase();
        String delquery = "delete from sponsor";
        database.execSQL(delquery);
        database.close();
    }

    public void insertfixtures(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fixtureid", queryValues.get("fixtureid"));
        values.put("matchno", queryValues.get("matchno"));
        values.put("teama", queryValues.get("teama"));
        values.put("teamalogoid", queryValues.get("teamalogoid"));
        values.put("vs", queryValues.get("vs"));
        values.put("teamb", queryValues.get("teamb"));
        values.put("teamblogoid", queryValues.get("teamblogoid"));

        //database.insert("result", null, values);
        database.insertWithOnConflict("fixtures", null,
                values, SQLiteDatabase.CONFLICT_IGNORE);
        database.close();
    }

    /**
     * Get list of result from SQLite DB as Array List
     * @return
     */
    public ArrayList<HashMap<String, String>> getfixtures() {
        ArrayList<HashMap<String, String>> usersList;
        int[] teamlogos = new int[]{
                R.drawable.fixtures,
                R.drawable.a,
                R.drawable.b,
                R.drawable.c,
                R.drawable.d,
                R.drawable.e,
                R.drawable.f
        };
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM fixtures ";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> map = new HashMap<>();
                map.put("fixtureid", cursor.getString(0));
                map.put("matchno", cursor.getString(1));
                map.put("teama", cursor.getString(2));

                int teama = Integer.parseInt(cursor.getString(3));
                switch(teama)
                {
                    case 0:
                        map.put("teamalogoid", Integer.toString(teamlogos[0]));
                        break;
                    case 1:
                        map.put("teamalogoid", Integer.toString(teamlogos[1]));
                        break;
                    case 2:
                        map.put("teamalogoid", Integer.toString(teamlogos[2]));
                        break;
                    case 3:
                        map.put("teamalogoid", Integer.toString(teamlogos[3]));
                        break;
                    case 4:
                        map.put("teamalogoid", Integer.toString(teamlogos[4]));
                        break;
                    case 5:
                        map.put("teamalogoid", Integer.toString(teamlogos[5]));
                        break;
                    case 6:
                        map.put("teamalogoid", Integer.toString(teamlogos[6]));
                        break;
                }
                map.put("vs",cursor.getString(4));


                int teamb = Integer.parseInt(cursor.getString(6));
                switch(teamb)
                {
                    case 0:
                        map.put("teamblogoid", Integer.toString(teamlogos[0]));
                        break;
                    case 1:
                        map.put("teamblogoid", Integer.toString(teamlogos[1]));
                        break;
                    case 2:
                        map.put("teamblogoid", Integer.toString(teamlogos[2]));
                        break;
                    case 3:
                        map.put("teamblogoid", Integer.toString(teamlogos[3]));
                        break;
                    case 4:
                        map.put("teamblogoid", Integer.toString(teamlogos[4]));
                        break;
                    case 5:
                        map.put("teamblogoid", Integer.toString(teamlogos[5]));
                        break;
                    case 6:
                        map.put("teamblogoid", Integer.toString(teamlogos[6]));
                        break;

                }

                map.put("teamb", cursor.getString(5));

                usersList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return usersList;
    }
    public void deletefixtures()
    {   SQLiteDatabase database = this.getWritableDatabase();
        String delquery = "delete from fixtures";
        database.execSQL(delquery);
        database.close();
    }

    public void insertteams(HashMap<String,String> scorefromserver) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", scorefromserver.get("teamid"));
        values.put("name", scorefromserver.get("teamname"));
        values.put("owner", scorefromserver.get("teamowner"));
        values.put("teamlogoid", scorefromserver.get("teamlogoid"));


        database.insertWithOnConflict("team", null,
                values, SQLiteDatabase.CONFLICT_IGNORE);
        database.close();
    }

    public ArrayList<HashMap<String, String>> getteams() {
        ArrayList<HashMap<String, String>> usersList;
        int[] teamlogos = new int[]{
                R.drawable.teams,
                R.drawable.a,
                R.drawable.b,
                R.drawable.c,
                R.drawable.d,
                R.drawable.e,
                R.drawable.f
        };
        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM team ";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> map = new HashMap<>();
                map.put("teamid", cursor.getString(0));
                int team = Integer.parseInt(cursor.getString(3));
                switch(team)
                {
                    case 0:
                        map.put("teamlogoid", Integer.toString(teamlogos[0]));
                        break;
                    case 1:
                        map.put("teamlogoid", Integer.toString(teamlogos[1]));
                        break;
                    case 2:
                        map.put("teamlogoid", Integer.toString(teamlogos[2]));
                        break;
                    case 3:
                        map.put("teamlogoid", Integer.toString(teamlogos[3]));
                        break;
                    case 4:
                        map.put("teamlogoid", Integer.toString(teamlogos[4]));
                        break;
                    case 5:
                        map.put("teamlogoid", Integer.toString(teamlogos[5]));
                        break;
                    case 6:
                        map.put("teamlogoid", Integer.toString(teamlogos[6]));
                        break;
                }

                map.put("teamname", cursor.getString(1));
                map.put("teamowner", cursor.getString(2));


                usersList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return usersList;
    }

    public void deleteteams()
    {   SQLiteDatabase database = this.getWritableDatabase();
        String delquery = "delete from team";
        database.execSQL(delquery);
        database.close();
    }

    public void insertresults(HashMap<String,String> scorefromserver) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", scorefromserver.get("resultid"));
        values.put("mainresult", scorefromserver.get("mainresult"));
        values.put("mom", scorefromserver.get("mom"));



        database.insertWithOnConflict("results", null,
                values, SQLiteDatabase.CONFLICT_IGNORE);
        database.close();
    }

    public ArrayList<HashMap<String, String>> getresults() {
        ArrayList<HashMap<String, String>> usersList;

        usersList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM results ";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String,String> map = new HashMap<>();
                map.put("resultsid", cursor.getString(0));
                map.put("mainresult", cursor.getString(1));
                map.put("mom", cursor.getString(2));


                usersList.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return usersList;
    }

    public void deleteresults()
    {   SQLiteDatabase database = this.getWritableDatabase();
        String delquery = "delete from results";
        database.execSQL(delquery);
        database.close();
    }

}
