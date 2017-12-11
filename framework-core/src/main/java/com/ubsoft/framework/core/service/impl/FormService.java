package com.ubsoft.framework.core.service.impl;

import com.ubsoft.framework.core.conf.AppConfig;
import com.ubsoft.framework.core.context.AppContext;
import com.ubsoft.framework.core.dal.model.*;
import com.ubsoft.framework.core.dal.util.SQLUtil;
import com.ubsoft.framework.core.exception.ComException;
import com.ubsoft.framework.core.exception.DataAccessException;
import com.ubsoft.framework.core.service.IFormListener;
import com.ubsoft.framework.core.service.IFormService;
import com.ubsoft.framework.core.support.util.StringUtil;
import com.ubsoft.framework.core.support.xml.XmlUtil;
import com.ubsoft.framework.metadata.cache.MemoryFdmMeta;
import com.ubsoft.framework.metadata.cache.MemoryFormMeta;
import com.ubsoft.framework.metadata.entity.FdmEntity;
import com.ubsoft.framework.metadata.entity.FormEntity;
import com.ubsoft.framework.metadata.model.form.*;
import com.ubsoft.framework.metadata.model.form.fdm.DetailMeta;
import com.ubsoft.framework.metadata.model.form.fdm.FdmMeta;
import com.ubsoft.framework.metadata.model.form.fdm.FormListenerMeta;
import com.ubsoft.framework.system.entity.UISetting;
import com.ubsoft.framework.system.model.Subject;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.Serializable;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("formService")
@Transactional
public class  FormService  extends BaseService<Serializable> implements IFormService {


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BioSet load(String fdmKey, Serializable id) {
		BioSet result = new BioSet();
		FdmMeta fdmMeta = MemoryFdmMeta.getInstance().get(fdmKey);
		if (fdmMeta == null) {
			throw new ComException(ComException.MIN_ERROR_CODE_FDM + 1, "找不到界面数据模型:" + fdmKey);
		}
		String sql = this.getMasterSql(fdmMeta, "id=?", null);
		List<Bio> masterBios = dataSession.queryBio(sql, new Object[] { id });

		if (masterBios.size() != 1) {
			throw new DataAccessException(DataAccessException.DAL_ERROR_GET_Entity_ERROR, "记录不唯一。");
		}
		Bio masterBio = masterBios.get(0);
		result.setMaster(masterBio);
		// 遍历所有子表
		if (fdmMeta.getDetails() != null) {
			for (DetailMeta detailMeta : fdmMeta.getDetails()) {
				String detailSql = detailMeta.getSql();
				if (detailSql.toLowerCase().indexOf("where") != -1) {
					detailSql += " AND ";
				} else {
					detailSql += " WHERE  ";
				}
				detailSql += "T." + detailMeta.getFk() + "=?";
				Object[] args = new Object[] { masterBio.getObject(detailMeta.getRefKey()) };
				List<Bio> detailSet = dataSession.queryBio(detailSql, args);
				// 如果没有ID取Bio名称否则取ID，考虑同一个BIO存在于多个明细中。
				String detailBioKey = detailMeta.getId() == null ? detailMeta.getBio() : detailMeta.getId();
				result.setDetail(detailBioKey, detailSet);
			}

		}
		fireFormListener("AFTERLOAD", fdmMeta, result);
		return result;
	}

	private void fireFormListener(String event, FdmMeta fdmMeta, BioSet set) {

		List<FormListenerMeta> metas = fdmMeta.getListeners();
		if (metas != null) {
			for (FormListenerMeta listener : metas) {
				if (listener.getEvent().equals(event)) {
					IFormListener formListener = (IFormListener) AppContext.getBean(listener.getName());
					formListener.execute(set);
				}
			}
		}

	}

