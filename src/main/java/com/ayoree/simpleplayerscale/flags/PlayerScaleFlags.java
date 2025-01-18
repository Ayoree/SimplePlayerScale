package com.ayoree.simpleplayerscale.flags;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

public class PlayerScaleFlags {
    public static StateFlag playerScaleFlag;

    public static void initPlayerScaleFlag() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag("player-scale", false);
            registry.register(flag);
            playerScaleFlag = flag;
        } catch (FlagConflictException e) {
            Flag<?> existing = registry.get("player-scale");
            if (existing instanceof StateFlag) {
                playerScaleFlag = (StateFlag) existing;
            }
        }
    }
}
