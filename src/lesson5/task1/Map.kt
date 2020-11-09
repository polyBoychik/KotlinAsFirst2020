@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson5.task1

import lesson3.task1.gcd

// Урок 5: ассоциативные массивы и множества
// Максимальное количество баллов = 14
// Рекомендуемое количество баллов = 9
// Вместе с предыдущими уроками = 33/47

/**
 * Пример
 *
 * Для заданного списка покупок `shoppingList` посчитать его общую стоимость
 * на основе цен из `costs`. В случае неизвестной цены считать, что товар
 * игнорируется.
 */
fun shoppingListCost(
    shoppingList: List<String>,
    costs: Map<String, Double>
): Double {
    var totalCost = 0.0

    for (item in shoppingList) {
        val itemCost = costs[item]
        if (itemCost != null) {
            totalCost += itemCost
        }
    }

    return totalCost
}

/**
 * Пример
 *
 * Для набора "имя"-"номер телефона" `phoneBook` оставить только такие пары,
 * для которых телефон начинается с заданного кода страны `countryCode`
 */
fun filterByCountryCode(
    phoneBook: MutableMap<String, String>,
    countryCode: String
) {
    val namesToRemove = mutableListOf<String>()

    for ((name, phone) in phoneBook) {
        if (!phone.startsWith(countryCode)) {
            namesToRemove.add(name)
        }
    }

    for (name in namesToRemove) {
        phoneBook.remove(name)
    }
}

/**
 * Пример
 *
 * Для заданного текста `text` убрать заданные слова-паразиты `fillerWords`
 * и вернуть отфильтрованный текст
 */
fun removeFillerWords(
    text: List<String>,
    vararg fillerWords: String
): List<String> {
    val fillerWordSet = setOf(*fillerWords)

    val res = mutableListOf<String>()
    for (word in text) {
        if (word !in fillerWordSet) {
            res += word
        }
    }
    return res
}

/**
 * Пример
 *
 * Для заданного текста `text` построить множество встречающихся в нем слов
 */
fun buildWordSet(text: List<String>): MutableSet<String> {
    val res = mutableSetOf<String>()
    for (word in text) res.add(word)
    return res
}


/**
 * Простая (2 балла)
 *
 * По заданному ассоциативному массиву "студент"-"оценка за экзамен" построить
 * обратный массив "оценка за экзамен"-"список студентов с этой оценкой".
 *
 * Например:
 *   buildGrades(mapOf("Марат" to 3, "Семён" to 5, "Михаил" to 5))
 *     -> mapOf(5 to listOf("Семён", "Михаил"), 3 to listOf("Марат"))
 */
fun buildGrades(grades: Map<String, Int>): Map<Int, List<String>> {
    val res = mutableMapOf<Int, MutableList<String>>()
    for ((student, mark) in grades) {
        if (res[mark] == null)
            res[mark] = mutableListOf()

        res[mark]!!.add(student)
    }
    return res
}

/**
 * Простая (2 балла)
 *
 * Определить, входит ли ассоциативный массив a в ассоциативный массив b;
 * это выполняется, если все ключи из a содержатся в b с такими же значениями.
 *
 * Например:
 *   containsIn(mapOf("a" to "z"), mapOf("a" to "z", "b" to "sweet")) -> true
 *   containsIn(mapOf("a" to "z"), mapOf("a" to "zee", "b" to "sweet")) -> false
 */
fun containsIn(a: Map<String, String>, b: Map<String, String>): Boolean {
    for ((key, value) in a) {
        if (b[key] != value)
            return false
    }
    return true
}

/**
 * Простая (2 балла)
 *
 * Удалить из изменяемого ассоциативного массива все записи,
 * которые встречаются в заданном ассоциативном массиве.
 * Записи считать одинаковыми, если и ключи, и значения совпадают.
 *
 * ВАЖНО: необходимо изменить переданный в качестве аргумента
 *        изменяемый ассоциативный массив
 *
 * Например:
 *   subtractOf(a = mutableMapOf("a" to "z"), mapOf("a" to "z"))
 *     -> a changes to mutableMapOf() aka becomes empty
 */
fun subtractOf(a: MutableMap<String, String>, b: Map<String, String>) {
    for ((key, value) in b) {
        a.remove(key, value)
    }
}

/**
 * Простая (2 балла)
 *
 * Для двух списков людей найти людей, встречающихся в обоих списках.
 * В выходном списке не должно быть повторяюихся элементов,
 * т. е. whoAreInBoth(listOf("Марат", "Семён, "Марат"), listOf("Марат", "Марат")) == listOf("Марат")
 */
