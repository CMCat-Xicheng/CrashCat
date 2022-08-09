package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo

@ModuleInfo(name = "AirJump", category = ModuleCategory.MOVEMENT)
class AirJump : Module() {
    @EventTarget
    fun onUpdate(event: UpdateEvent) {
      if (mc.gameSettings.keyBindJump.pressed && !mc.thePlayer.onGround) mc.thePlayer.jump()
    }
}
