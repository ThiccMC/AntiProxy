package ml.thelt.antiproxy.sql;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

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
                        + "`proxy_type` varchar(36) NOT NULL,"
                        + "`status` boolean NOT NULL,"
                        + "PRIMARY KEY (`ip`));"
        );

        ps.execute();
        this.closeConnection(c);
    }

    public GetInternetProtocol getIP(String ip) throws SQLException{
        checkAsync();

        Connection c = this.getConnection();
        PreparedStatement ps = c.prepareStatement("SELECT ip, proxy_type, status FROM "
                + table_name + " WHERE ip = ?"
        );

        ps.setString(1, ip);
        ps.execute();

        ResultSet res = ps.executeQuery();
        GetInternetProtocol ret = null;

        if (res.next()) {
            ret = new GetInternetProtocol(
                    res.getString(1),
                    res.getString(2),
                    res.getBoolean(3)
            );
        }

        this.closeConnection(c);
        return ret;
    }

    public void addIP(String ip, String proxy_type, Boolean status) throws SQLException{
        checkAsync();

        Connection c = this.getConnection();
        PreparedStatement ps = c.prepareStatement("INSERT INTO " + table_name+ " (ip, proxy_type, status) VALUES (?,?,?)");

        ps.setString(1, ip);
        ps.setString(2, proxy_type);
        ps.setBoolean(3, status);
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

    public void banIP(String ip, String proxy_type, Boolean status) throws SQLException {
        checkAsync();

        Connection c = this.getConnection();
        PreparedStatement ps = c.prepareStatement("UPDATE " + table_name + " SET proxy_type = ?, status = ? WHERE ip = ?");

        ps.setString(1, proxy_type);
        ps.setBoolean(2, status);
        ps.setString(3, ip);
        ps.execute();

        this.closeConnection(c);
    }

    public void clearList(Boolean status) throws SQLException {
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
        public final String proxy_type;
        public final boolean status;

        GetInternetProtocol(String ip, String proxy_type, boolean status) {
            this.ip = ip;
            this.proxy_type = proxy_type;
            this.status = status;
        }
    }
}
