package com.ubsoft.framework.core.dal.model;


public class ConditionLeafNode extends ConditionNode {

	private String field;
	private Object fieldValue;
	private String dataType;;

	private String operator;
	private String as;
	private String joinType;
	public ConditionLeafNode(){
		
	}
	public ConditionLeafNode(String as, String fieldName, String operator, Object fieldValue, String dataType,String joinType) {
		this.field = fieldName;
		this.as = as;
		this.operator = operator;
		this.fieldValue = fieldValue;
		this.dataType=dataType;
		this.joinType = joinType;
	}

	
	public String getDataType() {
		return dataType;
	}


	public void setDataType(String dataType) {
		this.dataType = dataType;
	}


	

	public String getField() {
		return field;
	}


	public void setField(String field) {
		this.field = field;
	}


	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getAs() {
		return as;
	}

	public void setAs(String as) {
		this.as = as;
	}

	public String getJoinType() {
		return joinType;
	}

	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}



}
