package whizzball1.apatheticmobs.command;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;


import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import whizzball1.apatheticmobs.data.WhitelistData;

public class CommandApatheticWhitelist {

    private List<String> subCommands = new ArrayList<>();
    
    public CommandApatheticWhitelist() {
        subCommands.add("get");
        subCommands.add("add");
        subCommands.add("remove");
    }

    public String getName() {
        return "apatheticwhitelist";
    }


    public int getRequiredPermissionLevel() {
        return 2;
    }

 

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(Commands.literal("apatheticwhitelist")
				.requires(commandSource -> commandSource.hasPermissionLevel(2))
				.then(Commands.literal("get")
						.executes(context -> {
							UUID id = context.getSource().asPlayer().getUniqueID();
							WhitelistData data = WhitelistData.get(context.getSource().getWorld());
							StringBuilder b = new StringBuilder();
			                for (UUID i : data.playerSet) {
			                    b.append(context.getSource().getServer().getPlayerProfileCache().getProfileByUUID(i).getName());
			                    b.append(", ");
			                }
			                b.reverse();
			                try {
			                    b.delete(0, 2);
			                } catch (StringIndexOutOfBoundsException e) {

			                }
			                b.reverse();
			                
							return 1;
						})
				)
				.then(Commands.literal("add")
						.then(Commands.argument("username", StringArgumentType.word())
						.executes(context -> {
							String username  = StringArgumentType.getString(context, "username");
							UUID id = context.getSource().getServer().getPlayerProfileCache().getGameProfileForUsername(username).getId();
							WhitelistData data = WhitelistData.get(context.getSource().getWorld());
							if (!data.playerSet.contains(id)) {
			                    data.playerSet.add(id);
			                    data.markDirty();
			                } else {
			                    context.getSource().asPlayer().sendMessage(new StringTextComponent("commands.apatheticmobs.aw.playerfound"), null);
			                }
							return 1;
						})
				)
				)
				.then(Commands.literal("remove")
						.then(Commands.argument("username", StringArgumentType.word())
						.executes(context -> {
							String username  = StringArgumentType.getString(context, "username");
							UUID id = context.getSource().getServer().getPlayerProfileCache().getGameProfileForUsername(username).getId();
							WhitelistData data = WhitelistData.get(context.getSource().getWorld());
							 if (data.playerSet.contains(id)) {
				                    data.playerSet.remove(id);
				                    data.markDirty();
				                } else {
				                	context.getSource().asPlayer().sendMessage(new StringTextComponent("commands.apatheticmobs.aw.playerfailed"), null);
				                }

							return 1;
						})
				)
				)
		);
		
		
	}
}
