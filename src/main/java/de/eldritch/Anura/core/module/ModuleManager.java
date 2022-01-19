package de.eldritch.Anura.core.module;

import de.eldritch.Anura.core.AnuraInstance;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;

public class ModuleManager {
    private final AnuraInstance instance;

    private final HashSet<AnuraModule> modules = new HashSet<>();

    @SafeVarargs
    public ModuleManager(@NotNull AnuraInstance instance, Map.Entry<Class<? extends AnuraModule>, Object[]>... modules) {
        this.instance = instance;

        this.registerModules(new HashMap<>(Map.ofEntries(modules)));
    }


    /**
     * Registers modules by instantiating them one at a time.
     */
    public void registerModules(HashMap<Class<? extends AnuraModule>, Object[]> modClasses) {
        for (Map.Entry<Class<? extends AnuraModule>, Object[]> entry : modClasses.entrySet()) {
            Class<? extends AnuraModule> clazz = entry.getKey();
            Object[] params = entry.getValue();
            Class<?>[] paramTypes = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++) {
                paramTypes[i] = params[i].getClass();
            }

            AnuraModule obj;
            try {
                // instantiate
                obj = clazz.getConstructor(paramTypes).newInstance(params);

                // add to set
                if (!modules.add(obj)) // stop if another object of this module has already been registered
                    throw new AnuraModuleEnableException(clazz.getSimpleName() + " already exists.");
                instance.getLogger().info("Registered " + clazz.getSimpleName() + ".");
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | AnuraModuleEnableException e) {
                instance.getLogger().log(Level.WARNING, "Unable to instantiate " + clazz.getSimpleName() + ". It will be ignored.", e);
            }
        }
    }

    /**
     * @return Set of all currently registered modules.
     */
    public HashSet<AnuraModule> getRegisteredModules() {
        return this.modules;
    }

    public boolean unregister(AnuraModule module) {
        if (modules.contains(module)) {
            module.setEnabled(false);
            return modules.remove(module);
        }
        return false;
    }

    /**
     * Returns the amount of enabled modules.
     * @return The amount of enabled modules.
     */
    public long getEnabled() {
        return modules.stream().filter(AnuraModule::isEnabled).count();
    }
}