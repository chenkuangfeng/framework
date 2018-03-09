package com.ubsoft.framework.mainframe.widgets.util;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSetView;
import com.borland.dx.dataset.ProviderHelp;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.dataset.RowStatus;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.ubsoft.framework.core.dal.entity.BioMeta;
import com.ubsoft.framework.core.dal.entity.BioPropertyMeta;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.util.TypeUtil;
import com.ubsoft.framework.core.exception.DataAccessException;
import com.ubsoft.framework.core.support.util.BeanUtil;

public class DataSetUtil {
	/**
	 * 从BioSet加载数据到dataSet
	 * 
	 * @param bioSet
	 * @param masterDataSet
	 */
	public static Map<String, Integer> typeMapping = new HashMap<String, Integer>();
	static {
		typeMapping.put(TypeUtil.STRING, Variant.STRING);
		typeMapping.put(TypeUtil.INTEGER, Variant.INT);
		typeMapping.put(TypeUtil.DOUBLE, Variant.DOUBLE);
		typeMapping.put(TypeUtil.LONG, Variant.LONG);
		typeMapping.put(TypeUtil.SHORT, Variant.SHORT);
		typeMapping.put(TypeUtil.BIGDECIMAL, Variant.BIGDECIMAL);
		typeMapping.put(TypeUtil.BYTE_ARRAY, Variant.BINARY_STREAM);
		typeMapping.put(TypeUtil.DATE, Variant.DATE);
		typeMapping.put(TypeUtil.TIME, Variant.TIME);
		typeMapping.put(TypeUtil.TIMESTAMP, Variant.TIMESTAMP);
	}

	public static void loadFromBio(List<Bio> bios, StorageDataSet dataSet) {
		LoadCanceler lc = new LoadCanceler() {
			boolean bLC = false;

			public void clearCancelled() {
				this.bLC = false;
			}

			public boolean isCancelled() {
				return this.bLC;
			}

			public void cancelLoad() {
				this.bLC = true;
			}
		};
		try {
			lc.clearCancelled();
			dataSet.empty();
			Variant[] rows = dataSet.startLoading(lc, RowStatus.LOADED, false);
			Column[] cols = dataSet.getColumns();// grid列
			for (Bio bio : bios) {
				int i = 0;
				for (Variant var : rows) {
					Column col = cols[i];
					String field = col.getColumnName();
					if (bio.containsKey(field)) {
						setVariant(var, cols[i], bio.get(field));
						i++;
					} else {
						throw new RuntimeException("不存在列field：" + field);
					}

				}
				dataSet.loadRow();
			}
			lc.cancelLoad();
		} finally {
			dataSet.endLoading();
			if (!dataSet.isOpen())
				dataSet.open();
		}

	}

	public static void setVariant(Variant variant, Column col, Object value) {
		int dataType = col.getDataType();
		if (value == null) {
			variant.setAssignedNull();
			return;
		}
		switch (dataType) {
		case Variant.STRING:
			Object strObj = TypeUtil.convert(TypeUtil.STRING, value);
			variant.setString((String) strObj);
			break;
		case Variant.INT:
			Object intObj = TypeUtil.convert(TypeUtil.INTEGER, value);
			variant.setInt((Integer) intObj);
			break;
		case Variant.DOUBLE:
			Object dbObj = TypeUtil.convert(TypeUtil.DOUBLE, value);
			variant.setDouble((Double) dbObj);
			break;
		case Variant.LONG:
			Object longObj = TypeUtil.convert(TypeUtil.LONG, value);
			variant.setLong((Long) longObj);
			break;
		case Variant.SHORT:
			Object shortObj = TypeUtil.convert(TypeUtil.LONG, value);
			variant.setShort((Short) shortObj);
			break;
		case Variant.BIGDECIMAL:
			Object decObj = TypeUtil.convert(TypeUtil.BIGDECIMAL, value);

			variant.setBigDecimal((BigDecimal) decObj);
			break;
		case Variant.BINARY_STREAM:
			Object baObj = TypeUtil.convert(TypeUtil.BYTE_ARRAY, value);
			//variant.setByteArray((byte[]) baObj, 0);
			variant.setInputStream(new ByteArrayInputStream((byte[])baObj));
			

			break;
		case Variant.DATE:
			Object dateObj = TypeUtil.convert(TypeUtil.DATE, value);
			Date dateVal = (Date) dateObj;
			variant.setDate(new java.sql.Date(dateVal.getTime()));
			break;
		case Variant.TIMESTAMP:
			Object tmObj = TypeUtil.convert(TypeUtil.TIMESTAMP, value);
			variant.setTimestamp((Timestamp) tmObj);
			break;
		case Variant.TIME:
			Object timeObj = TypeUtil.convert(TypeUtil.TIME, value);

			variant.setTime((Time) timeObj);
			break;
		default:
			break;

		}
	}

