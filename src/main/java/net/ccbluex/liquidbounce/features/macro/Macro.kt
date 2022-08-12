package net.ccbluex.liquidbounce.features.macro

import net.ccbluex.liquidbounce.LiquidBounce
import net.minecraft.entity.player.EntityPlayer

class Macro(val key: Int, val command: String) {
    fun exec() {
        mc.thePlayer.sendChatMessage(command)
    }
}
