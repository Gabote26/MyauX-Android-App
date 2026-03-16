import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetMockCreds {
    public static void main(String[] args) {
        String url = "jdbc:mysql://students-data-mysql-studentsdatamysql.c.aivencloud.com:11529/students_data_mysql" +
                     "?useSSL=true&requireSSL=true&verifyServerCertificate=false&serverTimezone=UTC&connectTimeout=8000&socketTimeout=8000";
        String user = "avnadmin";
        String password = "YOUR_AIVEN_PASSWORD_HERE";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            
            System.out.println("--- ALUMNOS DE PRUEBA ---");
            String query = "SELECT u.nombre, u.apellido, u.email, g.nombre_grupo FROM usuarios u JOIN grupos g ON u.grupo_id = g.id WHERE u.no_control >= 90000000 ORDER BY g.nombre_grupo";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                System.out.println("Grupo: " + rs.getString("nombre_grupo") + " | " + rs.getString("nombre") + " " + rs.getString("apellido") + " -> Correo: " + rs.getString("email") + " | Pass: 1234");
            }

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
