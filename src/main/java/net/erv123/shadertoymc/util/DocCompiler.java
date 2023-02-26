package net.erv123.shadertoymc.util;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import me.senseiwells.arucas.api.ArucasAPI;
import me.senseiwells.arucas.api.docs.visitor.ArucasDocParser;
import me.senseiwells.arucas.api.docs.visitor.impl.JsonDocVisitor;
import me.senseiwells.arucas.api.docs.visitor.impl.MarkdownDocVisitor;
import me.senseiwells.arucas.api.docs.visitor.impl.VSCSnippetDocVisitor;
import me.senseiwells.arucas.utils.Util;
import net.erv123.shadertoymc.ShadertoyMC;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class DocCompiler implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		String[] args = Arrays.stream(FabricLoader.getInstance().getLaunchArguments(true)).filter(opt -> !opt.equals("--")).toArray(String[]::new);

		// Prepare an OptionParser for our parameters
		OptionParser parser = new OptionParser();
		OptionSpec<String> pathSpec = parser.accepts("generate").withRequiredArg();

		// Minecraft may need more stuff later that we don't want to special-case
		parser.allowsUnrecognizedOptions();
		OptionSet options = parser.parse(args);

		// If our flag isn't set, continue regular launch
		if (!options.has(pathSpec)) {
			return;
		}

		Util.File file = Util.File.INSTANCE;
		Path path = file.ensureExists(Path.of(options.valueOf(pathSpec)));

		System.out.println(path);
		try {
			System.out.println(path.toRealPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		ArucasAPI api = ScriptUtils.generateApi();
		Path libPath = file.ensureExists(path.resolve("libs"));
		Path docPath = file.ensureExists(path.getParent().resolve("docs"));
		Path jsonPath = file.ensureExists(path.resolve("json"));
		Path mdPath = file.ensureExists(path.resolve("markdown"));
		Path snippetPath = file.ensureExists(path.resolve("snippets"));

		JsonDocVisitor jsonVisitor = new JsonDocVisitor();
		MarkdownDocVisitor markdownVisitor = new MarkdownDocVisitor();
		VSCSnippetDocVisitor snippetVisitor = new VSCSnippetDocVisitor();

		new ArucasDocParser(api).addVisitors(jsonVisitor, markdownVisitor, snippetVisitor).parse();

		try {
			Files.writeString(jsonPath.resolve("AllDocs.json"), jsonVisitor.getJson());
			api.generateNativeFiles(libPath);

			Files.writeString(snippetPath.resolve("arucas.json"), snippetVisitor.getJson());

			String extensions = markdownVisitor.getExtensions();
			String classes = markdownVisitor.getClasses();
			Files.writeString(mdPath.resolve("Extensions.md"), extensions);
			Files.writeString(mdPath.resolve("Classes.md"), classes);


			String full = Util.Network.INSTANCE.getStringFromUrl("https://raw.githubusercontent.com/senseiwells/Arucas/main/docs/FullLang.md");
			full += "\n\n" + extensions;
			full += "\n\n" + classes;
			Files.writeString(docPath.resolve("Full.md"), full);
		} catch (IOException e) {
			ShadertoyMC.LOGGER.info("Failed to generate docs", e);
		}
		System.exit(0);
	}

}
