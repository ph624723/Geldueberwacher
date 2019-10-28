package com.example.emptytest.datamanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.emptytest.Generic.SortableList;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "CostItemDatabase";    // Database Name
    //private static final String CostItems_TABLE_NAME = "CostItemsTable1";   // Table Name
    private static final int DATABASE_Version = 1;    // Database Version
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMyyyy");

    //Columns
        /*private static final String UID="_id";    // (Primary Key)
        private static final String SUBJECT = "Subject";
        private static final String VALUE = "Value";
        private static final String DATE = "Date";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+CostItems_TABLE_NAME;
        */

    private static final String CREATE_TABLE_COSTITEMS = "Create Table EmpInfo(ID Integer Primary Key AutoIncrement, ItemSubject Text, ItemValue Double, ItemDate Int8, ItemCategory Integer, ItemInfos Text)";

    private static DatabaseHandler mInstance = null;
    public static DatabaseHandler getmInstance(Context context){
        if (mInstance == null){
            mInstance = new DatabaseHandler(context);
        }
        return mInstance;
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COSTITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    private ContentValues makeContentValues(CostItem item){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ItemSubject", item.getSubject());
        contentValues.put("ItemValue", item.getValue());
        contentValues.put("ItemDate", item.getDate().getTime());
        contentValues.put("ItemCategory", item.getCategory().getId());
        contentValues.put("ItemInfos",item.getInfos());
        return contentValues;
    }

    public long insertCostItem(CostItem item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert("EmpInfo", null , makeContentValues(item));
        return id;
    }

    public Cursor getItemList(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select ID, ItemSubject, ItemValue, ItemDate, ItemCategory, ItemInfos from EmpInfo";
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public Cursor getItemListByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select ID, ItemSubject, ItemValue, ItemDate, ItemCategory, ItemInfos from EmpInfo Where ID Like "+id;
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public Cursor getItemListByCategoryAndDate(int catId, Long startDate, Long endDate){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select ID, ItemSubject, ItemValue, ItemDate, ItemCategory, ItemInfos from EmpInfo Where ItemCategory Like "+catId;
        if(startDate != null){
            query += " And ItemDate >= "+startDate;
        }
        if(endDate != null){
            query += " And ItemDate <= "+endDate;
        }
        Cursor cursor = db.rawQuery(query,null);
        return cursor;
    }

    public int delete(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] whereArgs ={id+""};

        int count =db.delete("EmpInfo" , "ID = ?",whereArgs);
        return  count;
    }

    public int delete(CostItem item){
        return delete(item.getUid());
    }

    public int update(CostItem item){
            SQLiteDatabase db = this.getWritableDatabase();
            String[] whereArgs ={item.getUid()+""};
            int count = db.update("EmpInfo", makeContentValues(item), "ID = ?", whereArgs);
            //db.execSQL("UPDATE EmpInfo SET ItemSubject='"+item.subjectString()+"', ItemValue="+item.getValue()+", ItemDate="+item.getDate().getTime()+", ItemCategory="+item.getCategory().getId()+" WHERE ID="+item.getUid(),null);
            //delete(item.getUid());
            //long id = insertCostItem(new CostItem(item.getSubject(),item.getValue(),item.getCategory(),item.getDate()));
            return count; //id == -1 ? -1 : 1;
    }

    public SortableList<CostItem> getData() throws Exception
    {
        Cursor cursor = getItemList();
        return loadDataSet(cursor);
    }

    public SortableList<CostItem> getData(Categories.Category category, Timespans.Timespan timespan) throws Exception
    {
        Long startDate;
        Long endDate;
        Times t = Times.getTimesInstance();
        if (timespan.equals(Timespans.ALL_TIME)){
            startDate = null;
            endDate = null;
        }else if(timespan.equals(Timespans.LAST_MONTH)){
            startDate = t.getStartOfMonth(-1);
            endDate = t.getEndOfMonth(-1);
        }else if(timespan.equals(Timespans.THIS_MONTH)){
            startDate = t.getStartOfMonth(0);
            endDate = t.getEndOfMonth(0);
        }else if(timespan.equals(Timespans.LAST_WEEK)){
            startDate = t.getStartOfWeek(-1);
            endDate = t.getEndOfWeek(-1);
        }else if(timespan.equals(Timespans.THIS_WEEK)){
            startDate = t.getStartOfWeek(0);
            endDate = t.getEndOfWeek(0);
        }else{
            throw new Exception("Wrong timespan given");
        }

        Cursor cursor = getItemListByCategoryAndDate(category.getId(), startDate, endDate);
        return loadDataSet(cursor);
    }

    private SortableList<CostItem> loadDataSet(Cursor cursor) throws Exception{
        SQLiteDatabase db = this.getWritableDatabase();
        SortableList<CostItem> items = new SortableList<>();

        try{
            while (cursor.moveToNext())
            {
                int uid = cursor.getInt(cursor.getColumnIndex("ID"));
                String subject = cursor.getString(cursor.getColumnIndex("ItemSubject"));
                Double value = cursor.getDouble(cursor.getColumnIndex("ItemValue"));

                long dateLong = dateLong= cursor.getLong(cursor.getColumnIndex("ItemDate"));
                Date date = new Date(dateLong);

                Categories.Category category = Categories.getCategory(cursor.getInt(cursor.getColumnIndex("ItemCategory")));
                String infos = cursor.getString(cursor.getColumnIndex("ItemInfos"));

                items.add(new CostItem(uid,subject,value, category, infos, date));
            }
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
        cursor.close();
        return items;
    }

    public CostItem getById(int id) throws Exception {
        Cursor cursor = getItemListByID(id);
        SortableList<CostItem> items = loadDataSet(cursor);
        if (items.size() == 0) return null;
        else if (items.size() == 1) return items.get(0);
        else throw new Exception("Multiple database entries with id: " + id + ".");
    }
}
