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
	private List<Block> replaceWhitelist = new ArrayList<Block>();

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

	public boolean addBlock(Block block, int id, int data) {
		return addBlock(block, id, (byte) data);
	}

	public boolean addBlock(Block block, Material material, byte data) {
		try {
			return addBlock(block, new BlockValues(material, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(Block block, Material material, int data) {
		return addBlock(block, material, (byte) data);
	}

	public boolean addBlock(Block block, String name, byte data) {
		try {
			return addBlock(block, new BlockValues(name, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(Block block, String name, int data) {
		return addBlock(block, name, (byte) data);
	}

	public boolean addBlock(Location location, int id, byte data) {
		try {
			return addBlock(location.getBlock(), new BlockValues(id, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(Location location, int id, int data) {
		return addBlock(location, id, (byte) data);
	}

	public boolean addBlock(Location location, Material material, byte data) {
		try {
			return addBlock(location.getBlock(), new BlockValues(material, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(Location location, Material material, int data) {
		return addBlock(location, material, (byte) data);
	}

	public boolean addBlock(Location location, String name, byte data) {
		try {
			return addBlock(location.getBlock(), new BlockValues(name, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(Location location, String name, int data) {
		return addBlock(location, name, (byte) data);
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

	public boolean addBlock(World world, int x, int y, int z, int id, int data) {
		return addBlock(world, x, y, z, id, (byte) data);
	}

	public boolean addBlock(World world, int x, int y, int z, Material material, byte data) {
		try {
			return addBlock(world.getBlockAt(x, y, z), new BlockValues(material, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(World world, int x, int y, int z, Material material, int data) {
		return addBlock(world, x, y, z, material, (byte) data);
	}

	public boolean addBlock(World world, int x, int y, int z, String name, byte data) {
		try {
			return addBlock(world.getBlockAt(x, y, z), new BlockValues(name, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean addBlock(World world, int x, int y, int z, String name, int data) {
		return addBlock(world, x, y, z, name, (byte) data);
	}

	public PandoraWorldGenerator addToBlacklist(Block block) {
		if(block == null)
			return this;

		return addToBlacklist(block.getTypeId(), block.getData());
	}

	public PandoraWorldGenerator addToBlacklist(Block blocks[]) {
		for(Block block : blocks) {
			if(block == null)
				continue;

			addToBlacklist(block.getTypeId(), block.getData());
		}

		return this;
	}

	public PandoraWorldGenerator addToBlacklist(BlockValues values) {
		for(BlockValues listItem : replaceBlacklist) {
			if(listItem.getId() != values.getId())
				continue;

			if(listItem.getData() != values.getData())
				continue;

			return this;
		}

		replaceBlacklist.add(values);

		return this;
	}

	public PandoraWorldGenerator addToBlacklist(BlockValues blocks[]) {
		for(BlockValues block : blocks) {
			if(block == null)
				continue;

			addToBlacklist(block);
		}

		return this;
	}

	public PandoraWorldGenerator addToBlacklist(int id) {
		return addToBlacklist(id, (byte) 0);
	}

	public PandoraWorldGenerator addToBlacklist(int ids[]) {
		for(int id : ids) {
			addToBlacklist(id);
		}

		return this;
	}

	public PandoraWorldGenerator addToBlacklist(List<Object> objects) {
		for(Object object : objects) {
			if(object instanceof Block)
				addToBlacklist((Block) object);
			else if(object instanceof BlockValues)
				addToBlacklist((BlockValues) object);
			else if(object instanceof Integer)
				addToBlacklist((Integer) object);
			else if(object instanceof Material)
				addToBlacklist((Material) object);
			else if(object instanceof String)
				addToBlacklist((String) object);
		}

		return this;
	}

	public PandoraWorldGenerator addToBlacklist(Location location) {
		if(location == null)
			return this;

		return addToBlacklist(location.getBlock());
	}

	public PandoraWorldGenerator addToBlacklist(Location locations[]) {
		for(Location location : locations) {
			if(location == null)
				continue;

			addToBlacklist(location);
		}

		return this;
	}

	public PandoraWorldGenerator addToBlacklist(Material material) {
		return addToBlacklist(material, (byte) 0);
	}

	public PandoraWorldGenerator addToBlacklist(Material materials[]) {
		for(Material material : materials) {
			if(material == null)
				continue;

			addToBlacklist(material);
		}

		return this;
	}

	public PandoraWorldGenerator addToBlacklist(String name) {
		return addToBlacklist(name, (byte) 0);
	}

	public PandoraWorldGenerator addToBlacklist(String names[]) {
		for(String name : names) {
			if(name == null)
				continue;

			addToBlacklist(name);
		}

		return this;
	}

	public PandoraWorldGenerator addToBlacklist(int id, byte data) {
		try {
			return addToBlacklist(new BlockValues(id, data));
		}
		catch(Exception e) {
			return this;
		}
	}

	public PandoraWorldGenerator addToBlacklist(int id, int data) {
		return addToBlacklist(id, (byte) data);
	}

	public PandoraWorldGenerator addToBlacklist(Material material, byte data) {
		try {
			return addToBlacklist(new BlockValues(material, data));
		}
		catch(Exception e) {
			return this;
		}
	}

	public PandoraWorldGenerator addToBlacklist(Material material, int data) {
		return addToBlacklist(material, (byte) data);
	}

	public PandoraWorldGenerator addToBlacklist(String name, byte data) {
		try {
			return addToBlacklist(new BlockValues(name, data));
		}
		catch(Exception e) {
			return this;
		}
	}

	public PandoraWorldGenerator addToBlacklist(String name, int data) {
		return addToBlacklist(name, (byte) data);
	}

	public PandoraWorldGenerator addToWhitelist(Block block) {
		if(block == null)
			return this;

		replaceWhitelist.add(block);
		return this;
	}

	public PandoraWorldGenerator addToWhitelist(Block blocks[]) {
		for(Block block : blocks) {
			if(block == null)
				continue;

			addToWhitelist(block);
		}

		return this;
	}

	public PandoraWorldGenerator addToWhitelist(List<Object> objects) {
		for(Object object : objects) {
			if(object instanceof Block)
				addToWhitelist((Block) object);
			else if(object instanceof Location)
				addToWhitelist((Location) object);
		}

		return this;
	}

	public PandoraWorldGenerator addToWhitelist(Location location) {
		if(location == null)
			return this;

		return addToWhitelist(location.getBlock());
	}

	public PandoraWorldGenerator addToWhitelist(Location locations[]) {
		for(Location location : locations) {
			if(location == null)
				continue;

			addToWhitelist(location);
		}

		return this;
	}

	public PandoraWorldGenerator addToWhitelist(World world, int x, int y, int z) {
		try {
			return addToWhitelist(world.getBlockAt(x, y, z));
		}
		catch(Exception e) {
			return this;
		}
	}

	protected abstract boolean generate(World world, Random random, int x, int y, int z);

	public void invertBlacklist() {
		invertBlacklist = (invertBlacklist ? false : true);
	}

	public boolean isInBlacklist(Block block) {
		if(block == null)
			return false;
		else if(isInWhitelist(block))
			return invertBlacklist;

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
			return false;
		else if(isInWhitelist(location))
			return invertBlacklist;

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
			return false;
		}
	}

	public boolean isInBlacklist(int id, int data) {
		return isInBlacklist(id, (byte) data);
	}

	public boolean isInBlacklist(Material material, byte data) {
		try {
			return isInBlacklist(new BlockValues(material, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean isInBlacklist(Material material, int data) {
		return isInBlacklist(material, (byte) data);
	}

	public boolean isInBlacklist(String name, byte data) {
		try {
			return isInBlacklist(new BlockValues(name, data));
		}
		catch(Exception e) {
			return false;
		}
	}

	public boolean isInBlacklist(String name, int data) {
		return isInBlacklist(name, (byte) data);
	}

	public boolean isInWhitelist(Block block) {
		if(block == null)
			return false;

		return replaceWhitelist.contains(block);
	}

	public boolean isInWhitelist(Location location) {
		if(location == null)
			return false;

		return isInWhitelist(location.getBlock());
	}

	public boolean isInWhitelist(World world, int x, int y, int z) {
		try {
			return isInWhitelist(world.getBlockAt(x, y, z));
		}
		catch(Exception e) {
			return false;
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

			if(fastFail && blacklisted && !invertBlacklist) {
				return false;
			}
			else if(fastFail && !blacklisted && invertBlacklist) {
				return false;
			}

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

			setBlock(block, modifiedBlocks.get(block));
		}

		replaceWhitelist.clear();
		modifiedBlocks.clear();
		return true;
	}

	public PandoraWorldGenerator removeFromBlacklist(Block block) {
		if(block == null)
			return this;

		return removeFromBlacklist(block.getTypeId(), block.getData());
	}

	public PandoraWorldGenerator removeFromBlacklist(Block blocks[]) {
		for(Block block : blocks) {
			if(block == null)
				continue;

			removeFromBlacklist(block.getTypeId(), block.getData());
		}

		return this;
	}

	public PandoraWorldGenerator removeFromBlacklist(BlockValues values) {
		for(BlockValues listItem : replaceBlacklist) {
			if(listItem.getId() != values.getId())
				continue;

			if(listItem.getData() != values.getData())
				continue;

			return this;
		}

		replaceBlacklist.remove(values);

		return this;
	}

	public PandoraWorldGenerator removeFromBlacklist(BlockValues blocks[]) {
		for(BlockValues block : blocks) {
			if(block == null)
				continue;

			removeFromBlacklist(block);
		}

		return this;
	}

	public PandoraWorldGenerator removeFromBlacklist(int id) {
		return removeFromBlacklist(id, (byte) 0);
	}

	public PandoraWorldGenerator removeFromBlacklist(int ids[]) {
		for(int id : ids) {
			removeFromBlacklist(id);
		}

		return this;
	}

	public PandoraWorldGenerator removeFromBlacklist(List<Object> objects) {
		for(Object object : objects) {
			if(object instanceof Block)
				removeFromBlacklist((Block) object);
			else if(object instanceof BlockValues)
				removeFromBlacklist((BlockValues) object);
			else if(object instanceof Integer)
				removeFromBlacklist((Integer) object);
			else if(object instanceof Material)
				removeFromBlacklist((Material) object);
			else if(object instanceof String)
				removeFromBlacklist((String) object);
		}

		return this;
	}

	public PandoraWorldGenerator removeFromBlacklist(Location location) {
		if(location == null)
			return this;

		return removeFromBlacklist(location.getBlock());
	}

	public PandoraWorldGenerator removeFromBlacklist(Location locations[]) {
		for(Location location : locations) {
			if(location == null)
				continue;

			removeFromBlacklist(location);
		}

		return this;
	}

	public PandoraWorldGenerator removeFromBlacklist(Material material) {
		return removeFromBlacklist(material, (byte) 0);
	}

	public PandoraWorldGenerator removeFromBlacklist(Material materials[]) {
		for(Material material : materials) {
			if(material == null)
				continue;

			removeFromBlacklist(material);
		}

		return this;
	}

	public PandoraWorldGenerator removeFromBlacklist(String name) {
		return removeFromBlacklist(name, (byte) 0);
	}

	public PandoraWorldGenerator removeFromBlacklist(String names[]) {
		for(String name : names) {
			if(name == null)
				continue;

			removeFromBlacklist(name);
		}

		return this;
	}

	public PandoraWorldGenerator removeFromBlacklist(int id, byte data) {
		try {
			return removeFromBlacklist(new BlockValues(id, data));
		}
		catch(Exception e) {
			return this;
		}
	}

	public PandoraWorldGenerator removeFromBlacklist(int id, int data) {
		return removeFromBlacklist(id, (byte) data);
	}

	public PandoraWorldGenerator removeFromBlacklist(Material material, byte data) {
		try {
			return removeFromBlacklist(new BlockValues(material, data));
		}
		catch(Exception e) {
			return this;
		}
	}

	public PandoraWorldGenerator removeFromBlacklist(Material material, int data) {
		return removeFromBlacklist(material, (byte) data);
	}

	public PandoraWorldGenerator removeFromBlacklist(String name, byte data) {
		try {
			return removeFromBlacklist(new BlockValues(name, data));
		}
		catch(Exception e) {
			return this;
		}
	}

	public PandoraWorldGenerator removeFromBlacklist(String name, int data) {
		return removeFromBlacklist(name, (byte) data);
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

	private void setBlock(Block block, int id, int data) {
		setBlock(block, id, (byte) data);
	}

	public void setScale(double xScale, double yScale, double zScale) {}
}
