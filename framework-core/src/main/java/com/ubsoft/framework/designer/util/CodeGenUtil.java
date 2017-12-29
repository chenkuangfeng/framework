package com.ubsoft.framework.designer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ubsoft.framework.core.cache.MemoryBioMeta;
import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.core.support.util.FreeMarkerUtil;
import com.ubsoft.framework.designer.cache.MemoryTableMeta;
import com.ubsoft.framework.designer.model.DbTableMeta;

public class CodeGenUtil {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void genCode(String masterBio,String [] detailBios,String pkg, String ftl,String fileName, Map model) throws Exception {
		Map root= new HashMap();
		root.put("pkg", pkg);
		root.putAll(model);
		BioMeta masterBioMeta= MemoryBioMeta.getInstance().get(masterBio);
		if(masterBio!=null){
			root.put("m", masterBioMeta);
		}
		if(detailBios!=null){
			List details= new ArrayList();
			for(String detail:detailBios){
				BioMeta detailBioMeta= MemoryBioMeta.getInstance().get(detail);
				details.add(detailBioMeta);
			}
			root.put("d", details);

		}		
		FreeMarkerUtil.getInstance().genFile(ftl, model, fileName);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void genCode(BioMeta masterBio,BioMeta [] detailBios,String pkg, String ftl,String fileName, Map model) throws Exception {
		Map root= new HashMap();
		root.put("pkg", pkg);
	
		if(masterBio!=null){
			root.put("m", masterBio);
		}
		if(detailBios!=null){	
			root.put("details", detailBios);
		}		
		FreeMarkerUtil.getInstance().genFile(ftl, model, fileName);
	}
}
