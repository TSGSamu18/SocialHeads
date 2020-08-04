package tsg.socialheads.main;

import org.bukkit.Location;

public class SocialHead {
	
	String type;
	String name;
	Location location;
	String link;
	
	public SocialHead(String type, String name, Location location, String link) {
       this.type = type;
       this.name = name;
       this.location = location;
       this.link = link;
	}
	
	public String getLink() {
		return this.link;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setLink(String l) {
		this.link = l;
	}
	
	public void setLocation(Location loc) {
		this.location = loc;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(String t) {
		this.type = t;
	}
}
