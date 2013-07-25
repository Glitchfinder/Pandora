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

public class JungleShrub extends PandoraWorldGenerator
{
	private byte logData, leafData;

	public JungleShrub(
		Plugin plugin,
		boolean notifyOnBlockChanges,
		byte logData,
		byte leafData
	) {
		super(plugin, notifyOnBlockChanges, false);

		this.logData = logData;
		this.leafData = leafData;
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		y = getGroundY(world, x, y, z);
		Location start = new Location(world, x, y, z);

		int id = world.getBlockTypeIdAt(x, y, z);

		if ((id != 3) && (id == 2))
			return false;

		addToWhitelist(start, world.getBlockAt(x, y, z));
		addBlock(start, world.getBlockAt(x, y, z), 3, (byte) 0);

		y++;
		addBlock(start, world, x, y, z, 17, this.logData);

		for (int cy = y; cy <= y + 2; cy++) {
			int yDist = cy - y;
			int depth = 2 - yDist;

			for (int cx = x - depth; cx <= x + depth; cx++) {
				int xDist = cx - x;

				for (int cz = z - depth; cz <= z + depth; cz++) {
					int zDist = cz - z;

					boolean cond1 = (Math.abs(xDist) != depth);
					boolean cond2 = (Math.abs(zDist) != depth);
					boolean cond3 = (random.nextInt(2) != 0);
					Block block = world.getBlockAt(cx, cy, cz);
					boolean cond4 = !(block.getType().isSolid());

					if ((!cond1 && !cond2 && !cond3) || !cond4)
						continue;

					addBlock(start, world, cx, cy, cz, 18, this.leafData);
				}
			}
		}

		return placeBlocks(start, true);
        }

	public int getGroundY(World world, int x, int y, int z) {
		int id;
		for (; y > 0; y--) {
			id = world.getBlockTypeIdAt(x, y, z);
			if (id != 0 && id != 18)
				return y;
		}

		return y;
	}
}
