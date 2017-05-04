package Test;

import com.example.liupan.zanrunworkclient.SqlLiteProxy;
import com.example.liupan.zanrunworkclient.entity.Employee;

import java.util.ArrayList;

/**
 * Created by liupan on 2017/5/3.
 */

public class DBTest {
    public static void dbTest(){
        //hasDb();

        SqlLiteProxy sqlLiteProxy = SqlLiteProxy.getInstance();
        sqlLiteProxy.getCurrentTime();
        Employee employee = new Employee();
        employee.setRfid("employee_rfid_01");
        employee.setEmployeeLevel(1);
        employee.setEmployeeNo("eno_01");
        employee.setCompanyId("cid_01");
        employee.setId("eid_01");
        sqlLiteProxy.insertEmployee(employee);
        ArrayList<Employee> employees = sqlLiteProxy.employees();
        Employee newEmployee = sqlLiteProxy.findEmployee("eid_01");

    }

    private static void hasDb(){
        SqlLiteProxy sqlLiteProxy = SqlLiteProxy.getInstance();
        if(sqlLiteProxy.isAvailable()){
            int a = 0;
        }
        else{
            int b=0;
        }
    }
}
