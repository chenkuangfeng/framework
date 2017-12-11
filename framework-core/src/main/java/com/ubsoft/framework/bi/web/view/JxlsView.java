package com.ubsoft.framework.bi.web.view;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.AbstractView;

public class JxlsView extends AbstractView {
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String destFileName = model.get("destFileName").toString();
		String templateFileName = model.get("templateFileName").toString();
		response.setHeader("Content-Disposition", "attachment;filename=" + destFileName);
		response.setContentType("application/vnd.ms-excel");
		Map beans = (Map) model.get("d");
		XLSTransformer transformer = new XLSTransformer();
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(templateFileName));
			Workbook workbook = transformer.transformXLS(in, beans);
			out = response.getOutputStream();
			// 将内容写入输出流并把缓存的内容全部发出去
			workbook.write(out);
			out.flush();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
