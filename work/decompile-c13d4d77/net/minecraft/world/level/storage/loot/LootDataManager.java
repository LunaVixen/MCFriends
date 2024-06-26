package net.minecraft.world.level.storage.loot;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import net.minecraft.resources.MinecraftKey;
import net.minecraft.server.packs.resources.IReloadListener;
import net.minecraft.server.packs.resources.IResourceManager;
import net.minecraft.server.packs.resources.ResourceDataJson;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.profiling.GameProfilerFiller;
import net.minecraft.world.level.storage.loot.parameters.LootContextParameterSets;
import org.slf4j.Logger;

public class LootDataManager implements IReloadListener, LootDataResolver {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = (new GsonBuilder()).create();
    public static final LootDataId<LootTable> EMPTY_LOOT_TABLE_KEY = new LootDataId<>(LootDataType.TABLE, LootTables.EMPTY);
    private Map<LootDataId<?>, ?> elements = Map.of();
    private Multimap<LootDataType<?>, MinecraftKey> typeKeys = ImmutableMultimap.of();

    public LootDataManager() {}

    @Override
    public final CompletableFuture<Void> reload(IReloadListener.a ireloadlistener_a, IResourceManager iresourcemanager, GameProfilerFiller gameprofilerfiller, GameProfilerFiller gameprofilerfiller1, Executor executor, Executor executor1) {
        Map<LootDataType<?>, Map<MinecraftKey, ?>> map = new HashMap();
        CompletableFuture<?>[] acompletablefuture = (CompletableFuture[]) LootDataType.values().map((lootdatatype) -> {
            return scheduleElementParse(lootdatatype, iresourcemanager, executor, map);
        }).toArray((i) -> {
            return new CompletableFuture[i];
        });
        CompletableFuture completablefuture = CompletableFuture.allOf(acompletablefuture);

        Objects.requireNonNull(ireloadlistener_a);
        return completablefuture.thenCompose(ireloadlistener_a::wait).thenAcceptAsync((ovoid) -> {
            this.apply(map);
        }, executor1);
    }

    private static <T> CompletableFuture<?> scheduleElementParse(LootDataType<T> lootdatatype, IResourceManager iresourcemanager, Executor executor, Map<LootDataType<?>, Map<MinecraftKey, ?>> map) {
        Map<MinecraftKey, T> map1 = new HashMap();

        map.put(lootdatatype, map1);
        return CompletableFuture.runAsync(() -> {
            Map<MinecraftKey, JsonElement> map2 = new HashMap();

            ResourceDataJson.scanDirectory(iresourcemanager, lootdatatype.directory(), LootDataManager.GSON, map2);
            map2.forEach((minecraftkey, jsonelement) -> {
                lootdatatype.deserialize(minecraftkey, jsonelement).ifPresent((object) -> {
                    map1.put(minecraftkey, object);
                });
            });
        }, executor);
    }

    private void apply(Map<LootDataType<?>, Map<MinecraftKey, ?>> map) {
        Object object = ((Map) map.get(LootDataType.TABLE)).remove(LootTables.EMPTY);

        if (object != null) {
            LootDataManager.LOGGER.warn("Datapack tried to redefine {} loot table, ignoring", LootTables.EMPTY);
        }

        Builder<LootDataId<?>, Object> builder = ImmutableMap.builder();
        com.google.common.collect.ImmutableMultimap.Builder<LootDataType<?>, MinecraftKey> com_google_common_collect_immutablemultimap_builder = ImmutableMultimap.builder();

        map.forEach((lootdatatype, map1) -> {
            map1.forEach((minecraftkey, object1) -> {
                builder.put(new LootDataId<>(lootdatatype, minecraftkey), object1);
                com_google_common_collect_immutablemultimap_builder.put(lootdatatype, minecraftkey);
            });
        });
        builder.put(LootDataManager.EMPTY_LOOT_TABLE_KEY, LootTable.EMPTY);
        ProblemReporter.a problemreporter_a = new ProblemReporter.a();
        final Map<LootDataId<?>, ?> map1 = builder.build();
        LootCollector lootcollector = new LootCollector(problemreporter_a, LootContextParameterSets.ALL_PARAMS, new LootDataResolver() {
            @Nullable
            @Override
            public <T> T getElement(LootDataId<T> lootdataid) {
                return map1.get(lootdataid);
            }
        });

        map1.forEach((lootdataid, object1) -> {
            castAndValidate(lootcollector, lootdataid, object1);
        });
        problemreporter_a.get().forEach((s, s1) -> {
            LootDataManager.LOGGER.warn("Found loot table element validation problem in {}: {}", s, s1);
        });
        this.elements = map1;
        this.typeKeys = com_google_common_collect_immutablemultimap_builder.build();
    }

    private static <T> void castAndValidate(LootCollector lootcollector, LootDataId<T> lootdataid, Object object) {
        lootdataid.type().runValidation(lootcollector, lootdataid, object);
    }

    @Nullable
    @Override
    public <T> T getElement(LootDataId<T> lootdataid) {
        return this.elements.get(lootdataid);
    }

    public Collection<MinecraftKey> getKeys(LootDataType<?> lootdatatype) {
        return this.typeKeys.get(lootdatatype);
    }
}
