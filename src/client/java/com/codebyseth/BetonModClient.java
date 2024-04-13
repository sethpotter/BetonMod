package com.codebyseth;

import com.codebyseth.client.config.BetonConfig;
import com.codebyseth.client.config.BetonConfigSerializer;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.ClientModInitializer;

public class BetonModClient implements ClientModInitializer {

	public static BetonConfig config;
	public static double mouseScroll;

	@Override
	public void onInitializeClient() {
		AutoConfig.register(BetonConfig.class, BetonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(BetonConfig.class).getConfig();
	}

}