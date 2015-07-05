package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "story")
public class Story {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String url;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date localDate;
	private String primaryImage;
	private String primaryImageCaption;
	private String primaryImageRightsInformation;
	private String subjects;
	private String station;
	private String state;
	private String place;
	private String keywords;
	private String latitude;
	private String longitude;
	private String mediaRSS;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getLocalDate() {
		return localDate;
	}

	public void setLocalDate(Date localDate) {
		this.localDate = localDate;
	}

	public String getPrimaryImage() {
		return primaryImage;
	}

	public void setPrimaryImage(String primaryImage) {
		this.primaryImage = primaryImage;
	}

	public String getPrimaryImageCaption() {
		return primaryImageCaption;
	}

	public void setPrimaryImageCaption(String primaryImageCaption) {
		this.primaryImageCaption = primaryImageCaption;
	}

	public String getPrimaryImageRightsInformation() {
		return primaryImageRightsInformation;
	}

	public void setPrimaryImageRightsInformation(String primaryImageRightsInformation) {
		this.primaryImageRightsInformation = primaryImageRightsInformation;
	}

	public String getSubjects() {
		return subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}

	public String getStation() {
		return station;
	}

	public void setStation(String station) {
		this.station = station;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getMediaRSS() {
		return mediaRSS;
	}

	public void setMediaRSS(String mediaRSS) {
		this.mediaRSS = mediaRSS;
	}

}
