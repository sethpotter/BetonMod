package com.codebyseth.client.config;

import com.google.gson.*;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.SimpleOption;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class BetonConfigSerializer extends GsonConfigSerializer<BetonConfig> {

    private static final Map<String, Object> valueMap = new HashMap<>();
    private static final BetonConfig templateConfig = new BetonConfig();

    public BetonConfigSerializer(Config definition, Class<BetonConfig> betonConfigClass, Gson gson) {
        super(definition, BetonConfig.class, gson);
    }

    public BetonConfigSerializer(Config definition, Class<BetonConfig> betonConfigClass) {
        this(definition, betonConfigClass, createGson());
    }

    public void postValidation(BetonConfig config) {
        for (SimpleOption option : config.options()) {
            Object value = valueMap.get(option.toString());
            option.setValue(value);
        }
    }

    private static SimpleOption<?> getOptionFromTemplate(String text) {
        for (SimpleOption option : templateConfig.options()) {
            if (option.toString().equals(text))
                return option;
        }
        return null;
    }

    private static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(SimpleOption.class, new SimpleOptionGsonSerializer());
        return gsonBuilder.setPrettyPrinting().create();
    }

    @Environment(EnvType.CLIENT)
    public static class SimpleOptionGsonSerializer implements JsonSerializer<SimpleOption<?>>, JsonDeserializer<SimpleOption<?>> {

        @Override
        public JsonElement serialize(SimpleOption<?> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", src.toString());
            obj.addProperty("type", src.getValue().getClass().getName());
            obj.addProperty("value", src.getValue().toString());
            return obj;
        }

        @Override
        public SimpleOption<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
        {
            JsonObject jsonObject = json.getAsJsonObject();

            String name = jsonObject.get("name").getAsString();

            Class<?> type;
            try {
                type = Class.forName(jsonObject.get("type").getAsString());
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e);
            }

            Object value = jsonObject.get("value").getAsString();

            if (Double.class.isAssignableFrom(type)) {
                value = Double.parseDouble((String) value);
            }

            //valueMap.put(name, value);
            SimpleOption templateOption = getOptionFromTemplate(name);
            templateOption.setValue(value);
            return templateOption;
        }
    }
}
