package com.example.first;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class clockSQLiteHelper extends SQLiteOpenHelper {

	private double p=0.00001;
	//private final  String database_name="clockstore.db";
	public clockSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		try{
		this.getWritableDatabase(); //只有在建立过程中才会去生成
		// TODO Auto-generated constructor stub
		}catch(Exception e)
		{
			Log.v("error", "SQLhelper "+e.getMessage());
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub the first sqlcreate
		try{
		String sql="CREATE TABLE clock ("
			+"[id]          INTEGER PRIMARY KEY AUTOINCREMENT ,"
			+"[x]            REAL,"
			+"[y]            REAL,"
			+"[startTime]    INTEGER DEFAULT 0,"
			+"[endTime]      INTEGER DEFAULT 0,"
			+"[content]      TEXT);";
		db.execSQL(sql);
		}catch(Exception e)
		{
			Log.v("error", "create error");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	public Cursor query( double xc,double yc,double p)
    {
		try{
			double xMax=xc+p,xMin=xc-p,yMax=yc+p,yMin=yc-p;
			Log.v("JUDGES",xMin+"  "+xMax+"==="+yMin+" "+yMax);
		SQLiteDatabase dbb=this.getWritableDatabase();
		String sql="select * from clock where x<"+xMax+" AND x>"+xMin+" AND y<"+yMax+" AND y>"+yMin;
    	Cursor cursor=dbb.rawQuery(sql, null);
    	return cursor;
		}catch(Exception e)
		{
			Log.v("error","SQLitehelper query "+e.toString());
			return null;
		}
    	
    } 
	public int querys()
    {
		try{
			
		SQLiteDatabase dbb=this.getWritableDatabase();
		String sql="select count(*) from clock ;";
    	Cursor cursor=dbb.rawQuery(sql, null);
    	cursor.moveToFirst();
    	Log.v("COUNT",Integer.toString(cursor.getInt(0)));
    	return cursor.getInt(0);
    	//return cursor;
		}catch(Exception e)
		{
			Log.v("error","SQLitehelper query "+e.toString());
			return 0;
		}
    	
    } 
	
	public Cursor queryall()
    {
		try{
		SQLiteDatabase dbb=this.getWritableDatabase();
		String sql="select * from clock ;";
    	Cursor cursor=dbb.rawQuery(sql, null);
    	return cursor;
		}catch(Exception e)
		{
			Log.v("error","SQLitehelper query "+e.toString());
			return null;
		}
    	
    } 
	
	public boolean insert(double x,double y,int start ,int end,String content)
	{
		SQLiteDatabase dbb=this.getWritableDatabase();
		ContentValues value;
		value=new ContentValues();
		double t=x;
		x=y;y=t;
		value.put("x", x);
		value.put("y", y);
		Log.v("INSERT",x+" "+y);
		value.put("startTime",start );
		value.put("endTime", end);
		value.put("content",content);
		if(dbb.insert("clock", null, value)>0) return true;
		else return false;
		
	}
	
	public boolean delete(int id)
	{
		SQLiteDatabase dbb=this.getWritableDatabase();
		if(dbb.delete("clock", "id="+id, null)>0) return true;
		else return false;
	}
	

}