fun whoAreInBoth(a: List<String>, b: List<String>): List<String> = a.toSet().filter { it in b }

/**
 * Средняя (3 балла)
 *
 * Объединить два ассоциативных массива `mapA` и `mapB` с парами
 * "имя"-"номер телефона" в итоговый ассоциативный массив, склеивая
 * значения для повторяющихся ключей через запятую.
 * В случае повторяющихся *ключей* значение из mapA должно быть
 * перед значением из mapB.
 *
 * Повторяющиеся *значения* следует добавлять только один раз.
 *
 * Например:
 *   mergePhoneBooks(
 *     mapOf("Emergency" to "112", "Police" to "02"),
 *     mapOf("Emergency" to "911", "Police" to "02")
 *   ) -> mapOf("Emergency" to "112, 911", "Police" to "02")
 */
fun mergePhoneBooks(mapA: Map<String, String>, mapB: Map<String, String>): Map<String, String> {
    val mergedMap = mutableMapOf<String, String>()
    mergedMap += mapA

    for ((key, value) in mapB) {
        if (mergedMap.containsKey(key)) {
            if (value != mergedMap[key]!!)
                mergedMap[key] += ", $value"
        } else
            mergedMap[key] = value
    }

    return mergedMap
}

/**
 * Средняя (4 балла)
 *
 * Для заданного списка пар "акция"-"стоимость" вернуть ассоциативный массив,
 * содержащий для каждой акции ее усредненную стоимость.
 *
 * Например:
 *   averageStockPrice(listOf("MSFT" to 100.0, "MSFT" to 200.0, "NFLX" to 40.0))
 *     -> mapOf("MSFT" to 150.0, "NFLX" to 40.0)
 */
fun averageStockPrice(stockPrices: List<Pair<String, Double>>): Map<String, Double> {
    val averageStockPrice = mutableMapOf<String, Double>()
    val stockCount = mutableMapOf<String, Int>()

    for ((stock, price) in stockPrices) {
        if (stockCount[stock] == null) {
            stockCount[stock] = 1
            averageStockPrice[stock] = price
        } else {
            stockCount[stock] = stockCount[stock]!! + 1
            averageStockPrice[stock] = averageStockPrice[stock]!! + price
        }

    }
    for ((stock, price) in stockCount)
        averageStockPrice[stock] = averageStockPrice[stock]!! / price
    return averageStockPrice
}

/**
 * Средняя (4 балла)
 *
 * Входными данными является ассоциативный массив
 * "название товара"-"пара (тип товара, цена товара)"
 * и тип интересующего нас товара.
 * Необходимо вернуть название товара заданного типа с минимальной стоимостью
 * или null в случае, если товаров такого типа нет.
 *
 * Например:
 *   findCheapestStuff(
 *     mapOf("Мария" to ("печенье" to 20.0), "Орео" to ("печенье" to 100.0)),
 *     "печенье"
 *   ) -> "Мария"
 */
fun findCheapestStuff(stuff: Map<String, Pair<String, Double>>, kind: String): String? {
    var suitableShop: String? = null
    var minPrice: Double? = null

    for ((shop, value) in stuff) {
        if (value.first == kind) {
            if (suitableShop == null) {
                suitableShop = shop
                minPrice = value.second
            } else {
                if (value.second < minPrice!!) {
                    minPrice = value.second
                    suitableShop = shop
                }
            }
        }
    }

    return suitableShop
}

/**
 * Средняя (3 балла)
 *
 * Для заданного набора символов определить, можно ли составить из него
 * указанное слово (регистр символов игнорируется)
 *
 * Например:
 *   canBuildFrom(listOf('a', 'b', 'o'), "baobab") -> true
 */
fun canBuildFrom(chars: List<Char>, word: String) =
    chars.toSet().map { it.toLowerCase() }.containsAll(word.toLowerCase().toSet())


/**
 * Средняя (4 балла)
 *
 * Найти в заданном списке повторяющиеся элементы и вернуть
 * ассоциативный массив с информацией о числе повторений
 * для каждого повторяющегося элемента.
 * Если элемент встречается только один раз, включать его в результат
 * не следует.
 *
 * Например:
 *   extractRepeats(listOf("a", "b", "a")) -> mapOf("a" to 2)
 */
fun extractRepeats(list: List<String>): Map<String, Int> {
    val repeats = mutableMapOf<String, Int>()

    for (elem in list)
        repeats[elem] = if (!repeats.containsKey(elem)) 1 else repeats[elem]!! + 1

    return repeats.filter { it.value > 1 }
}

