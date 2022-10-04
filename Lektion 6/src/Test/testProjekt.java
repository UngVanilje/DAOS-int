package Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class testProjekt {
    /**
     * @param args
     */
    static Connection minConnection;
    static Statement stmt;
    static BufferedReader inLine;

    public static void insertmedstring() {
        try {
            // indlæsning
            System.out.println("Vi vil nu oprette en ny Bedømmelse");
            System.out.println("Indtast Forsøgsid (Skal være større end den sidste)");
            String forsoegsId=inLine.readLine();
            System.out.println("Indtast Karakter (Skal være lovlig karakter)");
            String karakter=inLine.readLine();
            System.out.println("Indtast Status");
            String status=inLine.readLine();
            System.out.println("Indtast Termin");
            String termin=inLine.readLine();
            System.out.println("Indtast Student ID (Skal være oprettet)");
            String studentID=inLine.readLine();
            System.out.println("Indtast Fag");
            String Enavn=inLine.readLine();

            // sender insert'en til db-serveren
            String sql = "insert into eksamensForsøg values (" + forsoegsId +", " + karakter + ",'" + status + "', " + " '" + termin +
                    "', " + studentID + ", '" + Enavn + "'" +")";
            System.out.println("SQL-streng er "+ sql);
            stmt.execute(sql);
            // pænt svar til brugeren
            System.out.println("Bedømmelse er nu registreret");
            if (!minConnection.isClosed()) minConnection.close();
        }
        catch (SQLException e) {
            switch (e.getErrorCode())
            // fejl-kode 547 svarer til en foreign key fejl
            { case 547 : {if (e.getMessage().contains("studentID"))
                System.out.println("StudentId er ikke oprettet");
            else
                System.out.println("ukendt fremmednøglefejl");
                break;
            }
            // fejl-kode 2627 svarer til primary key fejl
                case 2627: {System.out.println("den pågældende Bedømmelse er oprettet");
                    break;
                }
                default: System.out.println("fejlSQL:  "+e.getMessage());
            };
        }
        catch (Exception e) {
            System.out.println("fejl:  "+e.getMessage());
        }
    };

    public static void main(String[] args) {
        try {
            inLine = new BufferedReader(new InputStreamReader(System.in));
            //generel opsætning
            //via native driver
            String server="localhost\\SQLEXPRESS"; //virker måske hos dig
            //virker det ikke - prøv kun med localhost
            String dbnavn="projektdb";            //virker måske hos dig
            String login="sa";                     //skal ikke ændres
            String password="Emil1996";            //skal ændres
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            minConnection = DriverManager.getConnection("jdbc:sqlserver://"+server+";databaseName="+dbnavn+
                    ";user=" + login + ";password=" + password + ";");
            //minConnection = DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=eksempeldb;user=sa;password=torben07;");
            stmt = minConnection.createStatement();
            //Indlæsning og kald af den rigtige metode
            System.out.println("Indtast  ");
            System.out.println("s for at bedømme elev ");
            String in=inLine.readLine();
            switch (in)
            {case "s"  : {insertmedstring();break;}
                default : System.out.println("ukendt indtastning");
            }
        }
        catch (Exception e) {
            System.out.println("fejl:  "+e.getMessage());
        }
    }
}