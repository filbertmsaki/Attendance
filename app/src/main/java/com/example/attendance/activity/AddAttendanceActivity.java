package com.example.attendance.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance.R;
import com.example.attendance.bean.AttendanceBean;
import com.example.attendance.bean.StudentBean;
import com.example.attendance.context.ApplicationContext;
import com.example.attendance.db.DBAdapter;


public class AddAttendanceActivity extends AppCompatActivity {

	ArrayList<StudentBean> studentBeanList;
	private ListView listView ;  
	private ArrayAdapter<String> listAdapter;
	int sessionId=0;
	String status="P";
	String branch;
	String year;
	Button attendanceSubmit;
	DBAdapter dbAdapter = new DBAdapter(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.__listview_main);

		sessionId = getIntent().getExtras().getInt("sessionId");
		
		
		
		listView=(ListView)findViewById(R.id.listview);
		final ArrayList<String> studentList = new ArrayList<String>();
		ApplicationContext context = new ApplicationContext();
		studentBeanList=context.getStudentBeanList();

		branch=getIntent().getExtras().getString("branch");
		year =getIntent().getExtras().getString("year");
		studentBeanList=dbAdapter.getAllStudentByBranchYear(branch, year);

		for(StudentBean studentBean : studentBeanList)
		{
			String users = studentBean.getStudent_firstname()+","+studentBean.getStudent_lastname();
				
			studentList.add(users);
			Log.d("users: ", users); 

		}

		listAdapter = new ArrayAdapter<String>(this, R.layout.add_student_attendance, R.id.labelA, studentList);
		listView.setAdapter( listAdapter ); 

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				arg0.getChildAt(arg2).setBackgroundColor(Color.TRANSPARENT);
				//arg0.setBackgroundColor(234567);
				arg1.setBackgroundColor(334455);
				final StudentBean studentBean = studentBeanList.get(arg2);
				final Dialog dialog = new Dialog(AddAttendanceActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//...........
				dialog.setContentView(R.layout.test_layout);
				// set title and cancelable
				RadioGroup radioGroup;
				RadioButton present;
				RadioButton absent;
				radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
				present=(RadioButton)dialog.findViewById(R.id.PresentradioButton);
				absent=(RadioButton)dialog.findViewById(R.id.AbsentradioButton);
				radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if(checkedId == R.id.PresentradioButton) {
							
							status = "P";
						} else if(checkedId == R.id.AbsentradioButton) {

							status = "A";
						} else {
						}
					}
				});

				attendanceSubmit = (Button)dialog.findViewById(R.id.attendanceSubmitButton);
				attendanceSubmit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						AttendanceBean attendanceBean = new AttendanceBean();
						
						attendanceBean.setAttendance_session_id(sessionId);
						attendanceBean.setAttendance_student_id(studentBean.getStudent_id());
						attendanceBean.setAttendance_status(status);
						
						DBAdapter dbAdapter = new DBAdapter(AddAttendanceActivity.this);
						dbAdapter.addNewAttendance(attendanceBean);
						
						dialog.dismiss();
						
					}
				});
				
				dialog.setCancelable(true);
				dialog.show();
			}
		});



	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
