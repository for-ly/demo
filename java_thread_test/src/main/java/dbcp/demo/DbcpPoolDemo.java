package dbcp.demo;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;

/**
 * Created by T440P on 2017/8/10.
 */
public class DbcpPoolDemo {
    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Connection conn = DbcpUtils.getConnection();
            System.out.println(i);
            DbcpUtils.closeConnection(conn);
        }
        long end = System.currentTimeMillis();
        System.out.println("用时：" + (end - begin));
    }
}
