package com.ubsoft.framework.bi.web.view;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.ubsoft.framework.bi.util.JDataSourceUtil;

public class JasperView extends AbstractView {

	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response) {
		try {
			String type = model.get("type").toString();
			String reportFile = model.get("reportFile").toString();
			Map parameters = (Map) model.get("p"); 
			Object record = model.get("d");			
			JDataSourceUtil ds = null;
			if (record instanceof List) {
				ds = new JDataSourceUtil((List<Map>) record);
			} else if (record instanceof Map) {
				ds = new JDataSourceUtil((Map) record);
				}
			if (type.equals("HTML")) {
				String imgDir = model.get("imgDir").toString();
				String imgUrl = model.get("imgUrl").toString();
				//JasperUtil.showHtml(reportFile, imgDir, imgUrl, request, response, parameters, ds);
			} else if(type.equals("PDF")){
				//JasperUtil.showPdf(reportFile, request, response, parameters, ds);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
