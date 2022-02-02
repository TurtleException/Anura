package de.eldritch.anura.core.module;

import de.eldritch.anura.core.AnuraInstance;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.logging.Level;

public class ModuleManager {
    private final AnuraInstance instance;

    private final HashSet<AnuraModule> modules = new HashSet<>();

    public ModuleManager(@NotNull AnuraInstance instance) {
        this.instance = instance;
    }


    /**
     * Registers modules by instantiating them one at a time.
     */
    public void registerModule(@NotNull Class<? extends AnuraModule> clazz, Object... params) {
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
    public long countEnabled() {
        return modules.stream().filter(AnuraModule::isEnabled).count();
    }
}