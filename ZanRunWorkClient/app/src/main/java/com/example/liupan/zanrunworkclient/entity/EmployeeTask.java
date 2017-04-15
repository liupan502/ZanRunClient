package com.example.liupan.zanrunworkclient.entity;

/**
 * Created by Panda on 2017/3/27.
 */

import com.example.liupan.zanrunworkclient.entity.Employee;
import com.example.liupan.zanrunworkclient.entity.Task;
import com.example.liupan.zanrunworkclient.entity.Procedure;
import java.util.UUID;


public class EmployeeTask extends BaseObject {

	private String employeeId;
	private String fcId;
	private String procedureId;
	private String employeeName;
	private int productionNum;
	private int badProductionNum;
	private int status;
	private String qcId;
	private String managerId;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	private String startTime;
	private String updateTime;

	//public static final int ET_STATUS_NONE = 0;
	public static final int ET_STATUS_DEFAULT = 1;
	public static final int ET_STATUS_QC_CONFIRM = 2;
	public static final int ET_STATUS_MANAGER_CONFIRM = 3;

	public EmployeeTask(){
		super();
	}

	public EmployeeTask(Employee employee,FlowCard fc,Procedure procedure){
		super();
		status = ET_STATUS_DEFAULT;
		this.setProductionNum(0);
		this.setBadProductionNum(0);
		this.setEmployeeName(employee.getName());
		this.setEmployeeId(employee.getId());
		this.setProcedureId(procedure.getId());
		this.setFcId(fc.getId());
		String id =  UUID.randomUUID().toString();
		id.replace("-","");
		this.setId(id);


	}

	public String getProcedureId(){
		return procedureId;
	}

	public void setProcedureId(String procedureId){
		this.procedureId = procedureId;
	}

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
		this.badProductionNum = badProductionNum;
	}

	public String getEmployeeId(){
		return employeeId;
	}

	public void setEmployeeId(String employeeId){
		this.employeeId = employeeId;
	}

	public String getFcId(){
		return fcId;
	}

	public void setFcId(String fcId){
		this.fcId = fcId;
	}

	public int getStatus(){
		return status;
	}

	public void setStatus(int status){
		this.status = status;
	}

	public String getQCId(){
		return qcId;
	}

	public void setQCId(String id){
		this.qcId = id;
	}

	public String getManagerId(){
		return this.managerId;
	}

	public void setManagerId(String id){
		this.managerId = id;
	}
}
