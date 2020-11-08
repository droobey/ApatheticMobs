package whizzball1.apatheticmobs.command;

import net.minecraftforge.event.RegisterCommandsEvent;


public class ModCommands {

    public static void registerCommands(RegisterCommandsEvent e) {
        CommandApatheticWhitelist.register(e.getDispatcher());
    }
}
