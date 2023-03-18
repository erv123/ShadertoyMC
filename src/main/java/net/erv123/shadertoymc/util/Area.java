package net.erv123.shadertoymc.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Area implements Iterable<BlockPos> {
	private BlockPos positionA;
	private BlockPos positionB;
	private BlockPos origin;
	private Vec3i size;

	public Area(BlockPos position) {
		this.positionA = position;
		this.positionB = position;
		this.calculateOriginSize();
	}

	private void calculateOriginSize() {
		int oX = Math.min(this.getA().getX(), this.getB().getX());
		int oY = Math.min(this.getA().getY(), this.getB().getY());
		int oZ = Math.min(this.getA().getZ(), this.getB().getZ());
		this.origin = new BlockPos(oX, oY, oZ);
		int sX = Math.max(this.getA().getX(), this.getB().getX()) - oX;
		int sY = Math.max(this.getA().getY(), this.getB().getY()) - oY;
		int sZ = Math.max(this.getA().getZ(), this.getB().getZ()) - oZ;
		this.size = new BlockPos(sX, sY, sZ);
	}

	public void setA(BlockPos position) {
		this.positionA = position;
		this.calculateOriginSize();
	}

	public void setB(BlockPos position) {
		this.positionB = position;
		this.calculateOriginSize();
	}

	public void setOrigin(BlockPos origin) {
		this.origin = origin;
		this.positionA = origin;
		this.positionB = origin.add(this.size);
	}

	public void setSize(Vec3i size) {
		this.size = size;
		this.positionA = this.origin;
		this.positionB = origin.add(this.size);
	}

	public BlockPos getA() {
		return this.positionA;
	}

	public BlockPos getB() {
		return this.positionB;
	}

	public BlockPos getOrigin() {
		int oX = Math.min(this.getA().getX(), this.getB().getX());
		int oY = Math.min(this.getA().getY(), this.getB().getY());
		int oZ = Math.min(this.getA().getZ(), this.getB().getZ());
		return new BlockPos(oX, oY, oZ);
	}

	public Vec3i getSize() {
		Vec3i difference = this.getA().subtract(this.getB());
		return new Vec3i(Math.abs(difference.getX()), Math.abs(difference.getY()), Math.abs(difference.getZ()));
	}

	public boolean isWithinArea(Vec3d pos) {
		int x1 = Math.min(this.getA().getX(), this.getB().getX());
		int y1 = Math.min(this.getA().getY(), this.getB().getY());
		int z1 = Math.min(this.getA().getZ(), this.getB().getZ());
		int x2 = Math.max(this.getA().getX(), this.getB().getX());
		int y2 = Math.max(this.getA().getY(), this.getB().getY());
		int z2 = Math.max(this.getA().getZ(), this.getB().getZ());
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		return (x > x1 && x < x2) && (y > y1 && y < y2) && (z > z1 && z < z2);
	}

	@NotNull
	@Override
	public Iterator<BlockPos> iterator() {
		return BlockPos.iterate(this.getA(), this.getB()).iterator();
	}

	@Override
	public String toString() {
		return "Area{pos1: " + this.getA().toShortString() + ", pos2: " + this.getB().toShortString() + ", origin: " + this.getOrigin().toShortString() + ", size: " + this.getSize().toShortString() + "}";
	}
}
