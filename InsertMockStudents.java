import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;

public class InsertMockStudents {
    public static void main(String[] args) {
        String url = "jdbc:mysql://students-data-mysql-studentsdatamysql.c.aivencloud.com:11529/students_data_mysql" +
                     "?useSSL=true&requireSSL=true&verifyServerCertificate=false&serverTimezone=UTC&connectTimeout=8000&socketTimeout=8000";
        String user = "avnadmin";
        String password = "YOUR_AIVEN_PASSWORD_HERE";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);

            // Clean up old students
            PreparedStatement psDelAsist = conn.prepareStatement("DELETE FROM asistencias WHERE num_control >= 90000000");
            psDelAsist.executeUpdate();
            PreparedStatement psDelCalif = conn.prepareStatement("DELETE FROM calificaciones WHERE num_control >= 90000000");
            psDelCalif.executeUpdate();
            PreparedStatement psDelUser = conn.prepareStatement("DELETE FROM usuarios WHERE role = 'ESTUDIANTE' AND no_control >= 90000000");
            psDelUser.executeUpdate();

            // Limit generation to all semesters
            String groupQuery = "SELECT id, nombre_grupo FROM grupos ORDER BY nombre_grupo";
            PreparedStatement getGroups = conn.prepareStatement(groupQuery);
            ResultSet rsGroups = getGroups.executeQuery();

            String insertUser = "INSERT INTO usuarios (nombre, apellido, email, password, role, no_control, grupo_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psUser = conn.prepareStatement(insertUser);

            String insertAsistencia = "INSERT INTO asistencias (num_control, materia, fecha, estado) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE estado = VALUES(estado)";
            PreparedStatement psAsist = conn.prepareStatement(insertAsistencia);

            String insertCalificacion = "INSERT INTO calificaciones (num_control, materia, parcial_1) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE parcial_1 = VALUES(parcial_1)";
            PreparedStatement psCalif = conn.prepareStatement(insertCalificacion);

            long currentNoControl = 90000000L;
            Random rand = new Random();
            String[] materias = {"Lengua", "Matematicas"};
            String[] estados = {"A", "F", "A", "A", "P"};
            String[] nombresReales = {"Carlos", "Sofia", "Diego", "Valentina", "Mateo", "Camila", "Alejandro", "Laura", "Daniel", "Valeria"};

            while (rsGroups.next()) {
                int groupId = rsGroups.getInt("id");
                String groupName = rsGroups.getString("nombre_grupo");
                
                for (int i = 0; i < 2; i++) {
                    String nombre = nombresReales[rand.nextInt(nombresReales.length)];
                    String apellido = groupName;
                    String email = nombre + i + groupName.replace("-", "") + "@myaux.com";
                    long noControl = currentNoControl++;
                    
                    psUser.setString(1, nombre);
                    psUser.setString(2, apellido);
                    psUser.setString(3, email);
                    psUser.setString(4, "1234");
                    psUser.setString(5, "ESTUDIANTE");
                    psUser.setLong(6, noControl);
                    psUser.setInt(7, groupId);
                    psUser.executeUpdate();

                    for (int d = 0; d < 3; d++) {
                        psAsist.setLong(1, noControl);
                        psAsist.setString(2, materias[0]);
                        psAsist.setDate(3, Date.valueOf(LocalDate.now().minusDays(d)));
                        psAsist.setString(4, estados[rand.nextInt(estados.length)]);
                        psAsist.executeUpdate();
                    }

                    for (int m = 0; m < 2; m++) {
                        psCalif.setLong(1, noControl);
                        psCalif.setString(2, materias[m]);
                        double score = 6.0 + (rand.nextDouble() * 4.0);
                        psCalif.setDouble(3, Math.round(score * 10.0) / 10.0);
                        psCalif.executeUpdate();
                    }
                }
            }

            conn.commit();
            System.out.println("Mock students inserted successfully.");

            rsGroups.close();
            getGroups.close();
            psUser.close();
            psAsist.close();
            psCalif.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
