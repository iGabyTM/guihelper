package me.gabytm.minecraft.guihelper.items.heads.exceptions

import me.gabytm.minecraft.guihelper.items.heads.providers.HeadIdProvider

class HeadIdProviderNotSupportByPluginException(provider: HeadIdProvider.Provider, plugin: String) : Exception() {

    override val message: String = "$provider is not supported by $plugin"

}