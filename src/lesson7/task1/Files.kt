@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson7.task1

import lesson3.task1.digitNumber
import lesson3.task1.pow
import lesson3.task1.revert
import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.*

// Урок 7: работа с файлами
// Урок интегральный, поэтому его задачи имеют сильно увеличенную стоимость
// Максимальное количество баллов = 55
// Рекомендуемое количество баллов = 20
// Вместе с предыдущими уроками (пять лучших, 3-7) = 55/103

/**
 * Пример
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Вывести его в выходной файл с именем outputName, выровняв по левому краю,
 * чтобы длина каждой строки не превосходила lineLength.
 * Слова в слишком длинных строках следует переносить на следующую строку.
 * Слишком короткие строки следует дополнять словами из следующей строки.
 * Пустые строки во входном файле обозначают конец абзаца,
 * их следует сохранить и в выходном файле
 */
fun alignFile(inputName: String, lineLength: Int, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    var currentLineLength = 0
    fun append(word: String) {
        if (currentLineLength > 0) {
            if (word.length + currentLineLength >= lineLength) {
                writer.newLine()
                currentLineLength = 0
            } else {
                writer.write(" ")
                currentLineLength++
            }
        }
        writer.write(word)
        currentLineLength += word.length
    }
    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) {
            writer.newLine()
            if (currentLineLength > 0) {
                writer.newLine()
                currentLineLength = 0
            }
            continue
        }
        for (word in line.split(Regex("\\s+"))) {
            append(word)
        }
    }
    writer.close()
}

/**
 * Простая (8 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * Некоторые его строки помечены на удаление первым символом _ (подчёркивание).
 * Перенести в выходной файл с именем outputName все строки входного файла, убрав при этом помеченные на удаление.
 * Все остальные строки должны быть перенесены без изменений, включая пустые строки.
 * Подчёркивание в середине и/или в конце строк значения не имеет.
 */
fun deleteMarked(inputName: String, outputName: String) {
    val reader = File(inputName).bufferedReader()
    val writer = File(outputName).bufferedWriter()

    reader.forEachLine {
        if (it.isNotEmpty()) {
            if (it[0] != '_') {
                writer.write(it)
                writer.newLine()
            }
        } else {
            writer.newLine()
        }
    }

    reader.close()
    writer.close()
}

/**
 * Возвращает количество вхождений needle в haystack без учета регистра
 */
