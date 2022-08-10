package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.player.EntityPlayer

@ModuleInfo(name = "AirJump", category = ModuleCategory.MOVEMENT)
class AirJump : Module() {
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
      if (mc.thePlayer.jumping && mc.thePlayer.jumpingCooldown == 0){
          mc.thePlayer.jump();
          mc.thePlayer.jumpingCooldown = 10;
      }
    }
}
