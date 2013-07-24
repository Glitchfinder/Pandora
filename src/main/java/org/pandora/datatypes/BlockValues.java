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

package org.pandora.datatypes;

//* IMPORTS: JDK/JRE
	import java.io.Serializable;
	import java.lang.String;
//* IMPORTS: BUKKIT
	import org.bukkit.Material;
//* IMPORTS: PANDORA
	//* NOT NEEDED
//* IMPORTS: OTHER
	//* NOT NEEDED

public class BlockValues implements Serializable {
	private int id;
	private byte data;
	private int x, y, z;

	public BlockValues(int id) throws IllegalArgumentException {
		this(id, (byte) 0, 0, 0, 0);
	}

	public BlockValues(Material material) throws IllegalArgumentException {
		this(material, (byte) 0, 0, 0, 0);
	}

	public BlockValues(String name) throws IllegalArgumentException {
		this(name, (byte) 0, 0, 0, 0);
	}

	public BlockValues(int id, byte data) throws IllegalArgumentException {
		this(id, data, 0, 0, 0);
	}

	public BlockValues(int id, int data) throws IllegalArgumentException {
		this(id, (byte) data, 0, 0, 0);
	}

	public BlockValues(Material material, byte data) throws IllegalArgumentException {
		this(material, data, 0, 0, 0);
	}

	public BlockValues(Material material, int data) throws IllegalArgumentException {
		this(material, (byte) data, 0, 0, 0);
	}

	public BlockValues(String name, byte data) throws IllegalArgumentException {
		this(name, data, 0, 0, 0);
	}

	public BlockValues(String name, int data) throws IllegalArgumentException {
		this(name, (byte) data, 0, 0, 0);
	}

	public BlockValues(int id, byte data, int x, int y, int z) throws IllegalArgumentException {
		setId(id);
		setData(data);
		setCoordinates(x, y, z);
	}

	public BlockValues(int id, int data, int x, int y, int z) throws IllegalArgumentException {
		this(id, (byte) data, x, y, z);
	}

	public BlockValues(Material material, byte data, int x, int y, int z) throws IllegalArgumentException {
		setMaterial(material);
		setData(data);
		setCoordinates(x, y, z);
	}

	public BlockValues(Material material, int data, int x, int y, int z) throws IllegalArgumentException {
		this(material, (byte) data, x, y, z);
	}

	public BlockValues(String name, byte data, int x, int y, int z) throws IllegalArgumentException {
		setMaterial(name);
		setData(data);
		setCoordinates(x, y, z);
	}

	public BlockValues(String name, int data, int x, int y, int z) throws IllegalArgumentException {
		this(name, (byte) data, x, y, z);
	}

	private void checkData(byte data) throws IllegalArgumentException {
		if(data < ((byte) 0) || data > ((byte) 15)) {
			// TODO: Localization
			String message = "" + data + " is not a valid block metadata value!";
			throw new IllegalArgumentException(message);
		}
	}

	private void checkId(int id) throws IllegalArgumentException {
		if(Material.getMaterial(id) == null) {
			// TODO: Localization
			String message = "" + id +  " is not a valid block id!";
			throw new IllegalArgumentException(message);
		}
	}

	private void checkMaterial(String name) throws IllegalArgumentException {
		if(Material.matchMaterial(name) == null) {
			// TODO: Localization
			String message = "" + name +  " is not a valid block type!";
			throw new IllegalArgumentException(message);
		}
	}
	
	public byte getData() {
		return data;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public Material getMaterial() {
		if(Material.getMaterial(id) == null)
			return Material.AIR;

		return Material.getMaterial(id);
	}

	public BlockValues setCoordinates(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;

		return this;
	}

	public BlockValues setData(byte data) throws IllegalArgumentException {
		checkData(data);
		this.data = data;

		return this;
	}

	public BlockValues setData(int data) throws IllegalArgumentException {
		checkData((byte) data);
		this.data = ((byte) data);

		return this;
	}

	public BlockValues setId(int id) throws IllegalArgumentException {
		checkId(id);
		this.id = id;

		return this;
	}

	public BlockValues setMaterial(Material material) throws IllegalArgumentException {
		this.id = material.getId();
		return this;
	}

	public BlockValues setMaterial(String name) throws IllegalArgumentException {
		checkMaterial(name);
		this.id = id;

		return this;
	}

	public BlockValues setX(int x) {
		this.x = x;
		return this;
	}

	public BlockValues setY(int y) {
		this.y = y;
		return this;
	}

	public BlockValues setZ(int z) {
		this.z = z;
		return this;
	}
}
