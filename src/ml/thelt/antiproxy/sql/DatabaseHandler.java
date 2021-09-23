package ml.thelt.antiproxy.sql;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DatabaseHandler {
    protected final JavaPlugin plugin;
    protected final SecureRandom random;

    protected DatabaseHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.random = new SecureRandom();
    }

    public String table_name = "ip_data";

    protected abstract Connection getConnection() throws SQLException;

    protected abstract void closeConnection(Connection c) throws SQLException;

    protected void initialize() throws SQLException {
        Connection c = this.getConnection();
        PreparedStatement ps = c.prepareStatement(
                "CREATE TABLE IF NOT EXISTS `" + table_name + "` ("
                        + "`ip` varchar(18) NOT NULL,"
                        + "`score` int(10) NOT NULL,"
                        + "`ip_type` varchar(36) NOT NULL,"
                        + "`status` boolean NOT NULL,"
                        + "PRIMARY KEY (`ip`));"
        );

        ps.execute();
        this.closeConnection(c);
    }

    public GetInternetProtocol getIP(String ip) throws SQLException{
        checkAsync();

        Connection c = this.getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT ip, score, ip_type, status FROM "
                + table_name + " WHERE ip = ?"
        );

        ps.setString(1, ip);
        ps.execute();

        ResultSet res = ps.executeQuery();
        GetInternetProtocol ret = null;

        if (res.next()) {
            ret = new GetInternetProtocol(
                    res.getString(1),
                    res.getInt(2),
                    res.getString(3),
                    res.getBoolean(4)
            );
        }

        this.closeConnection(c);
        return ret;
    }

    public void addIP(String ip, int score, String ip_type, Boolean status) throws SQLException{
        checkAsync();

        Connection c = this.getConnection();
        PreparedStatement ps = c.prepareStatement("INSERT INTO " + table_name+ " (ip, score, ip_type, status) VALUES (?,?,?,?)");

        ps.setString(1, ip);
        ps.setInt(2, score);
        ps.setString(3, ip_type);
        ps.setBoolean(4, status);
        ps.execute();

        this.closeConnection(c);
    }

    public void removeIP(String ip) throws SQLException {
        checkAsync();

        Connection c = this.getConnection();
        PreparedStatement ps = c.prepareStatement("DELETE FROM " + table_name + " WHERE ip = ?");

        ps.setString(1, ip);
        ps.execute();

        this.closeConnection(c);
    }

    public void clearList(String proxy_type) throws SQLException {
        checkAsync();

        Connection c = this.getConnection();
        PreparedStatement ps = c.prepareStatement("DELETE FROM " + table_name+ " WHERE ip_type = ?");

        ps.setString(1, proxy_type);
        ps.execute();

        this.closeConnection(c);
    }

    public void clearData(Boolean status) throws SQLException {
        checkAsync();

        Connection c = this.getConnection();
        PreparedStatement ps = c.prepareStatement("DELETE FROM " + table_name+ " WHERE status = ?");

        ps.setBoolean(1, status);
        ps.execute();

        this.closeConnection(c);
    }

    private final void checkAsync() {
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Attempted to execute a database operation from the server thread!");
        }
    }

    public static class GetInternetProtocol {
        public final String ip;
        public final int score;
        public final String ip_type;
        public final boolean status;

        GetInternetProtocol(String ip, int score, String ip_type, boolean status) {
            this.ip = ip;
            this.score = score;
            this.ip_type = ip_type;
            this.status = status;
        }
    }
}
