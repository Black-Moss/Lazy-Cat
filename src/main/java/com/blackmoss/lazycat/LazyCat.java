package com.blackmoss.lazycat;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

//@EventBusSubscriber(modid = LazyCat.MODID)
@Mod(LazyCat.MODID)
public class LazyCat {
    public static final String MODID = "lazycat";
    public static final Logger LOGGER = LogUtils.getLogger();

    public LazyCat(IEventBus modEventBus, ModContainer modContainer) {
    }
}