/**
 * Средняя (3 балла)
 *
 * Для заданного списка слов определить, содержит ли он анаграммы.
 * Два слова здесь считаются анаграммами, если они имеют одинаковую длину
 * и одно можно составить из второго перестановкой его букв.
 * Скажем, тор и рот или роза и азор это анаграммы,
 * а поле и полено -- нет.
 *
 * Например:
 *   hasAnagrams(listOf("тор", "свет", "рот")) -> true
 */
fun hasAnagrams(words: List<String>): Boolean {
    val word = mutableSetOf<Set<Char>>()
    words.toMutableSet().forEach { word += it.toSet() }
    return word.size != words.size
}

/**
 * Удаляет name из handshakes[ name ] для каждого ключа name ассоциативного массива handshakes
 */
fun deleteNonsense(handshakes: MutableMap<String, MutableSet<String>>) {
    for ((name, _) in handshakes)
        handshakes[name]!!.remove(name)
}

/**
 * Сложная (5 баллов)
 *
 * Для заданного ассоциативного массива знакомых через одно рукопожатие `friends`
 * необходимо построить его максимальное расширение по рукопожатиям, то есть,
 * для каждого человека найти всех людей, с которыми он знаком через любое
 * количество рукопожатий.
 *
 * Считать, что все имена людей являются уникальными, а также что рукопожатия
 * являются направленными, то есть, если Марат знает Свету, то это не означает,
 * что Света знает Марата.
 *
 * Оставлять пустой список знакомых для людей, которые их не имеют (см. EvilGnome ниже),
 * в том числе для случая, когда данного человека нет в ключах, но он есть в значениях
 * (см. GoodGnome ниже).
 *
 * Например:
 *   propagateHandshakes(
 *     mapOf(
 *       "Marat" to setOf("Mikhail", "Sveta"),
 *       "Sveta" to setOf("Marat"),
 *       "Mikhail" to setOf("Sveta"),
 *       "Friend" to setOf("GoodGnome"),
 *       "EvilGnome" to setOf()
 *     )
 *   ) -> mapOf(
 *          "Marat" to setOf("Mikhail", "Sveta"),
 *          "Sveta" to setOf("Marat", "Mikhail"),
 *          "Mikhail" to setOf("Sveta", "Marat"),
 *          "Friend" to setOf("GoodGnome"),
 *          "EvilGnome" to setOf(),
 *          "GoodGnome" to setOf()
 *        )
 */
fun propagateHandshakes(friends: Map<String, Set<String>>): Map<String, Set<String>> {
    val extendedFriends = mutableMapOf<String, MutableSet<String>>()
    val checked = mutableMapOf<String, Boolean>()

    for ((key, value) in friends) {
        if (!extendedFriends.containsKey(key)) {
            extendedFriends[key] = mutableSetOf()
        }
        extendedFriends[key]!!.addAll(value)
        checked[key] = false

        for (name in value) {
            if (!extendedFriends.containsKey(name)) {
                extendedFriends[name] = mutableSetOf()
                checked[name] = false
            }
        }
    }

    for (i in 0 until extendedFriends.size) {
        for ((name, _) in extendedFriends) {
            val whoWillBeAdded = mutableMapOf<String, MutableSet<String>>()
            for (friend in extendedFriends[name]!!) {
                for (deepFriend in extendedFriends[friend]!!) {
                    if (whoWillBeAdded[name] == null) {
                        whoWillBeAdded[name] = mutableSetOf()
                    }
                    whoWillBeAdded[name]!!.add(deepFriend)
                }
            }
            for ((person, hisFriends) in whoWillBeAdded) {
                extendedFriends[person]!!.addAll(hisFriends)
            }
        }
    }

    deleteNonsense(extendedFriends)
    return extendedFriends
}

/**
 * Сложная (6 баллов)
 *
 * Для заданного списка неотрицательных чисел и числа определить,
 * есть ли в списке пара чисел таких, что их сумма равна заданному числу.
 * Если да, верните их индексы в виде Pair<Int, Int>;
 * если нет, верните пару Pair(-1, -1).
 *
 * Индексы в результате должны следовать в порядке (меньший, больший).
 *
 * Постарайтесь сделать ваше решение как можно более эффективным,
 * используя то, что вы узнали в данном уроке.
 *
 * Например:
 *   findSumOfTwo(listOf(1, 2, 3), 4) -> Pair(0, 2)
 *   findSumOfTwo(listOf(1, 2, 3), 6) -> Pair(-1, -1)
 */
