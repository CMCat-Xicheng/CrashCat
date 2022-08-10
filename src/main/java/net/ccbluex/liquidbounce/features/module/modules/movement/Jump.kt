package net.ccbluex.liquidbounce.features.module.modules.movement

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.player.EntityPlayer

@ModuleInfo(name = "Jump", category = ModuleCategory.MOVEMENT, canEnable = false)
class Jump : Module() {
        mc.thePlayer.jump();
}
