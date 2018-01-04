package com.ubsoft.framework.designer.service;

import java.util.Map;

import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.designer.model.DbTableMeta;

/**
 * 代码生成服务类
 * 
 * @author chenkf
 * 
 */
public interface ICodeGenService {
	/**
	 * 
	 * @param ftl
	 * @param model
	 * @param path
	 */
	void genCode(BioMeta masterBio, BioMeta[] detailTables, String pkg, String ftl, String fileName, Map model);
	/**
	 * 
	 * @param masterTable
	 * @param detailTables
	 * @param pkg
	 * @param ftl
	 * @param fileName
	 * @param model
	 */
	void genCode(String masterBio, String[] detailBio, String pkg, String ftl, String fileName, Map model);
	
	/**
	 * 根据bio生成fdm
	 * @param masterBio
	 * @param detailTables
	 */
	void genFdm(String masterBio, String[] detailBios);
	/**
	 * 根据BIO生生成实体bean
	 * @param masterBio
	 * @param pkg
	 */
	void genEntity(String masterBio,String pkg,String path);
	/**
	 * 生成sevices接口和实现类
	 * @param masterBio
	 * @param pkg
	 */
	void genService(String masterBio,String pkg,String path);
	
	/**
	 * 生成界面
	 * @param masterBio
	 * @param detailBios
	 * @param pkg
	 */
	void genForm(String formFtl,String masterBio,String[] detailBios,String path);





}
