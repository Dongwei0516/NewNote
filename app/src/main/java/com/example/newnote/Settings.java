package com.example.newnote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Settings extends Activity {
	private EditText editText1;
	private EditText editText2;
	private EditText editText3;
	private RadioGroup radioGroup1;
	private RadioGroup radioGroup2;
	private RadioButton radioButton1;
	private RadioButton radioButton2;
	private RadioButton radioButton3;
	private RadioButton radioButton4;
	private Button button1;
	private String strPW;
	private String strOrderItem;
	private String strOrderSort;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.passwordpage);

		editText1 = (EditText) findViewById(R.id.formerpassword);
		editText2 = (EditText) findViewById(R.id.newpasssword);
		editText3 = (EditText) findViewById(R.id.againpassword);
		radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
		radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
		radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
		radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
		radioButton4 = (RadioButton) findViewById(R.id.radioButton4);



	Bundle bundle = this.getIntent().getExtras();
	strPW = bundle.getString("strPW");
	strOrderItem = bundle.getString("strOrderItem");
	strOrderSort = bundle.getString("strOrderSort");

	if(strOrderItem.equals("upt_date")){
		radioButton2.setChecked(true);
	}else{
		radioButton1.setChecked(true);
	}
	if(strOrderSort.equals("PositiveSequence")){
		radioButton4.setChecked(true);
	}else{
		radioButton3.setChecked(true);
	}

	button1 = (Button) findViewById(R.id.confirm);
	button1.setOnClickListener(
			new View.OnClickListener() {
		public void onClick(View arg0) {
			submit();
		}
	}
	);

	radioGroup1.setOnCheckedChangeListener(
			new RadioGroup.OnCheckedChangeListener() {
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if(checkedId==radioButton2.getId()){
				strOrderItem = "upt_date";
				radioButton2.setChecked(true);
			}else{
				strOrderItem = "title";
				radioButton1.setChecked(true);
			}
			Notepad.myToDoDB.setSettings(2, strOrderItem);
		}
	}
	);

	radioGroup2.setOnCheckedChangeListener(
			new RadioGroup.OnCheckedChangeListener() {
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if(checkedId==radioButton4.getId()){
				strOrderSort = "PositiveSequence";
				radioButton4.setChecked(true);
			}else{
				strOrderSort = "ReverseOrder";
				radioButton3.setChecked(true);
			}
			Notepad.myToDoDB.setSettings(3, strOrderSort);
		}
	}
	);
}

	private void submit(){
		String oldPW = editText1.getText().toString();
		String newPW = editText2.getText().toString();
		String verPW = editText3.getText().toString();
		
		if(oldPW.equals(strPW)){
			if(oldPW.equals(newPW)){
				strDialog(R.string.error, R.string.password_error1);
			}else{
				if(newPW.equals(verPW)){
					Notepad.myToDoDB.setSettings(1, newPW);
					strDialog(R.string.confirm, R.string.password_change);
					editText1.setText("");
					editText2.setText("");
					editText3.setText("");
					strPW = newPW;
				}else{
					strDialog(R.string.error, R.string.password_error2);
				}
			}
		}else{
			strDialog(R.string.error, R.string.password_error3);
		}
	}
	
	private void strDialog(int title, int msg){
    	new AlertDialog.Builder(this)
    	.setTitle(title)
    	.setMessage(msg)
    	.setPositiveButton(R.string.confirm,
    		new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialoginterface, int i) {
				}
			}
    	)
		.show();
    }

}