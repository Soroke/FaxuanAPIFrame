package faxuan.index;

import net.faxuan.data.SourceType;
import net.faxuan.sql.*;
import net.faxuan.util.DataBase;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import util.LoadFile;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by song on 17/5/18.
 */
public class test {
    private Logger log = Logger.getLogger(this.getClass());
    private String domainCode="101002001002002";

    public static void main(String[] ages) {
        new LoadFile();
    }

    private String head = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <title>测试报告</title>\n" +
            "    <!-- 支持移动设备的缩放 -->\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "\n" +
            "    <script src=\"https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js\"></script>\n" +
            "    <link href=\"https://cdn.bootcss.com/bootstrap-validator/0.5.3/css/bootstrapValidator.min.css\" rel=\"stylesheet\">\n" +
            "    <link href=\"https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
            "    <script src=\"https://cdn.bootcss.com/bootstrap-validator/0.5.3/js/bootstrapValidator.min.js\"></script>\n" +
            "    <script src=\"https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>\n" +
            "\n" +
            "    <!-- bootstrap-datetimepicker时间控件 -->\n" +
            "    <link href=\"https://cdn.bootcss.com/smalot-bootstrap-datetimepicker/2.4.4/css/bootstrap-datetimepicker.min.css\" rel=\"stylesheet\">\n" +
            "    <script src=\"https://cdn.bootcss.com/smalot-bootstrap-datetimepicker/2.4.4/js/bootstrap-datetimepicker.js\"></script>\n" +
            "    <script src=\"https://cdn.bootcss.com/smalot-bootstrap-datetimepicker/2.4.4/js/bootstrap-datetimepicker.min.js\"></script>\n" +
            "    <script src=\"https://cdn.bootcss.com/smalot-bootstrap-datetimepicker/2.4.4/js/locales/bootstrap-datetimepicker.zh-CN.js\"></script>\n" +
            "</head>\n" +
            "<body>";
    private String tail = "<!-- 适配移动端浏览器 -->\n" +
            "<script>window.jQuery || document.write('<script src=\"//cdn.bootcss.com/jquery/3.2.1/jquery.min.js\"><\\/script>')</script>\n" +
            "</body>\n" +
            "</html>";

    @Test(testName = " 测试创建测试页")
    public void test() {
        File file = new File(System.getProperty("user.dir") + "\\report.html");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("文件创建失败");
                e.printStackTrace();
            }
        } else {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("文件创建失败");
                e.printStackTrace();
            }
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
            writer.write(head);
            writer.write("        <div class=\"alert alert-success\">\n" +
                    "            <label>默认考试开始时间是当前时间，考试结束时间为1周后</label>\n" +
                    "        </div>\n" +
                    "\n" +
                    "        <div class=\"form-group\">\n" +
                    "            <label class=\"col-md-2 control-label\">考试开始时间</label>\n" +
                    "            <div class=\"input-group date form_datetime col-md-5\">\n" +
                    "                <input size=\"16\" type=\"text\" name=\"datetimeStart\" id=\"datetimeStart\" readonly class=\"form-control\">\n" +
                    "                <span class=\"input-group-addon\"><span class=\"glyphicon glyphicon-remove\"></span></span>\n" +
                    "                <span class=\"input-group-addon\"><span class=\"glyphicon glyphicon-calendar\"></span></span>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "\n" +
                    "        <div class=\"form-group\">\n" +
                    "            <label class=\"col-md-2 control-label\">考试结束时间</label>\n" +
                    "            <div class=\"input-group date form_datetime col-md-5\">\n" +
                    "                <input size=\"16\" type=\"text\" name=\"datetimeEnd\" id=\"datetimeEnd\" readonly class=\"form-control\">\n" +
                    "                <span class=\"input-group-addon\"><span class=\"glyphicon glyphicon-remove\"></span></span>\n" +
                    "                <span class=\"input-group-addon\"><span class=\"glyphicon glyphicon-calendar\"></span></span>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "<script type=\"text/javascript\">\n" +
                    "    $('#datetimeStart').datetimepicker({\n" +
                    "        language:  'zh-CN',\n" +
                    "        format: 'yyyy-mm-dd hh:ii',\n" +
                    "        minView:'hour',\n" +
                    "        weekStart: 1,\n" +
                    "        todayBtn:  1,\n" +
                    "        autoclose: 1,\n" +
                    "        todayHighlight: 1,\n" +
                    "        startView: 2,\n" +
                    "        forceParse: 0,\n" +
                    "        showMeridian: 1\n" +
                    "    });\n" +
                    "    $('#datetimeEnd').datetimepicker({\n" +
                    "        language:  'zh-CN',\n" +
                    "        format: 'yyyy-mm-dd hh:ii',\n" +
                    "        minView:'hour',\n" +
                    "        weekStart: 1,\n" +
                    "        todayBtn:  1,\n" +
                    "        autoclose: 1,\n" +
                    "        todayHighlight: 1,\n" +
                    "        startView: 2,\n" +
                    "        forceParse: 0,\n" +
                    "        showMeridian: 1\n" +
                    "    });\n" +
                    "</script>");
            writer.write(tail);
        } catch (FileNotFoundException e) {
            System.err.println("没有找到文件");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("写入文件失败");
            e.printStackTrace();
        } finally {
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        System.err.println(System.getProperty("user.dir"));
    }

    @Test
    public void createTable() {
        DataBase dataBase = new DataBase(SourceType.SOURSE1);
        Boolean b1 = DomainCredit.create(dataBase);
        Boolean b2 = DomainExam.create(dataBase);
        Boolean b3 = DomainPoint.create(dataBase);
        Boolean b4 = ExamUnit.create(dataBase);
        Boolean b5 = UserCredit.create(dataBase);
        Boolean b6 = UserExam.create(dataBase);
        Boolean b7 = UserPoint.create(dataBase);
        Assert.assertEquals(b1 == b2 == b3 == b4 == b5 == b6 == b7 == true,true);
        dataBase.deconnSQL();
    }
}
