package com.example.task;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.dataentry.PullToRefreshListView;
import com.example.dataentry.ServiceHelper;

public class DeleteTask extends AsyncTask<String, Void, String> {

	private Activity context;
	private SimpleAdapter simpleAdapter;
	private ArrayList<Map<String, Object>> arrayList;
	private int selectedIndex;
	private PullToRefreshListView refreshableView;

	public DeleteTask(Activity context, PullToRefreshListView refreshableView) {
		super();
		this.context = context;
		// this.simpleAdapter = simpleAdapter;
		// this.arrayList = arrayList;
		// this.selectedIndex = selectedIndex;
		this.refreshableView = refreshableView;
	}

	@Override
	protected String doInBackground(String... params) {
		String json = params[0];
		String result = ServiceHelper.SendPostRequest("Delete", json);
		return result;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {
		if (result.equals("true")) {
			Toast.makeText(context, "É¾³ý³É¹¦£¡", Toast.LENGTH_LONG).show();

			refreshableView.setSelection(0);
			refreshableView.prepareForRefresh();
			refreshableView.onRefresh();

		}
	}

}
