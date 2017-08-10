package dbcp.demo;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by T440P on 2017/8/10.
 */
public class DbcpUtils {
    private static BasicDataSource dataSource;

    static {
        Properties properties = new Properties();
        try {
            properties.load(DbcpUtils.class.getClassLoader().getResourceAsStream("dbcp.properties"));
            dataSource = (BasicDataSource) BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("初始化连接错误，请检查配置文件！");
        }
    }

    public static DataSource getDataSource(){
        return dataSource;
    }

    public static Connection getConnection(){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection(Connection connection){
        try {
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void release (Connection connection, Statement statement, ResultSet resultSet) throws SQLException {
        resultSet.close();
        statement.close();
        connection.close();
    }

}
