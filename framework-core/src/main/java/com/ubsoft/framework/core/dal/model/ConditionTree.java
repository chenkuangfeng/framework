package com.ubsoft.framework.core.dal.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ubsoft.framework.core.dal.util.DataType;
import com.ubsoft.framework.core.support.util.StringUtil;

public class ConditionTree implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<ConditionNode> nodes;

	public List<ConditionNode> getNodes() {

		return nodes;
	}

	public void setNodes(List<ConditionNode> nodes) {
		this.nodes = nodes;
	}

	/**
	 * 组合条件语句
	 * 
	 * @return
	 */
	
	public String transferCondition() {
		String strWhere = "";
		if (nodes != null) {
			for (ConditionNode node : nodes) {
				strWhere += transferConditionNode(node);
			}
		}
		if (strWhere.length() > 4) {
			strWhere = strWhere.substring(0, strWhere.length() - 4);
		}
		return strWhere;
	}

	
	private String transferConditionNode(ConditionNode node) {
		String strWhereItem = "";
		if (node instanceof ConditionJointNode) {
			ConditionJointNode jointNode = (ConditionJointNode) node;
			if (jointNode.getNodes() != null && jointNode.getNodes().size() > 0) {
				strWhereItem += "(";
				for (ConditionNode item : node.getNodes()) {
					strWhereItem += transferConditionNode(item);
				}
				strWhereItem += ") " + jointNode.getJoinType() + " ";
			}

		} else {
			ConditionLeafNode leafNode = (ConditionLeafNode) node;
			// 传递数组参数是,条件拆分成or连接
			if (leafNode.getFieldValue() instanceof Object[]) {
				Object[] value = (Object[]) leafNode.getFieldValue();
				strWhereItem += " (";
				for (int i = 0; i < value.length; i++) {
					if (StringUtil.isNotEmpty(leafNode.getAs())) {
						strWhereItem += leafNode.getAs() + ".";
					}
					if (StringUtil.isNotEmpty(leafNode.getField())) {
						strWhereItem += leafNode.getField() + " ";
					}
					if (StringUtil.isNotEmpty(leafNode.getOperator())) {
						strWhereItem += leafNode.getOperator() + " ? ";
					}
					strWhereItem += " or ";
				}
				strWhereItem = strWhereItem.substring(0, strWhereItem.length() - 3);

				strWhereItem += " )";
			} else {
				if (StringUtil.isNotEmpty(leafNode.getAs())) {
					strWhereItem += leafNode.getAs() + ".";
				}
				if (StringUtil.isNotEmpty(leafNode.getField())) {
					strWhereItem += leafNode.getField() + " ";
				}
				if (StringUtil.isNotEmpty(leafNode.getOperator())) {
					strWhereItem += leafNode.getOperator() + " ? ";
				}
			}
			if (StringUtil.isNotEmpty(leafNode.getJoinType())) {
				strWhereItem += leafNode.getJoinType() + " ";
			} else {
				strWhereItem += " and ";
			}

		}

		return strWhereItem;

	}

	
	public void addNode(ConditionNode node) {
		if (nodes == null) {
			nodes = new ArrayList<ConditionNode>();
		}
		nodes.add(node);
	}

	/**
	 * 组合参数
	 * 
	 * @return
	 */
	
	public Object[] transferParameter() {
		List<Object> parameters = new ArrayList<Object>();
		if (nodes != null) {
			for (ConditionNode node : nodes) {
				transferParameterNode(node, parameters);
			}
		}
		return parameters.toArray();
	}

	
	private void transferParameterNode(ConditionNode node, List<Object> parameters) {
		if (node instanceof ConditionJointNode) {
			ConditionJointNode jointNode = (ConditionJointNode) node;
			if (jointNode.getNodes() != null && jointNode.getNodes().size() > 0) {
				for (ConditionNode item : node.getNodes()) {
					transferParameterNode(item, parameters);
				}

			}

		} else {
			ConditionLeafNode leafNode = (ConditionLeafNode) node;
			if (leafNode.getFieldValue() instanceof Object[]) {
				for (Object obj : (Object[]) leafNode.getFieldValue()) {
					if (leafNode.getDataType() != null) {
						obj = DataType.convert(leafNode.getDataType(), obj);
						parameters.add(obj);
					} else {
						parameters.add(obj);
					}

				}
			} else {
				if (leafNode.getDataType() != null) {
					Object obj = DataType.convert(leafNode.getDataType(), leafNode.getFieldValue());
					parameters.add(obj);
				} else {
					parameters.add(leafNode.getFieldValue());
				}
			}
		}
	}

	
	public static void main(String[] arg) {
		ConditionTree tree = new ConditionTree();
		ConditionNode node11 = new ConditionJointNode("and");
		ConditionNode node12 = new ConditionLeafNode("B", "name", "=", "1", null, null);
		List<ConditionNode> items = new ArrayList<ConditionNode>();
		// items.add(node11);
		items.add(node12);

		ConditionNode node111 = new ConditionLeafNode("c", "name", "=", "2", null, "and");
		ConditionNode node112 = new ConditionLeafNode("d", "name", "=", "3", null, null);
		List<ConditionNode> node11Items = new ArrayList<ConditionNode>();
		node11.addNode(node111);
		node11.addNode(node112);

		tree.setNodes(items);
		System.out.println(tree.transferCondition());
		for (Object obj : tree.transferParameter()) {
			System.out.println(obj.toString());
		}

	}
}
