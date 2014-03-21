package com.example.dataentry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.example.task.DeleteTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.os.Handler;
import android.os.Message;

import android.util.Base64;

import android.view.Gravity;
import android.view.Menu;

import android.view.View;
import android.view.Window;

import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;

import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final int MENU_VIVE_ID = 1001;
	public static final int MENU_EDIT_ID = 1002;
	public static final int MENU_DELETE_ID = 1003;
	public static final int MENU_CANCEL_ID = 1004;
	public static final int MENU_ADD_ID = 1005;

	public static final int ACTION_DELETE = 2002;
	public static final int ACTION_SEARCH = 2004;

	int action;
	final Context context = this;

	protected ArrayList<Map<String, Object>> arrayList;
	protected SimpleAdapter simpleAdapter;// 适配器
	MyAdapter adapter;
	int selectIndex;
	SelectMenuPopupWindow menuWindow;
	PullToRefreshListView refreshableView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		refreshableView = (PullToRefreshListView) findViewById(R.id.refreshable_view);
		refreshableView
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
					@Override
					public void onRefresh(PullToRefreshListView list) {
						MainActivity.this.callService(ACTION_SEARCH);
					}
				});

		arrayList = new ArrayList<Map<String, Object>>();

		simpleAdapter = new SimpleAdapter(this, arrayList,// 数据来源
				R.layout.listitem,// XML实现
				new String[] { "MenuName" }, // 动态数组与ImageItem对应的子项
				new int[] { R.id.listItem });

		refreshableView.setAdapter(simpleAdapter);
		// 添加消息处理
		showListViewContextMenu();
		// 监听列表项目的长按事件
		refreshableView
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						selectIndex = position - 1;
						// 实例化SelectPicPopupWindow
						menuWindow = new SelectMenuPopupWindow(
								MainActivity.this, itemsOnClick);
						// 显示窗口
						menuWindow.showAtLocation(
								MainActivity.this.findViewById(R.id.main),
								Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
								0); // 设置layout在PopupWindow中显示的位置

						return false;
					}

				});

		// 监听列表项目的普通点击事件
		refreshableView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectIndex = position - 1;
				ViewMenu();
			}
		});

		refreshableView.setSelection(0);
		refreshableView.prepareForRefresh();
		refreshableView.onRefresh();

		Button btn_new = (Button) this.findViewById(R.id.btn_new);
		btn_new.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpToAddOrEditPage(false);
			}
		});
	}

	void callService(int action) {
		this.action = action;
		new Thread(runnable).start();
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			if (action == ACTION_SEARCH) {
				getData();
			}
		}
	};

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			if (action == ACTION_SEARCH) {
				simpleAdapter.notifyDataSetChanged();
				refreshableView.onRefreshComplete();
			}

		}
	};

	public void getData() {

		String result = ServiceHelper.SendGetRequest("SearchAll", null);
		if (result == null)
			return;

		try {
			arrayList.clear();
			JSONArray jsonArray = new JSONArray(result);
			for (int i = 0; i < jsonArray.length(); ++i) {
				JSONObject o = (JSONObject) jsonArray.get(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("RowId", o.get("RowId"));
				map.put("MenuPic", o.get("MenuPic"));
				map.put("MenuName", o.get("MenuName"));
				map.put("MenuPrice", o.get("MenuPrice"));
				map.put("SaleUnit", o.get("SaleUnit"));
				arrayList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		handler.obtainMessage(1, null).sendToTarget();

	}

	void ViewMenu() {
		Map<String, Object> map = this.readItem(selectIndex);
		Intent intent = new Intent();
		intent.setClass(context, DetailView.class);
		Bundle mBundle = new Bundle();
		mBundle.putInt("RowId",Integer.parseInt( map.get("RowId").toString()));
		mBundle.putString("MenuName", map.get("MenuName").toString());
		mBundle.putString("MenuPrice", map.get("MenuPrice").toString());
		mBundle.putString("SaleUnit", map.get("SaleUnit").toString());
		mBundle.putString("MenuPic", map.get("MenuPic").toString());
		intent.putExtras(mBundle);
		startActivity(intent);

	}

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent data) {

		switch (requestCode) {
			case MENU_ADD_ID:
			case MENU_EDIT_ID: {
				refreshableView.setSelection(0);
				refreshableView.prepareForRefresh();
				refreshableView.onRefresh();
			}
		}
	}

	

	void jumpToAddOrEditPage(boolean isEdit) {
		Intent intent = new Intent();
		intent.setClass(context, AddView.class);
		Bundle mBundle = new Bundle();
		if (isEdit) {
			Map<String, Object> map = this.readItem(selectIndex);
			mBundle.putBoolean("IsEdit", true);
			mBundle.putInt("RowId",
					Integer.parseInt(map.get("RowId").toString()));
			mBundle.putString("MenuName", map.get("MenuName").toString());
			mBundle.putString("MenuPrice", map.get("MenuPrice").toString());
			mBundle.putString("SaleUnit", map.get("SaleUnit").toString());
			mBundle.putString("MenuPic", map.get("MenuPic").toString());

			intent.putExtras(mBundle);
			this.startActivityForResult(intent, MENU_EDIT_ID);
			action = MENU_EDIT_ID;
		} else {
			mBundle.putBoolean("IsEdit", false);
			intent.putExtras(mBundle);
			this.startActivityForResult(intent, MENU_ADD_ID);
			action = MENU_ADD_ID;

		}
	}

	public Map<String, Object> readItem(int index) {
		return arrayList.get(index);
	}

	public void showListViewContextMenu() {

	}

	private OnClickListener itemsOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_view:
				ViewMenu();
				break;
			case R.id.btn_view_edit:
				jumpToAddOrEditPage(true);
				break;
			case R.id.btn_delete:
				DeleteTask task = new DeleteTask((Activity) context,
						refreshableView);
				String id = readItem(selectIndex).get("RowId").toString();

				try {
					JSONStringer json = new JSONStringer().object().key("menu")
							.object().key("RowId").value(id).endObject()
							.endObject();
					task.execute(json.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}

				// callService(ACTION_DELETE);
				break;
			}

		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// 清空列表
	public void RemoveAll() {
		arrayList.clear();
		simpleAdapter.notifyDataSetChanged();
	}

}
