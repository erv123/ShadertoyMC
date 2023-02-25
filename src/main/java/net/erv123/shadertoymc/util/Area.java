package net.erv123.shadertoymc.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class Area implements Iterable<BlockPos> {
	private BlockPos positionA;
	private BlockPos positionB;

	public Area(BlockPos position) {
		this.positionA = position;
		this.positionB = position;
	}

	public void setA(BlockPos position) {
		this.positionA = position;
	}

	public void setB(BlockPos position) {
		this.positionB = position;
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

	@NotNull
	@Override
	public Iterator<BlockPos> iterator() {
		return BlockPos.iterate(this.getA(), this.getB()).iterator();
	}

	@Override
	public String toString() {
		return "Area{pos1: " + this.getA().toShortString() + ", pos2: " + this.getB().toShortString() + "}";
	}
}
