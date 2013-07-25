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

public class BigJungleTree extends PandoraWorldGenerator
{
	private boolean skipSolids = false;
	private int maxHeight;
	private byte logData, leafData;

	public BigJungleTree(
		Plugin plugin,
		boolean notifyOnBlockChanges,
		boolean skipSolids,
		int maxHeight,
		byte logData,
		byte leafData
	) {
		super(plugin, notifyOnBlockChanges, true);

		this.maxHeight = maxHeight;
		this.logData = logData;
		this.leafData = leafData;

		addToBlacklist(0);

		for (byte i = ((byte) 0); i < ((byte) 16); i++) {
			addToBlacklist(18, i);
		}
	}

	public boolean generate(World world, Random random, int x, int y, int z) {
		int height = random.nextInt(3) + this.maxHeight;
		Location start = new Location(world, x, y, z);

		if ((y < 1) || (y + height + 1 > 256))
			return false;

		if (!skipSolids) {
			for (int cy = y; cy <= y + 1 + height; cy++) {
				byte radius = 2;

				if (cy == y) {
					radius = 1;
				}

				if (cy >= y + 1 + height - 2) {
					radius = 2;
				}

				for (int cx = x - radius; (cx <= x + radius); cx++) {
					for (int cz = z - radius; (cz <= z + radius); cz++) {
						if ((cy < 0) || (cy >= 256))
							return false;

						int id = world.getBlockTypeIdAt(cx, cy, cz);
						if (!isBlockedId(id))
							return false;
					}
				}
			}
		}

		int id = world.getBlockTypeIdAt(x, y - 1, z);
		if (((id != 2) && (id != 3)) || (y < 256 - height - 1))
			return false;

		addToWhitelist(start, world.getBlockAt(x, y - 1, z));
		addToWhitelist(start, world.getBlockAt(x + 1, y - 1, z));
		addToWhitelist(start, world.getBlockAt(x, y - 1, z + 1));
		addToWhitelist(start, world.getBlockAt(x + 1, y - 1, z + 1));
		addBlock(start, world.getBlockAt(x, y - 1, z), 3, (byte) 0);
		addBlock(start, world.getBlockAt(x + 1, y - 1, z), 3, (byte) 0);
		addBlock(start, world.getBlockAt(x, y - 1, z + 1), 3, (byte) 0);
		addBlock(start, world.getBlockAt(x + 1, y - 1, z + 1), 3, (byte) 0);

		generateLeaves(start, world, x, z, y + height, 2, random);

		int cy = y + height - 2 - random.nextInt(4);

		for (; cy > y + height / 2; cy -= 2 + random.nextInt(4)) {
			float f = random.nextFloat() * 3.141593F * 2.0F;

			int cx = x + (int)(0.5F + Math.cos(f) * 4.0F);
			int cz = z + (int)(0.5F + Math.sin(f) * 4.0F);

			generateLeaves(start, world, cx, cz, cy, 0, random);

			for (int depth = 0; depth < 5; depth++) {
				cx = x + (int)(1.5F + Math.cos(f) * depth);
				cz = z + (int)(1.5F + Math.sin(f) * depth);

				int yLoc = cy - 3 + depth / 2;
				addBlock(start, world, cx, yLoc, cz, 17, this.logData);
			}
		}

		for (int depth = 0; depth < height; depth++) {
			int cDepth = y + depth;

			addBlock(start, world, x, y + depth, z, 17, this.logData);
			if (depth > 0) {
				if (chance(random) && (isEmpty(world, x - 1, cDepth, z)))
					addBlock(start, world, x - 1, cDepth, z, 106, (byte) 8);

				if (chance(random) && (isEmpty(world, x, cDepth, z - 1)))
					addBlock(start, world, x, cDepth, z - 1, 106, (byte) 1);
			}

			if (depth >= height - 1)
				continue;

			addBlock(start, world, x + 1, cDepth, z, 17, this.logData);
			if (depth > 0) {
				if (chance(random) && (isEmpty(world, x + 2, cDepth, z)))
					addBlock(start, world, x + 2, cDepth, z, 106, (byte) 2);

				if (chance(random) && (isEmpty(world, x + 1, cDepth, z - 1)))
					addBlock(start, world, x + 1, cDepth, z - 1, 106, (byte) 1);
			}

			addBlock(start, world, x + 1, cDepth, z + 1, 17, this.logData);
			if (depth > 0) {
				if (chance(random) && (isEmpty(world, x + 2, cDepth, z + 1)))
					addBlock(start, world, x + 2, cDepth, z + 1, 106, (byte) 2);

				if (chance(random) && (isEmpty(world, x + 1, cDepth, z + 2)))
					addBlock(start, world, x + 1, cDepth, z + 2, 106, (byte) 4);
			}

			addBlock(start, world, x, cDepth, z + 1, 17, this.logData);
			if (depth > 0) {
				if (chance(random) && (isEmpty(world, x - 1, cDepth, z + 1)))
					addBlock(start, world, x - 1, cDepth, z + 1, 106, (byte) 8);

				if (chance(random) && (isEmpty(world, x, cDepth, z + 2)))
					addBlock(start, world, x, cDepth, z + 2, 106, (byte) 4);
			}
		}

		return placeBlocks(start, true);
        }

	private void generateLeaves(Location start, World world, int x, int z, int y, int radius,
		Random random)
	{
		byte maxRadius = 2;

		for (int cy = y - maxRadius; cy <= y; cy++) {
			int yRadius = cy - y;
			int cRadius = radius + 1 - yRadius;
			int cRadiusSq = (int) Math.pow(cRadius, 2);
			int cRadiusSq1 = (int) Math.pow((cRadius + 1), 2);
			int cRadiusSq2 = (int) Math.pow((cRadius - 1), 2);

			for (int cx = x - cRadius; cx <= x + cRadius + 1; cx++) {
				int xRadius = cx - x;
				int xRadiusSq = (int) Math.pow(xRadius, 2);

				for (int cz = z - cRadius; cz <= z + cRadius + 1; cz++) {
					int zRadius = cz - z;
					int zRadiusSq = (int) Math.pow(zRadius, 2);
					int xzRadiusSq = xRadiusSq + zRadiusSq;

					boolean cond1 = ((xRadius < 0) && (zRadius < 0));

					if (cond1 && (xzRadiusSq > cRadiusSq))
						continue;

					boolean cond2 = ((xRadius > 0) || (zRadius > 0));
					boolean cond3 = (xzRadiusSq > cRadiusSq1);
					boolean cond4 = (random.nextInt(4) == 0);
					boolean cond5 = (xzRadiusSq > cRadiusSq2);

					if (cond2 && (cond3 || (cond4 && cond5)))
						continue;

					addBlock(start, world, cx, cy, cz, 18, this.leafData);
				}
			}
		}
	}

	public boolean chance(Random random) {
		return (random.nextInt(3) > 0);
	}

	public boolean isEmpty(World world, int x, int y, int z) {
		return (world.getBlockTypeIdAt(x, y, z) == 0);
	}

	public boolean isBlockedId(int id) {
		if (id == 2 || id == 3 || id == 17 || id == 6)
			return true;

		return false;
	}
}
