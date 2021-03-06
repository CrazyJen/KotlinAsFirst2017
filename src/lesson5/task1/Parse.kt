@file:Suppress("UNUSED_PARAMETER")

package lesson5.task1

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main(args: Array<String>) {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}

val months = listOf("января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября",
        "октября", "ноября", "декабря")

/**
 * Средняя
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку
 */
fun dateStrToDigit(str: String): String {
    val list = str.split(" ")
    if (!str.matches(Regex("""\d?\d [а-я]+ \d+""")) || list[1] !in months) return ""
    return String.format("%02d.%02d.%s", list[0].toInt(), months.indexOf(list[1]) + 1, list[2])
}


/**
 * Средняя
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 */
fun dateDigitToStr(digital: String): String {
    val list = digital.split(".")
    if (!digital.matches(Regex("""\d\d.\d\d.\d+""")) || list[1].toInt() !in 1..12)
        return ""
    return String.format("%d %s %s", list[0].toInt(), months[list[1].toInt() - 1], list[2])
}

/**
 * Средняя
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -98 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку
 */
fun flattenPhoneNumber(phone: String): String {
    if (!phone.matches(Regex(""" *(\+\d+)?[ -]*(\(\d+\))?[\d -]*"""))) return ""
    return Regex("""[() -]""").replace(phone, "")
}

/**
 * Средняя
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    if (jumps.contains(Regex("""[^0-9% -]""")) || !jumps.contains(Regex("""[0-9]""")))
        return -1
    val input = Regex("""[% -]+""").replace(jumps, " ")
    val result = input.trim().split(" ").map { it.toInt() }
    return result.sortedDescending()[0]
}

/**
 * Сложная
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    if (jumps.contains(Regex("""[^\d %+-]""")) || '+' !in jumps) return -1
    var input = Regex("""\d+ [%-]+ """).replace(jumps + " ", "")
    input = Regex(""" [^\d]+ *""").replace(input + " ", " ")
    val inputList = input.trim().split(" ").map { it.toInt() }
    return inputList.sortedDescending()[0]
}


/**
 * Сложная
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int {
    if (!expression.matches(Regex("""(\d+ [+-] )*(\d+)""")))
        throw IllegalArgumentException("Неверный формат строки")
    val input = expression.split(" ")
    var result = input[0].toInt()
    for (i in 1..input.size - 2 step 2)
        if (input[i] == "+") result += input[i + 1].toInt()
        else result -= input[i + 1].toInt()
    return result
}

/**
 * Сложная
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String): Int {
    val input = str.toLowerCase().split(" ")
    var result = 0
    for (i in 0..input.size - 2)
        if (input[i] == input[i + 1]) return result
        else result += input[i].length + 1
    return -1
}

/**
 * Сложная
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62.5; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть положительными
 */
fun mostExpensive(description: String): String {
    if (!description.matches(Regex("""(.* \d+(\.\d+)?; )*(.* \d+(\.\d+)?)""")))
        return ""
    val input = description.split("; ")
    var result = ""
    var maxCost = 0.0
    for (element in input) {
        val matches = Regex("""(.*) (\d+(\.\d+)?)""").find(element)!!.groupValues
        val name = matches[1]
        val cost = matches[2].toDouble()
        if (cost >= maxCost) {
            maxCost = cost
            result = name
        }
    }
    return result
}

/**
 * Сложная
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 *
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int {
    if (!roman.matches(Regex
    ("""(M)*(CM)?(D)?(CD)?(C)*(XC)?(L)?(XL)?(X)*(IX)?(V)?(IV)?(I)*""")) || roman.isEmpty())
        return -1
    val romanNumbers = listOf(Pair(4, "IV"), Pair(9, "IX"), Pair(40, "XL"), Pair(90, "XC"),
            Pair(400, "CD"), Pair(900, "CM"), Pair(1, "I"), Pair(5, "V"), Pair(10, "X"), Pair(50, "L"),
            Pair(100, "C"), Pair(500, "D"), Pair(1000, "M"))
    var result = 0
    var inputString = roman
    for (element in romanNumbers) {
        if (element.second in inputString) {
            val matches = Regex(element.second).findAll(inputString)
            for (match in matches) result += element.first
            inputString = Regex(element.second).replace(inputString, "")
            if (inputString.isEmpty()) return result
        }
    }
    return result
}

/**
 * Очень сложная
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    if (commands.contains(Regex("""[^-+><\[\] ]""")) ||
            commands.indexOf(']') < commands.indexOf('[') ||
            commands.lastIndexOf('[') > commands.lastIndexOf(']') ||
            Regex("""\[""").findAll(commands).count() != Regex("""\]""").findAll(commands).count()
            ) throw IllegalArgumentException("Неверный формат строки commands")
    val result = MutableList(cells) { 0 }
    var currentPosition = cells / 2
    var currentCommand = 0
    var commandCount = 0
    while (commandCount < limit && currentCommand < commands.length) {
        when (commands[currentCommand]) {
            '-' -> {
                result[currentPosition]--
                currentCommand++
            }
            '+' -> {
                result[currentPosition]++
                currentCommand++
            }
            '<' -> {
                currentPosition--
                currentCommand++
            }
            '>' -> {
                currentPosition++
                currentCommand++
            }
            '[' -> {
                currentCommand = if (result[currentPosition] == 0)
                    impossibleHelper(commands, currentCommand, true)
                else currentCommand + 1
            }
            ']' -> {
                currentCommand = if (result[currentPosition] != 0)
                    impossibleHelper(commands, currentCommand, false)
                else currentCommand + 1
            }
            else -> currentCommand++
        }
        commandCount++
        if (currentPosition !in 0 until cells) throw IllegalStateException("Выход за границы конвеера")
    }
    return result
}

/**
 * Вспомогательная
 */
fun impossibleHelper(commands: String, currentCommand: Int, type: Boolean): Int {
    var openCount = 0
    var closeCount = 0
    if (type) {
        for (i in currentCommand until commands.length) {
            if (commands[i] == '[') openCount++
            else if (commands[i] == ']') closeCount++
            if (openCount == closeCount) return i + 1
        }
    } else {
        for (i in currentCommand downTo 0) {
            if (commands[i] == '[') openCount++
            else if (commands[i] == ']') closeCount++
            if (openCount == closeCount) return i + 1
        }
    }
    return currentCommand+1
}