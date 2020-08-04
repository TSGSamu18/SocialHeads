package tsg.socialheads.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import me.arcaniax.hdb.api.HeadDatabaseAPI;

public class SocialHeads extends JavaPlugin {
	
	public static SocialHeads instance;
	public static SocialHeads getInstance() {
		return SocialHeads.instance;
	}
	
	public Logger log = getLogger();
		
	HeadDatabaseAPI api;
	public FileManager fileManager;
	public Utils utils;
	
	public Messages messages;
	//@param ID, Type
	HashMap<String, String> idList;
	
	ArrayList<SocialHead> placedHeadMap;
	
	public String mainCommand;
	
	public void hookHD() {
		if(Bukkit.getPluginManager().getPlugin("HeadDatabase") == null) {
			log.severe("-----------------------");
			log.severe("HeadDatabase not found!");
			log.severe("Plugin will be disabled");
			log.severe("-----------------------");
			this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.api = new HeadDatabaseAPI();
		log.info("----------------------------------");
		log.info("§e[§a+§e]§aHooked in HeadDatabase!");
		log.info("----------------------------------");
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	@Override
	public void onEnable() {
		setup();
		hookHD();
		utils.onEnable();
		super.onEnable();
	}
	
	public void registerCommands(String fallback, Command command) {
	   try {
		  Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
          bukkitCommandMap.setAccessible(true);
          CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
          commandMap.register(fallback, command);
       }catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
         log.severe("Error while registering commands:" + e.toString());
       }
	}
	
	public void setup() {
		instance = this;
		saveDefaultConfig();
		idList = new HashMap<String, String>();
		placedHeadMap = new ArrayList<SocialHead>();
		fileManager = new FileManager();
		utils = new Utils();
		messages = new Messages();
		try {
			mainCommand = this.getConfig().getConfigurationSection("command").getString("Main");
		}catch(NullPointerException e) {
			log.severe("Error while was registering main command:" + e.toString());
		}
		registerCommands(mainCommand, new CommandListener(mainCommand));
		getServer().getPluginManager().registerEvents(new PlaceBreakInteractListener(), this);
	}
}
