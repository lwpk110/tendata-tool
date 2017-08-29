package cn.tendata.batch.webpower.item.process;

import cn.tendata.batch.webpower.item.model.WebpowerCnReportData;
import cn.tendata.batch.webpower.item.model.WebpowerNldReportData;
import cn.tendata.batch.webpower.item.util.WebpowerCnToNldStatusUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

/**
 * {@inheritDoc}
 *
 * Created by ernest on 2017/8/28.
 */
public class WebpowerCsvItemProcessor implements ItemProcessor<WebpowerCnReportData,WebpowerNldReportData> {
    @Override
    public WebpowerNldReportData process(WebpowerCnReportData item) throws Exception {
        WebpowerNldReportData webpowerNldReportData = new WebpowerNldReportData();
        BeanUtils.copyProperties(item,webpowerNldReportData);
        webpowerNldReportData.setDMDtype(WebpowerCnToNldStatusUtils.transform(item.getDMDtype().toUpperCase()));
        webpowerNldReportData.setDMDcampaignID("0");
        webpowerNldReportData.setDMDmailingID("0");
        return webpowerNldReportData;
    }
}
