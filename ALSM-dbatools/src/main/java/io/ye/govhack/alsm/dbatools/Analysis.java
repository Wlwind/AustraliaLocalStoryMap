package io.ye.govhack.alsm.dbatools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Analysis {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection("jdbc:h2:~/stories2009-2014", "sa", "");

		// Count stories by state
		Statement stat = conn.createStatement();
		stat.execute("drop table countstate IF EXISTS;");
		stat.execute("create table countstate(state varchar(255), stories int)");

		ResultSet resultSet = stat.executeQuery("SELECT state, count(state) FROM story group by state;");

		while (resultSet.next()) {
			String state = resultSet.getString("state");
			int count = resultSet.getInt(2);
			System.out.println(state + "  :  " + count);

			Statement statement = conn.createStatement();
			statement.execute("INSERT INTO countstate values('" + state + "','" + count + "')");
			statement.close();
		}
		stat.close();

		// Count stories by year
		stat = conn.createStatement();
		stat.execute("drop table countyear IF EXISTS;");
		stat.execute("create table countyear(year varchar(255), stories int)");

		resultSet = stat.executeQuery(
				"SELECT substr(localDate, 0, 4), count(localDate) FROM story where substr(localDate, 0, 4) > 2000 group by substr(localDate, 0, 4);");

		while (resultSet.next()) {
			String year = resultSet.getString(1);
			int count = resultSet.getInt(2);
			System.out.println(year + "  :  " + count);

			Statement statement = conn.createStatement();
			statement.execute("INSERT INTO countyear values('" + year + "','" + count + "')");
			statement.close();
		}
		stat.close();
		
		// Count Ballarat
		stat = conn.createStatement();
		stat.execute("drop table countballarat IF EXISTS;");
		stat.execute("create table countballarat(category varchar(255), stories int)");

		resultSet = stat.executeQuery(
				"SELECT count(mediaRSS) FROM story where mediaRSS='BPHOTO';");

		while (resultSet.next()) {
			int count = resultSet.getInt(1);
			System.out.println("Ballarat Historic Lanscape Photographs:  " + count);

			Statement statement = conn.createStatement();
			statement.execute("INSERT INTO countballarat values('Historic Lanscape Photographs','" + count + "')");
			statement.close();
		}
		stat.close();
		

		conn.close();
	}
}
