package controllers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import models.Story;
import play.Logger;
import play.data.Form;
import play.db.DB;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

	@Transactional(readOnly=true)
	public Result index() {
		return ok(views.html.index.render(getStateCount(), getYearCount(), getBallaratCount(), null));
	}

	@Transactional(readOnly=true)
	public Result mapByState(String state) {
		return result(getStoriesByState(state));
	}

	@Transactional(readOnly=true)
	public Result mapByYear(String year) {
		return result(getStoriesByYear(year));
	}

	@Transactional(readOnly=true)
	public Result storiesByYear(String year) {
		List<Story> resultList = getStoriesByYear(year);
		Logger.info(year + ":" + String.valueOf(resultList.size()));

		return ok(views.html.stories.render(resultList));
	}

	@Transactional(readOnly=true)
	public Result storiesByState(String state) {
		List<Story> resultList = getStoriesByState(state);
		Logger.info(state + ":" + String.valueOf(resultList.size()));

		return ok(views.html.stories.render(resultList));
	}
	
	@Transactional(readOnly=true)
	public Result showStory(Long id){
		Story story = JPA.em().find(Story.class, id);
		Logger.info("Story :"+ story.getTitle());
		
		return ok(Json.toJson(story));
	}
	
	@Transactional(readOnly=true)
	public Result mapByBallaratPhotograph(){
		List<Story> resultList = JPA.em().createQuery("SELECT s FROM Story s where mediaRSS='BPHOTO'").getResultList();
		Logger.info("Ballarat Historic Photographs:" + String.valueOf(resultList.size()));

		return result(resultList);
	}
	
	@Transactional(readOnly=true)
	public Result search(){
		String key = Form.form().bindFromRequest().get("key");
		if(key.isEmpty()) key = "NOKEYINSEARCH";
		List<Story> resultList = JPA.em().createQuery("SELECT s FROM Story s where lower(title) like '%"+key+"%'").getResultList();
		Logger.info("Search :" + String.valueOf(resultList.size()));

		return result(resultList);
	}

	private Result result(List<Story> showList) {
		return ok(views.html.index.render(getStateCount(), getYearCount(), getBallaratCount(), showList));
	}
	
	@Transactional(readOnly=true)
	public Result apiByState(String state) {
		return ok(Json.toJson(getStoriesByState(state)));
	}

	@Transactional(readOnly=true)
	public Result apiByYear(String year) {
		return ok(Json.toJson(getStoriesByYear(year)));
	}
	
	@Transactional(readOnly=true)
	public Result apiByBallaratPhotograph(){
		List<Story> resultList = JPA.em().createQuery("SELECT s FROM Story s where mediaRSS='BPHOTO'").getResultList();
		return ok(Json.toJson(resultList));
	}
	
	@Transactional(readOnly=true)
	public Result apiSearch(String key){
		if(key.isEmpty()) key = "NOKEYINSEARCH";
		List<Story> resultList = JPA.em().createQuery("SELECT s FROM Story s where lower(title) like '%"+key+"%'").getResultList();
		return ok(Json.toJson(resultList));
	}
	

	private List<Story> getStoriesByYear(String year) {
		return JPA.em().createQuery("SELECT s FROM Story s where s.localDate Like '" + year + "%'").getResultList();
	}

	private List<Story> getStoriesByState(String state) {
		return JPA.em().createQuery("SELECT s FROM Story s where s.state = '" + state + "'").getResultList();
	}

	private Map<String, Integer> getStateCount() {
		return queryCount("SELECT * from countstate order by state asc;");
	}

	private Map<String, Integer> getYearCount() {
		return queryCount("SELECT * from countyear order by year asc;");
	}
	
	private Map<String, Integer> getBallaratCount() {
		return queryCount("SELECT * from countballarat;");
	}
	
	private Map<String, Integer> queryCount(String query){
		Map<String, Integer> stateCount = new TreeMap<>();
		try {
			Connection conn = DB.getConnection();
			Statement stat = conn.createStatement();
			ResultSet executeQuery = stat.executeQuery(query);
			while (executeQuery.next()) {
				stateCount.put(executeQuery.getString(1), executeQuery.getInt(2));
			}
			stat.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stateCount;
	}

}