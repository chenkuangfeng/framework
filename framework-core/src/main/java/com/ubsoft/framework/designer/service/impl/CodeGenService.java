package com.ubsoft.framework.designer.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.core.exception.ComException;
import com.ubsoft.framework.designer.service.ICodeGenService;
import com.ubsoft.framework.designer.util.CodeGenUtil;

@Service("codeGenService")
public class CodeGenService implements ICodeGenService {

	@Override
	public void genCode(BioMeta masterBio, BioMeta [] detailBios, String pkg, String ftl, String fileName, Map model) {
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

	


	@Override
	public void genEntity(String masterBio, String pkg,String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void genService(String masterBio, String pkg,String path) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void genFdm(String masterBio, String[] detailBios) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void genForm(String formFtl, String masterBio, String[] detailBios, String path) {
		// TODO Auto-generated method stub
		
	}

	

	
	

}