fun findSumOfTwo(list: List<Int>, number: Int): Pair<Int, Int> {
    val numbers = mutableMapOf<Int, Int>()

    for ((idx, value) in list.withIndex()) {
        if (numbers.containsKey(number - value))
            return Pair(numbers[number - value]!!, idx)
        else
            if (!numbers.containsKey(value))
                numbers[value] = idx
    }

    return Pair(-1, -1)
}

/**
 * Возвращает восстановленный список взятых сокровищ
 */
fun getTreasures(
    bag: Array<Array<Array<Int>>>,
    treasures: List<Pair<String, Pair<Int, Int>>>
): Set<String> {
    val tookTreasures = mutableSetOf<String>()

    var i = bag.lastIndex
    var j = bag.last().lastIndex

    var iLink = i
    var jLink = j

    while (iLink != -1 && jLink != -1) {

        iLink = bag[i][j][1]
        jLink = bag[i][j][2]

        if (iLink != -1 && jLink != -1) {

            if (bag[i][j][0] != bag[iLink][jLink][0])
                tookTreasures.add(treasures[i].first)

            i = iLink
            j = jLink

        } else {
            tookTreasures.add(treasures[i].first)
        }
    }

    return tookTreasures
}

/**
 * Очень сложная (8 баллов)
 *
 * Входными данными является ассоциативный массив
 * "название сокровища"-"пара (вес сокровища, цена сокровища)"
 * и вместимость вашего рюкзака.
 * Необходимо вернуть множество сокровищ с максимальной суммарной стоимостью,
 * которые вы можете унести в рюкзаке.
 *
 * Перед решением этой задачи лучше прочитать статью Википедии "Динамическое программирование".
 *
 * Например:
 *   bagPacking(
 *     mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
 *     850
 *   ) -> setOf("Кубок")
 *   bagPacking(
 *     mapOf("Кубок" to (500 to 2000), "Слиток" to (1000 to 5000)),
 *     450
 *   ) -> emptySet()
 */
fun bagPacking(treasures: Map<String, Pair<Int, Int>>, capacity: Int): Set<String> {

    if (treasures.isEmpty())
        return emptySet()

    val weightList = mutableListOf<Int>()
    treasures.forEach { weightList.add(it.value.first) }

    // Получение всех весов от минимального до вместимости рюкзака с шагом НОД всех весов
    var gcd = weightList[0]

    for (i in weightList) {
        gcd = gcd(gcd, i)
    }

    val weights = mutableListOf<Int>()
    val minWeight = weightList.minOrNull()!!
    for (i in minWeight..capacity step gcd) {
        weights.add(i)
    }
    weights.sort()

    val treases = treasures.toList().sortedBy { it.second.first }

    /* Data structure table

|                |           Bag's weight 1     |   Bag's weight 2...
--------------------------------------------------------------------
| (w, c) of tr.1 | total weight, previous: i, j |   ...
| (w, c) of tr.2 | ...                          |
| ...            | ...                          |

*/

    // 0: cost, 1: row, 2: column with previous treasures
    val tableTotal: Array<Array<Array<Int>>> = Array(treases.size) { Array(weights.size) { arrayOf(0, -1, -1) } }

    // Заполнение первой строки одним сокровищем
    for (weight in weights.indices) {
        tableTotal[0][weight][0] = treases[0].second.second
    }

    // Заполнение остальных строк таблицы
    for (treasure in 1 until treases.size) {
        for (weight in weights.indices) {

            var stockWeight = weights[weight] - treases[treasure].second.first
            var stockPrice = 0

            if (stockWeight > 0) {
                stockWeight = weights.indexOf(stockWeight)
                stockPrice =
                    if (stockWeight != -1) tableTotal[treasure - 1][stockWeight][0]
                    else 0
            }

            if (tableTotal[treasure - 1][weight][0] < treases[treasure].second.second + stockPrice &&
                treases[treasure].second.first <= weights[weight]
            ) {

                tableTotal[treasure][weight][0] = treases[treasure].second.second

                if (stockPrice > 0) {
                    tableTotal[treasure][weight][0] += stockPrice

                    tableTotal[treasure][weight][1] = treasure - 1
                    tableTotal[treasure][weight][2] = stockWeight

                }
            } else {
                tableTotal[treasure][weight][0] = tableTotal[treasure - 1][weight][0]
                tableTotal[treasure][weight][1] = treasure - 1
                tableTotal[treasure][weight][2] = weight
            }
        }
    }
    return getTreasures(tableTotal, treases)
}
