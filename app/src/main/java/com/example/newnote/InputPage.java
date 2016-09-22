package com.example.newnote;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class InputPage extends Activity {
	private EditText editText1;
	private EditText editText2;
	private CheckBox checkBox1;
	private Button button1;
	private int id;
	private String title;
	private String content;
	private String use_pw;
	private String op;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputpage);
		// 取得intent中的bundle对象
		editText1 = (EditText) findViewById(R.id.title);
		editText2 = (EditText) findViewById(R.id.content);
		checkBox1 = (CheckBox) findViewById(R.id.checkbox);
		// 取得Intent中的Bundle对象
				Bundle bundle = this.getIntent().getExtras();
				id = bundle.getInt("id");
				title = bundle.getString("title");
				content = bundle.getString("content");
				use_pw = bundle.getString("use_pw");
				op = bundle.getString("op");
				if(op.equals("edit")){
					editText1.setText(title);
					editText2.setText(content);
					if(use_pw.equals("★")){
						checkBox1.setChecked(true);
					}
				}
		button1 = (Button) findViewById(R.id.save);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				saveDialog();
			}
		});

	}

	private void saveDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.save);
		builder.setMessage(R.string.save_confirm);
		builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialoginterface, int i) {
				saveTodo();
				Intent intent=null;
				intent = new Intent(InputPage.this,
						Notepad.class);
				startActivity(intent);
			}
		}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialoginterface, int i) {
			}
		}).show();
	}

	private void saveTodo() {
		if (editText1.getText().toString().equals("")) {
			editText1.setText(R.string.empty);
		}
		title = editText1.getText().toString();
		if (editText2.getText().toString().equals("")) {
			editText2.setText(R.string.empty);
		}
		content = editText2.getText().toString();

		if (checkBox1.isChecked()) {
			use_pw = "★";
		}
		if (op.equals("edit")) {
			Notepad.myToDoDB.update(id, title, content, use_pw);
		} else {
			Notepad.myToDoDB.insert(title, content, use_pw);
		}
		Notepad.myListView.invalidateViews();
		//Notepad.emptyInfo();
		Toast.makeText(InputPage.this, R.string.success, Toast.LENGTH_LONG).show();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			String strPW = "";
			if (checkBox1.isChecked()) {
				strPW = "★";
			}
			if (title.equals(editText1.getText().toString())
					&& content.equals(editText2.getText().toString())
					&& use_pw.equals(strPW)) {
				InputPage.this.finish();
			} else {
				new AlertDialog.Builder(this)
						.setTitle(R.string.save)
						.setMessage(R.string.save_confirm)
						.setPositiveButton(R.string.save,
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialoginterface,
											int i) {
										saveTodo();
										Intent intent=null;
										intent = new Intent(InputPage.this,
												Notepad.class);
										startActivity(intent);
									}
								})
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialoginterface,
											int i) {
										InputPage.this.finish();
									}
								}).show();
			}
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}