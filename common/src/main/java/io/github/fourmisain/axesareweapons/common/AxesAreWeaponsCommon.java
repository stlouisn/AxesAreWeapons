package io.github.fourmisain.axesareweapons.common;

import io.github.fourmisain.axesareweapons.common.config.AxesAreWeaponsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Jankson;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AxesAreWeaponsCommon {

	public static final String MOD_ID = "axesareweapons";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static ThreadLocal<DynamicRegistryManager> registryManager = ThreadLocal.withInitial(() -> null);

	public static Identifier id(String id) {
		return Identifier.of(MOD_ID, id);
	}

	public final static AxesAreWeaponsConfig CONFIG;
	static {
		Jankson jankson = new Jankson.Builder()
			.registerSerializer(Identifier.class, ((identifier, marshaller) -> marshaller.serialize(identifier.toString())))
			.registerDeserializer(String.class, Identifier.class, (object, marshaller) -> Identifier.of(object)).build();

		ConfigHolder<AxesAreWeaponsConfig> configHolder = AutoConfig.register(AxesAreWeaponsConfig.class, (config, configClass) -> new JanksonConfigSerializer<>(config, configClass, jankson));
		CONFIG = configHolder.getConfig();
	}

	public static void commonInit() {

	}

	public static boolean isWeapon(Item item, boolean checkTags) {
		var entry = item.getRegistryEntry();

		return item instanceof AxeItem
			|| CONFIG.weaponIds.contains(Registries.ITEM.getId(item));
	}

	public static boolean isSpeedyWeb(Item item, BlockState state) {
		return CONFIG.fastCobWebBreaking && state.getBlock() == Blocks.COBWEB && isWeapon(item, true);
	}
}
