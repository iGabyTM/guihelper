import me.gabytm.minecraft.guihelper.command.arg.ArgumentParser
import me.gabytm.minecraft.guihelper.command.arg.impl.EnumArgument
import me.gabytm.minecraft.guihelper.command.arg.impl.IntegerArgument
import java.lang.IllegalArgumentException

fun main() {
	val list = listOf(
		IntegerArgument("page", true, 0, false, emptyList()),
		IntegerArgument("test", true, 0, true, emptyList()),
		IntegerArgument("something", false, 0, true, emptyList()),
		IntegerArgument("def", true, 25, false, emptyList()),
		EnumArgument("enum", true, TestEnum::class, null, false, emptyList())
	)

	try {
		val arguments = ArgumentParser().parse("page:25 test:255 something def:55", list)
		println("Page: ${arguments.get<Int>("page")}")
		println("Def: ${arguments.get<Int>("def")}")
		println("Enum: ${arguments.get<TestEnum>("enum")}")
		println("Something exists: ${arguments.exists("something")}")
	} catch (e: IllegalArgumentException) {
		println(e.message)
	}
}
