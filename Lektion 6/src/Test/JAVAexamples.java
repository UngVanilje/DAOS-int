package Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JAVAexamples {

	/**
	 * @param args
	 */
	static Connection minConnection;
	static Statement stmt;
	static BufferedReader inLine;
	
	public static void selectudenparm() {
	try {
		// Laver sql-sætning og får den udført
		String sql = "select init, aarstal, plac from placering";
		System.out.println("SQL-streng er "+sql);
		ResultSet res=stmt.executeQuery(sql);
		// gennemløber svaret
		while (res.next()) {
			String init;
			int aarstal;
			int plac;
			init = res.getString("init");
			aarstal = res.getInt(2);
			plac = res.getInt(3);
			if (plac > 0)
				System.out.println("Initialer: " + init + "    " + "Placering: " + plac + "   		" + "Årstal: " +  aarstal);
			else
			{
				System.out.println("Initialer: " + init + "    " + "Placering: UDGÅET" + "   	" + "Årstal: " +  aarstal);
			}
		}
		// pæn lukning
 		if (!minConnection.isClosed()) minConnection.close();
		}
		catch (Exception e) {
			System.out.println("fejl:  "+e.getMessage());
		}
	}
	
	public static void selectmedparm() {
	try {
		// Indlæser søgestreng
		System.out.println("Indtast søgestreng");
		String inString = inLine.readLine();
		// Laver sql-sætning og får den udført
		String sql = "select init, rytternavn from rytter where init like '" + inString + "%'";
		System.out.println("SQL-streng er "+ sql);
		ResultSet res=stmt.executeQuery(sql);
		//gennemløber svaret
		while (res.next()) {
			System.out.println("Initialer:" + res.getString(1) + "    " + "Rytternavn:" + res.getString(2));
		}
		// pæn lukning
 		if (!minConnection.isClosed()) minConnection.close();
		}
		catch (Exception e) {
			System.out.println("fejl:  "+e.getMessage());
		}
	}
	
	public static void insertmedstringRytter() {
		try {
			// indlæsning
			System.out.println("Vi vil nu oprette en ny rytter");
			System.out.println("Indtast rytter initialer (rytter skal være oprettet på forhånd");
			String initnavnstr=inLine.readLine();
			System.out.println("Indtast rytter rytternavn (rytter skal være oprettet på forhånd");
			String rytternavnstr=inLine.readLine();

		
			// sender insert'en til db-serveren
			String sql = "insert into rytter values ('" + initnavnstr + "','" + rytternavnstr + "')";
			System.out.println("SQL-streng er "+ sql);
			stmt.execute(sql);
			// pænt svar til brugeren
			System.out.println("Rytteren er nu registreret");
			if (!minConnection.isClosed()) minConnection.close();
		}
		catch (SQLException e) {
			        switch (e.getErrorCode())
			        // fejl-kode 547 svarer til en foreign key fejl
			        { case 547 : {if (e.getMessage().contains("initrytterforeign"))
										System.out.println("rytter initialer er ikke oprettet");
			        			  else
			        			  if (e.getMessage().contains("rytternavnforeign"))
										System.out.println("rytternavn er ikke oprettet");
			        			  else
			        				    System.out.println("ukendt fremmednøglefejl");
								  break;
			        			}
			        // fejl-kode 2627 svarer til primary key fejl
			          case 2627: {System.out.println("Den pågældende rytter er allerede oprettet");
			          	          break;
			         			 }
			          default: System.out.println("fejlSQL:  "+e.getMessage());
				};
		}
		catch (Exception e) {
			System.out.println("fejl:  "+e.getMessage());
		}
	};
	public static void insertmedstringPlacering() {
		try {
			// indlæsning
			System.out.println("Vi vil nu oprette en ny placering i året 2021");
			System.out.println("Indtast årstal (placering skal være oprettet på forhånd");
			String aarstalnavnstr=inLine.readLine();
			System.out.println("Indtast rytter initialer (placering skal være oprettet på forhånd");
			String initnavnstr=inLine.readLine();
			System.out.println("Indtast placering (placering skal være oprettet på forhånd");
			String placnavnstr=inLine.readLine();
//			System.out.println("Indtast firmanr (placering skal være oprettet på forhånd");
//			String firmastr=inLine.readLine();

			// sender insert'en til db-serveren
			String sql = "insert into placering values ('" + aarstalnavnstr + "','" + initnavnstr + "','" + placnavnstr + "')";
			System.out.println("SQL-streng er "+ sql);
			stmt.execute(sql);
			// pænt svar til brugeren
			System.out.println("Placering er nu registreret");
			if (!minConnection.isClosed()) minConnection.close();
		}
		catch (SQLException e) {
			switch (e.getErrorCode())
			// fejl-kode 547 svarer til en foreign key fejl
			{ case 547 : {if (e.getMessage().contains("initforeign"))
				System.out.println("rytter initialer er ikke oprettet");
			else
			if (e.getMessage().contains("placforeign"))
				System.out.println("placering er ikke oprettet");
			else
			if (e.getMessage().contains("aarstalforeign"))
				System.out.println("aarstal er ikke oprettet");
			else
				System.out.println("ukendt fremmednøglefejl");
				break;
			}
			// fejl-kode 2627 svarer til primary key fejl
				case 2627: {System.out.println("den pågældende rytter er allerede oprettet");
					break;
				}
				default: System.out.println("fejlSQL:  "+e.getMessage());
			};
		}
		catch (Exception e) {
			System.out.println("fejl:  "+e.getMessage());
		}
	};
	
	public static void insertprepared() {
		try {
			// indl�sning
			System.out.println("Indtast aarstal (aarstal skal være oprettet på forhånd");
			String aarstalstr=inLine.readLine();
			System.out.println("Indtast initialer (rytteren skal være oprettet på forhånd");
			String initstr=inLine.readLine();
			System.out.println("Indtast placering (rytteren skal være oprettet på forhånd");
			String placstr=inLine.readLine();
			// Anvendelse af prepared statement
			String sql = "insert into placering values (?,?,?)";
			PreparedStatement prestmt = minConnection.prepareStatement(sql);
			prestmt.clearParameters();
			prestmt.setString(1,"2021");
			prestmt.setString(2,initstr);
			prestmt.setInt(3,Integer.parseInt(placstr));
			prestmt.setNull(3, 0);

			// Udf�rer s�tningen
			prestmt.execute();
			// p�nt svar til brugeren
			System.out.println("Placering er nu registreret");
			if (!minConnection.isClosed()) minConnection.close();
		}
		catch (SQLException e) {
			switch (e.getErrorCode())
			// fejl-kode 547 svarer til en foreign key fejl
			{ case 547 : {if (e.getMessage().contains("initforeign"))
				System.out.println("rytter initialer er ikke oprettet");
			else
			if (e.getMessage().contains("placforeign"))
				System.out.println("placering er ikke oprettet");
			else
			if (e.getMessage().contains("aarstalforeign"))
				System.out.println("aarstal er ikke oprettet");
			else
				System.out.println("ukendt fremmednøglefejl");
				break;
			}
			// fejl-kode 2627 svarer til primary key fejl
				case 2627: {System.out.println("den pågældende rytter er allerede oprettet");
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
		// TODO Auto-generated method stub
		try {
			inLine = new BufferedReader(new InputStreamReader(System.in));
			//generel opsætning
			//via native driver
			String server="localhost\\SQLEXPRESS"; //virker måske hos dig
			                                       //virker det ikke - prøv kun med localhost
			String dbnavn="cykeldb";            //virker måske hos dig
			String login="sa";                     //skal ikke ændres
			String password="Emil1996";            //skal ændres
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			minConnection = DriverManager.getConnection("jdbc:sqlserver://"+server+";databaseName="+dbnavn+
					";user=" + login + ";password=" + password + ";");
			//minConnection = DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=eksempeldb;user=sa;password=Emil1996;");
			stmt = minConnection.createStatement();
			//Indlæsning og kald af den rigtige metode
			System.out.println("Indtast  "); 
			System.out.println("s for select uden parameter  ");
			System.out.println("sp for select med parameter  ");
			System.out.println("ip for insert med strengmanipulation placering");
			System.out.println("ir for insert med strengmanipulation rytter");
			System.out.println("ps for insert med prepared statement ");
			String in=inLine.readLine();
			switch (in)
			{case "s"  : {selectudenparm();break;}
			 case "sp" : {selectmedparm();break;}
			 case "ip"  : {insertmedstringPlacering();break;}
				case "ir" : {insertmedstringRytter();break;}
			 case "ps"  : {insertprepared();break;}
			default : System.out.println("ukendt indtastning"); 
			} 
		}
		catch (Exception e) {
			System.out.println("fejl:  "+e.getMessage());
		}
	}



}

