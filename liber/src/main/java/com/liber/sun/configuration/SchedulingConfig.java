package com.liber.sun.configuration;


import com.liber.sun.utils.MyFileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by sunlingzhi on 2017/12/18.
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Value("${web.upload-path}")
    private String ROOT;

    @Scheduled(cron = "0 0 3 * * ?")// 每天凌晨3点促发 分别为 秒 分钟 小时 天（0-31） 月（0-11） 星期（1-7）
    public void testSchedule() {
        try {
            MyFileUtils.deleteFiles(ROOT + "dataProcess/temp", true);
            MyFileUtils.deleteFiles(ROOT + "dataProcess/localGeojson", true);
            MyFileUtils.deleteFiles(ROOT + "dataProcess/localShapZip", true);
            MyFileUtils.deleteFiles(ROOT + "dataProcess/dataType", true);
            MyFileUtils.deleteFiles(ROOT + "dataProcess/colorMapping", true);
            System.out.println("定时删除数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
