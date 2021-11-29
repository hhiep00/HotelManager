package DBConnection;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public Connection databaseLink;
    public Connection getConnection(){
        String user = "sa";
        String password = "123456";
        String url = "jdbc:sqlserver://localhost:1433;databaseName=HotelManager";
        try {
            databaseLink = DriverManager.getConnection(url, user, password);
        } catch (Exception e){
            e.printStackTrace();
        }
        return databaseLink;
    }
}
