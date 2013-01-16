/*
 * Copyright (c) 2012 Sean Porter <glitchkey@gmail.com>
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
	import java.util.List;
	import java.util.Random;
//* IMPORTS: BUKKIT
	import org.bukkit.Chunk;
	import org.bukkit.generator.BlockPopulator;
	import org.bukkit.World;
//* IMPORTS: OTHER
	//* NOT NEEDED

public class PandoraPopulator extends BlockPopulator
{
	private List<PandoraBiomePopulator> populators = new ArrayList<PandoraBiomePopulator>();
	private PandoraBiomePopulator lastPop, defaultPop, tempPop;
	private int lastX, lastZ, xPos, zPos, currentX, currentZ, cXPos, cZPos;
	private int edgeMax, edgeXPos, edgeZPos;
	private double temperature, humidity, tempRange, humidityRange;

	public PandoraPopulator addBiome(PandoraBiomePopulator biome) {
		if(biome == null)
			return this;

		populators.add(biome);
		return this;
	}

	public void populate(World world, Random random, Chunk source) {
		xPos = source.getX() * 16;
		zPos = source.getZ() * 16;

		for(currentX = 0; currentX < 16; currentX++) {
			cXPos = currentX + xPos;
			for(currentZ = 0; currentZ < 16; currentZ++) {
				cZPos = currentZ + zPos;
				getPopulator(world, cXPos, cZPos);

				if(lastPop == null)
					return;

				lastPop.populate(world, random, source, cXPos, cZPos);
			}
		}
	}

	public List<PandoraBiomePopulator> getDefaultBiomes(World world) {
		return populators;
	}

	private boolean isNearEdge(World world, int x, int z, int period, int count) {
		if(world == null)
			return false;

		getPopulator(world, x, z);

		if(lastPop == null)
			return false;

		tempPop = lastPop;
		edgeMax = period * count;

		for(edgeXPos = (x - edgeMax); edgeXPos <= (x + edgeMax); x += period) {
			for(edgeZPos = (z - edgeMax); edgeZPos <= (z + edgeMax); z += period) {
				getPopulator(world, edgeXPos, edgeZPos);

				if(lastPop != tempPop)
					return true;
			}
		}

		return false;
	}

	private void getPopulator(World world, int x, int z) {
		if((x == lastX && z == lastZ && lastPop != null) || world == null)
			return;

		temperature = world.getTemperature(x, z);
		humidity = world.getHumidity(x, z);
		lastPop = defaultPop;

		for(PandoraBiomePopulator currentPop : populators) {
			if(currentPop.minTemperature > temperature)
				continue;
			else if(currentPop.maxTemperature < temperature)
				continue;
			else if(currentPop.minHumidity > humidity)
				continue;
			else if(currentPop.maxHumidity < humidity)
				continue;
			else if(lastPop == defaultPop) {
				lastPop = currentPop;
				tempRange = lastPop.maxTemperature - lastPop.minTemperature;
				humidityRange = lastPop.maxHumidity - lastPop.minHumidity;
				continue;
			}
			else if(tempRange <= (currentPop.maxTemperature - currentPop.minTemperature))
				continue;
			else if(humidityRange <= (currentPop.maxHumidity - currentPop.minHumidity))
				continue;

			lastPop = currentPop;
			tempRange = lastPop.maxTemperature - lastPop.minTemperature;
			humidityRange = lastPop.maxHumidity - lastPop.minHumidity;
		}
	}

	public PandoraPopulator setDefaultBiome(PandoraBiomePopulator biome) {
		if(biome == null)
			return this;

		defaultPop = biome;
		return this;
	}
}