	@Override
	public BioSet saveForm(String fdmKey, BioSet set) {
		FdmMeta fdmMeta = MemoryFdmMeta.getInstance().get(fdmKey);
		if (fdmMeta == null) {
			throw new ComException(ComException.MIN_ERROR_CODE_FDM + 1, "找不到界面数据模型:" + fdmKey);
		}
		// 保存前插件
		fireFormListener("BEFORESAVE", fdmMeta, set);
		// 保存主表
		dataSession.saveBio(set.getMaster());

		if (fdmMeta.getDetails() != null) {
			for (DetailMeta detailMeta : fdmMeta.getDetails()) {
				// 如果没有ID取Bio名称否则取ID，考虑同一个BIO存在于多个明细中。
				String detailBioKey = detailMeta.getId() == null ? detailMeta.getBio() : detailMeta.getId();

				if (set.getDetail(detailBioKey) != null) {
					List<Bio> bios = set.getDetail(detailBioKey);
					// 设置外键值和Bio名称
					Object refKeyValue = set.getMaster().getObject(detailMeta.getRefKey());
					for (Bio bio : bios) {
						bio.setObject(detailMeta.getFk(), refKeyValue);
						bio.setName(detailMeta.getBio());

					}
					dataSession.batchSaveBio(bios);

				}
			}
		}
		fireFormListener("AFTERSAVE", fdmMeta, set);
		return set;
	}

	@Override
	public List<Bio> saveForm(String fdmKey, List<Bio> set) { 
		FdmMeta fdmMeta = MemoryFdmMeta.getInstance().get(fdmKey);
		if (fdmMeta == null) {
			throw new ComException(ComException.MIN_ERROR_CODE_FDM + 1, "找不到界面数据模型:" + fdmKey);
		}
		// 保存前插件
		for (Bio bio : set) {
			BioSet bioSet = new BioSet();
			bioSet.setMaster(bio);
			fireFormListener("BEFORESAVE", fdmMeta, bioSet);
			bio.setName(fdmMeta.getMaster().getBio());
			dataSession.saveBio(bio);
			fireFormListener("AFTERSAVE", fdmMeta, bioSet);
		}

		return set;
	}

	@Override
	public void deleteForm(String fdmKey, Serializable id) {
		FdmMeta fdmMeta = MemoryFdmMeta.getInstance().get(fdmKey);
		if (fdmMeta == null) {
			throw new ComException(ComException.MIN_ERROR_CODE_FDM + 1, "找不到界面数据模型:" + fdmKey);
		}
		// 构建只有ID的BioSet,用户监听删除事件
		BioSet set = new BioSet();
		Bio master = new Bio();
		master.setObject(master.getPrimaryKey(), id);
		set.setMaster(master);
		fireFormListener("BEFOREDELETE", fdmMeta, set);
		for (DetailMeta detailMeta : fdmMeta.getDetails()) {
			String bioName = detailMeta.getBio();
			dataSession.deleteBio(bioName, detailMeta.getFk(), new Object[] { id });
		}
		String masterBioName = fdmMeta.getMaster().getBio();

		dataSession.deleteBio(masterBioName, new Object[] { id });
		// 注册删除事件监听
		fireFormListener("AFTERDELETE", fdmMeta, set);

	}

	@Override
	public void deleteForm(String fdmKey, Serializable[] ids) {
		for (Serializable id : ids) {
			deleteForm(fdmKey, id);
		}
	}

	@Override
	public FormMeta getFormMeta(String formKey) throws Exception {
		// 如果是debug模式每次读取测试界面
		if (AppConfig.isDebug()) {
			this.refreshFormMeta(formKey);
		}
		FormMeta meta = MemoryFormMeta.getInstance().get(formKey);
		if (meta == null) {
			throw new ComException(ComException.MIN_ERROR_CODE_FDM + 1, "不存在Form:" + formKey);
		}
		meta.setId(formKey);
		setDefaultUnPermission(meta);
		return meta;
	}

