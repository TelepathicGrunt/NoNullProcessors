package com.telepathicgrunt.no_null_processors.mixin;

import com.mojang.serialization.DynamicOps;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryReadOps;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.WeakHashMap;

@Mixin(RegistryReadOps.class)
public class RegistryReadOpsMixin {
    @Unique
    private static final Map<ResourceManager, RegistryReadOps<?>> cachedOps = new WeakHashMap<>();

    @Inject(method = "create(Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/resources/RegistryReadOps;",
            at = @At("HEAD"),
            cancellable = true)
    private static <T> void cacheOpsForPoolElementStructurePiece1(DynamicOps<T> ops, ResourceManager manager, RegistryAccess registryAccess, CallbackInfoReturnable<RegistryReadOps<T>> cir) {
        if (ops == NbtOps.INSTANCE) {
            RegistryReadOps<?> nbtOps = cachedOps.get(manager);
            if (nbtOps != null) {
                cir.setReturnValue((RegistryReadOps<T>) nbtOps);
            }
        }
    }

    @Inject(method = "create(Lcom/mojang/serialization/DynamicOps;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/resources/RegistryReadOps;",
            at = @At("RETURN"),
            cancellable = true)
    private static <T> void cacheOpsForPoolElementStructurePiece2(DynamicOps<T> ops, ResourceManager manager, RegistryAccess registryAccess, CallbackInfoReturnable<RegistryReadOps<T>> cir) {
        if (ops == NbtOps.INSTANCE) {
            cachedOps.put(manager, cir.getReturnValue());
        }
    }
}