	/**
	 * 转换DataRow到BIO
	 * 
	 * @param row
	 * @param bio
	 */
	public static void dataRow2Bio(ReadWriteRow row, Bio bio) {
		Column[] cols = row.getColumns();
		for (Column col : cols) {
			String field = col.getColumnName();
			int dataType = col.getDataType();
			if (dataType == Variant.STRING) {
				bio.setString(field, row.getString(field));
			} else if (dataType == Variant.INT) {
				bio.setInt(field, row.getInt(field));
			} else if (dataType == Variant.DOUBLE) {
				bio.setDouble(field, row.getDouble(field));
			} else if (dataType == Variant.LONG) {
				
				bio.setLong(field, row.getLong(field));
			} else if (dataType == Variant.SHORT) {
				bio.setShort(field, row.getShort(field));
			} else if (dataType == Variant.BIGDECIMAL) {
				bio.setBigDecimal(field, row.getBigDecimal(field));
			} else if (dataType == Variant.DATE) {
				bio.setDate(field, row.getDate(field));
			} else if (dataType == Variant.TIMESTAMP) {
				bio.setTimestamp(field, row.getTimestamp(field));
			} else if (dataType == Variant.TIME) {
				bio.setTime(field, row.getTime(field));
			} else if (dataType == Variant.BINARY_STREAM) {
				bio.setBinary(field, row.getByteArray(field)); 
			} else {
				throw new DataAccessException(5, "不支持Variant类型：" + dataType);
			}
		}

	}

	/**
	 * 转换Bio到DataRow
	 * 
	 * @param bio
	 * @param row
	 */
	public static void bio2DataRow(Bio bio, ReadWriteRow row) {
		Column[] cols = row.getColumns();
		for (Column col : cols) {
			String field = col.getColumnName();
			int dataType = col.getDataType();
			if (!bio.containsKey(field)) {
				throw new DataAccessException(6, "bio不存在Key：" + field);
			}
			Variant variant = new Variant();
			setVariant(variant, col, bio.getObject(field));
			row.setVariant(field, variant);
		}
	}

	/**
	 * 获取新增和修改的行，不包含删除的行
	 * 
	 * @param ds
	 * @return
	 */
	public static List<Bio> getChanges(StorageDataSet dataSet) {
		dataSet.post();
		List<Bio> bioSet = new ArrayList<Bio>();
		DataSetView updatedView = new DataSetView();
		DataSetView insertView = new DataSetView();
		dataSet.getUpdatedRows(updatedView);
		dataSet.getInsertedRows(insertView);
		if (insertView.getRowCount() > 0) {
			DataRow row = new DataRow(insertView);
			insertView.first();
			do {
				Bio bio = new Bio();
				insertView.getDataRow(row);
				dataRow2Bio(row, bio);
				bio.setStatus(Bio.NEW);
				bioSet.add(bio);
			} while (insertView.next());
		}
		if (updatedView.getRowCount() > 0) {
			DataRow row = new DataRow(updatedView);
			updatedView.first();
			do {
				Bio bio = new Bio();
				updatedView.getDataRow(row);
				dataRow2Bio(row, bio);
				bio.setStatus(Bio.UPDATE);
				bioSet.add(bio);
			} while (updatedView.next());
		}
		return bioSet;
	}

