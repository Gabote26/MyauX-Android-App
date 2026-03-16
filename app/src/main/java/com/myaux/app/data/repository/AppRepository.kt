package com.myaux.app.data.repository

import com.myaux.app.data.db.DatabaseConnection
import com.myaux.app.data.model.*
import com.myaux.app.ui.screens.AlumnoAsistencia
import com.myaux.app.ui.screens.AlumnoCalificacion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class AppRepository {

    // ==================== LOGIN ====================

    suspend fun login(email: String, password: String): LoginResult = withContext(Dispatchers.IO) {
        try {
            val cn = DatabaseConnection.getConnection()
                ?: return@withContext LoginResult.Error("No se pudo conectar a la base de datos")

            val query = """
                SELECT u.id, u.nombre, u.apellido, u.role, u.no_control, 
                       COALESCE(g.nombre_grupo, 'No asignado') as grupo_nombre
                FROM usuarios u
                LEFT JOIN grupos g ON u.grupo_id = g.id
                WHERE u.email = ? AND u.password = ?
            """

            cn.use { connection ->
                connection.prepareStatement(query).use { ps ->
                    ps.setString(1, email)
                    ps.setString(2, password)
                    val rs = ps.executeQuery()

                    if (rs.next()) {
                        LoginResult.Success(
                            Usuario(
                                id = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                apellido = rs.getString("apellido"),
                                email = email,
                                role = rs.getString("role"),
                                numControl = rs.getLong("no_control"),
                                grupoNombre = rs.getString("grupo_nombre")
                            )
                        )
                    } else {
                        LoginResult.Error("Usuario o contraseña incorrectos")
                    }
                }
            }
        } catch (e: Exception) {
            LoginResult.Error("Error: ${e.message}")
        }
    }

    suspend fun getUserByEmail(email: String): Usuario? = withContext(Dispatchers.IO) {
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext null

            val query = """
                SELECT u.id, u.nombre, u.apellido, u.email, u.role, u.no_control,
                       COALESCE(g.nombre_grupo, 'No asignado') as grupo_nombre
                FROM usuarios u
                LEFT JOIN grupos g ON u.grupo_id = g.id
                WHERE u.email = ?
            """

            cn.use { connection ->
                connection.prepareStatement(query).use { ps ->
                    ps.setString(1, email)
                    val rs = ps.executeQuery()

                    if (rs.next()) {
                        Usuario(
                            id = rs.getInt("id"),
                            nombre = rs.getString("nombre"),
                            apellido = rs.getString("apellido"),
                            email = rs.getString("email"),
                            role = rs.getString("role"),
                            numControl = rs.getLong("no_control"),
                            grupoNombre = rs.getString("grupo_nombre")
                        )
                    } else null
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    // ==================== CALIFICACIONES ====================

    suspend fun getCalificaciones(numControl: Long): List<Calificacion> = withContext(Dispatchers.IO) {
        val lista = mutableListOf<Calificacion>()
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext lista

            val sql = """
                SELECT id, num_control, materia, parcial_1, parcial_2, parcial_3
                FROM calificaciones
                WHERE num_control = ?
                ORDER BY materia
            """

            cn.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setLong(1, numControl)
                    val rs = ps.executeQuery()
                    while (rs.next()) {
                        lista.add(
                            Calificacion(
                                id = rs.getInt("id"),
                                numControl = rs.getLong("num_control"),
                                materia = rs.getString("materia"),
                                parcial1 = rs.getObject("parcial_1") as? Double,
                                parcial2 = rs.getObject("parcial_2") as? Double,
                                parcial3 = rs.getObject("parcial_3") as? Double,
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        lista
    }

    // ==================== ASISTENCIAS ====================

    suspend fun getEstadisticasAsistencia(numControl: Long): EstadisticasAsistencia = withContext(Dispatchers.IO) {
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext EstadisticasAsistencia()

            val sql = """
                SELECT
                    COUNT(*) as total,
                    SUM(CASE WHEN estado = 'A' THEN 1 ELSE 0 END) as presentes,
                    SUM(CASE WHEN estado = 'F' THEN 1 ELSE 0 END) as faltas,
                    SUM(CASE WHEN estado = 'P' THEN 1 ELSE 0 END) as permisos
                FROM asistencias
                WHERE num_control = ?
            """

            cn.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setLong(1, numControl)
                    val rs = ps.executeQuery()
                    if (rs.next()) {
                        EstadisticasAsistencia(
                            total = rs.getInt("total"),
                            presentes = rs.getInt("presentes"),
                            faltas = rs.getInt("faltas"),
                            permisos = rs.getInt("permisos")
                        )
                    } else EstadisticasAsistencia()
                }
            }
        } catch (e: Exception) {
            EstadisticasAsistencia()
        }
    }

    suspend fun getAsistencias(numControl: Long, materia: String? = null): List<Asistencia> =
        withContext(Dispatchers.IO) {
            val lista = mutableListOf<Asistencia>()
            try {
                val cn = DatabaseConnection.getConnection() ?: return@withContext lista

                val sql = buildString {
                    append("SELECT fecha, materia, estado FROM asistencias WHERE num_control = ?")
                    if (materia != null) append(" AND materia = ?")
                    append(" ORDER BY fecha DESC")
                }

                cn.use { connection ->
                    connection.prepareStatement(sql).use { ps ->
                        ps.setLong(1, numControl)
                        if (materia != null) ps.setString(2, materia)
                        val rs = ps.executeQuery()
                        while (rs.next()) {
                            lista.add(
                                Asistencia(
                                    numControl = numControl,
                                    materia = rs.getString("materia"),
                                    fecha = rs.getDate("fecha").let {
                                        Instant.ofEpochMilli(it.time).atZone(ZoneId.systemDefault()).toLocalDate()
                                    },
                                    estado = rs.getString("estado")
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            lista
        }

    // ==================== MENSAJES ====================

    suspend fun getMensajes(usuarioId: Int): List<Mensaje> = withContext(Dispatchers.IO) {
        val lista = mutableListOf<Mensaje>()
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext lista

            val sql = """
                SELECT m.id, m.remitente_id, m.tipo_mensaje, m.asunto,
                       m.contenido, m.fecha_envio, md.leido
                FROM mensajes m
                INNER JOIN mensajes_destinatarios md ON m.id = md.mensaje_id
                WHERE md.destinatario_id = ?
                ORDER BY m.fecha_envio DESC
            """

            cn.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setInt(1, usuarioId)
                    val rs = ps.executeQuery()
                    while (rs.next()) {
                        lista.add(
                            Mensaje(
                                id = rs.getInt("id"),
                                remitenteId = rs.getInt("remitente_id"),
                                tipoMensaje = rs.getString("tipo_mensaje"),
                                asunto = rs.getString("asunto"),
                                contenido = rs.getString("contenido"),
                                fechaEnvio = rs.getTimestamp("fecha_envio").let {
                                    Instant.ofEpochMilli(it.time).atZone(ZoneId.systemDefault()).toLocalDateTime()
                                },
                                leido = rs.getBoolean("leido")
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        lista
    }

    suspend fun marcarComoLeido(mensajeId: Int, usuarioId: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext false

            val sql = """
                UPDATE mensajes_destinatarios
                SET leido = TRUE, fecha_lectura = NOW()
                WHERE mensaje_id = ? AND destinatario_id = ?
            """

            cn.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setInt(1, mensajeId)
                    ps.setInt(2, usuarioId)
                    ps.executeUpdate() > 0
                }
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun contarMensajesNoLeidos(usuarioId: Int): Int = withContext(Dispatchers.IO) {
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext 0

            val sql = """
                SELECT COUNT(*) as total
                FROM mensajes_destinatarios
                WHERE destinatario_id = ? AND leido = FALSE
            """

            cn.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setInt(1, usuarioId)
                    val rs = ps.executeQuery()
                    if (rs.next()) rs.getInt("total") else 0
                }
            }
        } catch (e: Exception) {
            0
        }
    }

    suspend fun getUserId(numControl: Long): Int = withContext(Dispatchers.IO) {
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext -1

            cn.use { connection ->
                connection.prepareStatement("SELECT id FROM usuarios WHERE no_control = ?").use { ps ->
                    ps.setLong(1, numControl)
                    val rs = ps.executeQuery()
                    if (rs.next()) rs.getInt("id") else -1
                }
            }
        } catch (e: Exception) {
            -1
        }
    }

    // ==================== PROFESOR: MATERIAS ====================

    suspend fun getMateriasProfesor(profesorId: Int): List<String> = withContext(Dispatchers.IO) {
        val lista = mutableListOf<String>()
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext lista
            val sql = """
                SELECT DISTINCT materia FROM profesor_materias WHERE profesor_id = ? ORDER BY materia
            """
            cn.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setInt(1, profesorId)
                    val rs = ps.executeQuery()
                    while (rs.next()) {
                        lista.add(rs.getString("materia"))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        lista
    }

    // ==================== PROFESOR: GRUPOS ====================

    suspend fun getGruposProfesor(profesorId: Int): List<Pair<Int, String>> = withContext(Dispatchers.IO) {
        val lista = mutableListOf<Pair<Int, String>>()
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext lista
            val sql = """
                SELECT DISTINCT g.id, g.nombre_grupo 
                FROM grupos g
                INNER JOIN profesor_grupos pg ON g.id = pg.grupo_id
                WHERE pg.profesor_id = ?
                ORDER BY g.nombre_grupo
            """
            cn.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setInt(1, profesorId)
                    val rs = ps.executeQuery()
                    while (rs.next()) {
                        lista.add(Pair(rs.getInt("id"), rs.getString("nombre_grupo")))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        lista
    }

    suspend fun getAlumnosByGrupoYFecha(grupoNombre: String, fecha: LocalDate): List<AlumnoAsistencia> = withContext(Dispatchers.IO) {
        val lista = mutableListOf<AlumnoAsistencia>()
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext lista
            val sql = """
                SELECT u.id, u.nombre, u.apellido, u.no_control, 
                       COALESCE(a.estado, '') as estado,
                       CASE WHEN a.fecha_registro IS NOT NULL AND TIMESTAMPDIFF(MINUTE, a.fecha_registro, NOW()) > 15 
                            THEN 1 ELSE 0 END as locked
                FROM usuarios u
                INNER JOIN grupos g ON u.grupo_id = g.id
                LEFT JOIN asistencias a ON u.no_control = a.num_control AND a.fecha = ?
                WHERE g.nombre_grupo = ? AND LOWER(u.role) IN ('alumno', 'estudiante')
                ORDER BY u.apellido, u.nombre
            """
            cn.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setDate(1, java.sql.Date.valueOf(fecha.toString()))
                    ps.setString(2, grupoNombre)
                    val rs = ps.executeQuery()
                    while (rs.next()) {
                        lista.add(
                            AlumnoAsistencia(
                                id = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                apellido = rs.getString("apellido"),
                                numControl = rs.getLong("no_control"),
                                estado = rs.getString("estado"),
                                locked = rs.getInt("locked") == 1
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        lista
    }

    suspend fun getAlumnosCalificacionByGrupoNombre(grupoNombre: String, materia: String): List<AlumnoCalificacion> = withContext(Dispatchers.IO) {
        val lista = mutableListOf<AlumnoCalificacion>()
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext lista
            val sql = """
                SELECT u.id, u.nombre, u.apellido, u.no_control,
                       COALESCE(c.parcial_1, '') as p1,
                       COALESCE(c.parcial_2, '') as p2,
                       COALESCE(c.parcial_3, '') as p3
                FROM usuarios u
                INNER JOIN grupos g ON u.grupo_id = g.id
                LEFT JOIN calificaciones c ON u.no_control = c.num_control AND c.materia = ?
                WHERE g.nombre_grupo = ? AND LOWER(u.role) IN ('alumno', 'estudiante')
                ORDER BY u.apellido, u.nombre
            """
            cn.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    ps.setString(1, materia)
                    ps.setString(2, grupoNombre)
                    val rs = ps.executeQuery()
                    while (rs.next()) {
                        lista.add(
                            AlumnoCalificacion(
                                id = rs.getInt("id"),
                                nombre = rs.getString("nombre"),
                                apellido = rs.getString("apellido"),
                                numControl = rs.getLong("no_control"),
                                parcial1 = rs.getString("p1") ?: "",
                                parcial2 = rs.getString("p2") ?: "",
                                parcial3 = rs.getString("p3") ?: ""
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        lista
    }

    // ==================== PROFESOR: GUARDAR ASISTENCIAS ====================

    suspend fun saveAsistencias(alumnos: List<AlumnoAsistencia>, materia: String, fecha: LocalDate): Boolean = withContext(Dispatchers.IO) {
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext false
            val sql = """
                INSERT INTO asistencias (num_control, materia, fecha, estado, fecha_registro)
                VALUES (?, ?, ?, ?, NOW())
                ON DUPLICATE KEY UPDATE estado = VALUES(estado), fecha_registro = IF(TIMESTAMPDIFF(MINUTE, fecha_registro, NOW()) > 15, fecha_registro, NOW())
            """
            cn.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    // Only save those not locked and with an actual state assigned
                    alumnos.filter { it.estado.isNotBlank() && !it.locked }.forEach { alumno ->
                        ps.setLong(1, alumno.numControl)
                        ps.setString(2, materia)
                        ps.setDate(3, java.sql.Date.valueOf(fecha.toString()))
                        ps.setString(4, alumno.estado)
                        ps.addBatch()
                    }
                    ps.executeBatch()
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // ==================== PROFESOR: GUARDAR CALIFICACIONES ====================

    suspend fun saveCalificaciones(alumnos: List<AlumnoCalificacion>, materia: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext false
            val sql = """
                INSERT INTO calificaciones (num_control, materia, parcial_1)
                VALUES (?, ?, ?)
                ON DUPLICATE KEY UPDATE parcial_1 = VALUES(parcial_1)
            """
            cn.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    alumnos.filter { it.parcial1.isNotBlank() }.forEach { alumno ->
                        ps.setLong(1, alumno.numControl)
                        ps.setString(2, materia)
                        ps.setDouble(3, alumno.parcial1.toDoubleOrNull() ?: 0.0)
                        ps.addBatch()
                    }
                    ps.executeBatch()
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // ==================== PROFESOR: ENVIAR MENSAJE ====================

    suspend fun enviarMensaje(
        remitenteId: Int,
        destinatarioIds: List<Int>,
        tipoMensaje: String,
        asunto: String,
        contenido: String
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext false

            cn.use { connection ->
                // Insert the message
                val sqlMsg = """
                    INSERT INTO mensajes (remitente_id, tipo_mensaje, asunto, contenido, fecha_envio)
                    VALUES (?, ?, ?, ?, NOW())
                """
                val msgPs = connection.prepareStatement(sqlMsg, java.sql.Statement.RETURN_GENERATED_KEYS)
                msgPs.setInt(1, remitenteId)
                msgPs.setString(2, tipoMensaje)
                msgPs.setString(3, asunto)
                msgPs.setString(4, contenido)
                msgPs.executeUpdate()

                val rs = msgPs.generatedKeys
                if (rs.next()) {
                    val mensajeId = rs.getInt(1)

                    // Insert recipients
                    val sqlDest = """
                        INSERT INTO mensajes_destinatarios (mensaje_id, destinatario_id, leido)
                        VALUES (?, ?, FALSE)
                    """
                    connection.prepareStatement(sqlDest).use { ps ->
                        destinatarioIds.forEach { destId ->
                            ps.setInt(1, mensajeId)
                            ps.setInt(2, destId)
                            ps.addBatch()
                        }
                        ps.executeBatch()
                    }
                }
                msgPs.close()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    // ==================== PROFESOR: GET ALUMNOS PARA MENSAJES ====================

    suspend fun getAlumnosParaMensaje(grupoId: Int? = null): List<Pair<Int, String>> = withContext(Dispatchers.IO) {
        val lista = mutableListOf<Pair<Int, String>>()
        try {
            val cn = DatabaseConnection.getConnection() ?: return@withContext lista
            val sql = buildString {
                append("SELECT id, CONCAT(apellido, ' ', nombre) as nombre_completo FROM usuarios WHERE LOWER(role) IN ('alumno', 'estudiante')")
                if (grupoId != null) append(" AND grupo_id = ?")
                append(" ORDER BY apellido, nombre")
            }
            cn.use { connection ->
                connection.prepareStatement(sql).use { ps ->
                    if (grupoId != null) ps.setInt(1, grupoId)
                    val rs = ps.executeQuery()
                    while (rs.next()) {
                        lista.add(Pair(rs.getInt("id"), rs.getString("nombre_completo")))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        lista
    }
}
