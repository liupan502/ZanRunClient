package com.example.liupan.zanrunworkclient.entity;

/**
 * Created by Panda on 2017/3/27.
 */

import Employee;
import Task;
public class EmployeeTask extends BaseObject {

	private String employeeId;
	private String taskId;
	private String employeeName;
	private int productionNum;
	private int badProductionNum;
	private int status;

	public String getEmployeeName(){
		return employeeName;
	}

	public void setEmployeeName(String employeeName){
		this.employeeName = employeeName;
	}

	public int getProductionNum(){
		return productionNum;
	}

	public void setProductionNum(int productionNum){
		this.productionNum = productionNum;
	}

	public int getBadProductionNum(){
		return badProductionNum;
	}

	public void setBadProductionNum(int badProductionNum){
		return this.badProductionNum;
	}

	public String getEmployeeId(){
		return employeeId;
	}

	public void setEmployeeId(String employeeId){
		this.employeeId = employeeId;
	}

	public String getTaskId(){
		return taskId;
	}

	public String setTaskId(String taskId){
		this.taskId = taskId;
	}

	public int getStatus(){
		return status;
	}

	public void setStatus(int status){
		this.status = status;
	}
}