	public static void fillbackFromResult(Object result, StorageDataSet dataSet) throws Exception {
		dataSet.enableDataSetEvents(false);
		DataSetView updatedView = new DataSetView();
		DataSetView insertView = new DataSetView();
		dataSet.getUpdatedRows(updatedView);
		dataSet.getInsertedRows(insertView);
		int i = 0;
		if (insertView.getRowCount() > 0) {

			insertView.first();
			do {
				if (!dataSet.goToInternalRow(insertView.getInternalRow()))
					continue;
				// if (result instanceof List) {
				// List<Bio> set = (List<Bio>) result;
				// //bean2DataRow(set.get(i),dataSet);
				// bio2DataRow(set.get(i), dataSet);
				// i++;
				// }
				List listEntity = (List) result;
				bean2DataRow(listEntity.get(i), dataSet);
				i++;

				dataSet.post();

			} while (insertView.next());
		}
		if (updatedView.getRowCount() > 0) {

			updatedView.first();
			do {

				if (!dataSet.goToInternalRow(updatedView.getInternalRow()))
					continue;

				// if (result instanceof BioSet) {
				// BioSet set = (BioSet) result;
				//
				// bio2DataRow(set.get(i), dataSet);
				// i++;
				// } else {
				List listEntity = (List) result;
				bean2DataRow(listEntity.get(i), dataSet);

				i++;
				// }
				dataSet.post();

			} while (updatedView.next());
		}
		dataSet.enableDataSetEvents(true);

	}

	public static void fillbackFromBio(List<Bio> result, StorageDataSet dataSet) throws Exception {
		dataSet.enableDataSetEvents(false);
		DataSetView updatedView = new DataSetView();
		DataSetView insertView = new DataSetView();
		dataSet.getUpdatedRows(updatedView);
		dataSet.getInsertedRows(insertView);
		int i = 0;
		if (insertView.getRowCount() > 0) {

			insertView.first();
			do {
				if (!dataSet.goToInternalRow(insertView.getInternalRow()))
					continue;
				

				bio2DataRow(result.get(i), dataSet);
				i++;

				dataSet.post();

			} while (insertView.next());
		}
		if (updatedView.getRowCount() > 0) {

			updatedView.first();
			do {

				if (!dataSet.goToInternalRow(updatedView.getInternalRow()))
					continue;

				
				bio2DataRow(result.get(i), dataSet);

				i++;
			
				dataSet.post();

			} while (updatedView.next());
		}
		dataSet.enableDataSetEvents(true);

	}

