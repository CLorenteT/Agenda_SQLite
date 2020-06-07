import java.sql.*;

public class Main {
    public static void main( String args[] ) {
        Connection c = null;
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:agenda.db");
            System.out.println("Opened database successfully");


            /* TABLES */

            stmt = c.createStatement();

            String sql = "DROP TABLE CONTACTOS";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE EMAILS";
            stmt.executeUpdate(sql);

            sql = "DROP TABLE TELEFONOS";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE CONTACTOS " +
                    "(DNI VARCHAR(20) PRIMARY KEY NOT NULL," +
                    " NOMBRE VARCHAR(30) NOT NULL, " +
                    " DIRECCION VARCHAR(30) NOT NULL)";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE EMAILS " +
                    "(DNI VARCHAR(20) NOT NULL," +
                    " EMAIL VARCHAR(50) NOT NULL, " +
                    " FOREIGN KEY(DNI) REFERENCES CONTACTOS (DNI) ON UPDATE CASCADE ON DELETE CASCADE)";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE TELEFONOS " +
                    "(DNI VARCHAR(20) NOT NULL," +
                    " TELEFONO INT NOT NULL, " +
                    " FOREIGN KEY(DNI) REFERENCES CONTACTOS (DNI) ON UPDATE CASCADE ON DELETE CASCADE)";
            stmt.executeUpdate(sql);


            /* INSERTS */

            sql = "INSERT INTO CONTACTOS " +
                    "VALUES ('48584159A', 'Messi', 'Barcelona');";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO CONTACTOS " +
                    "VALUES ('12345678W', 'Jero', 'Mallorca');";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO CONTACTOS " +
                    "VALUES ('87654321Q', 'Juan Francisco', 'Mallorca');";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO EMAILS " +
                    "VALUES ('48584159A', 'messi@gmail.com');";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO EMAILS " +
                    "VALUES ('12345678W', 'jero13@gmail.com');";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO EMAILS " +
                    "VALUES ('87654321Q', 'jfpinanm@gmail.com');";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO TELEFONOS " +
                    "VALUES ('48584159A', 971020201);";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO TELEFONOS " +
                    "VALUES ('48584159A', 659010203);";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO TELEFONOS " +
                    "VALUES ('12345678W', 659040401);";
            stmt.executeUpdate(sql);

            sql = "INSERT INTO TELEFONOS " +
                    "VALUES ('87654321Q', 671020406);";
            stmt.executeUpdate(sql);


            /* SELECTS */

            ResultSet rs = stmt.executeQuery( "SELECT * FROM CONTACTOS C, EMAILS E, TELEFONOS T WHERE C.DNI = E.DNI AND C.DNI = T.DNI ORDER BY C.DNI DESC;" );

            String lastDNI = "";

            while (rs.next()) {
                String dni = rs.getString("DNI");
                if (!lastDNI.equals(dni)) {
                    String name = rs.getString("NOMBRE");
                    String dir = rs.getString("DIRECCION");
                    String email = rs.getString("EMAIL");
                    String telf = rs.getString("TELEFONO");

                    System.out.println("DNI = " + dni);
                    System.out.println("NOMBRE = " + name);
                    System.out.println("DIRECCION = " + dir);
                    System.out.println("EMAIL = " + email);
                    System.out.println("TELEFONO = " + telf);
                } else {
                    String email = rs.getString("EMAIL");
                    String telf = rs.getString("TELEFONO");

                    System.out.println("EMAIL = " + email);
                    System.out.println("TELEFONO = " + telf);
                    System.out.println();
                }
                lastDNI = dni;
                System.out.println();
            }

            rs.close();
            stmt.close();
            c.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Table created successfully");
    }
}
