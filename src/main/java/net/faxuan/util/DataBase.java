package net.faxuan.util;

import net.faxuan.data.SourceType;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.Map;

/**
 * Created by song on 2017/7/25.
 */
public class DataBase {
    private Connection conn = null;
    PreparedStatement statement = null;
    private Logger log = Logger.getLogger(this.getClass());

    private String url = null;
    private String userName = null;
    private String passWord = null;

    /**
     * 构造方法初始化数据库链接属性
     * @param sourceType 连接数据库
     */
    public DataBase(SourceType sourceType) {
        Map<Object,Object> dbinfor = new DataSource(sourceType).getDBInfo();
        this.url = dbinfor.get("url").toString();
        this.userName = dbinfor.get("user").toString();
        this.passWord = dbinfor.get("password").toString();
        connSQL();
    }

    /**
     *连接数据库
     */
    private void connSQL() {
        if (url==null || url.equals("") || userName==null || passWord==null){
            log.error("数据库连接信息未设置完整");
            System.err.println("数据库连接信息未设置完整！");
            System.err.println("请先设置数据库连接信息！！");
            return;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection( url, userName, passWord);
            log.info("数据库:" + url + "--连接成功");
        }
        catch ( ClassNotFoundException cnfex ) {
            log.error("装载 JDBC/ODBC 驱动程序失败");
            cnfex.printStackTrace();
        }
        catch ( SQLException sqlex ) {
            log.error("无法连接数据库");
            System.err.println( "无法连接数据库" );
            sqlex.printStackTrace();
        }
    }

    /**
     *断开数据库连接
     */
    public void deconnSQL() {
        try {
            if (conn != null)
                conn.close();
            log.info("数据库:" + url + "--连接已关闭");
        } catch (Exception e) {
            log.error("数据库:" + url + "--关闭数据库链接时出错");
            e.printStackTrace();
        }
    }

    /**
     *执行数据库查询语句
     * @param sql SQL语句
     * @return
     */
    public ResultSet selectSQL(String sql) {
        ResultSet rs = null;
        try {
            statement = conn.prepareStatement(sql);
            rs = statement.executeQuery(sql);
            log.info("执行sql查询\t" + sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    // 执行数据库插入语句
    public boolean insertSQL(String sql) {
        try {
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            log.info("执行sql插入\t" + sql);
            return true;
        } catch (SQLException e) {
            log.error("插入数据库时出错");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("插入时出错：");
            e.printStackTrace();
        }
        return false;
    }
    //执行数据库删除语句
    public boolean deleteSQL(String sql) {
        try {
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            log.info("执行sql删除\t" + sql);
            return true;
        } catch (SQLException e) {
            System.err.println("插入数据库时出错：");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("插入时出错：");
            e.printStackTrace();
        }
        return false;
    }
    //执行数据库更新语句
    public boolean updateSQL(String sql) {
        try {
            statement = conn.prepareStatement(sql);
            statement.executeUpdate();
            log.info("执行sql更新\t" + sql);
            return true;
        } catch (SQLException e1) {
            log.error("插入数据库时出错");
            System.out.println("插入数据库时出错：");
            e1.printStackTrace();
        } catch (Exception e) {
            System.out.println("插入时出错：");
            e.printStackTrace();
        }
        return false;
    }
    // 将返回结果按照一定格式显示
    void layoutStyle2(ResultSet rs) {
        System.out.println("-----------------");
        System.out.println("执行结果如下所示:");
        System.out.println("-----------------");
        System.out.println(" ID" + "/t/t" + "name" + "/t/t" + "说明");
        System.out.println("-----------------");
        try {
            while (rs.next()) {
                System.out.println(rs.getInt("id") + "/t/t"
                        + rs.getString("name") + "/t/t"
                        + rs.getString("function"));
            }
        } catch (SQLException e) {
            System.out.println("显示时数据库出错。");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("显示出错。");
            e.printStackTrace();
        }
    }

    /**
     *按照 用例的名称查询该用例有多少组数据
     * @param caseName 用例名称
     * @return 该用例的数据组的id数组
     */
    private int[] getGroup(String caseName){
        String getGroupLength = "SELECT count(*) FROM cases,groups WHERE cases.`name`='"+ caseName+"' AND cases.id=groups.case_id;";
        String getGroup = "SELECT groups.id FROM cases,groups WHERE cases.`name`='"+ caseName+"' AND cases.id=groups.case_id;";
        ResultSet rs = selectSQL(getGroupLength);
        ResultSet rs2 = selectSQL(getGroup);
        int groupIds[] = null;
        try{
            int groupLength = 0;
            while (rs.next()) {
                groupLength = rs.getInt("count(*)");
            }
            groupIds = new int[groupLength];
            int i=0;
            while (rs2.next()) {
                int groupId = rs2.getInt("id");
                groupIds[i] = groupId;
                i++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return groupIds;
    }

    /**
     *根据数据组取出 该组下的预期结果和所有输入数据
     * @param groupId 要查询的数据组id
     * @return 此数据组下的所有数据Object[]类型数组
     */
    private Object[] getData(int groupId){
        String seeLengthSql = "SELECT count(*) FROM datas WHERE group_id=" + groupId + ";";
        String getCorrectSql = "SELECT result_correct FROM groups WHERE id=" + groupId + ";";
        String getDataSql = "SELECT * FROM datas WHERE group_id=" + groupId + ";";
        Object[] obj = null;
        ResultSet rs = selectSQL(seeLengthSql);
        ResultSet rs1 = selectSQL(getCorrectSql);
        ResultSet rs2 = selectSQL(getDataSql);
        try {
            while (rs.next()) {
                int length = rs.getInt("count(*)");
//                obj = new Object[length];
                obj = new Object[length+1];
            }
            int i=0;
            while(rs1.next()) {
                Object correct = rs1.getObject("result_correct");
                obj[i] = correct;
                i++;
            }
            while(rs2.next()) {
                Object data = rs2.getObject("data");
                obj[i] = data;
                i++;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return obj;
    }


    /**
     *根据用例名称查找该用例下所有的组，和组下的数据并 以组为单位放入Object[]内。然后把所有的组放入以用例为单位的Object[]内
     * @param caseName 用例名称
     * @return 从数据库找到的Object[][]数组对象
     */
    public Object[][] getJDBCData(String caseName){
        connSQL();
        int groupIds[] = getGroup(caseName);
        Object obj[][] = new Object[groupIds.length][];
        for(int i=0; i<groupIds.length; i++){
            obj[i] = getData(groupIds[i]);
        }
        deconnSQL();
        return obj;
    }

}