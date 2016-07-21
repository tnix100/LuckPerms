package me.lucko.luckperms.commands.group.subcommands;

import me.lucko.luckperms.LuckPermsPlugin;
import me.lucko.luckperms.commands.Sender;
import me.lucko.luckperms.commands.group.GroupSubCommand;
import me.lucko.luckperms.constants.Message;
import me.lucko.luckperms.constants.Permission;
import me.lucko.luckperms.exceptions.ObjectLacksException;
import me.lucko.luckperms.groups.Group;
import me.lucko.luckperms.utils.Patterns;

import java.util.List;

public class GroupUnsetInherit extends GroupSubCommand {
    public GroupUnsetInherit() {
        super("unsetinherit", "Unsets another group for this group to inherit permissions from",
                "/%s group <group> unsetinherit <group> [server]", Permission.GROUP_UNSETINHERIT);
    }

    @Override
    protected void execute(LuckPermsPlugin plugin, Sender sender, Group group, List<String> args, String label) {
        String groupName = args.get(0).toLowerCase();

        if (groupName.contains("/") || groupName.contains("$")) {
            sendUsage(sender, label);
            return;
        }

        try {
            if (args.size() == 2) {
                final String server = args.get(1).toLowerCase();
                if (Patterns.NON_ALPHA_NUMERIC.matcher(server).find()) {
                    Message.SERVER_INVALID_ENTRY.send(sender);
                    return;
                }

                group.unsetPermission("group." + groupName, server);
                Message.GROUP_UNSETINHERIT_SERVER_SUCCESS.send(sender, group.getName(), groupName, server);
            } else {
                group.unsetPermission("group." + groupName);
                Message.GROUP_UNSETINHERIT_SUCCESS.send(sender, group.getName(), groupName);
            }

            saveGroup(group, sender, plugin);
        } catch (ObjectLacksException e) {
            Message.GROUP_DOES_NOT_INHERIT.send(sender, group.getName(), groupName);
        }
    }

    @Override
    public List<String> onTabComplete(Sender sender, List<String> args, LuckPermsPlugin plugin) {
        return getGroupTabComplete(args, plugin);
    }

    @Override
    public boolean isArgLengthInvalid(int argLength) {
        return argLength != 1 && argLength != 2;
    }
}