	/**
	 * 从实体bean里面加载dateset结构
	 * 
	 * @param dataSet
	 * @param clazz
	 * @throws Exception
	 */
	public static void loadDataSetMetaFromClass(StorageDataSet dataSet, Class clazz) throws Exception {
		PropertyDescriptor[] propertyDescriptors = BeanUtil.getPropertyDescriptors(clazz);
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String key = descriptor.getName();
			Class pclass = descriptor.getPropertyType();
			if (!key.equals("class")) {
				Column column = dataSet.hasColumn(key);
				if (column == null) {
					column = new Column();
					column.setVisible(0);
					column.setColumnName(key);
					if (typeMapping.containsKey(pclass.getSimpleName())) {
						column.setDataType(typeMapping.get(pclass.getSimpleName()));
					} else {
						throw new RuntimeException("不支持类型" + pclass.getSimpleName() + "到dataSet转换.");
					}
					dataSet.addColumn(column);
				}
			}
		}
	}

	public static void loadDataSetMetaFromBioMeta(StorageDataSet dataSet, BioMeta meta) throws Exception {
		Set<BioPropertyMeta> properties = meta.getPropertySet();
		for (BioPropertyMeta property : properties) {
			String key = property.getPropertyKey();
			String dataType = property.getDataType();
			Column column = dataSet.hasColumn(key);
			if (column == null) {
				column = new Column();
				column.setVisible(0);
				column.setColumnName(key);
				if (typeMapping.containsKey(dataType)) {
					if(dataType.equals(TypeUtil.BYTE_ARRAY)){
					 System.out.println(dataType);
					}
					column.setDataType(typeMapping.get(dataType));
				} else {
					throw new RuntimeException("不支持BioMeta数据类型" + dataType + "到dataSet转换.");
				}
				dataSet.addColumn(column);
			}

		}
	}

	public static <T> void loadFromBeans(List<T> beans, StorageDataSet dataSet) throws Exception {
		LoadCanceler lc = new LoadCanceler() {
			boolean bLC = false;

			public void clearCancelled() {
				this.bLC = false;
			}

			public boolean isCancelled() {
				return this.bLC;
			}

			public void cancelLoad() {
				this.bLC = true;
			}
		};
		try {
			lc.clearCancelled();
			dataSet.empty();
			Variant[] rows = dataSet.startLoading(lc, RowStatus.LOADED, false);
			Column[] cols = dataSet.getColumns();// grid列

			PropertyDescriptor[] propertyDescriptors = null;
			if (beans.size() > 0) {
				Class type = beans.get(0).getClass();
				propertyDescriptors = BeanUtil.getPropertyDescriptors(type);
			}
			for (T bean : beans) {
				int i = 0;
				for (Variant var : rows) {
					Column column = cols[i];
					Object beanVal = null;
					for (int j = 0; j < propertyDescriptors.length; j++) {
						PropertyDescriptor descriptor = propertyDescriptors[j];
						String field = descriptor.getName();
						if (field.equals(column.getColumnName())) {
							Method readMethod = descriptor.getReadMethod();
							beanVal = readMethod.invoke(bean, new Object[0]);
							break;
						}
					}
					setVariant(var, column, beanVal);
					i++;
				}
				dataSet.loadRow();
			}
			lc.cancelLoad();
		} finally {
			dataSet.endLoading();
			if (!dataSet.isOpen())
				dataSet.open();
		}

	}

	public static <T> List<T> getChanges(StorageDataSet dataSet, Class<T> clazz) throws Exception {
		dataSet.post();
		List<T> list = new ArrayList<T>();
		DataSetView updatedView = new DataSetView();
		DataSetView insertView = new DataSetView();
		dataSet.getUpdatedRows(updatedView);
		dataSet.getInsertedRows(insertView);
		if (insertView.getRowCount() > 0) {
			DataRow row = new DataRow(insertView);
			insertView.first();
			do {
				T bean = null;
				bean = clazz.newInstance();
				insertView.getDataRow(row);
				dataRow2Bean(row, bean);
				list.add(bean);
			} while (insertView.next());
		}
		if (updatedView.getRowCount() > 0) {
			DataRow row = new DataRow(updatedView);
			updatedView.first();
			do {
				T bean = null;
				try {
					bean = clazz.newInstance();
				} catch (InstantiationException e) {

				} catch (IllegalAccessException e) {

				}
				updatedView.getDataRow(row);
				dataRow2Bean(row, bean);
				list.add(bean);
			} while (updatedView.next());
		}
		return list;
	}

	/**
	 * 包含删除
	 * 
	 * @param dataSet
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> Map<String, List<T>> getAllChanges(StorageDataSet dataSet, Class<T> clazz) throws Exception {
		dataSet.post();
		Map<String, List<T>> items = new HashMap<String, List<T>>();
		// 包含新增和修改的数据行
		List<T> updateItems = new ArrayList<T>();
		items.put("update", updateItems);
		// 删除的数据行
		List<T> deleteItems = new ArrayList<T>();
		items.put("delete", deleteItems);
		items.put("update", updateItems);
		DataSetView insertView = new DataSetView();
		DataSetView updatedView = new DataSetView();
		DataSetView deleteView = new DataSetView();
		dataSet.getInsertedRows(insertView);
		dataSet.getUpdatedRows(updatedView);
		dataSet.getDeletedRows(deleteView);
		if (insertView.getRowCount() > 0) {
			DataRow row = new DataRow(insertView);
			insertView.first();
			do {
				T bean = null;
				bean = clazz.newInstance();
				insertView.getDataRow(row);
				dataRow2Bean(row, bean);
				updateItems.add(bean);
			} while (insertView.next());
		}

		if (updatedView.getRowCount() > 0) {
			DataRow row = new DataRow(updatedView);
			updatedView.first();
			do {
				T bean = null;
				bean = clazz.newInstance();
				updatedView.getDataRow(row);
				dataRow2Bean(row, bean);
				updateItems.add(bean);
			} while (updatedView.next());
		}
		if (deleteView.getRowCount() > 0) {
			DataRow row = new DataRow(deleteView);
			deleteView.first();
			do {
				T bean = null;
				bean = clazz.newInstance();
				deleteView.getDataRow(row);
				dataRow2Bean(row, bean);
				deleteItems.add(bean);
			} while (deleteView.next());
		}
		return items;
	}

	@SuppressWarnings("unchecked")
	public static <T> void bean2DataRow(T bean, ReadWriteRow row) throws Exception {
		Class type = bean.getClass();
		PropertyDescriptor[] propertyDescriptors = BeanUtil.getPropertyDescriptors(type);
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String field = descriptor.getName();
			if (!field.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object beanVal = readMethod.invoke(bean, new Object[0]);
				Variant variant = new Variant();
				Column column = row.hasColumn(field);
				if (column != null) {
					setVariant(variant, column, beanVal);
					row.setVariant(field, variant);
				}
				// if(beanVal==null)
				// continue;
				// Column column = row.hasColumn(field);
				// if(column==null){
				// Column col=new Column();
				// col.setColumnName(field);
				//
				// }
				// try {
				// column = row.getColumn(field);
				// } catch (Exception ex) {
				//
				// }
				//
				// if (column != null) {
				// int dataType = column.getDataType();
				// if (dataType == Variant.STRING) {
				// row.setString(field, beanVal.toString());
				// //f(field.equals("codeName"))
				// //MessageBox.showInfo(beanVal.toString());
				// } else if (dataType == Variant.INT) {
				// int tempVal = 0;
				// if (beanVal instanceof Integer) {
				// tempVal = (Integer) beanVal;
				// } else {
				// tempVal = Integer.parseInt(beanVal.toString());
				// }
				// row.setInt(field, tempVal);
				// } else if (dataType == Variant.DOUBLE) {
				// double tempVal = 0;
				// if (beanVal instanceof Double) {
				// tempVal = (Double) beanVal;
				// } else {
				// tempVal = Double.parseDouble(beanVal.toString());
				// }
				// row.setDouble(field, tempVal);
				// } else if (dataType == Variant.BIGDECIMAL) {
				// BigDecimal tempVal = null;
				// if (beanVal instanceof BigDecimal) {
				// tempVal = (BigDecimal) beanVal;
				// } else {
				// tempVal = new BigDecimal(beanVal.toString());
				// }
				// row.setBigDecimal(field, tempVal);
				// } else if (dataType == Variant.LONG) {
				// long tempVal = 0;
				// if (beanVal instanceof Long) {
				// tempVal = (Long) beanVal;
				// } else {
				// tempVal = Long.parseLong(beanVal.toString());
				// }
				// row.setLong(field, tempVal);
				// } else if (dataType == Variant.DATE) {
				//
				// java.sql.Date tempVal = null;
				// if (beanVal instanceof Date) {
				// Date dt = (Date) beanVal;
				// tempVal = new java.sql.Date(dt.getTime());
				// } else if (beanVal instanceof java.sql.Date) {
				// tempVal = (java.sql.Date) beanVal;
				// } else if (beanVal instanceof Timestamp) {
				// Timestamp dt = (Timestamp) beanVal;
				// tempVal = new java.sql.Date(dt.getTime());
				// tempVal = (java.sql.Date) beanVal;
				// } else {
				// throw new DataAccessException(5, "数据类型错误，不能转换成日期类型");
				// }
				// row.setDate(field, tempVal);
				// } else if (dataType == Variant.TIMESTAMP) {
				// Timestamp tempVal = null;
				// if (beanVal instanceof Date) {
				// Date dt = (Date) beanVal;
				// tempVal = new Timestamp(dt.getTime());
				// } else if (beanVal instanceof java.sql.Date) {
				// java.sql.Date dt = (java.sql.Date) beanVal;
				// tempVal = new Timestamp(dt.getTime());
				// } else if (beanVal instanceof Timestamp) {
				// tempVal = (Timestamp) beanVal;
				// } else {
				// throw new DataAccessException(5, "数据类型错误，不能转换成日期类型");
				// }
				// row.setTimestamp(field, tempVal);
				// } else if (dataType == Variant.TIME) {
				// Time tempVal = null;
				// if (beanVal instanceof Time) {
				// tempVal = (Time) beanVal;
				// } else if (beanVal instanceof Date) {
				// Date dt = (Date) beanVal;
				// tempVal = new Time(dt.getTime());
				// } else if (beanVal instanceof java.sql.Date) {
				// java.sql.Date dt = (java.sql.Date) beanVal;
				// tempVal = new Time(dt.getTime());
				// } else if (beanVal instanceof Timestamp) {
				// tempVal = (Time) beanVal;
				// } else {
				// throw new DataAccessException(5, "数据类型错误，不能转换成日期类型");
				// }
				// row.setTime(field, tempVal);
				// } else {
				// throw new DataAccessException(5, "不支持Variant类型：" + dataType);
				//
				// }
				// }
			}
		}
		/*
		 * Column[] cols = row.getColumns(); for (Column col : cols) { String
		 * field = col.getColumnName(); int dataType = col.getDataType(); Object
		 * beanVal = null; try { beanVal = PropertyUtils.getProperty(bean,
		 * field); } catch (Exception e) { throw new DataAccessException(7,
		 * "不存在属性：" + field); } if (beanVal == null) {
		 * row.setAssignedNull(field); } else { if (dataType == Variant.STRING)
		 * { row.setString(field, beanVal.toString()); } else if (dataType ==
		 * Variant.INT) { int tempVal = 0; if (beanVal instanceof Integer) {
		 * tempVal = (Integer) beanVal; } else { tempVal =
		 * Integer.parseInt(beanVal.toString()); } row.setInt(field, tempVal); }
		 * else if (dataType == Variant.DOUBLE) { double tempVal = 0; if
		 * (beanVal instanceof Double) { tempVal = (Double) beanVal; } else {
		 * tempVal = Double.parseDouble(beanVal.toString()); }
		 * row.setDouble(field, tempVal); } else if (dataType ==
		 * Variant.BIGDECIMAL) { BigDecimal tempVal = null; if (beanVal
		 * instanceof BigDecimal) { tempVal = (BigDecimal) beanVal; } else {
		 * tempVal = new BigDecimal(beanVal.toString()); }
		 * row.setBigDecimal(field, tempVal); } else if (dataType ==
		 * Variant.LONG) { long tempVal = 0; if (beanVal instanceof Long) {
		 * tempVal = (Long) beanVal; } else { tempVal =
		 * Long.parseLong(beanVal.toString()); } row.setLong(field, tempVal); }
		 * else if (dataType == Variant.DATE) { java.sql.Date tempVal = null; if
		 * (beanVal instanceof Date) { Date dt = (Date) beanVal; tempVal = new
		 * java.sql.Date(dt.getTime()); } else if (beanVal instanceof
		 * java.sql.Date) { tempVal = (java.sql.Date) beanVal; } else if
		 * (beanVal instanceof Timestamp) { Timestamp dt = (Timestamp) beanVal;
		 * tempVal = new java.sql.Date(dt.getTime()); tempVal = (java.sql.Date)
		 * beanVal; } else { throw new DataAccessException(5,
		 * "数据类型错误，不能转换成日期类型"); } row.setDate(field, tempVal); } else if
		 * (dataType == Variant.TIMESTAMP) { Timestamp tempVal = null; if
		 * (beanVal instanceof Date) { Date dt = (Date) beanVal; tempVal = new
		 * Timestamp(dt.getTime()); } else if (beanVal instanceof java.sql.Date)
		 * { java.sql.Date dt = (java.sql.Date) beanVal; tempVal = new
		 * Timestamp(dt.getTime()); } else if (beanVal instanceof Timestamp) {
		 * tempVal = (Timestamp) beanVal; } else { throw new
		 * DataAccessException(5, "数据类型错误，不能转换成日期类型"); } row.setTimestamp(field,
		 * tempVal); } else if (dataType == Variant.TIME) { Time tempVal = null;
		 * if (beanVal instanceof Time) { tempVal = (Time) beanVal; } else if
		 * (beanVal instanceof Date) { Date dt = (Date) beanVal; tempVal = new
		 * Time(dt.getTime()); } else if (beanVal instanceof java.sql.Date) {
		 * java.sql.Date dt = (java.sql.Date) beanVal; tempVal = new
		 * Time(dt.getTime()); } else if (beanVal instanceof Timestamp) {
		 * tempVal = (Time) beanVal; } else { throw new DataAccessException(5,
		 * "数据类型错误，不能转换成日期类型"); } row.setTime(field, tempVal); } else { throw
		 * new DataAccessException(5, "不支持Variant类型：" + dataType);
		 * 
		 * } } }
		 */
	}

	@SuppressWarnings("unchecked")
	public static <T> void dataRow2Bean(ReadWriteRow row, T bean) throws Exception {
		Class type = bean.getClass();
		PropertyDescriptor[] propertyDescriptors = BeanUtil.getPropertyDescriptors(type);
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String field = descriptor.getName();
			String beanFieldType = descriptor.getPropertyType().getSimpleName();
			Column col = null;
			try {
				col = row.getColumn(field);
			} catch (Exception ex) {

			}
			if (col != null) {
				int dataType = row.getColumn(field).getDataType();
				if (beanFieldType.equals("String")) {
					descriptor.getWriteMethod().invoke(bean, new Object[] { row.getString(field) });
				} else if (beanFieldType.equals("Integer")) {
					descriptor.getWriteMethod().invoke(bean, new Object[] { row.getInt(field) });
				} else if (beanFieldType.equals("Double")) {
					descriptor.getWriteMethod().invoke(bean, new Object[] { row.getDouble(field) });
				} else if (beanFieldType.equals("Long")) {
					descriptor.getWriteMethod().invoke(bean, new Object[] { row.getLong(field) });
				} else if (beanFieldType.equals("Short")) {
					descriptor.getWriteMethod().invoke(bean, new Object[] { row.getShort(field) });
				} else if (beanFieldType.equals("Date")) {
					Date value = null;
					if (dataType == Variant.TIMESTAMP) {
						Timestamp tmValue = row.getTimestamp(field);
						if (tmValue.getTime() == new Date(0).getTime())
							tmValue = null;
						if (tmValue != null) {
							value = new Date(tmValue.getTime());
						}
					}
					if (value != null)
						descriptor.getWriteMethod().invoke(bean, new Object[] { value });
				} else if (beanFieldType.equals("Timestamp")) {
					descriptor.getWriteMethod().invoke(bean, new Object[] { row.getTimestamp(field) });
				} else if (beanFieldType.equals("Time")) {
					descriptor.getWriteMethod().invoke(bean, new Object[] { row.getTime(field) });
				} else if (beanFieldType.equals("byte[]")) {
					descriptor.getWriteMethod().invoke(bean, new Object[] { row.getByteArray(field) });
				} else {
					throw new DataAccessException(5, "不支持Variant类型：" + dataType);
				}

			}
		}
		/*
		 * Column[] cols = row.getColumns(); for (Column col : cols) { String
		 * field = col.getColumnName(); int dataType = col.getDataType();
		 * Class<T> clazz=null; try { clazz =
		 * PropertyUtils.getPropertyType(bean, field); } catch (Exception e)
		 * {//如果属性不存在，继续下一个 continue; } try { String beanFieldType =
		 * clazz.getSimpleName(); if (beanFieldType.equals("String")) { // if
		 * (dataType == Variant.STRING) { PropertyUtils.setProperty(bean, field,
		 * row.getString(field)); // } } else if
		 * (beanFieldType.equals("Integer")) { PropertyUtils.setProperty(bean,
		 * field, row.getInt(field)); } else if (beanFieldType.equals("Double"))
		 * { PropertyUtils.setProperty(bean, field, row.getDouble(field)); }
		 * else if (beanFieldType.equals("Long")) {
		 * PropertyUtils.setProperty(bean, field, row.getLong(field)); } else if
		 * (beanFieldType.equals("Date")) { Date value=null; if (dataType ==
		 * Variant.TIMESTAMP) { Timestamp tmValue=row.getTimestamp(field);
		 * if(tmValue!=null){ value =new Date(tmValue.getTime()); } }
		 * PropertyUtils.setProperty(bean, field,value); } else if
		 * (beanFieldType.equals("Timestamp")) { PropertyUtils.setProperty(bean,
		 * field, row.getTimestamp(field)); } else if
		 * (beanFieldType.equals("Time")) { PropertyUtils.setProperty(bean,
		 * field, row.getTime(field)); } else { throw new DataAccessException(5,
		 * "不支持Variant类型：" + dataType); } } catch (Exception e) { throw new
		 * DataAccessException(10, "数据类型不匹配：" +e.getMessage()); } }
		 */

	}

	/**
	 * 是否有修改
	 * 
	 * @param dataSet
	 * @return
	 */
	public static boolean isModified(StorageDataSet dataSet) {
		dataSet.post();
		DataSetView deletedView = new DataSetView();
		DataSetView updatedView = new DataSetView();
		DataSetView insertView = new DataSetView();
		dataSet.getDeletedRows(deletedView);
		dataSet.getUpdatedRows(updatedView);
		dataSet.getInsertedRows(insertView);
		if (insertView.getRowCount() > 0) {
			return true;
		}
		if (updatedView.getRowCount() > 0) {
			return true;
		}
		if (deletedView.getRowCount() > 0) {
			return true;
		}
		insertView.close();
		deletedView.close();
		updatedView.close();
		return false;

	}

	/**
	 * 接受修改，还原dataSet行状态
	 * 
	 * @param dataSet
	 */
	public static void acceptChanges(StorageDataSet dataSet) {
		dataSet.post();
		DataSetView deletedView = new DataSetView();
		DataSetView updatedView = new DataSetView();
		DataSetView insertView = new DataSetView();
		dataSet.getDeletedRows(deletedView);
		dataSet.getUpdatedRows(updatedView);
		dataSet.getInsertedRows(insertView);
		try {
			ProviderHelp.startResolution(dataSet, true);
			if (insertView.getRowCount() > 0) {
				insertView.first();
				do {
					ProviderHelp.markPendingStatus(insertView, true);
				} while (insertView.next());
			}
			if (updatedView.getRowCount() > 0) {
				updatedView.first();
				do {

					ProviderHelp.markPendingStatus(updatedView, true);
				} while (updatedView.next());
			}
			if (deletedView.getRowCount() > 0) {
				deletedView.first();
				do {
					ProviderHelp.markPendingStatus(deletedView, true);
				} while (deletedView.next());
			}
			dataSet.resetPendingStatus(true);
		} catch (Exception ex) {

		} finally {
			ProviderHelp.endResolution(dataSet);
			insertView.close();
			deletedView.close();
			updatedView.close();
		}

	}

	public static StorageDataSet cloneDataSet(StorageDataSet dataSet) {
		StorageDataSet destDataSet = dataSet.cloneDataSetStructure();
		for (int i = 0; i < destDataSet.getColumnCount(); i++) {
			Column column = destDataSet.getColumn(i);
			if (column.getColumnChangeListener() == null)
				continue;
			column.removeColumnChangeListener(column.getColumnChangeListener());
		}
		destDataSet.open();
		DataRow localDataRow = new DataRow(dataSet);
		for (int j = 0; j < dataSet.getRowCount(); j++) {
			dataSet.getDataRow(j, localDataRow);
			destDataSet.insertRow(false);
			localDataRow.copyTo(destDataSet);
			destDataSet.post();
		}
		return destDataSet;
	}

	public static void copyDataRow(ReadWriteRow fromRow, ReadWriteRow toRow) {
		for (Column col : fromRow.getColumns()) {
			if (toRow.hasColumn(col.getColumnName()) == null)
				continue;
			String field = col.getColumnName();
			int dataType = col.getDataType();
			if (dataType == Variant.STRING) {
				toRow.setString(field, fromRow.getString(field));
			} else if (dataType == Variant.INT) {
				toRow.setInt(field, fromRow.getInt(field));
			} else if (dataType == Variant.DOUBLE) {
				toRow.setDouble(field, fromRow.getDouble(field));
			} else if (dataType == Variant.LONG) {
				toRow.setLong(field, fromRow.getLong(field));
			} else if (dataType == Variant.SHORT) {
				toRow.setShort(field, fromRow.getShort(field));
			} else if (dataType == Variant.BIGDECIMAL) {
				toRow.setBigDecimal(field, fromRow.getBigDecimal(field));
			} else if (dataType == Variant.DATE) {
				toRow.setDate(field, fromRow.getDate(field));
			} else if (dataType == Variant.TIMESTAMP) {
				toRow.setTimestamp(field, fromRow.getTimestamp(field));
			} else if (dataType == Variant.TIME) {
				toRow.setTime(field, fromRow.getTime(field));
			} else if (dataType == Variant.OBJECT) {
				// toRow.setObject(field, fromRow.getObject(field));
			} else if (dataType == Variant.BINARY_STREAM) {
				toRow.setInputStream(field, new ByteArrayInputStream(fromRow.getByteArray(field)));
				//variant.setInputStream();

			}else {
				throw new DataAccessException(5, "不支持Variant类型：" + dataType);
			}
		}

	}

	public static StorageDataSet cloneDataSetStructure(StorageDataSet oraDataSet) {
		StorageDataSet destDataSet = oraDataSet.cloneDataSetStructure();
		for (int i = 0; i < oraDataSet.getColumnCount(); i++) {
			Column oraColumn = oraDataSet.getColumn(i);
			Column destColumn = destDataSet.getColumn(i);
			if (oraColumn.getPickList() != null) {
				destColumn.setPickList(oraColumn.getPickList());
			}
			if (destColumn.getColumnChangeListener() == null)
				continue;
			destColumn.removeColumnChangeListener(destColumn.getColumnChangeListener());

		}

		destDataSet.open();
		return destDataSet;
	}

}
