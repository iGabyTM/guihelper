package me.gabytm.minecraft.guihelper.utils

import me.gabytm.minecraft.guihelper.functions.color

enum class Message(var value: String) {

    EMPTY_GUI("&cPlease add some items to the GUI first!");

    init {
        value = value.color()
    }

}