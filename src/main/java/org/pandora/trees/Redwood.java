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
	import org.bukkit.Location;
	import org.bukkit.plugin.Plugin;
	import org.bukkit.World;
//* IMPORTS: PANDORA
	import org.pandora.PandoraWorldGenerator;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class Redwood extends PandoraWorldGenerator
{
	public Redwood(Plugin plugin, boolean notifyOnBlockChanges) {
		super(plugin, notifyOnBlockChanges, true);

		addToBlacklist(0);

		for (byte i = ((byte) 0); i < ((byte) 16); i++) {
			addToBlacklist(18, i);
		}
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		int maxHeight = random.nextInt(4) + 6;
		int leafHeight = maxHeight - (1 + random.nextInt(2));
		int leafWidth = 2 + random.nextInt(2);
		Location start = new Location(world, x, y, z);

		if ((y < 1) || (y + maxHeight + 1 > 256))
			return false;

		int baseId = world.getBlockTypeIdAt(x, y - 1, z);

		if (((baseId != 2) && (baseId != 3)) || (y >= 256 - maxHeight - 1))
			return false;

		addToWhitelist(start, world.getBlockAt(x, y - 1, z));
		addBlock(start, world.getBlockAt(x, y - 1, z), 3, (byte) 0);

		int radius = random.nextInt(2);
		int width = 1;
		byte canopySpawned = 0;

		for (int depth = 0; depth <= leafHeight; depth++)
		{
			int cy = y + maxHeight - depth;

			for (int cx = x - radius; cx <= x + radius; cx++)
			{
				int xRadius = cx - x;

				for (int cz = z - radius; cz <= z + radius; cz++)
				{
					int zRadius = cz - z;

					Block block = world.getBlockAt(cx, cy, cz);

					if (!isInBlacklist(block))
						continue;

					boolean cond1 = (Math.abs(xRadius) != radius);
					boolean cond2 = (Math.abs(zRadius) != radius);
					boolean cond3 = (cond1 || cond2 || (radius <= 0));

					if (!cond3)
						continue;

					addBlock(start, block, 18, (byte) 1);
				}
			}

			if (radius >= width) {
				radius = canopySpawned;
				canopySpawned = 1;
				width++;
				if (width > leafWidth)
					width = leafWidth;
			}
			else {
				radius++;
			}
		}

		int depth = random.nextInt(3);

		for (int cDepth = 0; cDepth < maxHeight - depth; cDepth++) {
			Block block = world.getBlockAt(x, y + cDepth, z);

			if (!isInBlacklist(block))
				continue;

			addBlock(start, block, 17, (byte) 1);
		}

		return placeBlocks(start, true);
	}
}
