package com.example.task;

import com.example.dataentry.PullToRefreshListView;
import com.example.dataentry.ServiceHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class AddTask extends AsyncTask<String, Void, String> {

	private ProgressDialog dialog;
	private Activity context;
	private boolean isEdit;
	
	
	public AddTask(Activity context,boolean isEdit) {		
		this.context = context;		
		this.isEdit=isEdit;
	}

	@Override
	protected void onPreExecute() {
		dialog = ProgressDialog.show(context, null, "正在保存。。。");
		// dialog.setCancelable(true);
		// dialog.setOnCancelListener(onCancelListener);
		dialog.setOwnerActivity((Activity) context);
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {

	
		String json = params[0];
		String str = null;
		if (!isEdit) {
			str = ServiceHelper.SendPostRequest("Create", json);
		} else {
			str = ServiceHelper.SendPostRequest("Update", json);
		}
		return str;
	}

	@Override
	protected void onPostExecute(String result) {

		if ("true".equals(result))
			Toast.makeText(context, "保存成功！", Toast.LENGTH_LONG).show();
		else
			Toast.makeText(context, "保存失败！", Toast.LENGTH_LONG).show();
		context.finish();

		/*refreshableView.setSelection(0);
		refreshableView.prepareForRefresh();
		refreshableView.onRefresh();*/

	}

}
