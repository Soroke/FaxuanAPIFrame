package net.faxuan.test;

import net.faxuan.data.ParamInfo;
import net.faxuan.init.BaseData;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by song on 2018/8/21.
 */
public class BaseDataTest {

    @Test(description = "获取临时表数据，包括考试、单位、用户、考试成绩、积分")
    public void getBaseData() {
        new BaseData();
    }
}
