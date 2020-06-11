import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Connection c;
    private static Statement st;
    private static ResultSet rs;
    private static Scanner s = new Scanner(System.in);

    public static void main(String args[]) throws Exception {
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:E:/SQLite Databases/agenda.sqlite");

        boolean keep = true;
        // TABLES
        createTables();
        // INSERTS
        insertData();

        while (keep) {
            System.out.println("Que acción desea realizar? (consultar, insertar, modificar o eliminar) ");
            String action = s.next();

            if (action.equalsIgnoreCase("consultar")) {
                System.out.println("Desea listar todos los contactos o consultar uno en concreto? (todos, concreto) ");
                String list = s.next();
                if (list.equalsIgnoreCase("todos"))
                    selectAll();
                else if (list.equalsIgnoreCase("concreto")) {
                    System.out.println("Introduce el DNI del contacto: ");
                    selectOne(s.next());
                }
            }
            else if (action.equalsIgnoreCase("insertar"))
                insert();
            else if (action.equalsIgnoreCase("modificar")) {
                System.out.println("DNI del contacto: ");
                String DNI = s.next();
                selectOne(DNI);
                modify(DNI);
            }
            else if (action.equalsIgnoreCase("eliminar")) {
                System.out.println("DNI del contacto: ");
                delete(s.next());
            }
            else
                System.out.println("Acción incorrecta");

            System.out.println("Desea continuar? (si, no)");
            if (!s.next().equalsIgnoreCase("si")) {
                keep = false;
                System.out.println("Adiós.");
            }
        }

        rs.close();
        st.close();
        c.close();
    }

    private static void createTables() throws Exception {
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
                " Telefono INT NOT NULL, " +
                " FOREIGN KEY(DNI) REFERENCES CONTACTOS (DNI) ON UPDATE CASCADE ON DELETE CASCADE)";
        st.execute(sql);
    }

    private static void insertData() throws Exception {
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

    private static void selectAll() throws Exception {
        rs = st.executeQuery("SELECT * FROM CONTACTOS;");

        System.out.println("+----------------+------------------------------+---------------------------------+");
        while (rs.next()) {
            String dni = rs.getString("DNI");
            String name = rs.getString("Nombre");
            String dir = rs.getString("Direccion");

            System.out.printf("| DNI: %9s | Nombre: %-20s | Direccion: %-20s |\n",
                    dni, name, dir);
        }
        System.out.println("+----------------+------------------------------+---------------------------------+\n");
    }

    private static void selectOne(String DNI) throws Exception {
        rs = st.executeQuery("SELECT C.DNI, C.Nombre, C.Direccion, E.Email, T.Telefono FROM CONTACTOS C" +
                " LEFT JOIN EMAILS E ON C.DNI = E.DNI" +
                " LEFT JOIN TELEFONOS T ON C.DNI = T.DNI" +
                " WHERE C.DNI = '" + DNI + "';");

        String dni = "";
        String name = "";
        String dir = "";
        List<String> emails = new ArrayList<>();
        List<Integer> telefonos = new ArrayList<>();

        while (rs.next()) {
            dni = rs.getString("DNI");
            name = rs.getString("Nombre");
            dir = rs.getString("Direccion");
            if (!emails.contains(rs.getString("Email")))
                emails.add(rs.getString("Email"));
            if (!telefonos.contains(rs.getInt("Telefono")))
                telefonos.add(rs.getInt("Telefono"));
        }
        System.out.printf("+----------------+------------------------------+---------------------------------+-----------------------------------------------------------+--------------------------------------------------------------+\n" +
                        "| DNI: %9s | Nombre: %-20s | Direccion: %-20s | Email: %-50s | Telefono: %-50s |" +
                        "\n+----------------+------------------------------+---------------------------------+-----------------------------------------------------------+--------------------------------------------------------------+\n",
                dni, name, dir, emails, telefonos);
    }

    private static void insert() throws Exception {
        System.out.println("DNI del contacto: ");
        String dni = s.next();
        System.out.println("Nombre del contacto: ");
        String nombre = s.next();
        System.out.println("Direccion del contacto: ");
        String dir = s.next();

        st.execute("INSERT INTO CONTACTOS VALUES ('" + dni + "', '" + nombre + "', '" + dir + "');");

        System.out.println("Cúantos emails quieres añadir? ");
        int nEmails = s.nextInt();

        if (nEmails > 0) {
            for (int i = 0; i < nEmails; i++) {
                System.out.println("Email: ");
                String email = s.next();
                st.execute("INSERT INTO EMAILS VALUES ('" + dni + "', '" + email + "');");
            }
        }
        else if (nEmails == 0)
            System.out.println("Emails no añadidos.");
        else
            System.out.println("Número de emails no aceptado.");

        System.out.println("Cúantos teléfonos quieres añadir? ");
        int nTelf = s.nextInt();

        if (nTelf > 0) {
            for (int i = 0; i < nTelf; i++) {
                System.out.println("Teléfono: ");
                int telf = s.nextInt();
                st.execute("INSERT INTO TELEFONOS VALUES ('" + dni + "', '" + telf + "');");
            }
        }
        else if (nTelf == 0)
            System.out.println("Teléfonos no añadidos.");
        else
            System.out.println("Número de teléfonos no aceptado.");

        System.out.println("Contacto añadido correctamente.");

    }

    private static void modify(String DNI) throws Exception {
        System.out.println("Nuevo nombre: ");
        String nombre = s.next();
        System.out.println("Nueva direccion: ");
        String direccion = s.next();

        st.executeUpdate("UPDATE CONTACTOS SET Nombre = '" + nombre + "', Direccion = '" + direccion + "' WHERE DNI = '" + DNI + "';");
        st.execute("DELETE FROM EMAILS WHERE DNI = '" + DNI + "';");
        st.execute("DELETE FROM TELEFONOS WHERE DNI = '" + DNI + "';");

        System.out.println("Cúantos emails quieres añadir? ");
        int nEmails = s.nextInt();

        if (nEmails > 0) {
            for (int i = 0; i < nEmails; i++) {
                System.out.println("Email: ");
                String email = s.next();
                st.execute("INSERT INTO EMAILS VALUES ('" + DNI + "', '" + email + "');");
            }
        }
        else if (nEmails == 0)
            System.out.println("Emails no añadidos.");
        else
            System.out.println("Número de emails no aceptado.");

        System.out.println("Cúantos teléfonos quieres añadir? ");
        int nTelf = s.nextInt();

        if (nTelf > 0) {
            for (int i = 0; i < nTelf; i++) {
                System.out.println("Teléfono: ");
                int telf = s.nextInt();
                st.execute("INSERT INTO TELEFONOS VALUES ('" + DNI + "', '" + telf + "');");
            }
        }
        else if (nTelf == 0)
            System.out.println("Teléfonos no añadidos.");
        else
            System.out.println("Número de teléfonos no aceptado.");
    }

    private static void delete(String DNI) throws Exception {
        System.out.println("Estás seguro de eliminar este contacto? Esta acción será irreversible (si, no)");
        String conf = s.next();

        if (conf.equalsIgnoreCase("si")) {
            st.execute("DELETE FROM CONTACTOS WHERE DNI = '" + DNI + "';");
            st.execute("DELETE FROM EMAILS WHERE DNI = '" + DNI + "';");
            st.execute("DELETE FROM TELEFONOS WHERE DNI = '" + DNI + "';");
            System.out.println("Contacto eliminado correctamente!");
        }
        else if (conf.equalsIgnoreCase("no"))
            System.out.println("Contacto no eliminado!");
        else
            System.out.println("Confirmación errónea, contacto no eliminado");
    }
}
