package com.example.newnote;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ShowpageActivity extends Activity {

    private TextView titleText;
    private TextView contentText;
    public ToDoDB myToDoDB;
    private int id;
    private String title;
    private String content;
    private String use_pw;
    public Cursor myCursor;
    protected final static int MENU_EDIT = Menu.FIRST;
    protected final static int MENU_DELECT = Menu.FIRST + 1;
    int index;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showpage);

        try {
            myToDoDB = new ToDoDB(this);
            myCursor = myToDoDB.select("", "");
            Intent intent = getIntent();
            index = intent.getIntExtra("index", -1);
            myCursor.moveToPosition(index);
            title = intent.getStringExtra("title");
            content = intent.getStringExtra("content");
            use_pw = intent.getStringExtra("use_pw");

//		id = myCursor.getInt(0);
//        title = myCursor.getString(1);
//        content = myCursor.getString(2);

            titleText = (TextView) findViewById(R.id.title);
            contentText = (TextView) findViewById(R.id.content);

            titleText.setText(title);
            contentText.setText(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 添加MENU
        menu.add(Menu.NONE, MENU_EDIT, 0, R.string.edit).setIcon(
                android.R.drawable.ic_menu_add);
        menu.add(Menu.NONE, MENU_DELECT, 0, R.string.delete).setIcon(
                android.R.drawable.ic_menu_preferences);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case MENU_EDIT:
                editDialog();
                break;
            case MENU_DELECT:
                deleteDialog();
                break;
        }
        return true;
    }


    private void editDialog() {
        try {
            Intent intent = new Intent(this, InputpageActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("title", title);
            intent.putExtra("content", content);
            intent.putExtra("op", "edit");
            intent.putExtra("use_pw", "use_pw");
            startActivityForResult(intent, 2002);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2002 && resultCode == RESULT_OK) {
            myCursor.moveToPosition(index);
            title = myCursor.getString(1);
            content = myCursor.getString(2);
            titleText.setText(title);
            contentText.setText(content);
            setResult(RESULT_OK);
        }
    }

    private void deleteDialog() {
        myCursor.moveToPosition(0);
        id = myCursor.getInt(0);
        if (id == 0)
            return;
        myToDoDB.delete(id);
        myCursor.requery();
        id = 0;
        setResult(RESULT_OK);
        finish();
    }
}





