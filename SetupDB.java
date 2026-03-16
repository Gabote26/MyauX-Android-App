import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SetupDB {
    public static void main(String[] args) {
        String url = "jdbc:mysql://students-data-mysql-studentsdatamysql.c.aivencloud.com:11529/students_data_mysql" +
                     "?useSSL=true&requireSSL=true&verifyServerCertificate=false&serverTimezone=UTC&connectTimeout=8000&socketTimeout=8000";
        String user = "avnadmin";
        String password = "YOUR_AIVEN_PASSWORD_HERE";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);

            // 1. Añadir columna a `asistencias`
            System.out.println("Alterando tabla asistencias...");
            try {
                String alterQuery = "ALTER TABLE asistencias ADD COLUMN fecha_registro DATETIME NULL DEFAULT NULL";
                PreparedStatement psAlter = conn.prepareStatement(alterQuery);
                psAlter.executeUpdate();
                psAlter.close();
            } catch (Exception alterEx) {
                System.out.println("Columna ya existe o error menor: " + alterEx.getMessage());
            }

            // 1.5 Crear tabla de materias si no existe
            System.out.println("Creando tabla profesor_materias si no existe...");
            String createTable = "CREATE TABLE IF NOT EXISTS profesor_materias (" +
                                 "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                 "profesor_id INT NOT NULL, " +
                                 "materia VARCHAR(255) NOT NULL, " +
                                 "FOREIGN KEY (profesor_id) REFERENCES usuarios(id) ON DELETE CASCADE)";
            PreparedStatement psCreate = conn.prepareStatement(createTable);
            psCreate.executeUpdate();
            String createTableGroup = "CREATE TABLE IF NOT EXISTS profesor_grupos (" +
                                      "id INT AUTO_INCREMENT PRIMARY KEY, " +
                                      "profesor_id INT NOT NULL, " +
                                      "grupo_id INT NOT NULL, " +
                                      "FOREIGN KEY (profesor_id) REFERENCES usuarios(id) ON DELETE CASCADE, " +
                                      "FOREIGN KEY (grupo_id) REFERENCES grupos(id) ON DELETE CASCADE)";
            PreparedStatement psCreateGrp = conn.prepareStatement(createTableGroup);
            psCreateGrp.executeUpdate();
            psCreateGrp.close();

            // 2. Traer el usuario de Juan Perez
            String getJuanQuery = "SELECT id FROM usuarios WHERE email = 'juan.perez@example.com'";
            PreparedStatement getJuan = conn.prepareStatement(getJuanQuery);
            ResultSet rsJuan = getJuan.executeQuery();
            int juanId = 0;
            if (rsJuan.next()) {
                juanId = rsJuan.getInt("id");
            }
            rsJuan.close();
            getJuan.close();

            // Insertar materias para juan
            if (juanId > 0) {
                String delJuan = "DELETE FROM profesor_materias WHERE profesor_id = ?";
                PreparedStatement psDelJuan = conn.prepareStatement(delJuan);
                psDelJuan.setInt(1, juanId);
                psDelJuan.executeUpdate();

                String insertJuan = "INSERT INTO profesor_materias (profesor_id, materia) VALUES (?, ?)";
                PreparedStatement psIns = conn.prepareStatement(insertJuan);
                psIns.setInt(1, juanId); psIns.setString(2, "Lengua"); psIns.executeUpdate();
                psIns.setInt(1, juanId); psIns.setString(2, "Sociales"); psIns.executeUpdate();
                psIns.close();
                psDelJuan.close();
            }

            // Creador helper function para añadir profes
            PreparedStatement psCheck = conn.prepareStatement("SELECT id FROM usuarios WHERE email = ?");
            PreparedStatement psInsUser = conn.prepareStatement("INSERT INTO usuarios (nombre, apellido, email, password, role, no_control) VALUES (?, ?, ?, '1234', 'profesor', ?)", java.sql.Statement.RETURN_GENERATED_KEYS);
            PreparedStatement psInsMat = conn.prepareStatement("INSERT INTO profesor_materias (profesor_id, materia) VALUES (?, ?)");

            String[][] profes = {
                {"Profe", "Ciencias", "profe.ciencias@example.com", "Matematicas", "Ciencias"},
                {"Profe", "Filosofia", "profe.filosofia@example.com", "Humanidades", "Filosofia"}
            };
            long noControlProfe = 50000000L;

            for (String[] p : profes) {
                psCheck.setString(1, p[2]);
                ResultSet rsCheck = psCheck.executeQuery();
                int pId = 0;
                if (rsCheck.next()) {
                    pId = rsCheck.getInt("id");
                } else {
                    psInsUser.setString(1, p[0]);
                    psInsUser.setString(2, p[1]);
                    psInsUser.setString(3, p[2]);
                    psInsUser.setLong(4, noControlProfe++);
                    psInsUser.executeUpdate();
                    ResultSet rsGen = psInsUser.getGeneratedKeys();
                    if (rsGen.next()) pId = rsGen.getInt(1);
                    rsGen.close();
                }
                rsCheck.close();

                if (pId > 0) {
                    // Borrar materias viejas
                    PreparedStatement delP = conn.prepareStatement("DELETE FROM profesor_materias WHERE profesor_id = ?");
                    delP.setInt(1, pId);
                    delP.executeUpdate();
                    delP.close();

                    psInsMat.setInt(1, pId); psInsMat.setString(2, p[3]); psInsMat.executeUpdate();
                    psInsMat.setInt(1, pId); psInsMat.setString(2, p[4]); psInsMat.executeUpdate();
                }
            }

            // Assign groups to teachers
            PreparedStatement psGroups = conn.prepareStatement("SELECT id FROM grupos");
            ResultSet rsGroups = psGroups.executeQuery();
            PreparedStatement psInsGrp = conn.prepareStatement("INSERT IGNORE INTO profesor_grupos (profesor_id, grupo_id) VALUES (?, ?)");
            
            // Get teacher IDs
            PreparedStatement psGetProfe = conn.prepareStatement("SELECT id FROM usuarios WHERE email IN ('profe.ciencias@example.com', 'profe.filosofia@example.com')");
            ResultSet rsProfe = psGetProfe.executeQuery();
            while (rsProfe.next()) {
                int profId = rsProfe.getInt("id");
                rsGroups.beforeFirst();
                while (rsGroups.next()) {
                   psInsGrp.setInt(1, profId);
                   psInsGrp.setInt(2, rsGroups.getInt("id"));
                   psInsGrp.executeUpdate();
                }
            }

            conn.commit();
            System.out.println("SUCCESS: Base de datos, maestros y grupos actualizados");

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
