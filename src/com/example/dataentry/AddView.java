package com.example.dataentry;

import org.json.JSONException;
import org.json.JSONStringer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.task.AddTask;
import com.example.task.DownLoadImageTask;

public class AddView extends Activity implements OnClickListener {

	Intent intent = null;
	EditText tvName;
	EditText tvPrice;
	EditText tvUnit;
	ImageView imageView;
	Button btnBrowse;
	private String picPath = null;
	Button btnSave;
	Button btnBack;
	boolean isEdit;
	String menuPic;

	int id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.addview);
		intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		tvName = (EditText) this.findViewById(R.id.edMenuName);
		tvPrice = (EditText) this.findViewById(R.id.edMenuPrice);
		tvUnit = (EditText) this.findViewById(R.id.edMenuUnit);
		imageView = (ImageView) this.findViewById(R.id.imageView1);
		btnBrowse = (Button) this.findViewById(R.id.btnBrowse);
		btnBrowse.setOnClickListener(this);
		btnSave = (Button) this.findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		btnBack = (Button) this.findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AddView.this.finish();
			}
		});

		isEdit = bundle.getBoolean("IsEdit");
		if (isEdit) {
			tvName.setText(bundle.getString("MenuName"));
			tvPrice.setText(bundle.getString("MenuPrice"));
			tvUnit.setText(bundle.getString("SaleUnit"));
			// tvPic.setText(bundle.getString("MenuPic"));

			menuPic = bundle.getString("MenuPic");
			String pic=menuPic;
			if (pic != null)
				pic = ServiceHelper.SERVICE_ROOT_URI + pic;
			new DownLoadImageTask(imageView).execute(pic);

			id = bundle.getInt("RowId");
		}

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBrowse:
			/***
			 * ����ǵ���android���õ�intent��������ͼƬ�ļ� ��ͬʱҲ���Թ���������
			 */
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			// �ص�ͼƬ��ʹ�õ�
			startActivityForResult(intent, RESULT_CANCELED);
			break;
		case R.id.btnSave:			
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			try {

				String pic = ServiceHelper.getBase64(this.picPath);
				if (pic == null)
					pic = menuPic;

				JSONStringer json = new JSONStringer().object().key("menu")
						.object().key("MenuName")
						.value(tvName.getText().toString()).key("MenuPrice")
						.value(Double.parseDouble( tvPrice.getText().toString())).key("MenuPic")
						.value(pic).key("RowId").value(id)
						.key("SaleUnit")
						.value(tvUnit.getText().toString())
						.endObject().endObject();

				AddTask task = new AddTask(this, isEdit);
				task.execute(json.toString());

			} catch (JSONException e) {
				e.printStackTrace();
			}

			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			/**
			 * ��ѡ���ͼƬ��Ϊ�յĻ����ڻ�ȡ��ͼƬ��;��
			 */
			Uri uri = data.getData();

			try {
				String[] pojo = { MediaStore.Images.Media.DATA };

				Cursor cursor = managedQuery(uri, pojo, null, null, null);
				if (cursor != null) {
					ContentResolver cr = this.getContentResolver();
					int colunm_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);
					/***
					 * ���������һ���ж���Ҫ��Ϊ�˵����������ѡ�񣬱��磺ʹ�õ��������ļ��������Ļ�����ѡ����ļ��Ͳ�һ����ͼƬ�ˣ�
					 * �����Ļ��������ж��ļ��ĺ�׺�� �����ͼƬ��ʽ�Ļ�����ô�ſ���
					 */
					if (path.endsWith("jpg") || path.endsWith("png")) {
						picPath = path;
						Bitmap bitmap = BitmapFactory.decodeStream(cr
								.openInputStream(uri));
						imageView.setImageBitmap(bitmap);
					} else {
						alert();
					}
				} else {
					alert();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * �ص�ʹ��
		 */
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void alert() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("��ʾ")
				.setMessage("��ѡ��Ĳ�����Ч��ͼƬ")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						picPath = null;
					}
				}).create();
		dialog.show();
	}
}
