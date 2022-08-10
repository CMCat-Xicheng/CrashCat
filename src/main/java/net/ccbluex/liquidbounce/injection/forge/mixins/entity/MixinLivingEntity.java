package net.ccbluex.liquidbounce.injection.mixins.minecraft.entity;

import net.ccbluex.liquidbounce.event.EventManager;
import net.ccbluex.liquidbounce.event.PlayerJumpEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.AirJump;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends MixinEntity {

    @Shadow
    protected abstract float getJumpVelocity();

    @Shadow
    public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    @Nullable
    public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow
    public abstract ItemStack getMainHandStack();

    @Shadow
    private int jumpingCooldown;

    @Shadow protected abstract void jump();

    @Shadow protected boolean jumping;

    /**
     * Hook anti levitation module
     */
    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z"))
    public boolean hookTravelStatusEffect(LivingEntity livingEntity, StatusEffect effect) {
        if ((effect == StatusEffects.LEVITATION || effect == StatusEffects.SLOW_FALLING) &&
                ModuleAntiLevitation.INSTANCE.getEnabled()) {
            livingEntity.fallDistance = 0f;
            return false;
        }

        return livingEntity.hasStatusEffect(effect);
    }

    @Inject(method = "hasStatusEffect", at = @At("HEAD"), cancellable = true)
    private void hookAntiNausea(StatusEffect effect, CallbackInfoReturnable<Boolean> cir) {
        if (effect == StatusEffects.NAUSEA && ModuleAntiBlind.INSTANCE.getEnabled() && ModuleAntiBlind.INSTANCE.getAntiNausea()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Redirect(method = "jump", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getJumpVelocity()F"))
    private float hookJumpEvent(LivingEntity entity) {
        // Check if entity is client user
        if ((Object) this == MinecraftClient.getInstance().player) {
            // Hook player jump event
            final PlayerJumpEvent jumpEvent = new PlayerJumpEvent(getJumpVelocity());
            EventManager.INSTANCE.callEvent(jumpEvent);
            if (jumpEvent.isCancelled()) {
                return 0f;
            }
            return jumpEvent.getMotion();
        }
        return getJumpVelocity();
    }

    @Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
    private void hookNoPush(CallbackInfo callbackInfo) {
        if (ModuleNoPush.INSTANCE.getEnabled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "tickMovement", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;jumping:Z"))
    private void hookJump(CallbackInfo callbackInfo) {
        if (AirJump.state && jumping && jumpingCooldown == 0) {
            this.jump();
            jumpingCooldown = 10;
        }
    }

}
