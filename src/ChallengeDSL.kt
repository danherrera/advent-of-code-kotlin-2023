import java.io.File

class FileConfig(
        private val day: Int,
        private val fileNameSuffix: String = "",
) {
    var file: File? = null
    var fileName: String? = null
    val fileToUse
        get() = file
                ?: File((fileName ?: "./src/Day${day.toString().padStart(2, '0')}$fileNameSuffix.txt"))

    val inputByLines: List<String> get() = fileToUse.readLines()
}

class DayContext(val day: Int) {
    val inputFileConfig = FileConfig(day)
    val sampleInputFileConfig = FileConfig(day, "_test")
}

fun day(number: Int, f: DayContext.() -> Unit) {
    DayContext(number).f()
}

fun <I, O> DayContext.part(
        number: Int,
        fileConfigPartInput: FileConfig.() -> PartInput<*> = { PartInput.AsListOfStrings(inputByLines) },
        f: PartContext<I, O>.(I) -> O,
) {
    val ensuredSamplePartInput = (sampleInputFileConfig.fileConfigPartInput() as? PartInput<I>) ?: run {
        log("Please specify a valid/matching sample part input and type")
        return
    }
    log("Checking part $number sample...")
    val samplePartContext = PartContext<I, O>(number, isSample = true)
    samplePartContext.dayContext = this
    val sampleResult = samplePartContext.f(ensuredSamplePartInput.value)
    samplePartContext.answer(sampleResult)

    val ensuredPartInput = (inputFileConfig.fileConfigPartInput() as? PartInput<I>) ?: run {
        log("Please specify a valid/matching part input and type")
        return
    }
    log("Checking part $number...")
    val partContext = PartContext<I, O>(number)
    partContext.dayContext = this
    val result = partContext.f(ensuredPartInput.value)
    partContext.answer(result)
}

private fun log(message: String) = println(message)

class PartContext<I, O>(
        private val number: Int,
        private val isSample: Boolean = false,
) {
    lateinit var dayContext: DayContext
    var sampleFileConfig: FileConfig? = null

    private var expectation: O? = null

    fun expect(sample: O) {
        expectation = sample
    }

    fun answer(value: O): O {
        val sampleLabel = if (isSample) " sample" else ""
        log("Part $number$sampleLabel: $value")
        expectation?.let {
            if (isSample) {
                check(value == it) { "Answer `$value` does not match expected `$expectation`" }
                log("✅ Part $number sample matches expected $expectation")
            }
        }
        return value
    }
}

sealed class PartInput<I>(val value: I) {
    data class AsListOfStrings(val input: List<String>) : PartInput<List<String>>(input)

    data class AsFile(val file: File) : PartInput<File>(file)
}

sealed class PartResult<T> {
    data object Unspecified : PartResult<Nothing>()
    data class Answer<T>(val value: T) : PartResult<T>()
}