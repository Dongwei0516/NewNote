package com.example.newnote;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
	private static String strPW;
	private static String strOrderItem;
	private static String strOrderSort;
	protected final static int MENU_ADD = Menu.FIRST;
	protected final static int MENU_SET = Menu.FIRST + 1;
	protected final static int MENU_ABOUT = Menu.FIRST + 2;
	protected final static int MENU_EXIT = Menu.FIRST + 3;

	
	 String[] books = new String[]{
	            "你好", "ShenZhen", "ShangHai","BeiJing","HaiNan","Shangdong","Shantou"
	         ,"随风而去","随你便","你是"};
	
	
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
		emptyInfo();

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

	public boolean onCreateOptionsMenu(Menu menu) {
		// 添加MENU
		menu.add(Menu.NONE, MENU_ADD, 0, "新增").setIcon(
				android.R.drawable.ic_menu_add);
		menu.add(Menu.NONE, MENU_SET, 0, "密码及排序").setIcon(
				android.R.drawable.ic_menu_preferences);
		menu.add(Menu.NONE, MENU_ABOUT, 0, "关于产品").setIcon(
				android.R.drawable.ic_menu_info_details);
		menu.add(Menu.NONE, MENU_EXIT, 0, "退出").setIcon(
				android.R.drawable.ic_menu_close_clear_cancel);
		return super.onCreateOptionsMenu(menu);
	}

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
				.setTitle("请输入密码")
				.setView(buttonEditText)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
							Toast.makeText(Notepad.this, "密码错误！",
									Toast.LENGTH_LONG).show();
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
					}
				}).show();
	}
	

	private void deleteDialog() {
		new AlertDialog.Builder(this).setMessage("编辑日记")
				.setPositiveButton("删除", new DialogInterface.OnClickListener() {
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
				}).setNegativeButton("修改", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						onListItemClick(0, "edit");
					}
				}).show();
	}

	private void aboutOptionsDialog() {
		new AlertDialog.Builder(this)
		.setMessage("使用说明")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
				.setTitle(" 退出")
				.setMessage("你确定要退出吗")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
						System.exit(0);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialoginterface, int i) {
					}
				}).show();
	}

	void onListItemClick(int index, String op) {
		// new一个Intent对象，并指定要启动的class
		Intent intent = null;
		intent = new Intent(this, mActivities[index]);
		// new一个Bundle对象，并将要传递的数据传入
		Bundle bundle = new Bundle();
		bundle.putInt("id", id);
		bundle.putString("title", title);
		bundle.putString("content", content);
		bundle.putString("use_pw", use_pw);
		bundle.putString("op", op);
		bundle.putString("strPW", strPW);
		bundle.putString("strOrderItem", strOrderItem);
		bundle.putString("strOrderSort", strOrderSort);
		// 将Bundle对象assign给Intent
		intent.putExtras(bundle);
		// 调用一个新的Activity
		this.startActivity(intent);
		// 关闭原来的Activity
		this.finish();
            	}

	public static void initSettings(Context context) {
		setCursor.moveToFirst();
		strPW = setCursor.getString(1);
		setCursor.moveToNext();
		strOrderItem = setCursor.getString(1);
		setCursor.moveToNext();
		strOrderSort = setCursor.getString(1);

		myCursor = myToDoDB.select(strOrderItem, strOrderSort);
		// new SimpleCursorAdapter并将myCursor传入
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

	protected void onDestroy() {
		System.exit(0);
		super.onDestroy();
	}
}