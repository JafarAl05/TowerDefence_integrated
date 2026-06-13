package be.uantwerpen.fti.ei.Game.Scripting;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Loads the wave-balancing Lua file.
 */
public final class LuaWaveScript {
    private static final Pattern ASSIGNMENT = Pattern.compile("^\\s*([A-Za-z_][A-Za-z0-9_]*)\\s*=\\s*([0-9]+(?:\\.[0-9]+)?)\\s*$");
    private final Map<String, Double> values = new HashMap<>();
    private Optional<Object> luaGlobals;
    private Optional<Class<?>> luaValueClass;

    public LuaWaveScript(String path) {
        values.put("speedScalePerWave", 0.15);
        values.put("healthScalePerWave", 0.20);
        values.put("rewardScalePerWave", 0.08);
        luaGlobals = Optional.empty();
        luaValueClass = Optional.empty();
        loadConstants(Path.of(path));
        tryLoadWithLuaJ(Path.of(path));
    }

    public double speedMultiplier(int wave) {
        return callLuaFunction("getEnemySpeedMultiplier", wave)
                .orElse(1.0 + values.get("speedScalePerWave") * Math.max(0, wave - 1));
    }

    public double healthMultiplier(int wave) {
        return callLuaFunction("getEnemyHealthMultiplier", wave)
                .orElse(1.0 + values.get("healthScalePerWave") * Math.max(0, wave - 1));
    }

    public double rewardMultiplier(int wave) {
        return callLuaFunction("getEnemyRewardMultiplier", wave)
                .orElse(1.0 + values.get("rewardScalePerWave") * Math.max(0, wave - 1));
    }

    private void tryLoadWithLuaJ(Path path) {
        try {
            Class<?> platformClass = Class.forName("org.luaj.vm2.lib.jse.JsePlatform");
            Class<?> valueClass = Class.forName("org.luaj.vm2.LuaValue");
            Object globals = platformClass.getMethod("standardGlobals").invoke(platformClass);
            Object chunk = globals.getClass().getMethod("loadfile", String.class).invoke(globals, path.toString());
            valueClass.getMethod("call").invoke(chunk);
            luaGlobals = Optional.of(globals);
            luaValueClass = Optional.of(valueClass);
        } catch (ClassNotFoundException exception) {
            // LuaJ is optional. Fallback parser remains active.
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
            System.err.println("LuaJ was found but the Lua script could not be executed. Fallback parser active. Reason: " + exception.getMessage());
        }
    }

    private Optional<Double> callLuaFunction(String functionName, int wave) {
        if (!luaGlobals.isPresent() || !luaValueClass.isPresent()) {
            return Optional.empty();
        }
        try {
            Class<?> valueClass = luaValueClass.get();
            Object globals = luaGlobals.get();
            Object function = globals.getClass().getMethod("get", String.class).invoke(globals, functionName);
            Object waveArgument = valueClass.getMethod("valueOf", int.class).invoke(valueClass, wave);
            Object result = valueClass.getMethod("call", valueClass).invoke(function, waveArgument);
            Object number = valueClass.getMethod("todouble").invoke(result);
            return Optional.of((Double) number);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassCastException exception) {
            return Optional.empty();
        }
    }

    private void loadConstants(Path path) {
        try {
            for (String line : Files.readAllLines(path)) {
                String noComment = line.split("--", 2)[0].trim();
                Matcher matcher = ASSIGNMENT.matcher(noComment);
                if (matcher.matches()) {
                    values.put(matcher.group(1), Double.parseDouble(matcher.group(2)));
                }
            }
        } catch (IOException exception) {
            System.err.println("Could not load Lua script '" + path + "'. Using defaults. Reason: " + exception.getMessage());
        }
    }
}
