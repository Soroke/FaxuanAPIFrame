package net.faxuan.exception;

import net.faxuan.exception.CheckException;
import net.faxuan.tableProject.*;

/**
 * Created by song on 2017/8/8.
 */
public class Check {

    public static void check(Object obj1,Object obj2,String s1,String s2){
        String info = "\n";
        if (obj1 instanceof UserExam && obj2 instanceof UserExam) {
            info += ("测试库用户考试对象" + obj1 + "\n学法库用户考试对象" + obj2);
        } else if (obj1 instanceof DomainExam && obj2 instanceof DomainExam) {
            info += ("测试库单位考试对象" + obj1 + "\n学法库单位考试对象" + obj2);
        } else if (obj1 instanceof UnitExam && obj2 instanceof UnitExam) {
            info += ("测试库考试汇总对象" + obj1 + "\n学法库考试汇总对象" + obj2);
        } else if (obj1 instanceof UserPoint && obj2 instanceof UserPoint) {
            info += ("测试库用户积分对象" + obj1 + "\n学法库用户积分对象" + obj2);
        } else if (obj1 instanceof DomainPoint && obj2 instanceof DomainPoint) {
            info += ("测试库单位积分对象" + obj1 + "\n学法库单位积分对象" + obj2);
        } else if (obj1 instanceof UserCredit && obj2 instanceof UserCredit) {
            info += ("测试库用户学分对象" + obj1 + "\n学法库用户学分对象" + obj2);
        } else if (obj1 instanceof DomainCredit && obj2 instanceof DomainCredit) {
            info += ("测试库单位学分对象" + obj1 + "\n学法库单位学分对象" + obj2);
        }
        String ss = null;
        if (s1 == ss && s2 == ss) {
            return;
        }
        if (!s1.equals(s2) || s1 == s2 ) {
            throw new CheckException(info + "\n字符串[" + s1 + "]和字符串[" +s2 + "]不相等");
        }
    }
    public static void check(String info,int s1,int s2){
        if (s1 != s2) {
            throw new CheckException(info + "数字[" + s1 + "]和数字[" +s2 + "]不相等");
        }
    }

    public static void check(Object obj1,Object obj2,double s1,double s2){
        String info = "\n";
        if (obj1 instanceof UserExam && obj2 instanceof UserExam) {
            info += ("测试库用户考试对象" + obj1 + "\n学法库用户考试对象" + obj2);
        } else if (obj1 instanceof DomainExam && obj2 instanceof DomainExam) {
            info += ("测试库单位考试对象" + obj1 + "\n学法库单位考试对象" + obj2);
        } else if (obj1 instanceof UnitExam && obj2 instanceof UnitExam) {
            info += ("测试库考试汇总对象" + obj1 + "\n学法库考试汇总对象" + obj2);
        } else if (obj1 instanceof UserPoint && obj2 instanceof UserPoint) {
            info += ("测试库用户积分对象" + obj1 + "\n学法库用户积分对象" + obj2);
        } else if (obj1 instanceof DomainPoint && obj2 instanceof DomainPoint) {
            info += ("测试库单位积分对象" + obj1 + "\n学法库单位积分对象" + obj2);
        } else if (obj1 instanceof UserCredit && obj2 instanceof UserCredit) {
            info += ("测试库用户学分对象" + obj1 + "\n学法库用户学分对象" + obj2);
        } else if (obj1 instanceof DomainCredit && obj2 instanceof DomainCredit) {
            info += ("测试库单位学分对象" + obj1 + "\n学法库单位学分对象" + obj2);
        }
        if (!(s1 == s2)) {
            throw new CheckException(info + "\n数字[" + s1 + "]和数字[" +s2 + "]不相等");
        }
    }
}
