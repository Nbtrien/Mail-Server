package core;

import java.io.Serializable;

public class Email implements Serializable{
	private int id;
	private String title;
	private User userFrom;
	private String Content;
	private User userTo;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public User getUserFrom() {
		return userFrom;
	}
	public void setUserFrom(User userFrom) {
		this.userFrom = userFrom;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	
	public User getUserTo() {
		return userTo;
	}
	public void setUserTo(User userTo) {
		this.userTo = userTo;
	}
	@Override
	public String toString() {
		return "Email [id=" + id + ", title=" + title + ", userFrom=" + userFrom + ", Content=" + Content + ", userTo="
				+ userTo + "]";
	}

	
	
}
