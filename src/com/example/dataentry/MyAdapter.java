package com.example.dataentry;


import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	
	private Context context;	
	private final ArrayList<Map<String, Object>> srcTable;
	
	public MyAdapter(Context context, ArrayList<Map<String, Object>> srcTable) {
		this.context = context;
		this.srcTable = srcTable;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			//gridView = inflater.inflate(R.layout.item, null);
			
			ImageView img = (ImageView) gridView .findViewById(R.id.imageView1);
//			TextView textView1 = (TextView) gridView.findViewById(R.id.textView1);
//			TextView textView2 = (TextView) gridView.findViewById(R.id.tvMenuName);
//			TextView textView3 = (TextView) gridView.findViewById(R.id.textView3);
			//Button button1= (Button) gridView.findViewById(R.id.btnSave);
			//Button button2= (Button) gridView.findViewById(R.id.button2);
			
			Map<String, Object> m= srcTable.get(position);
			
			img.setImageResource(R.drawable.ic_launcher);
//			textView1.setText(m.get("MenuName").toString());
//			textView2.setText(m.get("MenuPrice").toString());
//			textView3.setText(m.get("SaleUnit").toString());
			
		/*	button1.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View arg0) {					
					srcTable.remove(position);
					MyAdapter.this.notifyDataSetChanged();
				}
				
			});*/
			
			//button1.setText(m.get("ItemText4").toString());
			//button1.setText(m.get("ItemText5").toString());
			
		

		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

	@Override
	public int getCount() {
		return srcTable.size();
	}

	@Override
	public Object getItem(int position) {
		return srcTable.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
