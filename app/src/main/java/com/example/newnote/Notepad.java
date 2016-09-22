package com.example.newnote;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Notepad extends Activity {
	public static ToDoDB myToDoDB;
	public static Cursor myCursor;
	public static Cursor setCursor;
	public static ListView myListView;
	private static TextView textView1;
	private TextView buttonTextView;
	private EditText buttonEditText;
	private int id;
	private String title;
	private String content;
	private String use_pw;
	private String strPW;
	private String strOrderItem;
	private String strOrderSort;
	protected final static int MENU_ADD = Menu.FIRST;
	protected final static int MENU_SET = Menu.FIRST + 1;
	protected final static int MENU_ABOUT = Menu.FIRST + 2;
	protected final static int MENU_EXIT = Menu.FIRST + 3;

	
	 String[] books = new String[]{
	            "ShenZhen", "ShangHai","BeiJing"
	 };

	
	Class<?> mActivities[] = { InputPage.class, Settings.class ,showpage.class};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listpage);
        
		  ArrayAdapter<String> adr = new ArrayAdapter<String>( this,
		             android.R.layout.simple_dropdown_item_1line,
		             books);
		        AutoCompleteTextView  actv = (AutoCompleteTextView )
		        findViewById(R.id.auto);// set Adapter
		        actv.setAdapter(adr);
		
		textView1 = (TextView) findViewById(R.id.textView1);
		myListView = (ListView) findViewById(R.id.myListView);
		buttonTextView = new TextView(this);

		myToDoDB = new ToDoDB(this);
		// 获得DataBase里的数据
		setCursor = myToDoDB.getSettings();
		initSettings(this);


		// 将myListView添加OnItemClickListener
		myListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// 将myCursor移到所单击的值
						myCursor.moveToPosition(arg2);
						// 取得字段_id的值
						id = myCursor.getInt(0);
						title = myCursor.getString(1);
						content = myCursor.getString(2);
						use_pw = myCursor.getString(4);
						if (use_pw.equals("★")) {
							buttonEditText = new EditText(Notepad.this);
							verifyDialog(1);
						} else {
							onListItemClick(2, "edit");
						}
					}
				});

		myListView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						// 将myCursor移到所单击的值
						myCursor.moveToPosition(arg2);
						// 取得字段_id的值
						id = myCursor.getInt(0);
						use_pw = myCursor.getString(4);
						if (use_pw.equals("★")) {
							buttonEditText = new EditText(Notepad.this);
							verifyDialog(2);
						} else {
							deleteDialog();
						}
						return false;
					}
				});

		myListView
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
					}

					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
	}


	@Override
	public void onResume(){
		super.onResume();
		refresh();
	}

	public void refresh() {
		onCreate(null);
	}



    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 添加MENU
		menu.add(Menu.NONE, MENU_ADD, 0, R.string.newnote).setIcon(
				android.R.drawable.ic_menu_add);
		menu.add(Menu.NONE, MENU_SET, 0, R.string.title1).setIcon(
				android.R.drawable.ic_menu_preferences);
		menu.add(Menu.NONE, MENU_ABOUT, 0, R.string.title2).setIcon(
				android.R.drawable.ic_menu_info_details);
		menu.add(Menu.NONE, MENU_EXIT, 0, R.string.exit).setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return super.onCreateOptionsMenu(menu);
	}

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_ADD:
			title = "";
			content = "";
			use_pw = "";
			onListItemClick(0, "add");
			break;
		case MENU_SET:
			onListItemClick(1, "set");
			break;
		case MENU_ABOUT:
			aboutOptionsDialog();
			break;
		case MENU_EXIT:
			exitOptionsDialog();
			break;
		}
		return true;
	}

	private void verifyDialog(final int op) {
		new AlertDialog.Builder(this)
				.setTitle(R.string.inputpassword)
				.setView(buttonEditText)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						buttonTextView.setText(buttonEditText.getText()
								.toString());
						if (buttonTextView.getText().toString().equals(strPW)) {
							switch (op) {
							case 1:
								onListItemClick(2, "edit");
								break;
							case 2:
								deleteDialog();
								break;
							}
						} else {
							Toast.makeText(Notepad.this, R.string.wrongpassword,
									Toast.LENGTH_LONG).show();
						}
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
					}
				}).show();
	}
	

	private void deleteDialog() {
		new AlertDialog.Builder(this).setMessage(R.string.edit)
				.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						if (id == 0)
							return;
						// 删除数据
						myToDoDB.delete(id);
						myCursor.requery();
						myListView.invalidateViews();
						id = 0;
						emptyInfo();
					}
				}).setNegativeButton(R.string.edit, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						onListItemClick(0, "edit");
					}
				}).show();
	}

	private void aboutOptionsDialog() {
		new AlertDialog.Builder(this)
		.setMessage(R.string.introduce_name)
		.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						Intent intent=null;
						intent = new Intent(Notepad.this,
								introduce.class);
						startActivity(intent);
					}
				}).show();
	}

	private void exitOptionsDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.exit)
				.setMessage(R.string.exit_confirm)
				.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						System.exit(0);
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
					}
				}).show();
	}

	void onListItemClick(int index, String op) {
		Intent intent = null;
		intent = new Intent(this, mActivities[index]);
		Bundle bundle = new Bundle();
		bundle.putInt("id", id);
		bundle.putString("title", title);
		bundle.putString("content", content);
		bundle.putString("use_pw", use_pw);
		bundle.putString("op", op);
		bundle.putString("strPW", strPW);
		bundle.putString("strOrderItem", strOrderItem);
		bundle.putString("strOrderSort", strOrderSort);
		intent.putExtras(bundle);
		this.startActivity(intent);
            	}

	public void initSettings(Context context) {
		setCursor.moveToFirst();
		strPW = setCursor.getString(1);
		setCursor.moveToNext();
		strOrderItem = setCursor.getString(1);
		setCursor.moveToNext();
		strOrderSort = setCursor.getString(1);

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(context,
				R.layout.main, myCursor, new String[] { "use_pw", "title",
						"upt_date" }, new int[] { R.id.listTextView3,
						R.id.listTextView1, R.id.listTextView2 });
		myListView.setAdapter(adapter);
	}

	public static void emptyInfo() {
		if (myCursor.getCount() > 0) {
			textView1.setHeight(0);
		} else {
			textView1.setHeight(100);
		}
	}


}