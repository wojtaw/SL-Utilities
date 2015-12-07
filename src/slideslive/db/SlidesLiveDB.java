package slideslive.db;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Properties;

/**
 * Created by WojtawDesktop on 21.4.15.
 */
public class SlidesLiveDB {
    private Connection conn;

    public SlidesLiveDB(){

    }

    public void initConnection() {
        String url = "jdbc:postgresql://ec2-107-22-246-233.compute-1.amazonaws.com:5552/d860nhntdot3f0";
        Properties props = new Properties();
        props.setProperty("user","u4t1fr5r37qtkd");
        props.setProperty("password","p6p6f3er9s80cc63bpof19huhvs");
        props.setProperty("ssl","true");
        props.setProperty("sslfactory","org.postgresql.ssl.NonValidatingFactory");
        try {
            conn = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getServiceID(int presentationID) {
        Statement st = null;
        String serviceID = "";
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM presentations WHERE ID = "+presentationID);
            while (rs.next())
            {
                serviceID = rs.getString("external_service_id");
                if(serviceID.length() < 1)
                    serviceID = rs.getString("internal_service_id");
            } rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return serviceID;
    }

    public String getCanonicalName(int presentationID) {
        Statement st = null;
        String canonicalName = "";
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM presentations WHERE ID = "+presentationID);
            while (rs.next())
            {
                canonicalName = rs.getString("canonical_name");
            } rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return canonicalName;
    }
}
