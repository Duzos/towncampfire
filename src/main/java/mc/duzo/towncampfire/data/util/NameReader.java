package mc.duzo.towncampfire.data.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import mc.duzo.towncampfire.TCMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NameReader {
	private final ResourceLocation path;
	private List<String> cache;

	protected NameReader(ResourceLocation path) {
		this.path = path;
	}
	protected NameReader(String path) {
		this(new ResourceLocation(TCMod.MODID, path));
	}

	public String get() {
		if (shouldGenerate())
			load();

		if (cache == null)
			return "";

		return cache.get(TCMod.RANDOM.nextInt(cache.size()));
	}

	private boolean shouldGenerate() {
		return (cache == null || cache.isEmpty());
	}

	private void load() {
		if (cache == null)
			cache = new ArrayList<>();

		cache.clear();

		try {
			Optional<Resource> resource = ServerLifecycleHooks.getCurrentServer().getResourceManager().getResource(path);

			if (resource.isEmpty()) {
				TCMod.LOGGER.error("ERROR in {}", path);
				TCMod.LOGGER.error("Missing Resource");
				return;
			}

			InputStream stream = resource.get().open();

			JsonArray list = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonArray();

			for (JsonElement element : list) {
				cache.add(element.getAsString());
			}
		} catch (IOException e) {
			TCMod.LOGGER.error("ERROR in {}", path, e);
		}
	}
}