	/**
	 * 获取界面默认元素排除权限，主要针对工具栏按钮、列表字段、编辑字段、子列表字段、子表按钮等
	 * 规则：默认有全部权限，如果要对界面元素设置权限；需要把该元素的按照一定的命名规则加入到permission表里面，
	 * 然后在用户或者角色的权限维度表里面添加该权限。 本方法只返回权限为false的集合，前台根据本集合禁用或者隐藏界面元素
	 *
	 * @param meta
	 * @return
	 */
	private void setDefaultUnPermission(FormMeta meta) {
		String formId = meta.getId();
		Map<String, Boolean> formPermission = new HashMap<String, Boolean>();
		// 按钮权限
		String savePermKey = formId.toUpperCase() + "_SAVE";
		String newPermKey = formId.toUpperCase() + "_NEW";
		String delPermKey = formId.toUpperCase() + "_DEL";
		if (Subject.getSubject() != null) {
			if (!Subject.getSubject().isPermitted(savePermKey)) {
				formPermission.put(savePermKey, false);
			}
			if (!Subject.getSubject().isPermitted(newPermKey)) {
				formPermission.put(newPermKey, false);
			}
			if (!Subject.getSubject().isPermitted(delPermKey)) {
				formPermission.put(delPermKey, false);
			}
		}
		// 循环列
		// ----待实现
		// 循环编辑字段
		// -----待实现
		meta.setPermision(formPermission);

	}

	private String getDbType() {
		return "oracle";
	}

	@Override
	public List<Bio> query(QueryModel queryModel) {
		String fdmId = queryModel.getFdmId();
		FdmMeta fdmMeta = MemoryFdmMeta.getInstance().get(fdmId);
		if (fdmMeta == null) {
			throw new ComException(ComException.MIN_ERROR_CODE_FDM + 1, "找不到界面数据模型:" + fdmId);
		}
		String orderBy = queryModel.getOrderBy();// 默认排序
		ConditionTree ctree = queryModel.getConditionTree();// 条件树
		String condition = ctree.transferCondition();// 条件字符串
		Object[] params = ctree.transferParameter();// 查询参数
		String sql = this.getMasterSql(fdmMeta, condition, orderBy);
		int limit = queryModel.getLimit();
		if (limit > 0) {
			sql = SQLUtil.getLimitSql(sql, limit, this.getDbType());
		}
		List<Bio> listBio = dataSession.queryBio(sql, params);

		return listBio;
	}

	@SuppressWarnings("unchecked")
	@Override
	public PageResult<Bio> queryPage(QueryModel queryModel) {
		String fdmId = queryModel.getFdmId();
		FdmMeta fdmMeta = MemoryFdmMeta.getInstance().get(fdmId);
		if (fdmMeta == null) {
			throw new ComException(ComException.MIN_ERROR_CODE_FDM + 1, "找不到界面数据模型:" + fdmId);
		}
		String orderBy = queryModel.getOrderBy();// 默认排序
		// if (StringUtil.isEmpty(orderBy)) {
		// orderBy = "T.createddate desc";
		// }
		ConditionTree ctree = queryModel.getConditionTree();// 条件树
		String condition = ctree.transferCondition();// 条件字符串
		Object[] params = ctree.transferParameter();// 查询参数
		int pageNumber = queryModel.getPageNo();
		int pageSize = queryModel.getPageSize();
		String sql = this.getMasterSql(fdmMeta, condition, orderBy);
		sql = SQLUtil.getPageSql(sql, pageSize, pageNumber, this.getDbType());
		PageResult<Bio> pager = dataSession.queryBio(sql, pageSize, pageNumber, params);

		return pager;
	}

	private String getMasterSql(FdmMeta fdmMeta, String condition, String orderBy) {
		// 查询的sql
		String sql = fdmMeta.getMaster().getSql();

		if (StringUtil.isNotEmpty(sql)) {
			if (StringUtil.isNotEmpty(condition)) {
				if (sql.toLowerCase().indexOf(" where") != -1) {
					sql += " and (" + condition + ")";
				} else {
					sql += " where " + condition;
				}
			}
			sql = SQLUtil.getDimensionSql(sql);// 加入数据纬度权限
			if (StringUtil.isNotEmpty(orderBy)) {
				sql += " order by " + orderBy;
			}

		}
		return sql;
	}

	@Override
	public List<UISetting> getUISetting(String userKey, String uiKey) {
		List<UISetting> uiSettingList = dataSession.gets(UISetting.class, "userKey=? and uiKey=?", new Object[] {
				userKey, uiKey });
		return uiSettingList;
	}

