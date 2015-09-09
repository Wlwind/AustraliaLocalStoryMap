package io.ye.govhack.alsm.dbatools;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

public class ImportTools {
	public static void main(String[] args)
			throws JsonParseException, MalformedURLException, IOException, ClassNotFoundException, SQLException, JSONException {
		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection("jdbc:h2:~/stories2009-2014", "sa", "");
		// add application code here
		Statement statement = conn.createStatement();
		statement.execute("drop table story IF EXISTS;");
		statement.execute("create table story(" + "id int primary key," + " title varchar(255)," + " url varchar(255),"
				+ " localDate date," + " primaryImage varchar(255)," + " primaryImageCaption TEXT,"
				+ " primaryImageRightsInformation TEXT," + " subjects TEXT," + " station varchar(255),"
				+ " state varchar(255)," + " place varchar(255)," + " keywords TEXT," + " latitude varchar(255),"
				+ " longitude varchar(255)," + " mediaRSS varchar(255))");
		statement.close();

		String fileJson = "Localphotostories2009-2014-JSON.json";
		JsonFactory factory = new JsonFactory();
		JsonParser parser = factory.createParser(new File(fileJson));

		int idx = 1;
		while (parser.nextToken() != JsonToken.END_ARRAY) {
			while (parser.nextToken() != JsonToken.END_OBJECT) {
				if (parser.getCurrentToken() == JsonToken.START_OBJECT)
					continue;

				parser.nextToken();
				String title = parser.getText();

				parser.nextToken();
				parser.nextToken();
				String url = parser.getText();

				parser.nextToken();
				parser.nextToken();
				String date = parser.getText();
				DateTimeFormatter pattern = DateTimeFormatter.ofPattern("d/M/yyyy");
				LocalDate localDate = LocalDate.parse(date, pattern);

				parser.nextToken();
				parser.nextToken();
				String primaryImage = parser.getText();

				parser.nextToken();
				parser.nextToken();
				String primaryImageCaption = parser.getText();

				parser.nextToken();
				parser.nextToken();
				String primaryImageRightsInformation = parser.getText();

				parser.nextToken();
				parser.nextToken();
				String subjects = parser.getText();

				parser.nextToken();
				parser.nextToken();
				String station = parser.getText();

				parser.nextToken();
				parser.nextToken();
				String state = parser.getText();

				parser.nextToken();
				parser.nextToken();
				String place = parser.getText();

				parser.nextToken();
				parser.nextToken();
				String keywords = parser.getText();

				parser.nextToken();
				parser.nextToken();
				String latitude = parser.getText();

				parser.nextToken();
				parser.nextToken();
				String longitude = parser.getText();
				
				parser.nextToken();
				parser.nextToken();
				String mediaRSS = parser.getText();
				
				if (latitude.isEmpty() && longitude.isEmpty())
					continue;
				
				Statement cooridinate = conn.createStatement();
				ResultSet resultSet = cooridinate.executeQuery("SELECT id From story where latitude = '"+latitude+"' and longitude = '"+longitude+"'");
				
				while(resultSet.next()){
					int id = resultSet.getInt(1);
					
					Double tempLatitude = Double.valueOf(latitude);
					tempLatitude += Math.random()/100;
					
					Double tempLongitude = Double.valueOf(longitude);
					tempLongitude += Math.random()/100;
					
					Statement updatestat = conn.createStatement();
					updatestat.executeUpdate("UPDATE story set latitude = '"+tempLatitude+"', longitude = '"+tempLongitude+"' where id = "+id);
					updatestat.close();
				}
				cooridinate.close();
				
				PreparedStatement stm = conn.prepareStatement("insert into story(id," + " title," + " url,"
						+ " localDate," + " primaryImage," + " primaryImageCaption," + " primaryImageRightsInformation,"
						+ " subjects," + " station," + " state," + " place," + " keywords," + " latitude,"
						+ " longitude," + " mediaRSS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				stm.setInt(1, idx++);
				stm.setString(2, title);
				stm.setString(3, url);
				java.util.Date from = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
				stm.setDate(4, new Date(from.getTime()));
				stm.setString(5, primaryImage);
				stm.setString(6, primaryImageCaption);
				stm.setString(7, primaryImageRightsInformation);
				stm.setString(8, subjects);
				stm.setString(9, station);
				stm.setString(10, state);
				stm.setString(11, place);
				stm.setString(12, keywords);
				stm.setString(13, latitude);
				stm.setString(14, longitude);
				stm.setString(15, mediaRSS);

				
				stm.execute();
				stm.close();
			}
		}
		
		
		// Ballarat photographs data
		
		fileJson = "BallaratPhotographics.json";
		factory = new JsonFactory();
		parser = factory.createParser(new File(fileJson));
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JsonOrgModule());
		JSONArray value = mapper.readValue(parser, JSONArray.class);
		for(int i =0;i<value.length();i++){
			String title = value.getJSONObject(i).getString("FIELD1");
			
			PreparedStatement stm = conn.prepareStatement("insert into story(id," + " title," + " url,"
					+ " localDate," + " primaryImage," + " primaryImageCaption," + " primaryImageRightsInformation,"
					+ " subjects," + " station," + " state," + " place," + " keywords," + " latitude,"
					+ " longitude," + " mediaRSS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			stm.setInt(1, idx++);
			stm.setString(2, value.getJSONObject(i).getString("FIELD1"));
			stm.setString(3, value.getJSONObject(i).getString("FIELD4"));
			java.util.Date from = Date.valueOf(value.getJSONObject(i).getString("FIELD5")+"-01-01");
			stm.setDate(4, new Date(from.getTime()));
			stm.setString(5, value.getJSONObject(i).getString("FIELD3"));
			stm.setString(6, value.getJSONObject(i).getString("FIELD2"));
			stm.setString(7, "");
			stm.setString(8, "");
			stm.setString(9, "");
			stm.setString(10, "VIC");
			stm.setString(11, "Ballarat");
			stm.setString(12, value.getJSONObject(i).getString("FIELD1"));
			stm.setString(13, value.getJSONObject(i).getString("FIELD6"));
			stm.setString(14, value.getJSONObject(i).getString("FIELD7"));
			stm.setString(15, "BPHOTO");

			stm.execute();
			stm.close();
		}
		
		conn.close();
	}
}
