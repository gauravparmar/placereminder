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
        
        //���ɶ�̬���飬��������
        
        listItem = new ArrayList<HashMap<String, Object>>();
		SQLiteHelper =new clockSQLiteHelper(this,"clockstore.db",null,1);
		
        initdata(SQLiteHelper.querys());
        //������������Item�Ͷ�̬�����Ӧ��Ԫ��
        listItemAdapter = new SimpleAdapter(this,listItem,//����Դ 
            R.layout.itemrow,//ListItem��XMLʵ��
            //��̬������ImageItem��Ӧ������        
            new String[] {"ItemTitle", "ItemText"}, 
            //ImageItem��XML�ļ������һ��ImageView,����TextView ID
            new int[] {R.id.ItemTitle,R.id.ItemText}
        );
       
        //��Ӳ�����ʾ
        list.setAdapter(listItemAdapter);

        
        //��ӵ��
        list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final int position = arg2;
				Builder dialog = new AlertDialog.Builder(ViewTask.this);
                dialog.setTitle("���飺");
                dialog.setIcon(android.R.drawable.ic_dialog_info);
                dialog.setMessage(listItem.get(position).get("ItemText").toString());
                dialog.setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        
                    }
                });
                dialog.setNegativeButton("ɾ��", new DialogInterface.OnClickListener() {
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
        
      //��ӳ������
       
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

	//�����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		setTitle("����˳����˵�����ĵ�"+item.getItemId()+"����Ŀ"); 
		return super.onContextItemSelected(item);
	}
}