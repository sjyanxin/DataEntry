package com.example.task;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView imageView;

	public DownLoadImageTask(ImageView imageView) {
		// TODO Auto-generated constructor stub
		this.imageView = imageView;
	}

	
	@Override
	protected Bitmap doInBackground(String... urls) {
		// TODO Auto-generated method stub
		String url = urls[0];
		if(url==null)return null;
		Bitmap tmpBitmap = null;
		try {
			InputStream is = new java.net.URL(url).openStream();
			tmpBitmap = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("test", e.getMessage());
		}
		return tmpBitmap;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		if(result==null)return;
		imageView.setImageBitmap(result);
	}

}
