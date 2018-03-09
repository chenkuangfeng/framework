package com.ubsoft.framework.web.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.Tag;

import com.ubsoft.framework.core.context.AppContext;
import com.ubsoft.framework.core.service.IFormService;
import com.ubsoft.framework.system.entity.LookupDetail;
import com.ubsoft.framework.system.entity.UISetting;
import com.ubsoft.framework.system.model.Subject;

/**
 * 列表标签
 * 
 * @author chenkf
 * 
 */
public class DataGridTag extends BaseTag {
	protected String filter;
	protected String ui;
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getUi() {
		return ui;
	}

	public void setUi(String ui) {
		this.ui = ui;
	}

	protected List<DataGridColumnTag> columns;

	public void addColumn(DataGridColumnTag column) {
		if (columns == null) {
			columns = new ArrayList<DataGridColumnTag>();
		}
		columns.add(column);
	}

	public int doStartTag() throws JspTagException {
		Tag t = this.getParent();
		StringBuffer body = new StringBuffer();
		// 添加公共属性
		body.append(" <table ");
		addDefatultProperty(body);
		if (css == null) {
			this.addStringProperty(body, "class", "easyui-datagrid");
			this.addStringProperty(body, "fit", "true");

			// this.addStringProperty(body, "fitColumns", "true");

		}
		body.append(">");
		body.append("<thead class=\"filter\">");

		JspWriter out = this.pageContext.getOut();
		try {
			out.write(body.toString());
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Tag.EVAL_PAGE;
	}

	public int doEndTag() throws JspTagException {
		StringBuffer body = new StringBuffer();
		//用户自定义界面方案
		List<UISetting> uiSettings=null;
		Map<String,UISetting> colMap=null;
		if(ui!=null){
			IFormService formService = (IFormService) AppContext.getBean("formService");
			 uiSettings = formService.getUISetting(Subject.getSubject().getUserKey(), ui);
			 colMap= new HashMap<String,UISetting>();
			 for(UISetting uisetting:uiSettings){
				 colMap.put(uisetting.getField(), uisetting);
			 }
		}

		// 生成查询条件输入框
		if (filter == null || filter.equals("true")) {
			this.genFilter(body,colMap);
		}
		
		
		
		this.genColumn(body,colMap);

		body.append("</thead>");
		body.append("</table>");
		if (ui != null) {
			genUISetting(body,colMap);
		}
		JspWriter out = this.pageContext.getOut();
		try {
			out.write(body.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			clearPropertyValue();
		}
		return EVAL_PAGE;
	}

	/**
	 * 生成列
	 * 
	 * @param body
	 */
	private void genColumn(StringBuffer body,Map<String,UISetting> colSetting) {
		boolean hasUISetting=false;
		if(colSetting!=null&&colSetting.keySet().size()>0){
			hasUISetting=true;
		}
		
		body.append("<tr>");
		for (DataGridColumnTag column : columns) {
			String type = column.getType();
			if (type == null)
				type = "text";
			if(hasUISetting&&!type.equals("checked")&&!type.equals("editor")&&!colSetting.containsKey(column.getField())){
				//body.append("<th></th>");
				continue;
			}		
			String formatFunName = null;
			if (column.code != null) {
				formatFunName = addFormatFuction(body, column.code);
			}
			body.append("<th");
			column.addDefatultProperty(body);
			if (column.code != null) {

				column.addStringProperty(body, "formatter", formatFunName);
			}
			// 添加默认编辑框,必须grid是editable
			boolean coleditable=true;
			if(editable == null || editable.equals("true")){
				coleditable=true;
			}else{
				coleditable=false;
			}
			if(column.editable != null && column.editable.equals("true")){
				coleditable=true;
			}else if(column.editable != null && column.editable.equals("false")){
				coleditable=false;
			}
			if (coleditable) {				
				String editor = column.editor;
				String vtype = column.vtype;
				String required = column.required;
				
				if (type.equals("text")) {
					if (vtype != null && (vtype.toLowerCase().startsWith("numeric")||vtype.toLowerCase().startsWith("number"))) {
						String[] elements = vtype.split("\\(");
						int precision = 0;
						if (elements.length == 2) {
							precision = new Integer(elements[1].substring(0, elements[1].length() - 1));
						}
						column.addStringProperty(body, "editor", "{type:'numberbox',options:{precision:" + precision + "}}");
					} else {
						if (editor == null) {
							if (Boolean.parseBoolean(required)) {
								column.addStringProperty(body, "editor", "{type:'validatebox',options:{required:true}}");
							} else {
								column.addStringProperty(body, "editor", "text");
							}
						} else {
							column.addStringProperty(body, "editor", editor);
						}
					}
				} else if (type.equals("select")) {
					if (editor == null) {
						this.genSelectEditor(body, column);
					} else {
						column.addStringProperty(body, "editor", editor);
					}

				} else if (type.equals("datepicker")) {
					if (editor == null) {
						column.addStringProperty(body, "editor", "datebox");
					} else {
						column.addStringProperty(body, "editor", editor);
					}

				} else if (type.equals("datetimepicker")) {
					if (editor == null) {
						column.addStringProperty(body, "editor", "datetimebox");
					} else {
						column.addStringProperty(body, "editor", editor);
					}

				}
				
			}
			body.append(">");
			if (column.label != null) {
				body.append(column.label);
			}
			body.append("</th>");
		}
		body.append("</tr>");
	}

	/**
	 * 生成下拉框编辑数据
	 * 
	 * @param body
	 * @return
	 */
	private void genSelectEditor(StringBuffer body, DataGridColumnTag column) {
		List<LookupDetail> items = this.getLookupByCode(column.code);
		body.append(" editor=\"");
		body.append("{");
		body.append("type:'combobox',");
		body.append("options: {  ");
		body.append("data:[");// 从格式化里面取rets
		for (int i = 0; i < items.size(); i++) {
			body.append("{");
			body.append("value:").append("'").append(items.get(i).getLkdKey()).append("',");
			body.append("text:").append("'").append(items.get(i).getLkdName()).append("'");
			body.append("}");
			if (i != items.size() - 1) {
				body.append(",");
			}

		}
		body.append("],");
		body.append("valueField: 'value',");
		body.append("textField: 'text'");
		body.append("}");
		body.append("}");
		body.append("\"");

	}

	// 下拉框格式化函数,显示text值
	private String addFormatFuction(StringBuffer body, String code) {
		String formatFunName = "F_" + UUID.randomUUID().toString().replaceAll("-", "");
		body.append("<script>");
		List<LookupDetail> listResult = this.getLookupByCode(code);

		body.append("function ").append(formatFunName).append("(val,row){");
		body.append("var rets=[];");
		for (LookupDetail lkup : listResult) {
			body.append(" rets.push({value:").append("\"").append(lkup.getLkdKey()).append("\",");
			body.append("text:").append("\"").append(lkup.getLkdName()).append("\"});");

		}
		body.append("for(var i=0;i<rets.length;i++){");
		body.append("	if(val!=null &&(rets[i].value==val.toString())){");
		//body.append("		alert(i+rets[i].text);");
		body.append("		return rets[i].text;");
		body.append("	 }");
		body.append("}");
		body.append("return \"\";");
		body.append("}");

		body.append("</script>");
		return formatFunName;
	}

	/**
	 * 生成查询条件框,easyui datagrid添加过滤空支持easyui的class,需要单独处理
	 * 
	 * @param body
	 */
	private void genFilter(StringBuffer body,Map<String,UISetting> colSetting) {
		boolean hasUISetting=false;
		if(colSetting!=null&&colSetting.keySet().size()>0){
			hasUISetting=true;
		}
		 
		body.append("<tr>");
		for (DataGridColumnTag column : columns) {
			String type = column.getType();
			if (type == null)
				type = "text";
			if(hasUISetting&&!type.equals("checked")&&!type.equals("editor")&&!colSetting.containsKey(column.getField())){
				//body.append("<th></th>");
				continue;
			}
			body.append("<th>");
			if (column.getFilter() == null || (column.getFilter() != null && column.getFilter().equals("true"))) {
				
			
				if (type.equals("text")) {
					body.append(" <input ");
					column.addInputProperty(body);
					addStringProperty(body, "type", "text");
					if (column.css == null) {
						this.addStringProperty(body, "class", "form-control");
					}
					// 避免重复，id设置成tableid+"_"+field+"_FIlTER"+
					addStringProperty(body, "id", id.toUpperCase() + "_" + column.field + "_FILTER");
					body.append("/>");
				} else if (type.equals("datepicker")) {
					if (column.operater != null) {
						body.append(" <input ");
						column.addInputProperty(body);
						addStringProperty(body, "type", "datepicker");
						addStringProperty(body, "dataType", "Date");

						this.addStringProperty(body, "class", "form-control");
						addStringProperty(body, "id", id.toUpperCase() + "_" + column.field + "_FROM_FILTER");
						body.append("/>");
					} else {
						body.append(" <input ");
						column.addInputProperty(body);
						addStringProperty(body, "type", "datepicker");
						addStringProperty(body, "dataType", "Date");

						addStringProperty(body, "id", id.toUpperCase() + "_" + column.field + "_FROM_FILTER");
						addStringProperty(body, "operator", ">=");
						body.append("/>");
						body.append("-");
						body.append("<input ");
						column.addInputProperty(body);
						addStringProperty(body, "type", "datepicker");
						addStringProperty(body, "dataType", "Date");

						addStringProperty(body, "id", id.toUpperCase() + "_" + column.field + "_TO_FILTER");
						addStringProperty(body, "operator", "<=");
						body.append("/>");
					}

				} else if (type.equals("datetimepicker")) {
					if (column.operater != null) {
						body.append("<input ");
						column.addInputProperty(body);
						addStringProperty(body, "type", "datetimepicker");
						addStringProperty(body, "dataType", "Timestamp");

						this.addStringProperty(body, "class", "form-control");
						addStringProperty(body, "id", id.toUpperCase() + "_" + column.field + "_FROM_FILTER");
						body.append("/>");
					} else {
						body.append(" <input ");
						column.addInputProperty(body);
						addStringProperty(body, "type", "datetimepicker");
						addStringProperty(body, "dataType", "Timestamp");
						addStringProperty(body, "id", id.toUpperCase() + "_" + column.field + "_FROM_FILTER");
						addStringProperty(body, "operator", ">=");
						body.append("/>");
						body.append("-");
						body.append("<input ");
						column.addInputProperty(body);
						addStringProperty(body, "type", "datetimepicker");
						addStringProperty(body, "dataType", "Timestamp");
						addStringProperty(body, "id", id.toUpperCase() + "_" + column.field + "_TO_FILTER");
						addStringProperty(body, "operator", "<=");
						body.append("/>");
					}

				} else if (type.equals("select")) {
					body.append(" <select ");
					column.addInputProperty(body);
					// addStringProperty(body, "class", "easyui-combobox");
					this.addStringProperty(body, "class", "form-control");
					this.addStringProperty(body, "style", "padding:0px");

					addStringProperty(body, "id", id.toUpperCase() + "_" + column.field + "_FILTER");
					body.append(" >");
					if (column.code != null) {
						addSelectOptions(column.code, body);
					}
					body.append("</select>");
				} else if (type.equals("search")) {
					body.append(" <input ");
					column.addInputProperty(body);
					addStringProperty(body, "id", id.toUpperCase() + "_" + column.field + "_FILTER");
					addStringProperty(body, "type", "search");
					body.append("/>");
					// 选择框
				} else if (type != null && type.equals("checked")) {

					body.append("<div><a href=\"#\"");
					addStringProperty(body, "id", id.toUpperCase() + "_" + "CLEARFILTER");
					body.append("/>");
					body.append("<span style=\"font-size:13px;padding-top:5px;color:black\" class=\"glyphicon glyphicon-remove\"/></a></div>");
					// body.append("<button style=\"padding-top:1px;width:10px;height:30px;padding-bottom:1px\" class=\"btn btn-default\"");
					// addStringProperty(body, "id", id + "_cleanFilter");
					// body.append(">");
					// body.append("<span class=\"glyphicon glyphicon-clear\"");
					// body.append("/>");
					// body.append("</button>");

				}
			}
			body.append("</th>");
		}
		body.append("</tr>");
	}

	/**
	 * 自定义列
	 * 
	 * @param body
	 */
	private void genUISetting(StringBuffer body,Map<String,UISetting> colSetting) {
		StringBuffer sb=new StringBuffer();
		sb.append("<div id=\"").append(ui).append("\">");
		sb.append("<table style=\"width:170px;display:none\" id=\"").append(ui + "_dg").append("\" class=\"easyui-datagrid\">");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th field=\"field\" checkbox=\"true\">");
		sb.append("<th field=\"title\" width=\"130\"><b>列名</b>");
		sb.append("</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("</table>");
		sb.append("</div>");
		// 生成脚本
		body.append("<script type=\"text/javascript\">");
		body.append("$().ready(function() {");
		
		body.append("var div='"+sb.toString()+"';");
		body.append("$(\"body\").after(div);    ");
		// 目标grid
		//body.append("var " + ui + "_maindg=form.getDataGrid();");
		// 设置列grid
		body.append("var " + ui + "_settingdg=$(\"#" + ui + "_dg\").datagrid();");
		body.append(" var " + ui + "_settings=[];");	
		body.append(" var " + ui + "_dgColumns=[];");	

		//grid列
		for(DataGridColumnTag col:this.columns){
			
			body.append(ui + "_dgColumns.push({field:'" + col.getField() + "',title:'"+col.getLabel()+"'});");

		}
		//配置列
		for (Map.Entry<String, UISetting> setting : colSetting.entrySet()) {
			body.append(ui + "_settings.push('" + setting.getValue().getField() + "');");
		}
		String firstShow = ui + "firstshow";
		body.append(" var ").append(firstShow).append(" = true;");
		// body.append(" app.form.uisetting.initMainGrid("+ui+"_maindg,"+ui+"_settings);");
		body.append("$(\"#btn" + ui + "Setting\").click(function(e) {");
		body.append("var eve = e || window.event;");
		body.append("var x = eve.offsetX;"); // 相对于客户端的X坐标
		body.append("var y = eve.offsetY;"); // 相对于客户端的Y坐标		
		body.append("app.form.showDgSetting('" + ui + "'," + ui + "_dgColumns," + ui + "_settingdg," + ui + "_settings," + firstShow + ",x,y);");
		body.append(firstShow).append(" = false;");
		body.append("});");
	
		body.append("});");
		body.append("</script>");
	}

	public void release() {
		super.release();

	}

	public void addDefatultProperty(StringBuffer input) {
		super.addDefatultProperty(input);

	}

	public void clearPropertyValue() {
		super.clearPropertyValue();
		columns.clear();
		columns = null;
		filter = null;
		editable = null;
		ui = null;
	}

}
