package com.example.liupan.zanrunworkclient;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liupan on 2017/3/18.
 */

public class EmployeeSimpleAdapter extends SimpleAdapter implements AdapterView.OnItemClickListener{

    public static final String EMPLOYEE_NAME = "employee_name";

    public static final String PRODUCTION_NUM = "production_num";

    public static final String BAD_NUM = "bad_num";

    public static final String EMPLOYEE_ID = "employee_id";

    public static final String EMPLOYEE_STATUS = "employee_status";

    public static final String EMPLOYEE_TASK_ID = "employee_task_id";



    public EmployeeSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
    }

    @Override
    public Object getItem(int position){
        HashMap<String,Object> employee = (HashMap<String, Object>) super.getItem(position);
        if(employee.containsKey(EMPLOYEE_STATUS)){
            int status = (int)employee.get(EMPLOYEE_STATUS);
            if(status == 1){
                //employee.put(EmployeeSimpleAdapter.PRODUCTION_NUM,"qing zhao qa que ren!");
            }
        }

        return (Object)employee;
        //return super.getItem(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       //getResources().getColor(R.colorAccent);
        HashMap<String,Object> employee = (HashMap<String, Object>) getItem(position);
        if(employee != null){
            if(convertView ==null)
            {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.employee_list_item, null);
            }

            int status = -1;
            if(employee.containsKey(EMPLOYEE_STATUS))
                 status = (int)employee.get(EMPLOYEE_STATUS);
            switch (status){
                case 0:
                    convertView.setBackgroundColor(Color.BLUE);
                    break;
                case 1:
                    convertView.setBackgroundColor(Color.RED);
                    //TextView textView = (TextView)convertView.findViewById(R.id.production_num);
                    //textView.setText("qing zhao qa que ren!");
                    break;
                default:
                    //convertView.setBackgroundColor(Color.BLUE);
                    break;
            }



        }

        return super.getView(position, convertView, parent);

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private Context mContext = null;
}
