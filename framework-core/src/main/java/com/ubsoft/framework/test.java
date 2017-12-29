package com.ubsoft.framework;

import com.ubsoft.framework.core.dal.entity.BioPropertyMeta;
import com.ubsoft.framework.core.support.json.JsonHelper;


public class test {

	public static void main(String [] ss){
		boolean bbb=false;
		
		if(bbb==true){
					
		}
		
		for(int i=0;i<1;i++){
			BioPropertyMeta bm= new BioPropertyMeta();
			//bm.setPrimaryKey(true);
			//bm.setVersionKey(false);
			System.out.println(JsonHelper.bean2Json(bm));
			//System.out.println(Generators.timeBasedGenerator().generate());
		}
	}
}
