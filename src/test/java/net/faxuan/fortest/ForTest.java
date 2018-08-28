package net.faxuan.fortest;

import net.faxuan.exception.CheckException;
import net.faxuan.init.BaseData;
import net.faxuan.init.TestCase;
import net.faxuan.newData.*;
import net.faxuan.tableProject.ExamResult;
import net.faxuan.util.DataBase;
import net.faxuan.util.DataSource;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by song on 2018/8/13.
 */
public class ForTest extends TestCase{
    @BeforeSuite(description = "连接数据库；获取数据库report_point_user表信息然后创建测试report库中的user_point表")
    public void before() {
        System.out.println("before Test");
    }
    
    @Test(description = "新建测试库中测试表，并获取测试数据",priority = 1)
    public void getBaseData() {
        new BaseData();
    }

    @Test(description = "统计当年和往年的用户考试信息",priority = 2)
    public void getUserExam() {
        new UserExam();
    }

    @Test(description = "统计当年和往年的单位考试信息",priority = 3)
    public void getDomainExam() {
        new DomainExam();
    }

    @Test(description = "统计单位积分",priority = 4)
    public void getDomainPoint() {
        new Point();
    }

    @Test(description = "统计当年和往年的用户学分信息",priority = 5)
    public void getUserCredit() {
        new UserCredit();
    }

    @Test(description = "统计当年和往年的单位学分信息",priority = 6)
    public void getDomainCredit() {
        new DomainCredit();
    }

    @Test(description = "统计学员考核报表",priority = 7)
    public void getCheckUser() {
        new CheckUser("45654,45655,45658,45659,45662,45663,45664,45665");
    }
}
