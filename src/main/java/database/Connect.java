package database;

import util.GetProperties;

import java.sql.*;

/**
 * Created by song on 2017/5/19.
 */
public class Connect {

    /**
     * 数据库连接信息
     */
    private String driver;
    private String url;
    private String name;
    private String passwd;

    /**
     * 数据库连接对象
     */
    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public Connect() {
        initDBManager();
        connectDB();
    }


    /**
     * 初始化链接数据
     */
    private void initDBManager() {
        GetProperties getProperties = new GetProperties("db");
        this.driver = getProperties.getPropertie("driver");
        this.url = getProperties.getPropertie("url");
        this.name = getProperties.getPropertie("username");
        this.passwd = getProperties.getPropertie("password");
    }

    /**
     * 链接数据库
     */
    private void connectDB() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,name,passwd);
        } catch (ClassNotFoundException e) {
            System.err.println("装载 JDBC/ODBC 驱动程序失败。");
            e.printStackTrace();
        } catch (SQLException sqle) {
            System.err.println( "无法连接数据库" );
            sqle.printStackTrace();
        }
    }

    /**
     * 关闭数据库链接
     */
    public void disConnect() {
        try {
            if(rs != null) {
                rs.close();
                rs = null;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                if(stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            }catch(SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(conn != null) {
                        conn.close();
                        conn = null;
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }finally {
                    System.err.println( "数据库链接已关闭");
                }
            }

        }

    }

    // 执行数据库插入语句
   public boolean insertSQL(String sql) {
        try {
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.out.println("插入数据库时出错：");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("插入时出错：");
            e.printStackTrace();
        }
        return false;
    }

}
