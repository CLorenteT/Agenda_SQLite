import java.sql.*;

public class Main {
    static Connection c = null;
    static Statement st = null;

    public static void main( String args[] ) throws Exception {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:E:/SQLite Databases/agenda.sqlite");
            System.out.println("Opened database successfully");

            // TABLES
            createTables();

            /* INSERTS */
            insertData();

            // SELECTS

            // GENERAL

            selectAll();

            // CONCRET

            /*rs = st.executeQuery("SELECT * FROM CONTACTOS;");

            rs.close();
            st.close();
            c.close();
            */

    }

    static void createTables() throws Exception {
        st = c.createStatement();

        st.execute("DROP TABLE IF EXISTS CONTACTOS");
        st.execute("DROP TABLE IF EXISTS EMAILS");
        st.execute("DROP TABLE IF EXISTS TELEFONOS");

        // CONTACTOS
        String sql = "CREATE TABLE CONTACTOS " +
                "(DNI VARCHAR(9) PRIMARY KEY NOT NULL," +
                " Nombre VARCHAR(30) NOT NULL, " +
                " Direccion VARCHAR(30) NOT NULL)";
        st.execute(sql);
        // EMAILS
        sql = "CREATE TABLE EMAILS " +
                "(DNI VARCHAR(9) NOT NULL," +
                " Email VARCHAR(50) NOT NULL, " +
                " FOREIGN KEY(DNI) REFERENCES CONTACTOS (DNI) ON UPDATE CASCADE ON DELETE CASCADE)";
        st.execute(sql);
        // TELEFONOS
        sql = "CREATE TABLE TELEFONOS " +
                "(DNI VARCHAR(9) NOT NULL," +
                " Telefonos INT NOT NULL, " +
                " FOREIGN KEY(DNI) REFERENCES CONTACTOS (DNI) ON UPDATE CASCADE ON DELETE CASCADE)";
        st.execute(sql);
    }

    static void insertData() throws Exception {
        // CONTACTOS
        st.execute("INSERT INTO CONTACTOS VALUES ('48584159A', 'Messi', 'Barcelona');");
        st.execute("INSERT INTO CONTACTOS VALUES ('12345678W', 'Jero', 'Mallorca');");
        st.execute("INSERT INTO CONTACTOS VALUES ('87654321Q', 'Juan Francisco', 'Mallorca');");
        // EMAILS
        st.execute("INSERT INTO EMAILS VALUES ('48584159A', 'messi@gmail.com');");
        st.execute("INSERT INTO EMAILS VALUES ('12345678W', 'jero13@gmail.com');");
        st.execute("INSERT INTO EMAILS VALUES ('87654321Q', 'jfpinanm@gmail.com');");
        // TELEFONOS
        st.execute("INSERT INTO TELEFONOS VALUES ('48584159A', 971020201);");
        st.execute("INSERT INTO TELEFONOS VALUES ('48584159A', 659010203);");
        st.execute("INSERT INTO TELEFONOS VALUES ('12345678W', 659040401);");
        st.execute("INSERT INTO TELEFONOS VALUES ('87654321Q', 671020406);");
    }

    static void selectAll() throws Exception {
        ResultSet rs = st.executeQuery( "SELECT DNI, NOMBRE, DIRECCION FROM CONTACTOS ORDER BY DNI DESC;" );

        System.out.println("+----------------+------------------------------+---------------------------------+");
        while (rs.next()) {
            String dni = rs.getString("DNI");
            String name = rs.getString("Nombre");
            String dir = rs.getString("Direccion");

            System.out.printf("| DNI: %9s | Nombre: %-20s | Direccion: %-20s |\n", dni, name, dir);
        }
        System.out.println("+----------------+------------------------------+---------------------------------+");
    }
}
