package tsg.socialheads.main;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlaceBreakInteractListener implements Listener {

	boolean minecraftv;

	public PlaceBreakInteractListener() {
		minecraftv = minecarftversion();
	}

	public boolean minecarftversion() {
		if (Bukkit.getServer().getClass().getPackage().getName().contains("1_12")
				|| Bukkit.getServer().getClass().getPackage().getName().contains("1_13")
				|| Bukkit.getServer().getClass().getPackage().getName().contains("1_14")
				|| Bukkit.getServer().getClass().getPackage().getName().contains("1_15")
				|| Bukkit.getServer().getClass().getPackage().getName().contains("1_16")) {
			return true;
		}
		return false;
	}

	@EventHandler
	public void onHeadBreak(BlockBreakEvent e) {
		try {
			Player p = e.getPlayer();
			if (!p.hasPermission("socialheads.breakhead")) {
				return;
			}
			Block b = e.getBlock();
			if (!b.getType().equals(Material.SKULL)) {
				return;
			}
			if (!SocialHeads.getInstance().utils.isInPlacedList(b)) {
				return;
			}
			if (!p.isSneaking()) {
				e.setCancelled(true);
				p.sendMessage(SocialHeads.getInstance().messages.onHeadBreakWithoutShift);
				return;
			}
			SocialHeads.getInstance().utils.removeSocialHeadFromConfig(b.getLocation());
			SocialHeads.getInstance().utils.removeSocialHeadFromMap(b.getLocation());
			p.sendMessage(SocialHeads.getInstance().messages.onHeadBreak);
		} catch (NullPointerException ex) {
			SocialHeads.getInstance().log.severe("Error when break head:" + ex.toString());
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onHeadPlace(BlockPlaceEvent e) {
		try {
			Player p = e.getPlayer();
			if (!p.hasPermission("socialheads.placehead")) {
				return;
			}
			ItemStack is = p.getItemInHand();
			if (!is.getType().equals(Material.SKULL_ITEM)) {
				return;
			}
			if (!SocialHeads.getInstance().utils.isInIdList(is)) {
				return;
			}
			Block b = e.getBlock();
			String headId = SocialHeads.getInstance().utils.getHeadIdByItemStack(is);
			String type = SocialHeads.getInstance().idList.get(headId);
			String name = SocialHeads.getInstance().utils.genHeadName(headId);
			SocialHead head = new SocialHead(type, name, b.getLocation(), "");
			SocialHeads.getInstance().placedHeadMap.add(head);
			SocialHeads.getInstance().utils.addSocialHeadToConfig(head);
			p.sendMessage(SocialHeads.getInstance().messages.onHeadPlace);
		} catch (NullPointerException ex) {
			SocialHeads.getInstance().log.severe("Error when place head:" + ex.toString());
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		try {
			Player p = e.getPlayer();
			if (!p.hasPermission("socialheads.interacthead")) {
				return;
			}
			Block b = e.getClickedBlock();
			if (minecraftv) {
				EquipmentSlot ev = e.getHand();
				if (ev == null) {
					return;
				}
				if (ev.equals(EquipmentSlot.OFF_HAND)) {
					return;
				}
			}
			if (b == null || b.getType() == Material.AIR || !SocialHeads.getInstance().utils.isHead(p)) {
				return;
			}
			if (SocialHeads.getInstance().utils.isInPlacedList(b)) {
				p.sendMessage(
						SocialHeads.getInstance().messages.getLink.replaceAll("%link%", SocialHeads.getInstance().utils
								.getHeadByLocation(b.getLocation()).getLink().replaceAll("&", "§")));
			}
		} catch (NullPointerException ex) {
			SocialHeads.getInstance().log.severe("Error when interact with head:" + ex.toString());
		}
	}
}
