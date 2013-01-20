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

package org.pandora;

//* IMPORTS: JDK/JRE
	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	import java.util.Random;
//* IMPORTS: BUKKIT
	import org.bukkit.block.Block;
	import org.bukkit.block.BlockState;
	import org.bukkit.Bukkit;
	import org.bukkit.Location;
	import org.bukkit.Material;
	import org.bukkit.plugin.Plugin;
	import org.bukkit.World;
//* IMPORTS: PANDORA
	import org.pandora.datatypes.BlockValues;
	import org.pandora.events.PandoraStructureGenerateEvent;
//* IMPORTS: OTHER
	//* NOT NEEDED

public abstract class PandoraWorldGenerator
{
	private Map<Block, BlockValues> modifiedBlocks = new HashMap<Block, BlockValues>();
	private List<BlockValues> replaceBlacklist = new ArrayList<BlockValues>();

	private final Plugin plugin;
	private Location location;

	private final boolean notifyOnBlockChanges;
	private boolean invertBlacklist = false;

	public PandoraWorldGenerator(Plugin plugin) {
		this(plugin, false, false);
	}

	public PandoraWorldGenerator(Plugin plugin, boolean notifyOnBlockChanges) {
		this(plugin, notifyOnBlockChanges, false);
	}

	public PandoraWorldGenerator(Plugin plugin, boolean notifyOnBlockChanges, boolean invertBlacklist) {
		this.plugin = plugin;
		this.notifyOnBlockChanges = notifyOnBlockChanges;
		this.invertBlacklist = invertBlacklist;
	}

	public boolean addBlock(Block block, BlockValues values) {
		if(block == null || values == null)
			return false;

		if(modifiedBlocks.containsKey(block))
			return true;

		modifiedBlocks.put(block, values);

		if(isInBlacklist(block.getTypeId(), block.getData()))
			return false;

		return true;
	}

	public boolean addBlock(Block block, int id) {
		return addBlock(block, id, (byte) 0);
	}

	public boolean addBlock(Block block, Material material) {
		return addBlock(block, material, (byte) 0);
	}

	public boolean addBlock(Block block, String name) {
		return addBlock(block, name, (byte) 0);
	}

	public boolean addBlock(Location location, int id) {
		return addBlock(location, id, (byte) 0);
	}

	public boolean addBlock(Location location, Material material) {
		return addBlock(location, material, (byte) 0);
	}

	public boolean addBlock(Location location, String name) {
		return addBlock(location, name, (byte) 0);
	}

	public boolean addBlock(Block block, int id, byte data) {
		try {
			return addBlock(block, new BlockValues(id, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(Block block, Material material, byte data) {
		try {
			return addBlock(block, new BlockValues(material, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(Block block, String name, byte data) {
		try {
			return addBlock(block, new BlockValues(name, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(Location location, int id, byte data) {
		try {
			return addBlock(location.getBlock(), new BlockValues(id, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(Location location, Material material, byte data) {
		try {
			return addBlock(location.getBlock(), new BlockValues(material, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(Location location, String name, byte data) {
		try {
			return addBlock(location.getBlock(), new BlockValues(name, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(World world, int x, int y, int z, int id) {
		return addBlock(world, x, y, z, id, (byte) 0);
	}

	public boolean addBlock(World world, int x, int y, int z, Material material) {
		return addBlock(world, x, y, z, material, (byte) 0);
	}

	public boolean addBlock(World world, int x, int y, int z, String name) {
		return addBlock(world, x, y, z, name, (byte) 0);
	}

	public boolean addBlock(World world, int x, int y, int z, int id, byte data) {
		try {
			return addBlock(world.getBlockAt(x, y, z), new BlockValues(id, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(World world, int x, int y, int z, Material material, byte data) {
		try {
			return addBlock(world.getBlockAt(x, y, z), new BlockValues(material, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(World world, int x, int y, int z, String name, byte data) {
		try {
			return addBlock(world.getBlockAt(x, y, z), new BlockValues(name, data));
		}
		catch(Exception e) {
			return false;
		}
	}


	protected abstract boolean generate(World world, Random random, int x, int y, int z);

	public void invertBlacklist() {
		invertBlacklist = (invertBlacklist ? false : true);
	}

	public boolean isInBlacklist(Block block) {
		if(block == null)
			return true;

		return isInBlacklist(block.getTypeId(), block.getData());
	}

	public boolean isInBlacklist(BlockValues values) {
		for(BlockValues listItem : replaceBlacklist) {
			if(listItem.getId() != values.getId())
				continue;

			if(listItem.getData() != values.getData())
				continue;

			return true;
		}

		return false;
	}

	public boolean isInBlacklist(int id) {
		return isInBlacklist(id, (byte) 0);
	}

	public boolean isInBlacklist(Location location) {
		if(location == null)
			return true;

		return isInBlacklist(location.getBlock());
	}

	public boolean isInBlacklist(Material material) {
		return isInBlacklist(material, (byte) 0);
	}

	public boolean isInBlacklist(String name) {
		return isInBlacklist(name, (byte) 0);
	}

	public boolean isInBlacklist(int id, byte data) {
		try {
			return isInBlacklist(new BlockValues(id, data));
		}
		catch(Exception e) {
			return true;
		}
	}

	public boolean isInBlacklist(Material material, byte data) {
		try {
			return isInBlacklist(new BlockValues(material, data));
		}
		catch(Exception e) {
			return true;
		}
	}

	public boolean isInBlacklist(String name, byte data) {
		try {
			return isInBlacklist(new BlockValues(name, data));
		}
		catch(Exception e) {
			return true;
		}
	}

	public boolean place(World world, Random random, int x, int y, int z) {
		location = new Location(world, x, y, z);
		return generate(world, random, x, y, z);
	}

	public boolean placeBlocks() {
		return placeBlocks(false);
	}

	public boolean placeBlocks(boolean fastFail) {
		if(plugin == null || location == null)
			return false;

		if(modifiedBlocks.isEmpty())
			return true;

		List<BlockState> blocks = new ArrayList<BlockState>();

		for(Block block : modifiedBlocks.keySet()) {
			if(block == null)
				continue;

			boolean blacklisted = isInBlacklist(block);

			if(fastFail && blacklisted && !invertBlacklist)
				return false;
			else if(fastFail && !blacklisted && invertBlacklist)
				return false;

			blocks.add(block.getState());
		}

		PandoraStructureGenerateEvent event;
		event = new PandoraStructureGenerateEvent(location, blocks, plugin);

		Bukkit.getPluginManager().callEvent(event);

		if(event.isCancelled())
			return false;

		for(Block block : modifiedBlocks.keySet()) {
			if(block == null)
				continue;

			boolean blacklisted = isInBlacklist(block);

			if(fastFail && blacklisted && !invertBlacklist)
				continue;
			else if(fastFail && !blacklisted && invertBlacklist)
				continue;

			setBlock(block, modifiedBlocks.get(block));
		}

		return true;
	}

	private void setBlock(Block block, BlockValues values) {
		setBlock(block, values.getId(), values.getData());
	}

	private void setBlock(Block block, int id) {
		setBlock(block, id, (byte) 0);
	}

	private void setBlock(Block block, int id, byte data) {
		block.setTypeIdAndData(id, data, notifyOnBlockChanges);
	}

	public void setScale(double xScale, double yScale, double zScale) {}
}