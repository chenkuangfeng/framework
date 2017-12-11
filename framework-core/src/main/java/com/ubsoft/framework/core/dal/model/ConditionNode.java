package com.ubsoft.framework.core.dal.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class ConditionNode implements Serializable {
	public static final String OPERATOR_EQUAL = "=";
	public static final String OPERATOR_UNEQUAL = "!=";
	public static final String OPERATOR_LESS_THAN = "<";
	public static final String OPERATOR_NO_GREATER_THAN = "<=";
	public static final String OPERATOR_NO_LESS_THAN = ">=";
	public static final String OPERATOR_GREATER_THAN = ">";
	public static final String OPERATOR_BETWEEN = "BETWEEN";
	public static final String OPERATOR_LIKE = "LIKE";
	public static final String OPERATOR_NOT_LIKE = "NOT LIKE";
	public static final String OPERATOR_IN = "IN";
	public static final String OPERATOR_NOT_IN = "NOT IN";

	

	private List<ConditionNode> nodes;
	
	public List<ConditionNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<ConditionNode> nodes) {
		this.nodes = nodes;
	}

	public void addNode(ConditionNode node){
		if(nodes==null){
			nodes= new ArrayList<ConditionNode>();
		}
		nodes.add(node);
	}
	

}
