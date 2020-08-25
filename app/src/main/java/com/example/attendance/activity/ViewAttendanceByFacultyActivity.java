package com.example.attendance.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.attendance.R;
import com.example.attendance.bean.AttendanceBean;
import com.example.attendance.bean.AttendanceSessionBean;
import com.example.attendance.bean.FacultyBean;
import com.example.attendance.bean.StudentBean;
import com.example.attendance.context.ApplicationContext;
import com.example.attendance.db.DBAdapter;


public class ViewAttendanceByFacultyActivity extends AppCompatActivity {
	ApplicationContext context = new ApplicationContext();

	ArrayList<AttendanceBean> attendanceBeanList;
	private ListView listView ;  
	private ArrayAdapter<String> listAdapter;

	DBAdapter dbAdapter = new DBAdapter(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.__listview_main);

		listView=(ListView)findViewById(R.id.listview);
		final ArrayList<String> attendanceList = new ArrayList<String>();




		AttendanceSessionBean attendanceSessionBean = new AttendanceSessionBean();
		attendanceSessionBean.setAttendance_session_faculty_id(getIntent().getIntExtra("id",0));
		attendanceSessionBean.setAttendance_session_department(getIntent().getStringExtra("department"));
		attendanceSessionBean.setAttendance_session_class(getIntent().getStringExtra("class"));
		attendanceSessionBean.setAttendance_session_subject(getIntent().getStringExtra("subject"));

		DBAdapter dbAdapters = new DBAdapter(this);

		ArrayList<AttendanceBean> attendanceBeanList = dbAdapters.getAttendanceBySessionID(attendanceSessionBean);
		context.setAttendanceBeanList(attendanceBeanList);



		attendanceList.add("Id | StudentName |  Status");

		attendanceBeanList=context.getAttendanceBeanList();

		for(AttendanceBean attendanceBean : attendanceBeanList)
		{
			String users = "";
			if(attendanceBean.getAttendance_session_id() != 0)
			{
				DBAdapter dbAdapter = new DBAdapter(ViewAttendanceByFacultyActivity.this);
				StudentBean studentBean =dbAdapter.getStudentById(attendanceBean.getAttendance_student_id());
				users = attendanceBean.getAttendance_student_id()+".     "+studentBean.getStudent_firstname()+","+studentBean.getStudent_lastname()+"                  "+attendanceBean.getAttendance_status();
			}
			else
			{
				users = attendanceBean.getAttendance_status();
			}
			
			attendanceList.add(users);
			Log.d("users: ", users); 

		}

		listAdapter = new ArrayAdapter<String>(this, R.layout.view_attendance_list, R.id.labelAttendance, attendanceList);
		listView.setAdapter( listAdapter );

	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