fun countIgnoreCase(needle: String, haystack: String): Int {
    var count = 0

    val lowerHaystack = haystack.toLowerCase()
    val lowerNeedle = needle.toLowerCase()
    var idx = lowerHaystack.indexOf(lowerNeedle)
    while (idx != -1) {
        count++
        idx = lowerHaystack.indexOf(lowerNeedle, idx + 1)
    }

    return count
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст.
 * На вход подаётся список строк substrings.
 * Вернуть ассоциативный массив с числом вхождений каждой из строк в текст.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 */
fun countSubstrings(inputName: String, substrings: List<String>): Map<String, Int> {
    val entries = mutableMapOf<String, Int>()

    val subs = substrings.toSet()

    for (str in subs)
        entries[str] = 0

    val reader = File(inputName).bufferedReader()
    reader.forEachLine {
        for (str in subs)
            entries[str] = entries[str]!! + countIgnoreCase(str, it)
    }
    return entries
}


/**
 * Средняя (12 баллов)
 *
 * В русском языке, как правило, после букв Ж, Ч, Ш, Щ пишется И, А, У, а не Ы, Я, Ю.
 * Во входном файле с именем inputName содержится некоторый текст на русском языке.
 * Проверить текст во входном файле на соблюдение данного правила и вывести в выходной
 * файл outputName текст с исправленными ошибками.
 *
 * Регистр заменённых букв следует сохранять.
 *
 * Исключения (жюри, брошюра, парашют) в рамках данного задания обрабатывать не нужно
 *
 */
fun sibilants(inputName: String, outputName: String) {
    val dangerous = setOf('ж', 'ч', 'ш', 'щ')
    val replacements = mapOf('ы' to 'и', 'я' to 'а', 'ю' to 'у', 'Ы' to 'И', 'Я' to 'А', 'Ю' to 'У')
    val reader = File(inputName).bufferedReader()
    val writer = File(outputName).bufferedWriter()

    reader.forEachLine { line ->
        val writtenLine: StringBuilder = StringBuilder(line)
        for (i in 0 until line.length - 1) {
            if (line[i].toLowerCase() in dangerous)
                if (line[i + 1] in replacements)
                    writtenLine[i + 1] = replacements[line[i + 1]]!!
        }
        writer.write(writtenLine.toString())
        writer.newLine()
    }

    reader.close()
    writer.close()
}

fun alignedToCenter(str: String, len: Int): String {
    val trimmedStr = str.trim()
    return " ".repeat((len - trimmedStr.length) / 2) + trimmedStr
}

/**
 * Средняя (15 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по центру
 * относительно самой длинной строки.
 *
 * Выравнивание следует производить путём добавления пробелов в начало строки.
 *
 *
 * Следующие правила должны быть выполнены:
 * 1) Пробелы в начале и в конце всех строк не следует сохранять.
 * 2) В случае невозможности выравнивания строго по центру, строка должна быть сдвинута в ЛЕВУЮ сторону
 * 3) Пустые строки не являются особым случаем, их тоже следует выравнивать
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых)
 *
 */
fun centerFile(inputName: String, outputName: String) {
    val reader = File(inputName).bufferedReader()
    val writer = File(outputName).bufferedWriter()

    val lines = reader.readLines()
    val maxLineLen = lines.maxOfOrNull { it.trim().length } ?: return writer.close()

    lines.forEach {
        writer.write(alignedToCenter(it, maxLineLen))
        writer.newLine()
    }

    writer.close()
}

fun removeRedundantSpaces(str: String): String {
    var trimmed = str.trim()

    while (trimmed.contains("  ")) {
        trimmed = trimmed.replace("  ", " ")
    }
    return trimmed
}

/**
 * Сложная (20 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 * Вывести его в выходной файл с именем outputName, выровняв по левому и правому краю относительно
 * самой длинной строки.
 * Выравнивание производить, вставляя дополнительные пробелы между словами: равномерно по всей строке
 *
 * Слова внутри строки отделяются друг от друга одним или более пробелом.
 *
 * Следующие правила должны быть выполнены:
 * 1) Каждая строка входного и выходного файла не должна начинаться или заканчиваться пробелом.
 * 2) Пустые строки или строки из пробелов трансформируются в пустые строки без пробелов.
 * 3) Строки из одного слова выводятся без пробелов.
 * 4) Число строк в выходном файле должно быть равно числу строк во входном (в т. ч. пустых).
 *
 * Равномерность определяется следующими формальными правилами:
 * 5) Число пробелов между каждыми двумя парами соседних слов не должно отличаться более, чем на 1.
 * 6) Число пробелов между более левой парой соседних слов должно быть больше или равно числу пробелов
 *    между более правой парой соседних слов.
 *
 * Следует учесть, что входной файл может содержать последовательности из нескольких пробелов  между слвоами. Такие
 * последовательности следует учитывать при выравнивании и при необходимости избавляться от лишних пробелов.
 * Из этого следуют следующие правила:
 * 7) В самой длинной строке каждая пара соседних слов должна быть отделена В ТОЧНОСТИ одним пробелом
 * 8) Если входной файл удовлетворяет требованиям 1-7, то он должен быть в точности идентичен выходному файлу
 */
fun alignFileByWidth(inputName: String, outputName: String) {
    val reader = File(inputName).bufferedReader()

    val lines = reader.readLines().toMutableList()

    for (idx in lines.indices) {
        lines[idx] = removeRedundantSpaces(lines[idx])
    }
    val writer = File(outputName).bufferedWriter()

    // Если файл пуст, функция завершается закрытием файла
    val maxLineLen = lines.maxOfOrNull { it.length } ?: return writer.close()


    lines.forEach {
        val split = it.split(" ")

        writer.write(split[0])

        if (split.size > 1) {
            val strLen = split.fold(0) { acc: Int, s: String -> acc + s.length }
            val spaces = (maxLineLen - strLen) / (split.size - 1)
            val extended = (maxLineLen - strLen) - spaces * (split.size - 1)

            for (i in 1 until extended + 1)
                writer.write(" ".repeat(spaces + 1) + split[i])
            for (i in extended + 1 until split.size)
                writer.write(" ".repeat(spaces) + split[i])
        }
        writer.newLine()
    }
    writer.close()
}

/**
 * Средняя (14 баллов)
 *
 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * Вернуть ассоциативный массив, содержащий 20 наиболее часто встречающихся слов с их количеством.
 * Если в тексте менее 20 различных слов, вернуть все слова.
 * Вернуть ассоциативный массив с числом слов больше 20, если 20-е, 21-е, ..., последнее слова
 * имеют одинаковое количество вхождений (см. также тест файла input/onegin.txt).
 *
 * Словом считается непрерывная последовательность из букв (кириллических,
 * либо латинских, без знаков препинания и цифр).
 * Цифры, пробелы, знаки препинания считаются разделителями слов:
 * Привет, привет42, привет!!! -привет?!
 * ^ В этой строчке слово привет встречается 4 раза.
 *
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 * Ключи в ассоциативном массиве должны быть в нижнем регистре.
 *
 */
fun top20Words(inputName: String): Map<String, Int> {
    val reader = File(inputName).bufferedReader()

    val top = mutableMapOf<String, Int>()

    val lines = reader.readLines()

    lines.forEach { line ->
        // Выделил ё и Ё отдельно, т.к. почему-то regex не считает, что эти буквы входят в интервал А-я
        Regex("""[A-zА-яёЁ]+""").findAll(line).iterator().forEach { matchResult ->
            for (word in matchResult.groupValues) {
                val lower = word.toLowerCase()
                if (lower in top) {
                    top[lower] = top[lower]!! + 1
                } else {
                    top[lower] = 1
                }
            }
        }
    }

    val sortedTopList = top.toList().sortedByDescending { it.second }

    return if (top.size > 20)
        sortedTopList.take(20).toMap() + top.filter { it.value == sortedTopList[19].second }
    else
        sortedTopList.take(20).toMap()
}

/**
 * Средняя (14 баллов)
 *
 * Реализовать транслитерацию текста из входного файла в выходной файл посредством динамически задаваемых правил.

 * Во входном файле с именем inputName содержится некоторый текст (в том числе, и на русском языке).
 *
 * В ассоциативном массиве dictionary содержится словарь, в котором некоторым символам
 * ставится в соответствие строчка из символов, например
 * mapOf('з' to "zz", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "yy", '!' to "!!!")
 *
 * Необходимо вывести в итоговый файл с именем outputName
 * содержимое текста с заменой всех символов из словаря на соответствующие им строки.
 *
 * При этом регистр символов в словаре должен игнорироваться,
 * но при выводе символ в верхнем регистре отображается в строку, начинающуюся с символа в верхнем регистре.
 *
 * Пример.
 * Входной текст: Здравствуй, мир!
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Пример 2.
 *
 * Входной текст: Здравствуй, мир!
 * Словарь: mapOf('з' to "zZ", 'р' to "r", 'д' to "d", 'й' to "y", 'М' to "m", 'и' to "YY", '!' to "!!!")
 *
 * заменяется на
 *
 * Выходной текст: Zzdrавствуy, mир!!!
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun transliterate(inputName: String, dictionary: Map<Char, String>, outputName: String) {
    val reader = File(inputName).bufferedReader()
    val writer = File(outputName).bufferedWriter()

    while (reader.ready()) {
        val char = reader.read().toChar()
        var replacement = dictionary[char.toLowerCase()] ?: dictionary[char.toUpperCase()]
        if (replacement != null) {

            replacement = if (char.isUpperCase())
                replacement.toLowerCase().capitalize()
            else
                replacement.toLowerCase()

            writer.write(replacement)

        } else {
            writer.write(char.toInt())
        }
    }
    reader.close()
    writer.close()
}

/**
 * Средняя (12 баллов)
 *
 * Во входном файле с именем inputName имеется словарь с одним словом в каждой строчке.
 * Выбрать из данного словаря наиболее длинное слово,
 * в котором все буквы разные, например: Неряшливость, Четырёхдюймовка.
 * Вывести его в выходной файл с именем outputName.
 * Если во входном файле имеется несколько слов с одинаковой длиной, в которых все буквы разные,
 * в выходной файл следует вывести их все через запятую.
 * Регистр букв игнорировать, то есть буквы е и Е считать одинаковыми.
 *
 * Пример входного файла:
 * Карминовый
 * Боязливый
 * Некрасивый
 * Остроумный
 * БелогЛазый
 * ФиолетОвый

 * Соответствующий выходной файл:
 * Карминовый, Некрасивый
 *
 * Обратите внимание: данная функция не имеет возвращаемого значения
 */
fun chooseLongestChaoticWord(inputName: String, outputName: String) {
    val reader = File(inputName).bufferedReader()
    val writer = File(outputName).bufferedWriter()

    val longests = mutableListOf<String>()
    var longestLen = 0

    reader.forEachLine { word ->
        if (word.length > longestLen && word.length == word.toLowerCase().toSet().size) {
            longestLen = word.length
            longests.clear()
            longests.add(word)
        } else if (word.length == longestLen && word.length == word.toLowerCase().toSet().size) {
            longests.add(word)
        }
    }
    reader.close()
    writer.write(longests.joinToString(separator = ", "))
    writer.close()
}

/**
 * Возвращает пару (pattern, index), где pattern - первый встреченный шаблон из patterns в строке line[start:],
 * с индексом index
 */
fun getNearestLabel(start: Int, line: String, patterns: Collection<String>): Pair<String, Int> {
    var min = line.length
    var case = ""
    for (key in patterns) {
        val idx = line.indexOf(key, start)
        if (idx != -1 && idx < min) {
            min = idx
            case = key
        }
    }
    return Pair(case, min)
}

/**
 * Возвращает строку, преобразованную из markdown в html (содержащую только выделения текста)
 */
fun mdFormatToHtml(line: String, stack: Stack<String>): String {
    // by priority: highest >> >> >> least
    val rulesOpen = mapOf("**" to "<b>", "*" to "<i>", "~~" to "<s>")
    val rulesClose = mapOf("**" to "</b>", "*" to "</i>", "~~" to "</s>")

    val lineBuilder = StringBuilder(line)

    var idx = 0
    do {
        var case: String
        val nearest = getNearestLabel(idx, lineBuilder.toString(), rulesOpen.keys)
        case = nearest.first
        idx = nearest.second

        if (case != "") {
            val replacement: String?
            if (stack.isNotEmpty() && stack.peek() == case) {
                replacement = rulesClose[stack.pop()]
            } else if (stack.isNotEmpty() && stack.contains(case)) {
                case = stack.peek()
                replacement = rulesClose[stack.pop()]
            } else {
                stack.push(case)
                replacement = rulesOpen[case]
            }
            lineBuilder.replace(idx, idx + case.length, replacement)
            idx += replacement?.length ?: 0
        }

    } while (case != "")
    return lineBuilder.toString()
}

/**
 * Сложная (22 балла)
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе элементы текстовой разметки следующих типов:
 * - *текст в курсивном начертании* -- курсив
 * - **текст в полужирном начертании** -- полужирный
 * - ~~зачёркнутый текст~~ -- зачёркивание
 *
 * Следует вывести в выходной файл этот же текст в формате HTML:
 * - <i>текст в курсивном начертании</i>
 * - <b>текст в полужирном начертании</b>
 * - <s>зачёркнутый текст</s>
 *
 * Кроме того, все абзацы исходного текста, отделённые друг от друга пустыми строками, следует обернуть в теги <p>...</p>,
 * а весь текст целиком в теги <html><body>...</body></html>.
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 * Отдельно следует заметить, что открывающая последовательность из трёх звёздочек (***) должна трактоваться как "<b><i>"
 * и никак иначе.
 *
 * При решении этой и двух следующих задач полезно прочитать статью Википедии "Стек".
 *
 * Пример входного файла:
Lorem ipsum *dolor sit amet*, consectetur **adipiscing** elit.
Vestibulum lobortis, ~~Est vehicula rutrum *suscipit*~~, ipsum ~~lib~~ero *placerat **tortor***,

Suspendisse ~~et elit in enim tempus iaculis~~.
 *
 * Соответствующий выходной файл:
<html>
<body>
<p>
Lorem ipsum <i>dolor sit amet</i>, consectetur <b>adipiscing</b> elit.
Vestibulum lobortis. <s>Est vehicula rutrum <i>suscipit</i></s>, ipsum <s>lib</s>ero <i>placerat <b>tortor</b></i>.
</p>
<p>
Suspendisse <s>et elit in enim tempus iaculis</s>.
</p>
</body>
</html>
 *
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlSimple(inputName: String, outputName: String) {
    val mdLines =
        File(inputName).bufferedReader().readLines().dropWhile { it.trim() == "" }.dropLastWhile { it.trim() == "" }
    val writer = File(outputName).bufferedWriter()
    val stack: Stack<String> = Stack()

    writer.write("<html><body>")

    mdLines.forEach { line ->
        if (line.trim() == "") {
            if (stack.isNotEmpty() && stack.peek() == "</p>") {
                writer.write(stack.pop())
            }
        } else {
            if (stack.isEmpty()) {
                stack.push("</p>")
                writer.write("<p>")
            }
            writer.write(mdFormatToHtml(line, stack))
        }
    }

    while (stack.isNotEmpty())
        writer.write(stack.pop())

    writer.write("</body></html>")
    writer.close()
}

/**
 * Возвращает количество лидирующих пробелов в строке str
 */
fun getLeadSpaces(str: String): Int {
    var count = 0
    while (count < str.length && str[count] == ' ')
        count++

    return count
}

/**
 * Возвращает преобразованный элемент нумерованного списка из markdown в html
 */
fun getOrderedListElement(str: String) =
    str.substring(str.indexOf(". ") + 2)  // skip "{number}. ", where {number} is list element order

/**
 * Возвращает преобразованный элемент маркированного списка из markdown в html
 */
fun getUnOrderedListElement(str: String) = str.substring(2)  // skip "* "

/**
 * Возвращает преобразованный markdown элемент списка в html
 */
fun getListElement(str: String) =
    if (str[0] == '*') getUnOrderedListElement(str)
    else getOrderedListElement(str)

/**
 * Сложная (23 балла)
 *
 * Реализовать транслитерацию текста в заданном формате разметки в формат разметки HTML.
 *
 * Во входном файле с именем inputName содержится текст, содержащий в себе набор вложенных друг в друга списков.
 * Списки бывают двух типов: нумерованные и ненумерованные.
 *
 * Каждый элемент ненумерованного списка начинается с новой строки и символа '*', каждый элемент нумерованного списка --
 * с новой строки, числа и точки. Каждый элемент вложенного списка начинается с отступа из пробелов, на 4 пробела большего,
 * чем список-родитель. Максимально глубина вложенности списков может достигать 6. "Верхние" списки файла начинются
 * прямо с начала строки.
 *
 * Следует вывести этот же текст в выходной файл в формате HTML:
 * Нумерованный список:
 * <ol>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ol>
 *
 * Ненумерованный список:
 * <ul>
 *     <li>Раз</li>
 *     <li>Два</li>
 *     <li>Три</li>
 * </ul>
 *
 * Кроме того, весь текст целиком следует обернуть в теги <html><body><p>...</p></body></html>
 *
 * Все остальные части исходного текста должны остаться неизменными с точностью до наборов пробелов и переносов строк.
 *
 * Пример входного файла:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
 * Утка по-пекински
 * Утка
 * Соус
 * Салат Оливье
1. Мясо
 * Или колбаса
2. Майонез
3. Картофель
4. Что-то там ещё
 * Помидоры
 * Фрукты
1. Бананы
23. Яблоки
1. Красные
2. Зелёные
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 *
 *
 * Соответствующий выходной файл:
///////////////////////////////начало файла/////////////////////////////////////////////////////////////////////////////
<html>
<body>
<p>
<ul>
<li>
Утка по-пекински
<ul>
<li>Утка</li>
<li>Соус</li>
</ul>
</li>
<li>
Салат Оливье
<ol>
<li>Мясо
<ul>
<li>Или колбаса</li>
</ul>
</li>
<li>Майонез</li>
<li>Картофель</li>
<li>Что-то там ещё</li>
</ol>
</li>
<li>Помидоры</li>
<li>Фрукты
<ol>
<li>Бананы</li>
<li>Яблоки
<ol>
<li>Красные</li>
<li>Зелёные</li>
</ol>
</li>
</ol>
</li>
</ul>
</p>
</body>
</html>
///////////////////////////////конец файла//////////////////////////////////////////////////////////////////////////////
 * (Отступы и переносы строк в примере добавлены для наглядности, при решении задачи их реализовывать не обязательно)
 */
fun markdownToHtmlLists(inputName: String, outputName: String) {
    val indent = 4  // in spaces

    val reader = File(inputName).bufferedReader()
    val writer = File(outputName).bufferedWriter()

    // для обозначения того, что в текущий момент уровень вложенности минимальный
    var nesting = -1

    val listStack = Stack<String>()

    writer.write("<html><body><p>")
    listStack.push("</html>")
    listStack.push("</body>")
    listStack.push("</p>")

    reader.forEachLine { line ->
        val currentIndent = getLeadSpaces(line) / indent

        val trimmedLine = line.trimStart()

        when {
            currentIndent > nesting -> {
                if (trimmedLine[0] == '*') {
                    listStack.push("</ul>")
                    listStack.push("</li>")
                    writer.write("<ul><li>" + getUnOrderedListElement(trimmedLine))
                } else {
                    listStack.push("</ol>")
                    listStack.push("</li>")
                    writer.write("<ol><li>" + getOrderedListElement(trimmedLine))
                }
                nesting++
            }
            currentIndent == nesting -> {
                writer.write(
                    listStack.pop() + "<li>" + getListElement(trimmedLine)
                )
                listStack.push("</li>")
            }
            else -> {
                writer.write(
                    listStack.pop() + listStack.pop() + listStack.pop() + "<li>" + getListElement(trimmedLine)
                )
                listStack.push("</li>")
                nesting--
            }
        }
    }

    while (listStack.isNotEmpty())
        writer.write(listStack.pop())

    reader.close()
    writer.close()
}

/**
 * Очень сложная (30 баллов)
 *
 * Реализовать преобразования из двух предыдущих задач одновременно над одним и тем же файлом.
 * Следует помнить, что:
 * - Списки, отделённые друг от друга пустой строкой, являются разными и должны оказаться в разных параграфах выходного файла.
 *
 */
fun markdownToHtml(inputName: String, outputName: String) {
    // list nesting identifier
    val indent = 4  // in spaces

    val mdLines =
        File(inputName).bufferedReader().readLines().dropWhile { it.trim() == "" }.dropLastWhile { it.trim() == "" }
    val writer = File(outputName).bufferedWriter()

    val stack = Stack<String>()

    var nesting = -1

    writer.write("<html><body>")
    mdLines.forEach {
        val trimmedLine = it.trim()
        // closing of paragraph and skip empty string
        if (trimmedLine == "") {
            nesting = -1
            while (stack.isNotEmpty()) {
                writer.write(stack.pop())
            }
        } else {
            // paragraph's beginning
            if (stack.isEmpty()) {
                stack.push("</p>")
                writer.write("<p>")
            }

            val spacesStart = getLeadSpaces(it)
            if (spacesStart % indent == 0) {
                val currentNesting = spacesStart / indent

                // Suspicion of list
                if (trimmedLine[0] == '*' || Regex("""\d+.""").find(trimmedLine)?.range?.first == 0) {
                    when {
                        // current nesting
                        currentNesting == nesting -> {
                            writer.write(stack.pop() + "<li>" + mdFormatToHtml(getListElement(trimmedLine), stack))
                            stack.push("</li>")
                        }
                        // lower nesting level
                        currentNesting < nesting -> {
                            writer.write(
                                stack.pop() + stack.pop() + stack.pop() + "<li>" + mdFormatToHtml(
                                    getListElement(trimmedLine),
                                    stack
                                )
                            )
                            stack.push("</li>")
                            nesting--
                        }
                        // more nesting level
                        else -> {
                            if (trimmedLine[0] == '*') {
                                stack.push("</ul>")
                                stack.push("</li>")
                                writer.write("<ul><li>" + mdFormatToHtml(getUnOrderedListElement(trimmedLine), stack))
                            } else {
                                stack.push("</ol>")
                                stack.push("</li>")
                                writer.write("<ol><li>" + mdFormatToHtml(getOrderedListElement(trimmedLine), stack))
                            }
                            nesting++
                        }
                    }
                    // Line is not list
                } else {
                    writer.write(mdFormatToHtml(trimmedLine, stack))
                }

            }
        }
    }

    while (stack.isNotEmpty())
        writer.write(stack.pop())

    writer.write("</body></html>")
    writer.close()

}

/**
 * Средняя (12 баллов)
 *
 * Вывести в выходной файл процесс умножения столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 111):
19935
 *    111
--------
19935
+ 19935
+19935
--------
2212785
 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 * Нули в множителе обрабатывать так же, как и остальные цифры:
235
 *  10
-----
0
+235
-----
2350
 *
 */
fun printMultiplicationProcess(lhv: Int, rhv: Int, outputName: String) {
    // 5 = 2 lines are for factors, 2 lines are for parts dividers (---), 1 line is for result
    val divisorLen = digitNumber(rhv)
    val mulTable = MutableList(5 + divisorLen) { StringBuilder() }

    // add factors
    mulTable[0].append(lhv.toString().reversed())
    mulTable[1].append(rhv.toString().reversed())

    // add addendums
    for (i in 0 until divisorLen) {
        mulTable[3 + i].append(" ".repeat(i) + (lhv * (rhv / pow(10, i) % 10)).toString().reversed())
    }

    // add result line
    mulTable.last().append((lhv * rhv).toString().reversed())

    // add "*" symbol
    mulTable[1].append(
        " ".repeat(
            mulTable.last().length - mulTable[1].length
        ) + "*"
    )

    // add pluses to lines from second to last addendums
    for (i in 4..divisorLen + 2) {
        mulTable[i].append(" ".repeat(mulTable.last().length - mulTable[i].length) + "+")
    }

    val lineLen = mulTable[1].length

    // add dividers (----)
    mulTable[2].append("-".repeat(lineLen))
    mulTable[mulTable.lastIndex - 1].append("-".repeat(lineLen))

    val writer = File(outputName).bufferedWriter()
    for (str in mulTable) {
        // drop ladders of spaces in factors' tails
        writer.write(" ".repeat(lineLen - str.length) + str.reverse().dropLastWhile { it == ' ' })
        writer.newLine()
    }
    writer.close()
}


/**
 * Сложная (25 баллов)
 *
 * Вывести в выходной файл процесс деления столбиком числа lhv (> 0) на число rhv (> 0).
 *
 * Пример (для lhv == 19935, rhv == 22):
 19935 | 22
-198     906
----
  13
  -0
  --
  135
 -132
 ----
    3

 * Используемые пробелы, отступы и дефисы должны в точности соответствовать примеру.
 *
 */
fun printDivisionProcess(lhv: Int, rhv: Int, outputName: String) {

    fun writeSubtrahend(lines: MutableList<StringBuilder>, idx: Int, indent: Int, subtrahend: Int) {
        lines[idx].append("${" ".repeat(indent - digitNumber(subtrahend) - 1)}-${subtrahend}")
    }

    fun writeSeparator(lines: MutableList<StringBuilder>, idx: Int, indent: Int, minuend: Int, subtrahend: Int) {
        val totalLen = max(digitNumber(minuend), digitNumber(subtrahend) + 1)
        lines[idx].append("${" ".repeat(indent - totalLen)}${"-".repeat(totalLen)}")
    }

    fun writeDifference(lines: MutableList<StringBuilder>, idx: Int, indent: Int, difference: Int) {
        lines[idx].append("%${indent}s".format(difference))
    }

    /* 1 line is for quotient. 3 * digits of quotient are for
       xxx
       -yyy
       ----   */
    val divLines = MutableList(3 * digitNumber(lhv / rhv) + 1) { StringBuilder() }
    divLines[0].append("$lhv")

    var minuend = 0
    var idx = 0
    while (idx < divLines[0].length && minuend < rhv) {
        minuend = minuend * 10 + (divLines[0][idx++] - '0')
    }

    var lineIdx = 1
    do {
        val subtrahend = minuend / rhv * rhv

        // Отступ для первой строки, чтобы в вычитаемом вместился минус
        if (idx - digitNumber(subtrahend) < 1) {
            divLines[0].insert(0, " ")
            idx++
        }

        writeSubtrahend(divLines, lineIdx++, idx, subtrahend)
        writeSeparator(divLines, lineIdx++, idx, minuend, subtrahend)
        writeDifference(divLines, lineIdx, idx, minuend - subtrahend)

        minuend -= subtrahend
        if (idx < divLines[0].length) {
            divLines[lineIdx++].append(divLines[0][idx])
            minuend = minuend * 10 + (divLines[0][idx] - '0')
        }
        idx++
    } while (idx <= divLines[0].length)

    // write quotient and divisor
    divLines[1].append("${" ".repeat(divLines[0].length + 3 - divLines[1].length)}${lhv / rhv}")
    divLines[0].append(" | $rhv")

    val writer = File(outputName).bufferedWriter()

    for (line in divLines) {
        writer.write(line.toString())
        writer.newLine()
    }
    writer.close()
}

