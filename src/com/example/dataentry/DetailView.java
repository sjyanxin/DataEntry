package com.example.dataentry;


import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.task.DownLoadImageTask;

public class DetailView extends Activity {

	private int id;
	private String menuName;
	private String menuPrice;
	private String menuPic;
	private Bundle bundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.detailview);
		bundle = this.getIntent().getExtras();
		id=bundle.getInt("RowId");
		menuName=bundle.getString("MenuName");
		menuPrice=bundle.getString("MenuPrice");
		menuPic=bundle.getString("SaleUnit");

		TextView tvName = (TextView) this.findViewById(R.id.tvMenuName);
		TextView tvPrice = (TextView) this.findViewById(R.id.tvPrice);
		TextView tvUnit = (TextView) this.findViewById(R.id.tvUnit);
		ImageView detailImageView = (ImageView)findViewById(R.id.detailImageView);
		Button btn=(Button) this.findViewById(R.id.btnBack);
		btn.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		Button btn_edit=(Button) this.findViewById(R.id.btn_view_edit);
		btn_edit.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				intent.setClass(DetailView.this, AddView.class);
						
				
				bundle.putBoolean("IsEdit", true);
				/*	mBundle.putInt("RowId",
							Integer.parseInt(map.get("RowId").toString()));
					mBundle.putString("MenuName", map.get("MenuName").toString());
					mBundle.putString("MenuPrice", map.get("MenuPrice").toString());
					mBundle.putString("SaleUnit", map.get("SaleUnit").toString());
					mBundle.putString("MenuPic", map.get("MenuPic").toString());*/

					intent.putExtras(bundle);
					startActivityForResult(intent, MainActivity.MENU_EDIT_ID);
					finish();
				
			}
		});
		
		tvName.setText(bundle.getString("MenuName"));
		tvPrice.setText(bundle.getString("MenuPrice"));
		tvUnit.setText(bundle.getString("SaleUnit"));

		String s1 = bundle.getString("MenuPic");
		if (s1 != null)
		{			
			s1 = ServiceHelper.SERVICE_ROOT_URI + s1;			
		}
		new DownLoadImageTask(detailImageView).execute(s1);

		
	}

}
