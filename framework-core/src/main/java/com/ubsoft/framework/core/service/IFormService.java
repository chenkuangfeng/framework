package com.ubsoft.framework.core.service;

import java.io.Serializable;
import java.util.List;

import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.BioSet;
import com.ubsoft.framework.core.dal.model.PageResult;
import com.ubsoft.framework.core.dal.model.QueryModel;
import com.ubsoft.framework.metadata.model.form.FormMeta;
import com.ubsoft.framework.system.entity.UISetting;
/**
 * 根据fdm配置获取数据信息
 * @author Administrator
 *
 */
public interface IFormService extends IBaseService<Serializable> {
	
	void initFormMeta();
	
	void refreshFormMeta(String formId);
		
	
	public FormMeta getFormMeta(String formId) throws Exception;
	/**
	 * 基于fdm列表界面查询
	 * @param queryModel
	 * @return
	 */
	public List<Bio> query(QueryModel queryModel);
	/**
	 * 基于fdm列表界面分页查询
	 * @param queryModel
	 * @return
	 */
	public PageResult<Bio> queryPage(QueryModel queryModel);	
	
	
	/**
	 * 基于fdm编辑界面加载多个表的数据
	 * @param fdmId
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	BioSet  load(String fdmKey, Serializable id);		
	/**
	 * 基于fdm编辑界面保存多个表的数据
	 * @param forms
	 * @return
	 */
	 BioSet saveForm(String fdmKey, BioSet bioSet);
	 
	 /**
	  * 保存列表界面
	  * @param fdmKey
	  * @param list
	  * @return
	  */
	 List<Bio> saveForm(String fdmKey, List<Bio> list);

	
	/**
	 * 根据id删除数据，如果有子表先删除子表，再删除主表
	 * @param fdmMeta
	 * @param ids
	 */
	 void deleteForm(String fdmKey, Serializable[] ids);
	
	 void deleteForm(String fdmKey, Serializable id);



	 /**
     * 获取用户某个界面的自定义配置方案
     * @param userKey
     * @param uiKey
     * @return
     */
    List<UISetting> getUISetting(String userKey, String uiKey);
    
    /**
     * 保存用户界面方案
     * @param settings
     * @return
     */
    List<UISetting> saveUISetting(List<UISetting> settings);
	
	
	

	

	
	
	
	
	
	
	
}
