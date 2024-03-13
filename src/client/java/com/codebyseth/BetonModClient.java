package com.codebyseth;

import com.codebyseth.client.config.BetonConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class BetonModClient implements ClientModInitializer {

	public static BetonConfig config;

	@Override
	public void onInitializeClient() {
		AutoConfig.register(BetonConfig.class, GsonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(BetonConfig.class).getConfig();
	}

}