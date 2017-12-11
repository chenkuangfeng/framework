package com.ubsoft.framework.bi.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class JDataSourceUtil implements JRDataSource {  
    private Map[] datasHm = null;  
  
    private List<Map> datasList = null;  
  
    private Map temp = null;  
  
    private int loop = -1;  
  
    // 打印多条数据  
    public JDataSourceUtil(Map[] datasHm) {  
        this.datasHm = datasHm;  
    }  
  
    // 打印多条数据  
    public JDataSourceUtil(List<Map> datasList) {  
        this.datasList = datasList;  
    }  
  
    /** 
     * 打印一页，一条数据时，用此构造参数 
     *  
     * @param hm 
     */  
    public JDataSourceUtil(Map hm) {  
        this.datasList = new ArrayList<Map>();  
        datasList.add(hm);  
    }    
    public Object getFieldValue(JRField jRfield) throws JRException {  
        if (datasHm != null) {  
            temp = datasHm[loop];  
        } else {  
            temp = datasList.get(loop);  
        }  
        return temp.get(jRfield.getName()) == null ? "" : temp.get(jRfield  
                .getName());//过滤null值  
    }  
  
    public boolean next() throws JRException {  
        loop++;  
        if (datasHm != null) {  
            if (loop >= datasHm.length) {  
                return false;  
            } else {  
                return true;  
            }  
        } else {  
            if (loop >= datasList.size()) {  
                return false;  
            } else {  
                return true;  
            }  
  
        }  
    }  
}  
