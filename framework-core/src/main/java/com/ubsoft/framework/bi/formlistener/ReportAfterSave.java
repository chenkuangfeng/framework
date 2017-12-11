package com.ubsoft.framework.bi.formlistener;

import com.ubsoft.framework.bi.cache.MemoryReport;
import com.ubsoft.framework.bi.entity.Report;
import com.ubsoft.framework.bi.entity.ReportDataSet;
import com.ubsoft.framework.bi.entity.ReportField;
import com.ubsoft.framework.bi.entity.ReportParameter;
import com.ubsoft.framework.core.dal.model.Bio;
import com.ubsoft.framework.core.dal.model.BioSet;
import com.ubsoft.framework.core.service.IFormListener;
import com.ubsoft.framework.core.service.impl.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("ReportAfterSave")
@Transactional
public class ReportAfterSave extends BaseService<Report> implements IFormListener {
    @Override
    public void execute(BioSet set){
        // 更新缓存
        Bio master = set.getMaster();
        Report rpt = get(master.getString("ID"));
        List<ReportField> rptFields = null;// 报表字段
        List<ReportParameter> rptParameters = null;// 报表参数
        List<ReportDataSet> rptDataSets = null;// 报表子数据集
        List<Report> rpts = this.dataSession.gets(Report.class, "status=?", new Object[]{"1"});
        rptFields = this.dataSession.gets(ReportField.class, "reportId=?", "seq", rpt.getId());
        rptParameters = this.dataSession.gets(ReportParameter.class, "reportId=?", "seq", rpt.getId());
        // rptDataSets = this.dataSession.gets(ReportDataSet.class,
        // "reportId=?","seq" ,rpt.getId());
        rpt.setRptFields(rptFields);
        rpt.setRptParameters(rptParameters);
        rpt.setRptDataSets(rptDataSets);
        MemoryReport.getInstance().put(rpt);

    }

}
