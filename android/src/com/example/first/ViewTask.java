package com.example.first;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ViewTask extends Activity {
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		SQLiteHelper.close();
		super.onDestroy();
	}

	private ArrayList<HashMap<String, Object>> listItem;
	private clockSQLiteHelper SQLiteHelper;
	ListView list;
	SimpleAdapter listItemAdapter;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_task);

        list = (ListView) findViewById(R.id.list1);
        
        //生成动态数组，加入数据
        
        listItem = new ArrayList<HashMap<String, Object>>();
		SQLiteHelper =new clockSQLiteHelper(this,"clockstore.db",null,1);
		
        initdata(SQLiteHelper.querys());
        //生成适配器的Item和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(this,listItem,//数据源 
            R.layout.itemrow,//ListItem的XML实现
            //动态数组与ImageItem对应的子项        
            new String[] {"ItemTitle", "ItemText"}, 
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID
            new int[] {R.id.ItemTitle,R.id.ItemText}
        );
       
        //添加并且显示
        list.setAdapter(listItemAdapter);

        
        //添加点击
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final int position = arg2;
				Builder dialog = new AlertDialog.Builder(ViewTask.this);
                dialog.setTitle("详情：");
                dialog.setIcon(android.R.drawable.ic_dialog_info);
                dialog.setMessage(listItem.get(position).get("ItemText").toString());
                dialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        
                    }
                });
                dialog.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	
                    	SQLiteHelper.delete(Integer.parseInt(listItem.get(position).get("ItemText").toString()));
                    	
                    	listItem.remove(position);
                       // listItem.remove("Level"+position);
                        listItemAdapter.notifyDataSetChanged();
                        //list.setAdapter(listItemAdapter);
                        //list.invalidate();
                    }
                });
                dialog.show();
			}
		});
        
      //添加长按点击
       
    }	
	public void initdata(int count)
	{
		Cursor cursor=SQLiteHelper.queryall();
		cursor.moveToFirst();
		
        for(int i=0;i<count;i++)
        {
        	HashMap<String, Object> map = new HashMap<String, Object>();
        	
        	map.put("ItemTitle", cursor.getString(cursor.getColumnIndex("content")));
        	map.put("ItemText", cursor.getInt(cursor.getColumnIndex("id")));
        	listItem.add(map);
        	cursor.moveToNext();
        }
		
	}

	//长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		setTitle("点击了长按菜单里面的第"+item.getItemId()+"个项目"); 
		return super.onContextItemSelected(item);
	}
}