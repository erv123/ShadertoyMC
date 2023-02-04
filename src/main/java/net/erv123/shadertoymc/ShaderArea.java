package net.erv123.shadertoymc;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


interface IterableFunction {
    void run(double x, double y, double z);
}

public class ShaderArea {
    private static final Gson GSON = new Gson();


    Vec3i size;
    Vec3i origin;

    public static ShaderArea read() {
        Path areaPath = ShaderUtils.SHADERTOY_PATH.resolve("area.json");
        Gson gson = new Gson();
        if (Files.exists(areaPath)) {
            try (BufferedReader reader = Files.newBufferedReader(areaPath)) {
                return gson.fromJson(reader, ShaderArea.class);
            } catch (IOException | JsonParseException e) {
                ShadertoyMC.LOGGER.error("Failed to read area", e);
                return null;
            }
        }
        return null;
    }

    public ShaderArea(Vec3i pos1, Vec3i pos2) {
        this.size = new Vec3i(Math.abs(pos1.getX() - pos2.getX()), Math.abs(pos1.getY() - pos2.getY()), Math.abs(pos1.getZ() - pos2.getZ()));
        this.origin = new Vec3i(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
        ShadertoyMC.LOGGER.info("Pre save");
        this.save();
    }

    public void save() {
        ShadertoyMC.LOGGER.info("Staring save");
        String json = GSON.toJson(this);
        ShadertoyMC.LOGGER.info("File creation");
        try {
            Files.createDirectory(ShaderUtils.SHADERTOY_PATH);
        } catch (IOException e) {
            ShadertoyMC.LOGGER.error("Directory already exists", e);
        }
        Path areaPath = ShaderUtils.SHADERTOY_PATH.resolve("area.json");
        try (BufferedWriter writer = Files.newBufferedWriter(areaPath)) {
            ShadertoyMC.LOGGER.info(json);
            writer.write(json);
        } catch (IOException e) {
            ShadertoyMC.LOGGER.error("Failed to save area", e);
        }
    }

    public void setOrigin(Vec3i origin) {
        this.origin = origin;
        this.save();
    }

    public void setSize(Vec3i size) {
        this.size = size;
        this.save();
    }

    public void iterateThroughAllBlocks(IterableFunction function) {
        for (int dx = 0; dx < this.size.getX(); dx++) {
            for (int dy = 0; dy < this.size.getY(); dy++) {
                for (int dz = 0; dz < this.size.getZ(); dz++) {
                    function.run(this.origin.getX() + dx, this.origin.getY() + dy, this.origin.getZ() + dz);
                }
            }
        }
    }

    public void clear(ServerCommandSource source) {
        this.iterateThroughAllBlocks((x, y, z) -> {
            ServerWorld world = source.getWorld();
            world.breakBlock(new BlockPos(x, y, z), false);
        });
    }

    public List<Integer> getParams() {
        List<Integer> list = new ArrayList<>(6);
        list.add(this.origin.getX());
        list.add(this.origin.getY());
        list.add(this.origin.getZ());
        list.add(this.size.getX());
        list.add(this.size.getY());
        list.add(this.size.getZ());
        return list;
    }
}