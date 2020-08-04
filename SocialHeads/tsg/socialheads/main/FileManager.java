package tsg.socialheads.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class FileManager {

	public File messagesFile = new File(SocialHeads.getInstance().getDataFolder(), "messages.yml");
	public File headsFile = new File(SocialHeads.getInstance().getDataFolder(), "heads.yml");

	
	YamlConfiguration messagesConfig;
	YamlConfiguration headsConfig;
		
	
	public void createHeadsFile() {
		if(!headsFile.exists()) {
			try {
				headsFile.createNewFile();
			} catch (IOException e) {
				SocialHeads.getInstance().log.severe("Error when creating heads file:" + e.toString());
			}
		}
		headsConfig = YamlConfiguration.loadConfiguration(headsFile);
	}
	
	public void createMessagesFile() {
		if(!messagesFile.exists()) {
			SocialHeads.instance.saveResource("messages.yml", false);
		}	
		messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
	}
	
	public void setupConfig() {
	  if(!SocialHeads.getInstance().getConfig().isList("idList")) {
		  SocialHeads.getInstance().getConfig().createSection("idList");
	  }	
	  SocialHeads.getInstance().saveConfig();
	}
	
	public void setupFile() {
		setupConfig();
		createMessagesFile();
		createHeadsFile();
	}
}
