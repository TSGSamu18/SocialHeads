package tsg.socialheads.main;

import java.util.List; 

import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.error.YAMLException;

public class Messages {

	YamlConfiguration messages;

	String prefix;

	String onHeadPlace;
	String onHeadBreakWithoutShift;
	String onHeadBreak;
	String permissionDenied;
	String configReloaded;
	String listCommandHeader;
	String listCommandFormat;
	String idlistCommandHeader;
	String idlistCommandFormat;
	String linkSetted;
	String linkRemoved;
	String getLink;
	String blockNotHead;
	String headNotInList;
	String notplayer;
	List<String> helpMessages;

	public void setupMessages() {
		messages = SocialHeads.instance.fileManager.messagesConfig;
		try {
			// Set default prefix
			prefix = messages.getString("Prefix").replace("&", "§");
			// Set message on place
			onHeadPlace = traslator(messages.getString("Message-on-place"));
			onHeadBreakWithoutShift = traslator(messages.getString("Message-on-break-no-shift"));
			onHeadBreak = traslator(messages.getString("Message-on-break"));
			// Set not permission message
			permissionDenied = traslator(messages.getString("Message-permission-denied"));
			// Set default config reloaded message
			configReloaded = traslator(messages.getString("Message-config-reloaded"));
			// Set list header
			listCommandHeader = traslator(messages.getString("Message-command-list-header"));
			// Set list format
			listCommandFormat = traslator(messages.getString("Message-command-list-format"));
			// Set default id list header
			idlistCommandHeader = traslator(messages.getString("Message-command-idlist-header"));
			// Set default id list format
			idlistCommandFormat = traslator(messages.getString("Message-command-idlist-format"));
			// Set message on link set
			linkSetted = traslator(messages.getString("Message-command-link-setted"));
			// Set message on link remove
			linkRemoved = traslator(messages.getString("Message-command-link-removed"));
			// Set message on link get
			getLink = traslator(messages.getString("Message-command-link-get"));
			// Set message on target block isn't head
			blockNotHead = traslator(messages.getString("Message-blocknothead"));
			// Set message on target block is head but not in list
			headNotInList = traslator(messages.getString("Message-headnotinlist"));
			// Set message on executor of command isnìt player
			notplayer = traslator(messages.getString("Message-notplayer"));
			// Set help message
			helpMessages = messages.getStringList("Message-help-list");
		} catch (YAMLException e) {
			SocialHeads.getInstance().log.severe("Error when loading messages file:" + e.toString());
		}
	}

	public String traslator(String message) {
		return message.replaceAll("&", "§").replaceAll("%prefix%", prefix);
	}
}
