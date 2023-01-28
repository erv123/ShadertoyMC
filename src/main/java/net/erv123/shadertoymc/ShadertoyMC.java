package net.erv123.shadertoymc;


import me.senseiwells.arucas.api.ArucasAPI;
import me.senseiwells.arucas.api.ThreadHandler;
import me.senseiwells.arucas.core.Interpreter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;





/*
TODO:
make command actually work
save area in json
arucas files
*/

public class ShadertoyMC implements ModInitializer {
	public static final String MOD_ID = "shadertoymc";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);


	@Override
	public void onInitialize() {

		ArucasAPI api = new ArucasAPI.Builder()
				.addDefault()
				.build();


		Interpreter.of("print(\"f\");","name",api,ThreadHandler::new).getThreadHandler().executeAsync();
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			ShadertoyCommand.register(dispatcher);
		});

		if(ShaderArea.read()==null){
			ShadertoyMC.LOGGER.info("Time to initialize an area file");
			new ShaderArea(Vec3i.ZERO, Vec3i.ZERO);
		}


	}
}
