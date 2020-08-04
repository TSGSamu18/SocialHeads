package tsg.socialheads.main;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {

	public void addSocialHeadToConfig(SocialHead head) {
	     try {
		      SocialHeads.getInstance().fileManager.headsConfig.getConfigurationSection(head.getType()).createSection(head.getName());
		      ConfigurationSection s = SocialHeads.getInstance().fileManager.headsConfig.getConfigurationSection(head.getType()).getConfigurationSection(head.getName());
	          s.set("Type", head.getType());
	          s.set("World", head.getLocation().getWorld().getName());
	          s.set("X", head.getLocation().getX());
	          s.set("Y", head.getLocation().getY());
	          s.set("Z", head.getLocation().getZ());
	          s.set("Link", head.getLink());
		  }catch(NullPointerException e) {
			  SocialHeads.getInstance().log.severe("Error when save head to config:" + e.toString());
		  }
	  try {
		  SocialHeads.getInstance().fileManager.headsConfig.save(SocialHeads.getInstance().fileManager.headsFile);
	  } catch (IOException e) {
		  SocialHeads.getInstance().log.severe("Error when saving file:" + e.toString());
	  }
	}
	
	/*
	 * On Enable
	 */
	public void generateHeadType() {
		String type;
		for(String id : SocialHeads.getInstance().getConfig().getStringList("idList")) {
			type = id.split(":")[1];
			try{
              if(!SocialHeads.getInstance().fileManager.headsConfig.isConfigurationSection(type)) {
            	  SocialHeads.getInstance().fileManager.headsConfig.createSection(type);
              }
		   }catch(NullPointerException | IndexOutOfBoundsException nullpointer){
		      SocialHeads.getInstance().log.severe("Error when generating head type: " + id);
		      SocialHeads.getInstance().log.severe("Error:" + nullpointer.toString());
           }
		}
		try {
			SocialHeads.getInstance().fileManager.headsConfig.save(SocialHeads.getInstance().fileManager.headsFile);
		} catch (IOException e) {
			SocialHeads.getInstance().log.severe("Error when save heads file:" + e.toString());
		}
	}
	
	public String genHeadName(String id) {
		StringBuilder sb = new StringBuilder();
		Random rnd = new Random();
		sb.append(getHeadTypeById(id));
		sb.append(rnd.nextInt(10000000));
		return sb.toString();
	}
	
	public SocialHead getHeadByLocation(Location loc) {
  		for(SocialHead head : SocialHeads.getInstance().placedHeadMap) {
  			if(loc.equals(head.getLocation())) {
  				return head;
  			}
  		}
  		return null;
	}
	
	/*
	 * General Utils
	 */
	
	public SocialHead getHeadByName(String name) {
  		for(SocialHead head : SocialHeads.getInstance().placedHeadMap) {
  			if(name .equals(head.getName())) {
  				return head;
  			}
  		}
  		return null;
	}
		
	public String getHeadIdByItemStack(ItemStack is) {
		return SocialHeads.getInstance().api.getItemID(is);
	}
	
	public String getHeadTypeById(String id) {
		for(String i : SocialHeads.getInstance().idList.keySet()) {
			if(i.equals(id)) {
				return SocialHeads.getInstance().idList.get(i);
			}
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean isHead(Player p) {
		Block b = p.getTargetBlock((Set)null, 200);
		if(b != null && b.getType().equals(Material.SKULL)) { 
		    return true;
		}
		return false;
	}
	
	public boolean isInIdList(ItemStack is) {
		if(SocialHeads.getInstance().idList.containsKey(SocialHeads.getInstance().api.getItemID(is))) {
			 return true;
        }
		return false;
	}
	
	public boolean isInPlacedList(Block b) {
		for(SocialHead head : SocialHeads.getInstance().placedHeadMap) {
			if(head.getLocation().equals(b.getLocation())) {
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean isInPlacedList(Player p) {
		Block b = p.getTargetBlock((Set)null, 200);
	  	if(b != null) {
            if(getHeadByLocation(b.getLocation()) != null) {
            	return true;
            }
	  	}
	  	return false;
	}
	
	public void loadIdListFromConfig() {
		if(!SocialHeads.getInstance().getConfig().isList("idList")) {
			SocialHeads.getInstance().log.severe("Config error: idList not found!");
			return;
		}
		SocialHeads.getInstance().idList.clear();
		for(String id : SocialHeads.getInstance().getConfig().getStringList("idList")) {
		   try{
			  SocialHeads.getInstance().idList.put(id.split(":")[0], id.split(":")[1]);
		   }catch(NullPointerException nullpointer){
		      SocialHeads.getInstance().log.severe("Config error: could not found head with id: " + id);
		   }
		}
	}
	
	public void loadPlacedMapFromConfig() {
 	   SocialHeads.getInstance().placedHeadMap.clear();
		try {
	     for(String typeSection : SocialHeads.getInstance().fileManager.headsConfig.getKeys(false)) {
		     for(String value : SocialHeads.getInstance().fileManager.headsConfig.getConfigurationSection(typeSection).getKeys(false)) {
		    	 ConfigurationSection section = SocialHeads.getInstance().fileManager.headsConfig.getConfigurationSection(typeSection).getConfigurationSection(value);
		    	 String type = section.getString("Type");
		       	 Location loc = new Location(Bukkit.getWorld(section.getString("World")), section.getDouble("X"), section.getDouble("Y"), section.getDouble("Z"));;
		         String link = section.getString("Link");
		         SocialHeads.getInstance().placedHeadMap.add(new SocialHead(type, value, loc, link));
		     }
	     }
	   }catch (NullPointerException ex) {
		    SocialHeads.getInstance().log.severe("Error when loading placedmap:" + ex.toString());
	   }
   }
	
	public void onEnable() {
		SocialHeads.getInstance().idList.clear();
		SocialHeads.getInstance().placedHeadMap.clear();
		SocialHeads.getInstance().fileManager.setupFile();
        generateHeadType();
		loadIdListFromConfig();
		loadPlacedMapFromConfig();
	    SocialHeads.getInstance().messages.setupMessages(); 
	}
	
	public void removeSocialHeadFromConfig(Location loc) {
		SocialHead head = getHeadByLocation(loc);	
		SocialHeads.getInstance().fileManager.headsConfig.set(head.getType() + "." + head.getName(), null);
		try {
			SocialHeads.getInstance().fileManager.headsConfig.save(SocialHeads.getInstance().fileManager.headsFile);
		} catch (IOException e) {
			SocialHeads.getInstance().log.severe("Error when deleting head from config:" + e.toString());
		}
	}
	
	public void removeSocialHeadFromMap(Location location) {
		SocialHeads.getInstance().placedHeadMap.remove(getHeadByLocation(location));
	}
	
	public void removeSocialHeadFromMap(String name) {
		SocialHeads.getInstance().placedHeadMap.remove(getHeadByName(name));
	}
	
	public void setLink(SocialHead h, String link) {
		SocialHeads.getInstance().fileManager.headsConfig.getConfigurationSection(h.getType() + "." + h.getName()).set("Link", link);
		try {
			SocialHeads.getInstance().fileManager.headsConfig.save(SocialHeads.getInstance().fileManager.headsFile);
		} catch (IOException e) {
			SocialHeads.getInstance().log.severe("Error while setting link config:" + e.toString());
		}
	}
}
