import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestDBRows {
    public static void main(String[] args) {
        String url = "jdbc:mysql://students-data-mysql-studentsdatamysql.c.aivencloud.com:11529/students_data_mysql" +
                     "?useSSL=true&requireSSL=true&verifyServerCertificate=false&serverTimezone=UTC&connectTimeout=8000&socketTimeout=8000";
        String user = "avnadmin";
        String password = "YOUR_AIVEN_PASSWORD_HERE";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            
            String query = "SELECT nombre, apellido, role, grupo_id FROM usuarios";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            System.out.println("USUARIOS_START");
            while (rs.next()) {
                System.out.println("- " + rs.getString("nombre") + " " + rs.getString("apellido") + " | Role: " + rs.getString("role") + " | Grupo ID: " + rs.getInt("grupo_id"));
            }
            System.out.println("USUARIOS_END");
            
            query = "SELECT id, nombre_grupo FROM grupos";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            
            System.out.println("GRUPOS_START");
            while (rs.next()) {
                System.out.println("- ID: " + rs.getInt("id") + " | Nombre: " + rs.getString("nombre_grupo"));
            }
            System.out.println("GRUPOS_END");

            query = "SELECT g.nombre_grupo, COUNT(u.id) as num_alumnos FROM grupos g INNER JOIN usuarios u ON g.id = u.grupo_id WHERE u.role = 'alumno' GROUP BY g.nombre_grupo";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            
            System.out.println("GRUPOS_CON_ALUMNOS_START");
            while (rs.next()) {
                System.out.println("- " + rs.getString("nombre_grupo") + " (" + rs.getInt("num_alumnos") + " alumnos)");
            }
            System.out.println("GRUPOS_CON_ALUMNOS_END");

            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
