package com.ubsoft.framework.core.dal.model;


public class ConditionJointNode extends ConditionNode  {
	
	private String joinType;

	public String getJoinType() {
		return joinType;
	}

	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}
	
	public ConditionJointNode(String joinType){
		this.joinType=joinType;
	}

	
	
	

	

	

}
