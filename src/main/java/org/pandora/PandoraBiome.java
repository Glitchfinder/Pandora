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
	import java.lang.String;
	import java.util.HashMap;
	import java.util.Map;
	import java.util.Random;
//* IMPORTS: BUKKIT
	import org.bukkit.block.Block;
	import org.bukkit.generator.ChunkGenerator.BiomeGrid;
	import org.bukkit.Location;
	import org.bukkit.Material;
	import org.bukkit.util.noise.SimplexNoiseGenerator;
	import org.bukkit.World;
//* IMPORTS: PANDORA
	//* NOT NEEDED
//* IMPORTS: OTHER
	//* NOT NEEDED

public abstract class PandoraBiome
{
	public double minTemperature, maxTemperature, minHumidity, maxHumidity;
	private Map<World, SimplexNoiseGenerator> noises;
	private UnsupportedOperationException exception;
	private World world;

	public boolean canSpawn(World world, int x, int z) {
		if (world == null)
			return false;

		Block b;
		boolean spawnable;

		switch (world.getEnvironment()) {
			case NETHER:
				return true;
			case THE_END:
				b = world.getBlockAt(x, world.getHighestBlockYAt(x, z), z);
				spawnable = true;
				spawnable = ((b.getType() != Material.AIR) ? spawnable : false);
				spawnable = ((b.getType() != Material.WATER) ? spawnable : false);
				spawnable = ((b.getType() != Material.LAVA) ? spawnable : false);
				return spawnable;
			case NORMAL:
			default:
				b = world.getBlockAt(x, world.getHighestBlockYAt(x, z), z);
				spawnable = false;
				spawnable = ((b.getType() != Material.SAND) ? spawnable : true);
				spawnable = ((b.getType() != Material.GRAVEL) ? spawnable : true);
				return spawnable;
		}
	}

	@Deprecated
	public byte[] generate(World world, Random random, int x, int z) {
		if (exception != null)
			throw exception;


		String msg = "Custom generator is missing required methods: ";
		msg += "generate(), generateSections(), and generateExtSections()";
		exception = new UnsupportedOperationException(msg);

		throw exception;
	}

	public short[] generateExtSections(World world, Random random, int x, int z,
		BiomeGrid biomes)
	{
		return null;
	}

	public byte[] generateSections(World world, Random random, int x, int z, BiomeGrid biomes) {
		return null;
	}

	public PandoraGenerator getGenerator(World world) {
		if (!isPandoraWorld(world))
			return null;

		return ((PandoraGenerator) world.getGenerator());
	}

	public final byte[] getNearestEdgeColumn(World world, Random rand, int x, int z, int period, int count)
	{
		if (!isPandoraWorld(world))
			return null;

		PandoraGenerator gen = (PandoraGenerator) world.getGenerator();

		Location loc = gen.getNearestEdge(world, x, z, period, count);

		if (loc == null)
			return null;

		return gen.getColumnAt(world, rand, loc.getBlockX(), loc.getBlockZ());
	}

	public final byte[] getNearestEdgeColumnSection(World world, Random rand, int x, int z,
		BiomeGrid biomes, int period, int count)
	{
		if (!isPandoraWorld(world))
			return null;

		PandoraGenerator gen = (PandoraGenerator) world.getGenerator();

		Location loc = gen.getNearestEdge(world, x, z, period, count);

		if (loc == null)
			return null;

		return gen.getColumnSectionAt(world, rand, loc.getBlockX(), loc.getBlockZ(), biomes);
	}

	public final short[] getNearestEdgeExtColumnSection(World world, Random rand, int x, int z,
		BiomeGrid biomes, int period, int count)
	{
		if (!isPandoraWorld(world))
			return null;

		PandoraGenerator gen = (PandoraGenerator) world.getGenerator();

		Location loc = gen.getNearestEdge(world, x, z, period, count);

		if (loc == null)
			return null;

		return gen.getExtColumnSectionAt(world, rand, loc.getBlockX(), loc.getBlockZ(), biomes);
	}

	public final int getNoise(World world, int x, int z, double range, double scale,
		int octaves, double amplitude, double frequency)
	{
		if (world == null)
			return 0;
		else if (noises == null)
			noises = new HashMap<World, SimplexNoiseGenerator>();

		if (!noises.containsKey(world))
			noises.put(world, (new SimplexNoiseGenerator(world.getSeed())));

		SimplexNoiseGenerator noise = noises.get(world);

		if (noise == null)
			return 0;

		range /= 2;
		int xs = (int) Math.round(((double) x) / scale);
		int zs = (int) Math.round(((double) z) / scale);
		double cnoise = noise.getNoise(xs, zs, octaves, frequency, amplitude);
		return (int) ((range * cnoise) + range);
	}

	public final SimplexNoiseGenerator getNoiseGenerator(World world) {
		return noises.get(world);
	}

	public double getTemperature(World world, int x, int z) {
		if (!isPandoraWorld(world))
			return world.getTemperature(x, z);

		return ((PandoraGenerator) world.getGenerator()).getTemperature(world, x, z);
	}

	public double getHumidity(World world, int x, int z) {
		if (!isPandoraWorld(world))
			return world.getHumidity(x, z);

		return ((PandoraGenerator) world.getGenerator()).getHumidity(world, x, z);
	}

	public final boolean isPandoraWorld(World world) {
		if (world.getGenerator() == null)
			return false;
		else if (!(world.getGenerator() instanceof PandoraGenerator))
			return false;

		return true;
	}
}
