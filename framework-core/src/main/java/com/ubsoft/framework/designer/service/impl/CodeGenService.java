package com.ubsoft.framework.designer.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.core.dal.entity.BioPropertyMeta;
import com.ubsoft.framework.core.exception.ComException;
import com.ubsoft.framework.core.service.IBioMetaService;
import com.ubsoft.framework.core.service.IFdmMetaService;
import com.ubsoft.framework.core.service.impl.BaseService;
import com.ubsoft.framework.core.support.util.FreemarkerUtil;
import com.ubsoft.framework.designer.service.ICodeGenService;
import com.ubsoft.framework.designer.util.CodeGenUtil;
import com.ubsoft.framework.metadata.entity.FdmEntity;
import com.ubsoft.framework.metadata.entity.FormEntity;

@Service("codeGenService")
public class CodeGenService extends BaseService<Serializable> implements ICodeGenService {

	@Autowired
	IBioMetaService bioMetaService;

	@Autowired
	IFdmMetaService fdmMetaService;

	@Override
	public void genCode(BioMeta masterBio, BioMeta[] detailBios, String pkg, String ftl, String fileName, Map model) {
		try {
			CodeGenUtil.genCode(masterBio, detailBios, pkg, ftl, fileName, model);
		} catch (Exception ex) {
			throw new ComException(ComException.MIN_ERROR_CODE_GLOBAL + 1, masterBio.getTableKey() + "-" + ftl + "生成失败");
		}

	}

	@Override
	public void genCode(String masterBio, String[] detailBios, String pkg, String ftl, String fileName, Map model) {
		try {
			CodeGenUtil.genCode(masterBio, detailBios, pkg, ftl, fileName, model);
		} catch (Exception ex) {
			throw new ComException(ComException.MIN_ERROR_CODE_GLOBAL + 1, masterBio + "-" + ftl + "生成失败");
		}

	}

	/**
	 * 处理生成模型
	 * 
	 * @param meta
	 * @return
	 */
	private BioMeta dealMeta(BioMeta meta) {
		Set<BioPropertyMeta> properties = meta.getPropertySet();
		Set<BioPropertyMeta> propertiesNew = new HashSet<BioPropertyMeta>();
		for (BioPropertyMeta p : properties) {
			String pkey = p.getPropertyKey();
			if (pkey.equals("id") || pkey.equals("createdBy") || pkey.equals("createdDate") || pkey.equals("updatedBy") || pkey.equals("updatedDate")
					|| pkey.equals("status") || pkey.equals("version")) {
				continue;
			}
			if (p.getDataType().equals("Integer")) {
				p.setDataType("int");
			}
			if (p.getDataType().equals("Long")) {
				p.setDataType("long");
			}
			if (p.getDataType().equals("Double")) {
				p.setDataType("double");
			}
			propertiesNew.add(p);
		}
		meta.setPropertySet(propertiesNew);
		return meta;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void genEntity(String masterBio, String pkg, String path) {
		Map root = new HashMap();
		BioMeta meta = bioMetaService.getBioMeta(masterBio);
		meta = this.dealMeta(meta);
		root.put("pkg", pkg + ".entity");
		root.put("bioMeta", meta);
		root.put("properties", meta.getPropertySet());
		path = path + "\\entity";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		FreemarkerUtil.genFile("entity.ftl", root, path + "\\" + meta.getBioKey() + ".java");

	}

	@Override
	public void genService(String masterBio, String pkg, String path) {
		Map root = new HashMap();
		BioMeta meta = bioMetaService.getBioMeta(masterBio);
		String pathInterface = path + "\\service";
		path = path + "\\service\\impl";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		root.put("pkg", pkg + ".service");
		root.put("entitypkg", pkg + ".entity." + masterBio);
		root.put("bioMeta", meta);
		FreemarkerUtil.genFile("iservice.ftl", root, pathInterface + "\\I" + meta.getBioKey() + "Service.java");
		FreemarkerUtil.genFile("service.ftl", root, path + "\\" + meta.getBioKey() + "Service.java");

	}

	@Override
	public void genFdm(String masterBio, String[] detailBios) {
		FdmEntity fdmEntity = dataSession.get(FdmEntity.class, "fdmKey", masterBio);
		if (fdmEntity == null) {
			BioMeta meta = bioMetaService.getBioMeta(masterBio);
			Map root = new HashMap();
			root.put("master", meta);
			List<BioMeta> detailMetas = new ArrayList<BioMeta>();
			for (String detail : detailBios) {
				BioMeta detailMeta = bioMetaService.getBioMeta(detail);
				detailMetas.add(detailMeta);
			}
			root.put("details", detailMetas);
			String fdmxml = FreemarkerUtil.getTemplateResult("fdm.ftl", root);
			fdmEntity = new FdmEntity();
			fdmEntity.setFdmKey(masterBio);
			fdmEntity.setFdmName(meta.getBioName());
			fdmEntity.setFdmXml(fdmxml);
			dataSession.save(fdmEntity);

		} else {
			logger.error("..................已存在Fdm:" + masterBio + ",忽略生成.......................");
		}

	}

	@Override
	public void genForm(String formFtl, String masterBio, String[] detailBios, String path) {
		String formName = null;
		boolean isDb = false;
		if (formFtl.equals("listform.ftl")) {
			formName = masterBio + "List";
			isDb = true;
		} else if (formFtl.equals("editform.ftl")) {
			formName = masterBio + "Edit";
			isDb = true;
		} else if (formFtl.equals("explorerform.ftl")) {
			formName = masterBio + "Explorer";
			isDb = true;
		} else if (formFtl.equals("explorerform_md.ftl")) {
			formName = masterBio + "Explorer";
			isDb = true;
		} else if (formFtl.equals("listform-web.ftl")) {
			formName = masterBio + "Lst";
			isDb = false;
		}

		BioMeta meta = bioMetaService.getBioMeta(masterBio);
		Map root = new HashMap();
		root.put("master", meta);
		List<BioMeta> detailMetas = new ArrayList<BioMeta>();
		for (String detail : detailBios) {
			BioMeta detailMeta = bioMetaService.getBioMeta(detail);
			detailMetas.add(detailMeta);
		}
		root.put("details", detailMetas);
		
		if (isDb) {
			String formXml = FreemarkerUtil.getTemplateResult(formFtl, root);
			FormEntity formEntity = dataSession.get(FormEntity.class, "formKey", formName);
			if (formEntity == null) {
				formEntity = new FormEntity();
				formEntity.setFormKey(formName);
				formEntity.setFdmKey(masterBio);
				formEntity.setFormName(meta.getBioName());
				formEntity.setFormXml(formXml);
				dataSession.save(formEntity);
			} else {
				logger.info("已存在Form:" + formName);
			}
		}else{
			path = path + "\\form";
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
			FreemarkerUtil.genFile(formFtl, root, path + "\\"+formName+".jsp");

		}

	}

}
