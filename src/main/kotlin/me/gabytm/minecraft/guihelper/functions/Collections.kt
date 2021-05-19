package me.gabytm.minecraft.guihelper.functions

/**
 * Run the function only if the collection is not empty
 * @param function function to run
 */
fun <E> Collection<E>.ifNotEmpty(function: (Collection<E>) -> Unit) {
    if (isNotEmpty()) {
        function(this)
    }
}