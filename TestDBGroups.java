import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestDBGroups {
    public static void main(String[] args) {
        String url = "jdbc:mysql://students-data-mysql-studentsdatamysql.c.aivencloud.com:11529/students_data_mysql" +
                     "?useSSL=true&requireSSL=true&verifyServerCertificate=false&serverTimezone=UTC&connectTimeout=8000&socketTimeout=8000";
        String user = "avnadmin";
        String password = "YOUR_AIVEN_PASSWORD_HERE";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            
            String query = "SELECT g.nombre_grupo, COUNT(u.id) as num_alumnos FROM grupos g LEFT JOIN usuarios u ON g.id = u.grupo_id AND u.role = 'alumno' GROUP BY g.nombre_grupo HAVING num_alumnos > 0";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
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
