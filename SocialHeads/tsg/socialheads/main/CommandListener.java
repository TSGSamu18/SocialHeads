package tsg.socialheads.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
 
public class CommandListener extends BukkitCommand {
	
	List<String> commandAliases;
	
    public CommandListener(String name) {
        super(name);
        commandAliases = new ArrayList<String>();
        fillCommandAliases();
        this.description = "SocialHeads command!";
        this.setAliases(commandAliases);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean execute(CommandSender sender, String cmd, String[] args) {
        if(!(sender instanceof Player)) {
        	SocialHeads.getInstance().log.severe(SocialHeads.getInstance().messages.notplayer);
        	return true;
        }
        Player p = (Player)sender;
        if(args.length == 0) {
            if(!p.hasPermission("socialheads.help")) {
            	p.sendMessage(SocialHeads.getInstance().messages.permissionDenied);
            	return true;
            }
            for(String hm : SocialHeads.getInstance().messages.helpMessages) {
        	   p.sendMessage(SocialHeads.getInstance().messages.traslator(hm));
            }
        	return true;
        }else if(args.length == 1) {
        if(args[0].equalsIgnoreCase("help")) {
            if(!p.hasPermission("socialheads.help")) {
            	p.sendMessage(SocialHeads.getInstance().messages.permissionDenied);
            	return true;
            }
            for(String hm : SocialHeads.getInstance().messages.helpMessages) {
        	   p.sendMessage(SocialHeads.getInstance().messages.traslator(hm));
            }
        	return true;
        }else if(args[0].equalsIgnoreCase("list")) {
            if(!p.hasPermission("socialheads.list")) {
            	p.sendMessage(SocialHeads.getInstance().messages.permissionDenied);
            	return true;
            }
    		p.sendMessage(SocialHeads.getInstance().messages.listCommandHeader);
        	for(SocialHead sc : SocialHeads.getInstance().placedHeadMap) {
        		p.sendMessage(SocialHeads.getInstance().messages.listCommandFormat.replaceAll("%headname%", sc.getName()));
        	}
        	return true;
        }else if(args[0].equalsIgnoreCase("idlist")) {
            if(!p.hasPermission("socialheads.idlist")) {
            	p.sendMessage(SocialHeads.getInstance().messages.permissionDenied);
            	return true;
            }
            p.sendMessage(SocialHeads.getInstance().messages.idlistCommandHeader);
        	for(String id : SocialHeads.getInstance().idList.keySet()) {
        		p.sendMessage(SocialHeads.getInstance().messages.idlistCommandFormat.replaceAll("%id%", id));
        	}
        	return true;
        }else if(args[0].equalsIgnoreCase("reload")) {
            if(!p.hasPermission("socialheads.reload")) {
            	p.sendMessage(SocialHeads.getInstance().messages.permissionDenied);
            	return true;
            }
        	SocialHeads.getInstance().reloadConfig();
        	SocialHeads.getInstance().utils.onEnable();
        	p.sendMessage(SocialHeads.getInstance().messages.configReloaded);
        	return true;
        }else if(args[0].equalsIgnoreCase("getlink")) {
            if(!p.hasPermission("socialheads.getlink")) {
            	p.sendMessage(SocialHeads.getInstance().messages.permissionDenied);
            	return true;
            }
        	if(SocialHeads.getInstance().utils.isHead(p)) {
        		if(SocialHeads.getInstance().utils.isInPlacedList(p)) {
        			p.sendMessage(SocialHeads.getInstance().messages.getLink.replaceAll("%link%", SocialHeads.getInstance().utils.getHeadByLocation(p.getTargetBlock((Set)null, 200).getLocation()).getLink()));
        			return true;
        		}else {
            		p.sendMessage(SocialHeads.getInstance().messages.headNotInList);
            		return true;
        		}
        	}else {
        		p.sendMessage(SocialHeads.getInstance().messages.blockNotHead);
        		return true;
        	}
        }else if(args[0].equalsIgnoreCase("removelink")) {
            if(!p.hasPermission("socialheads.removelink")) {
            	p.sendMessage(SocialHeads.getInstance().messages.permissionDenied);
            	return true;
            }
          	if(SocialHeads.getInstance().utils.isHead(p)) {
        		if(SocialHeads.getInstance().utils.isInPlacedList(p)) {
        			SocialHead h = SocialHeads.getInstance().utils.getHeadByLocation(p.getTargetBlock((Set)null, 200).getLocation());
            		h.setLink("");
            		SocialHeads.getInstance().utils.setLink(h, h.getLink());
        			p.sendMessage(SocialHeads.getInstance().messages.linkRemoved.replaceAll("%link%", SocialHeads.getInstance().utils.getHeadByLocation(p.getTargetBlock((Set)null, 200).getLocation()).getLink()));
        			return true;
        		}else {
            		p.sendMessage(SocialHeads.getInstance().messages.headNotInList);
            		return true;
        		}
        	}else {
        		p.sendMessage(SocialHeads.getInstance().messages.blockNotHead);
        		return true;
        	}
        }
		return true;
	}else if(args.length == 2) {
		 if(args[0].equalsIgnoreCase("setlink")) {
	        if(!p.hasPermission("socialheads.setlink")) {
	            p.sendMessage(SocialHeads.getInstance().messages.permissionDenied);
	            return true;
	        }			 
        	if(SocialHeads.getInstance().utils.isHead(p)) {
        		if(SocialHeads.getInstance().utils.isInPlacedList(p)) {
        			SocialHead h = SocialHeads.getInstance().utils.getHeadByLocation(p.getTargetBlock((Set)null, 200).getLocation());
        			try {
        			h.setLink(args[1]);
            		SocialHeads.getInstance().utils.setLink(h, h.getLink());
        			}catch(ArrayIndexOutOfBoundsException e) {
        				SocialHeads.getInstance().log.severe("Error:" + e.toString());
        			}
        			p.sendMessage(SocialHeads.getInstance().messages.linkSetted.replaceAll("%link%", SocialHeads.getInstance().utils.getHeadByLocation(p.getTargetBlock((Set)null, 200).getLocation()).getLink()));
        			return true;
        		}else {
            		p.sendMessage(SocialHeads.getInstance().messages.headNotInList);
            		return true;
        		}
        	}else {
        		p.sendMessage(SocialHeads.getInstance().messages.blockNotHead);
        		return true;
        	}
		 }
	  }
        return true;
   }
	
	public void fillCommandAliases() {
    	if(SocialHeads.getInstance().getConfig().getConfigurationSection("command").isList("Aliases")) {
    		for(String alias : SocialHeads.getInstance().getConfig().getConfigurationSection("command").getStringList("Aliases")) {
    			commandAliases.add(alias);
    		}
    	}else {
    		SocialHeads.getInstance().log.severe("Error command alias not found");
    	}
    }
}
