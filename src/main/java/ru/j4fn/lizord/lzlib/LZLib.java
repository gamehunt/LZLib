package ru.j4fn.lizord.lzlib;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Constants.MODID)
public class LZLib {
    private Logger logger;

    public Logger getLogger() {
        return logger;
    }

    @Mod.Instance(Constants.MODID)
    public static LZLib instance = null;

    @SidedProxy(serverSide = "ru.j4fn.lizord.lzlib.CommonProxy", clientSide = "ru.j4fn.lizord.lzlib.ClientProxy")
    public static CommonProxy proxy = null;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e){
        logger = e.getModLog();
        proxy.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e){
        proxy.postInit(e);
    }
}
