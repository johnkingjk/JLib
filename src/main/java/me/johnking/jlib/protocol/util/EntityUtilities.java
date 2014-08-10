package me.johnking.jlib.protocol.util;

import me.johnking.jlib.reflection.ReflectionUtil;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * Created by Marco on 29.06.2014.
 */
public class EntityUtilities {

    private static final Method GET_WORLD_HANDLE = ReflectionUtil.getMethod(ReflectionUtil.getCraftBukkitClass("CraftWorld"), "getHandle");
    private static final Field ENTITY_TRACKER = ReflectionUtil.getFieldByType(ReflectionUtil.getMinecraftClass("WorldServer"), ReflectionUtil.getMinecraftClass("EntityTracker"));
    private static final Field TRACKED_ENTITIES = ReflectionUtil.getFieldByType(ReflectionUtil.getMinecraftClass("EntityTracker"), ReflectionUtil.getMinecraftClass("IntHashMap"));
    private static final Method ENTITY_TRACKER_ENTRY = ReflectionUtil.getMethod(ReflectionUtil.getMinecraftClass("IntHashMap"), new Class[]{Integer.TYPE}, Object.class);
    private static final Field TRACKER_PLAYER = ReflectionUtil.getFieldByType(ReflectionUtil.getMinecraftClass("EntityTrackerEntry"), Set.class);
    private static final Method SCAN_PLAYERS = ReflectionUtil.getMethod(ReflectionUtil.getMinecraftClass("EntityTrackerEntry"), "scanPlayers", new Class[]{List.class});
    private static final Method GET_HANDLE = ReflectionUtil.getMethod(ReflectionUtil.getCraftBukkitClass("entity.CraftEntity"), "getHandle");
    private static final Field KILLER_FIELD = ReflectionUtil.getField(ReflectionUtil.getMinecraftClass("EntityLiving"), "killer");
    private static final Field TIME_FIELD = ReflectionUtil.getField(ReflectionUtil.getMinecraftClass("EntityLiving"), "lastDamageByPlayerTime");


    public static void updateEntity(Entity entity, List<Player> player) {
        try {
            Object trackerEntry = getEntityTrackerEntry(entity.getWorld(), entity.getEntityId());

            for(int i = player.size(); i > 0; i--){
                if(!player.get(i - 1).getWorld().equals(entity.getWorld())){
                    player.remove(i - 1);
                }
            }

            Set trackedPlayer = (Set) TRACKER_PLAYER.get(trackerEntry);
            List<Object> nmsPlayer = PlayerUtilities.getNMSPlayers(player);

            trackedPlayer.removeAll(nmsPlayer);

            SCAN_PLAYERS.invoke(trackerEntry, nmsPlayer);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Object getEntityTrackerEntry(World world, int entityId){
        try {
            Object worldServer = GET_WORLD_HANDLE.invoke(world);

            Object entityTracker = ENTITY_TRACKER.get(worldServer);

            Object trackedEntities = TRACKED_ENTITIES.get(entityTracker);

            return ENTITY_TRACKER_ENTRY.invoke(trackedEntities, entityId);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void setKiller(LivingEntity entityLiving, Player player, int time){
        Object entityHandle = getHandle(entityLiving);
        Object playerHandle = getHandle(player);

        try {
            KILLER_FIELD.set(entityHandle, playerHandle);

            TIME_FIELD.set(entityHandle, time);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static Object getHandle(Entity entity){
        try {
            return GET_HANDLE.invoke(entity);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
