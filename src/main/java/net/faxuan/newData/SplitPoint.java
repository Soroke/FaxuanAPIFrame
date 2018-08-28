package net.faxuan.newData;

import net.faxuan.util.DataSource;
import net.faxuan.util.FromSourceCreateTable;

import java.util.Calendar;

/**
 * Created by song on 2018/8/23.
 */
public class SplitPoint {
    /**
     * 获取当前年份信息
     */
    Calendar date = Calendar.getInstance();
    private String yearString = String.valueOf(date.get(Calendar.YEAR));
    private int yearInt = date.get(Calendar.YEAR);

    public SplitPoint() {
        FromSourceCreateTable.create(DataSource.SourceType.TREPORT,"user_point","user_point" + (yearInt-1));
        FromSourceCreateTable.create(DataSource.SourceType.TREPORT,"domain_point","domain_point" + (yearInt-1));
    }

}