	@Override
	public List<UISetting> saveUISetting(List<UISetting> settings) {
		String userKey = settings.get(0).getUserKey();
		String uiKey = settings.get(0).getUiKey();
		String hql = "delete from UISetting where userKey=? and uiKey=?";
		dataSession.executeUpdate(hql, new Object[] { userKey, uiKey });
		dataSession.save(settings);
		return settings;
	}

	private String getFdmMetaConfig(String fdmId) {
		FdmEntity fdm = dataSession.get(FdmEntity.class, "formId", fdmId);
		return fdm.getFdmXml();
	}

	private String getFormMetaConfig(String formId) {
		FormEntity form = dataSession.get(FormEntity.class, "formId", formId);
		if (form != null)
			return form.getFormXml();
		else
			return null;
	}

	@Override
	public void initFormMeta() {
		List<FormEntity> forms = dataSession.gets(FormEntity.class);
		try {
			for (FormEntity form : forms) {
				String formXml = form.getFormXml();
				Document document = DocumentHelper.parseText(formXml);
				Element root = document.getRootElement();
				JAXBContext jc = null;
				if (root.getName().equals("ListForm")) {
					jc = JAXBContext.newInstance(ListFormMeta.class);
				} else if (root.getName().equals("EditForm")) {
					jc = JAXBContext.newInstance(EditFormMeta.class);
				} else if (root.getName().equals("ExplorerForm")) {
					jc = JAXBContext.newInstance(ExplorerFormMeta.class);
				} else if (root.getName().equals("SelectForm")) {
					jc = JAXBContext.newInstance(SelectFormMeta.class);
				} else {
					jc = JAXBContext.newInstance(FormMeta.class);
				}
				Unmarshaller m = jc.createUnmarshaller();
				StringReader in = new StringReader(formXml);
				FormMeta formMeta = (FormMeta) m.unmarshal(in);
				if (formMeta.getId() == null) {
					formMeta.setId(form.getFormKey());
				}
				FdmEntity fdmEntity = dataSession.get(FdmEntity.class, formMeta.getFdm());
				if (fdmEntity != null) {
					String fdmXml = fdmEntity.getFdmXml();
					FdmMeta fdmMeta = XmlUtil.fromXML(fdmXml, FdmMeta.class);
					MemoryFdmMeta.getInstance().put(fdmEntity.getFdmKey(), fdmMeta);
				} else {
					throw new ComException(ComException.MIN_ERROR_CODE_FDM + 1, "找不到界面数据模型:" + formMeta.getFdm());

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void refreshFormMeta(String formKey) {
		FormEntity form = dataSession.get(FormEntity.class, "formKey", formKey);
		try {
			String formXml = form.getFormXml();
			Document document = DocumentHelper.parseText(formXml);
			Element root = document.getRootElement();
			JAXBContext jc = null;
			if (root.getName().equals("ListForm")) {
				jc = JAXBContext.newInstance(ListFormMeta.class);
			} else if (root.getName().equals("EditForm")) {
				jc = JAXBContext.newInstance(EditFormMeta.class);
			} else if (root.getName().equals("ExplorerForm")) {
				jc = JAXBContext.newInstance(ExplorerFormMeta.class);
			} else if (root.getName().equals("SelectForm")) {
				jc = JAXBContext.newInstance(SelectFormMeta.class);
			} else {
				jc = JAXBContext.newInstance(FormMeta.class);
			}
			Unmarshaller m = jc.createUnmarshaller();
			StringReader in = new StringReader(formXml);
			FormMeta formMeta = (FormMeta) m.unmarshal(in);
			if (formMeta.getId() == null) {
				formMeta.setId(form.getFormKey());
			}
			FdmEntity fdmEntity = dataSession.get(FdmEntity.class, formMeta.getFdm());
			if (fdmEntity != null) {
				String fdmXml = fdmEntity.getFdmXml();
				FdmMeta fdmMeta = XmlUtil.fromXML(fdmXml, FdmMeta.class);
				MemoryFdmMeta.getInstance().put(fdmEntity.getFdmKey(), fdmMeta);
			} else {
				throw new ComException(ComException.MIN_ERROR_CODE_FDM + 1, "找不到界面数据模型:" + formMeta.getFdm());

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}





}
