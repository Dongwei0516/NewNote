package com.example.newnote;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class InputpageActivity extends Activity {
    private EditText editText1;
    private EditText editText2;
    private CheckBox checkBox1;
    private Button button1;
    private int id;
    private String title = "";
    private String content = "";
    private String use_pw = "";
    private String op = "";
    public ToDoDB myToDoDB;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.inputpage);
            // 取得intent中的bundle对象

            editText1 = (EditText) findViewById(R.id.title);
            editText2 = (EditText) findViewById(R.id.content);
            checkBox1 = (CheckBox) findViewById(R.id.checkbox);

            myToDoDB = new ToDoDB(this);
//		myCursor = myToDoDB.select("","");
//		myListView = new ListView(this);

//		Intent intent = getIntent();
//
//		int index  = intent.getIntExtra("index", -1);
//		myCursor.moveToPosition(index);
//		intent.putExtra("index",index);


//		title = intent.getStringExtra("title");
//		if (title == null) {
//			title= "back";
//		}

//		title = myCursor.getString(1);
//		content = myCursor.getString(2);
//
//
//		editText1.setText(title);
//		editText2.setText(content);

//		SharedPreferences sharedPreferences=getSharedPreferences("setting", Context.MODE_PRIVATE);
//		use_pw = sharedPreferences.getString("strPW",null);

//		use_pw = myCursor.getString(4);

            Bundle bundle = this.getIntent().getExtras();
            id = bundle.getInt("id");
            op = bundle.getString("op");
            title = bundle.getString("title");
            content = bundle.getString("content");
            use_pw = bundle.getString("use_pw");

            if (op.equals("edit")) {
                editText1.setText(title);
                editText2.setText(content);
                if (!TextUtils.isEmpty(use_pw) && use_pw.equals("★")) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.save);
        builder.setMessage(R.string.save_confirm);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
                saveTodo();
                finish();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoginterface, int i) {
            }
        }).show();
    }


    private void saveTodo() {
        try {
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
                myToDoDB.update(id, title, content);
            } else {
                myToDoDB.insert(title, content);
            }

//		myListView = (ListView) findViewById(R.id.myListView);
//		myListView.invalidateViews();
            Toast.makeText(InputpageActivity.this, R.string.success, Toast.LENGTH_LONG).show();

            setResult(RESULT_OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                InputpageActivity.this.finish();
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
                                        finish();
                                    }
                                })
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface,
                                            int i) {
                                        InputpageActivity.this.finish();
                                    }
                                }).show();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}

