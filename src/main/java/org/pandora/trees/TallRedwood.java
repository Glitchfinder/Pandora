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

package org.pandora.trees;

//* IMPORTS: JDK/JRE
	import java.util.Random;
//* IMPORTS: BUKKIT
	import org.bukkit.block.Block;
	import org.bukkit.plugin.Plugin;
	import org.bukkit.World;
//* IMPORTS: PANDORA
	import org.pandora.PandoraWorldGenerator;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class TallRedwood extends PandoraWorldGenerator
{
	private boolean skipSolids = true;

	public TallRedwood(Plugin plugin, boolean notifyOnBlockChanges, boolean invertBlacklist) {
		super(plugin, notifyOnBlockChanges, true);

		addToBlacklist(0);
		addToBlacklist(18);
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		int maxHeight = random.nextInt(5) + 7;
		int leafHeight = maxHeight - random.nextInt(2) - 3;
		int baseLeafWidth = maxHeight - leafHeight;
		int maxLeafWidth = 1 + random.nextInt(baseLeafWidth + 1);

		if (y < 1 || (y + maxHeight + 1) > 128)
			return false;

		int leafWidth;

		int baseId = world.getBlockTypeIdAt(x, y - 1, z);

		if ((baseId != 2 && baseId != 3) || y >= (128 - maxHeight - 1))
			return false;

		leafWidth = 0;

		for (int cy = y + maxHeight; cy >= y + leafHeight; --cy)
		{
			for (int cx = x - leafWidth; cx <= x + leafWidth; ++cx)
			{
				int width = Math.abs(cx - x);

				for (int cz = z - leafWidth; cz <= z + leafWidth; ++cz)
				{
					int length = Math.abs(cz - z);

					Block block = world.getBlockAt(cx, cy, cz);

					if(!skipSolids && block.getType().isSolid())
						continue;

					if (width == length && width == leafWidth && leafWidth > 0)
						continue;

					addBlock(block, 18, (byte) 1);
				}
			}

			if (leafWidth >= 1 && cy == (y + leafHeight + 1))
			{
				--leafWidth;
			}
			else if (leafWidth < maxLeafWidth)
			{
				++leafWidth;
			}
		}

		for (int cy = 0; cy < maxHeight - 1; ++cy)
		{
			Block block = world.getBlockAt(x, y + cy, z);

			if (block.getTypeId() != 0 && block.getTypeId() != 18)
				continue;

			addBlock(block, 17, (byte) 1);
		}

		boolean placed = placeBlocks(true);

		// TODO: Implement blacklist-free placement
		if(placed)
			world.getBlockAt(x, y - 1, z).setTypeId(3);

		return placed;
	}
}