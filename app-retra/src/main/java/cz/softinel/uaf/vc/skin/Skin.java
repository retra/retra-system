package cz.softinel.uaf.vc.skin;

public class Skin {

	private String name;
	private String title;
	
	public Skin(String name, String title) {
		this.name = name;
		this.title = title;
	}

	// Getter and setter methods ...
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
