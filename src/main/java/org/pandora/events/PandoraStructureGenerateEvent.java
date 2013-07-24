/*
 * Copyright (c) 2012-2013 Sean Porter <glitchkey@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.pandora.events;

//* IMPORTS: JDK/JRE
	import java.util.List;
//* IMPORTS: BUKKIT
	import org.bukkit.block.BlockState;
	import org.bukkit.event.Cancellable;
	import org.bukkit.event.HandlerList;
	import org.bukkit.event.world.WorldEvent;
	import org.bukkit.Location;
	import org.bukkit.plugin.Plugin;
//* IMPORTS: PANDORA
	//* NOT NEEDED
//* IMPORTS: OTHER
	//* NOT NEEDED

public class PandoraStructureGenerateEvent extends WorldEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final Location location;
	private final Plugin plugin;
	private final List<BlockState> blocks;
	private boolean cancelled = false;

	public PandoraStructureGenerateEvent(Location location, List<BlockState> blocks, Plugin plugin) {
		super(location.getWorld());

		this.location = location;
		this.plugin = plugin;
		this.blocks = blocks;
	}

	public List<BlockState> getBlocks() {
		return blocks;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Location getLocation() {
		return location;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}
}
