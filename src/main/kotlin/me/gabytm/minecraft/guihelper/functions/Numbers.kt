package me.gabytm.minecraft.guihelper.functions

/**
 * Run code only if the number is greater than 0
 * @param function function to run
 */
fun Int.ifNotZero(function: (Int) -> Unit) {
    if (this > 0) {
        function(this)
    }
}

/**
 * Run code only if the number is greater than 0
 * @param function function to run
 */
fun Short.ifNotZero(function: (Short) -> Unit) {
    if (this > 0) {
        function(this)
    }
}