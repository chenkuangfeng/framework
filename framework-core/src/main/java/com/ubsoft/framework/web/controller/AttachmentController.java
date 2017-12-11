package com.ubsoft.framework.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ubsoft.framework.core.conf.AppConfig;
import com.ubsoft.framework.core.support.json.JsonHelper;
import com.ubsoft.framework.core.support.util.IPUtil;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.system.entity.Attachment;
import com.ubsoft.framework.system.service.IAttachmentService;

@RequestMapping("/")
@Controller
public class AttachmentController extends BaseController {
	@Autowired
	IAttachmentService attService;

	@RequestMapping("/uploadAttachment.ctrl")
	@ResponseBody
	public String upload() {
		Map res = new HashMap();//
		// 业务单据键值
		String refId = request.getParameter("refId");
		if (refId == null) {
			refId = "DEFAULT";
		}
		// 附件所属的模块,目录名
		String module = request.getParameter("m");
		if (module == null) {
			module = "OTHER";
		}
		// 是否重命名guid文件名
		String rename = request.getParameter("rename");
		// 文件类型
		String fileType = null;
		// 文件大小
		long fileSize = 0;
		// 服务器端IP,考虑负载均衡,多服务情况
		String serverIp = IPUtil.getLocalIP();
		// 文件原始名称
		String fileName = null;

		String rootPath = request.getSession().getServletContext().getRealPath("/attachment");
		// 文件网络路径
		String urlPath = "/attachment";
		try {
			// 使用Apache文件上传组件处理文件上传步骤：
			// 1、创建一个DiskFileItemFactory工厂
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置 缓存的大小，当上传文件的容量超过该缓存时，直接放到 暂时存储室
			factory.setSizeThreshold(1024 * 1024);
			// 暂存路径,tomcat默认是temp目录
			// factory.setRepository(new File(rootPath));
			// 2、创建一个文件上传解析器
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 解决上传文件名的中文乱码
			upload.setHeaderEncoding("UTF-8");
			// 3、判断提交上来的数据是否是上传表单的数据
			// if (!ServletFileUpload.isMultipartContent(request)) {
			//
			// }
			List<Attachment> atts = new ArrayList<Attachment>();
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item : list) {

				// 如果fileitem中封装的是普通输入项的数据
				if (item.isFormField()) {
					String name = item.getFieldName();
					// 解决普通输入项的数据的中文乱码问题
					String value = item.getString("UTF-8");

				} else {
					// 得到上传的文件名称，
					fileName = item.getName();
					fileSize = item.getSize();

					if (fileName == null || fileName.trim().equals("")) {
						continue;
					}
					// 处理获取到的上传文件的文件名的路径部分，只保留文件名部分
					fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
					String[] fileArray = fileName.split("\\.");

					fileType = fileArray[fileArray.length - 1];

					// 最终保存文件路径,路径命名规则:根目录+模块名+年月+GUID(替换原有文件名,防止文件重名被覆盖)+文件扩展名
					// Calendar date = Calendar.getInstance();
					SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
					String yearMonth = format.format(new Date(System.currentTimeMillis()));
					String savePath = rootPath + "\\" + module + "\\" + yearMonth;

					File file = new File(savePath);
					// 判断上传文件的保存目录是否存在
					if (!file.exists()) {
						file.mkdirs();
					}
					String saveFileName = null;
					if (rename != null && rename.equals("true")) {
						saveFileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileType;
					} else {
						saveFileName = fileName;
					}
					// 文件web相对路径
					urlPath += "/" + module + "/" + yearMonth + "/" + saveFileName;
					// 获取item中的上传文件的输入流
					InputStream in = item.getInputStream();
					// 创建一个文件输出流
					FileOutputStream out = new FileOutputStream(savePath + "\\" + saveFileName);
					// 创建一个缓冲区
					byte buffer[] = new byte[1024];
					// 判断输入流中的数据是否已经读完的标识
					int len = 0;
					// 循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
					while ((len = in.read(buffer)) > 0) {
						out.write(buffer, 0, len);
					}
					// 关闭输入流
					in.close();
					// 关闭输出流
					out.close();
					// 删除处理文件上传时生成的临时文件
					item.delete();
					// 把上传结果保存到数据库
					Attachment att = new Attachment();
					att.setAttName(fileName.substring(0, fileName.lastIndexOf(".")));
					att.setPath(urlPath);
					att.setServer(serverIp);
					att.setAttModule(module);
					att.setAttType(fileType);
					att.setRefId(refId);
					att.setAttSize(Math.round(fileSize / 1024));
					attService.save(att);

					atts.add(att);
				}
			}
			for(Attachment att:atts){
				String remoteIP = AppConfig.getDataItem(att.getServer());
				if (StringUtil.isNotEmpty(remoteIP)) {
					att.setServer(remoteIP);
				}			
			}
			res.put("status", "S");
			res.put("ret", JsonHelper.collection2json(atts));
			return JsonHelper.bean2Json(res);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("status", "E");
			res.put("errMsg", e.getMessage());
			return JsonHelper.bean2Json(res);
		}
	}

	@RequestMapping("/downloadAttachment.ctrl")
	public void download(HttpServletResponse response) throws Exception {
		String attId = request.getParameter("id");
		Attachment att = attService.get(attId);
		String path = request.getSession().getServletContext().getRealPath(att.getPath());
		String filename=new String(att.getAttName().getBytes("utf-8"), "ISO8859-1");

		response.setHeader("content-disposition", "attachment;filename=" + filename + "." + att.getAttType());

		InputStream in = null;
		OutputStream out = null;

		try {
			in = new FileInputStream(path);
			int len = 0;
			byte[] buffer = new byte[1024];
			out = response.getOutputStream();
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}

			}
		}
	}

	@RequestMapping("/getAttachmentList.ctrl")
	@ResponseBody
	public String getAttachmentList() {
		String refId = request.getParameter("refId");
		List<Attachment> atts = attService.gets("refId=?", new Object[] { refId });
		for (Attachment att : atts) {
			String remoteIP = AppConfig.getDataItem(att.getServer());
			if (StringUtil.isNotEmpty(remoteIP)) {
				att.setServer(remoteIP);
			}
		}
		return JsonHelper.collection2json(atts);

	}

	@RequestMapping("/deleteAttachment.ctrl")
	@ResponseBody
	public String deleteAttachment(HttpServletResponse response) {
		String id = request.getParameter("id");
		Map res = new HashMap();
		try {
			response.addHeader("Access-Control-Allow-Origin", "*");
			Attachment att = attService.get("id", id);
			attService.delete(att);
			String path = request.getSession().getServletContext().getRealPath(att.getPath());
			File file = new File(path);
			// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
			if (file.exists() && file.isFile()) {
				if (file.delete()) {
					res.put("status", "S");
				}
			}
			return JsonHelper.bean2Json(res);
		} catch (Exception e) {
			res.put("status", "E");
			res.put("errMsg", e.getMessage());
			return JsonHelper.bean2Json(res);

		}

	}

}