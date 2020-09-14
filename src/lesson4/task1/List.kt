@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson4.task1

import lesson1.task1.discriminant
import lesson1.task1.sqr
import lesson3.task1.digitNumber
import lesson3.task1.pow
import kotlin.math.sqrt

// Урок 4: списки
// Максимальное количество баллов = 12
// Рекомендуемое количество баллов = 8
// Вместе с предыдущими уроками = 24/33

/**
 * Пример
 *
 * Найти все корни уравнения x^2 = y
 */
fun sqRoots(y: Double) =
    when {
        y < 0 -> listOf()
        y == 0.0 -> listOf(0.0)
        else -> {
            val root = sqrt(y)
            // Результат!
            listOf(-root, root)
        }
    }

/**
 * Пример
 *
 * Найти все корни биквадратного уравнения ax^4 + bx^2 + c = 0.
 * Вернуть список корней (пустой, если корней нет)
 */
fun biRoots(a: Double, b: Double, c: Double): List<Double> {
    if (a == 0.0) {
        return if (b == 0.0) listOf()
        else sqRoots(-c / b)
    }
    val d = discriminant(a, b, c)
    if (d < 0.0) return listOf()
    if (d == 0.0) return sqRoots(-b / (2 * a))
    val y1 = (-b + sqrt(d)) / (2 * a)
    val y2 = (-b - sqrt(d)) / (2 * a)
    return sqRoots(y1) + sqRoots(y2)
}

/**
 * Пример
 *
 * Выделить в список отрицательные элементы из заданного списка
 */
fun negativeList(list: List<Int>): List<Int> {
    val result = mutableListOf<Int>()
    for (element in list) {
        if (element < 0) {
            result.add(element)
        }
    }
    return result
}

/**
 * Пример
 *
 * Изменить знак для всех положительных элементов списка
 */
fun invertPositives(list: MutableList<Int>) {
    for (i in 0 until list.size) {
        val element = list[i]
        if (element > 0) {
            list[i] = -element
        }
    }
}

/**
 * Пример
 *
 * Из имеющегося списка целых чисел, сформировать список их квадратов
 */
fun squares(list: List<Int>) = list.map { it * it }

/**
 * Пример
 *
 * Из имеющихся целых чисел, заданного через vararg-параметр, сформировать массив их квадратов
 */
fun squares(vararg array: Int) = squares(array.toList()).toTypedArray()

/**
 * Пример
 *
 * По заданной строке str определить, является ли она палиндромом.
 * В палиндроме первый символ должен быть равен последнему, второй предпоследнему и т.д.
 * Одни и те же буквы в разном регистре следует считать равными с точки зрения данной задачи.
 * Пробелы не следует принимать во внимание при сравнении символов, например, строка
 * "А роза упала на лапу Азора" является палиндромом.
 */
fun isPalindrome(str: String): Boolean {
    val lowerCase = str.toLowerCase().filter { it != ' ' }
    for (i in 0..lowerCase.length / 2) {
        if (lowerCase[i] != lowerCase[lowerCase.length - i - 1]) return false
    }
    return true
}

/**
 * Пример
 *
 * По имеющемуся списку целых чисел, например [3, 6, 5, 4, 9], построить строку с примером их суммирования:
 * 3 + 6 + 5 + 4 + 9 = 27 в данном случае.
 */
fun buildSumExample(list: List<Int>) = list.joinToString(separator = " + ", postfix = " = ${list.sum()}")

/**
 * Простая (2 балла)
 *
 * Найти модуль заданного вектора, представленного в виде списка v,
 * по формуле abs = sqrt(a1^2 + a2^2 + ... + aN^2).
 * Модуль пустого вектора считать равным 0.0.
 */
fun abs(v: List<Double>): Double {
    var sqrSums = 0.0
    v.forEach { sqrSums += sqr(it) }
    return sqrt(sqrSums)
}

/**
 * Простая (2 балла)
 *
 * Рассчитать среднее арифметическое элементов списка list. Вернуть 0.0, если список пуст
 */
fun mean(list: List<Double>): Double {
    // А можно так?  ... = if (list.isNotEmpty) list.average() else 0.0

    var sum = 0.0
    list.forEach { sum += it }
    return if (list.isNotEmpty()) sum / list.size else 0.0
}

/**
 * Средняя (3 балла)
 *
 * Центрировать заданный список list, уменьшив каждый элемент на среднее арифметическое всех элементов.
 * Если список пуст, не делать ничего. Вернуть изменённый список.
 *
 * Обратите внимание, что данная функция должна изменять содержание списка list, а не его копии.
 */
fun center(list: MutableList<Double>): MutableList<Double> {
    if (list.isNotEmpty()) {
        val mean = mean(list)

        for (i in 0 until list.size) {
            list[i] -= mean
        }
    }
    return list
}

/**
 * Средняя (3 балла)
 *
 * Найти скалярное произведение двух векторов равной размерности,
 * представленные в виде списков a и b. Скалярное произведение считать по формуле:
 * C = a1b1 + a2b2 + ... + aNbN. Произведение пустых векторов считать равным 0.
 */
fun times(a: List<Int>, b: List<Int>): Int {
    var product = 0

    for (i in a.indices) {
        product += a[i] * b[i]
    }
    return product
}

/**
 * Средняя (3 балла)
 *
 * Рассчитать значение многочлена при заданном x:
 * p(x) = p0 + p1*x + p2*x^2 + p3*x^3 + ... + pN*x^N.
 * Коэффициенты многочлена заданы списком p: (p0, p1, p2, p3, ..., pN).
 * Значение пустого многочлена равно 0 при любом x.
 */
fun polynom(p: List<Int>, x: Int): Int {
    var value: Int
    if (p.isNotEmpty()) {
        value = p.last()

        for (i in p.size - 2 downTo 0) {
            value = value * x + p[i]
        }
        return value
    } else {
        value = 0
    }
    return value
}

/**
 * Средняя (3 балла)
 *
 * В заданном списке list каждый элемент, кроме первого, заменить
 * суммой данного элемента и всех предыдущих.
 * Например: 1, 2, 3, 4 -> 1, 3, 6, 10.
 * Пустой список не следует изменять. Вернуть изменённый список.
 *
 * Обратите внимание, что данная функция должна изменять содержание списка list, а не его копии.
 */
fun accumulate(list: MutableList<Int>): MutableList<Int> {
    if (list.isNotEmpty()) {
        var accumulatedSum = list[0]
        for (i in 1 until list.size) {
            val current = list[i]
            list[i] += accumulatedSum
            accumulatedSum += current
        }
    }
    return list
}

/**
 * Средняя (3 балла)
 *
 * Разложить заданное натуральное число n > 1 на простые множители.
 * Результат разложения вернуть в виде списка множителей, например 75 -> (3, 5, 5).
 * Множители в списке должны располагаться по возрастанию.
 */
fun factorize(n: Int): List<Int> {
    var mutableN = n
    val simpleDivisors = mutableListOf<Int>()
    var i = 2

    while (i < mutableN) {
        while (mutableN % i == 0) {
            simpleDivisors.add(i)
            mutableN /= i
        }
        i++
    }

    if (mutableN > 1)
        simpleDivisors.add(i)
    return simpleDivisors
}

/**
 * Сложная (4 балла)
 *
 * Разложить заданное натуральное число n > 1 на простые множители.
 * Результат разложения вернуть в виде строки, например 75 -> 3*5*5
 * Множители в результирующей строке должны располагаться по возрастанию.
 */
fun factorizeToString(n: Int): String = factorize(n).joinToString(separator = "*")

/**
 * Средняя (3 балла)
 *
 * Перевести заданное целое число n >= 0 в систему счисления с основанием base > 1.
 * Результат перевода вернуть в виде списка цифр в base-ичной системе от старшей к младшей,
 * например: n = 100, base = 4 -> (1, 2, 1, 0) или n = 250, base = 14 -> (1, 3, 12)
 */
fun convert(n: Int, base: Int): List<Int> {
    var number = n
    val result = mutableListOf<Int>()

    if (number == 0) {
        result.add(0)
    } else {

        while (number >= 1) {
            result.add(0, number % base)
            number /= base
        }
    }
    return result
}

/**
 * Сложная (4 балла)
 *
 * Перевести заданное целое число n >= 0 в систему счисления с основанием 1 < base < 37.
 * Результат перевода вернуть в виде строки, цифры более 9 представлять латинскими
 * строчными буквами: 10 -> a, 11 -> b, 12 -> c и так далее.
 * Например: n = 100, base = 4 -> 1210, n = 250, base = 14 -> 13c
 *
 * Использовать функции стандартной библиотеки, напрямую и полностью решающие данную задачу
 * (например, n.toString(base) и подобные), запрещается.
 */
fun convertToString(n: Int, base: Int): String =
    if (n == 0) "0" else
        convert(n, base).joinToString(separator = "") {
            if (it >= 10) (it - 10 + 'a'.toInt()).toChar().toString() else it.toString()
        }

/**
 * Средняя (3 балла)
 *
 * Перевести число, представленное списком цифр digits от старшей к младшей,
 * из системы счисления с основанием base в десятичную.
 * Например: digits = (1, 3, 12), base = 14 -> 250
 */
fun decimal(digits: List<Int>, base: Int): Int {
    var result = 0

    // Разряд числа
    var digit = 1

    for (i in digits.indices.reversed()) {
        result += digits[i] * digit
        digit *= base
    }
    return result
}

/**
 * Сложная (4 балла)
 *
 * Перевести число, представленное цифровой строкой str,
 * из системы счисления с основанием base в десятичную.
 * Цифры более 9 представляются латинскими строчными буквами:
 * 10 -> a, 11 -> b, 12 -> c и так далее.
 * Например: str = "13c", base = 14 -> 250
 *
 * Использовать функции стандартной библиотеки, напрямую и полностью решающие данную задачу
 * (например, str.toInt(base)), запрещается.
 */
fun decimalFromString(str: String, base: Int): Int =
    decimal(str.toList().map { if (it in 'a'..'z') it.toInt() - 'a'.toInt() + 10 else it.toInt() - '0'.toInt() }, base)

/**
 * Сложная (5 баллов)
 *
 * Перевести натуральное число n > 0 в римскую систему.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: 23 = XXIII, 44 = XLIV, 100 = C
 */
fun roman(n: Int): String {
    var mutableN = n
    var result = ""

    while (mutableN >= 1000) {
        result += "M"
        mutableN -= 1000
    }

    if (mutableN >= 900) {
        result += "CM"
        mutableN -= 900
    }

    while (mutableN >= 500) {
        result += "D"
        mutableN -= 500
    }

    if (mutableN >= 400) {
        result += "CD"
        mutableN -= 400
    }

    while (mutableN >= 100) {
        result += "C"
        mutableN -= 100
    }

    if (mutableN >= 90) {
        result += "XC"
        mutableN -= 90
    }

    while (mutableN >= 50) {
        result += "L"
        mutableN -= 50
    }

    if (mutableN >= 40) {
        result += "XL"
        mutableN -= 40
    }

    while (mutableN >= 10) {
        result += "X"
        mutableN -= 10
    }

    if (mutableN >= 9) {
        result += "IX"
        mutableN -= 9
    }

    while (mutableN >= 5) {
        result += "V"
        mutableN -= 5
    }

    if (mutableN >= 4) {
        result += "IV"
        mutableN -= 4
    }

    while (mutableN >= 1) {
        result += "I"
        mutableN -= 1
    }
    return result
}

/**
 * Очень сложная (7 баллов)
 *
 * Записать заданное натуральное число 1..999999 прописью по-русски.
 * Например, 375 = "триста семьдесят пять",
 * 23964 = "двадцать три тысячи девятьсот шестьдесят четыре"
 */
fun russian(n: Int): String {

    val ones = mapOf(
        1 to "один", 2 to "два", 3 to "три", 4 to "четыре", 5 to "пять",
        6 to "шесть", 7 to "семь", 8 to "восемь", 9 to "девять",
        11 to "одиннадцать", 12 to "двенадцать", 13 to "тринадцать", 14 to "четырнадцать", 15 to "пятнадцать",
        16 to "шестнадцать", 17 to "семнадцать", 18 to "восемнадцать", 19 to "девятнадцать"
    )

    val tens = mapOf(
        1 to "десять", 2 to "двадцать", 3 to "тридцать", 4 to "сорок", 5 to "пятьдесят",
        6 to "шестьдесят", 7 to "семьдесят", 8 to "восемьдесят", 9 to "девяносто"
    )

    val hundreds = mapOf(
        1 to "сто", 2 to "двести", 3 to "триста", 4 to "четыреста", 5 to "пятьсот",
        6 to "шестьсот", 7 to "семьсот", 8 to "восемьсот", 9 to "девятьсот"
    )

    val thousandsRus = "тысяч"
    val thousands = mapOf(
        0 to "тысяч", 1 to "одна тысяча", 2 to "две тысячи", 3 to "три тысячи", 4 to "четыре тысячи",
        5 to ones[5] + " " + thousandsRus, 6 to ones[6] + " " + thousandsRus, 7 to ones[7] + " " + thousandsRus,
        8 to ones[8] + " " + thousandsRus, 9 to ones[9] + " " + thousandsRus
    )

    var result = ""

    val digits = n.toString().toList().reversed().map { it.toInt() - '0'.toInt() }

    // 100..999 тысяч
    if (digits.size > 5 && digits[5] >= 1) {
        result += hundreds[digits[5]] + " "
    }

    // 10, 20..99 тысяч
    if (digits.size > 4 && (digits[4] > 1 || digits[4] == 1 && digits[3] == 0)) {
        result += tens[digits[4]] + " "
    }

    // 1..9, 11..19 тысяч
    if (digits.size > 3) {
        result += if (digits[3] >= 1) {
            if (digits.size > 4 && digits[4] == 1) {
                // 11..19 тысяч
                ones[digits[4] * 10 + digits[3]] + " " + thousandsRus
            } else {
                // 1..9 тысяч
                thousands[digits[3]]
            }

        } else {
            // Если XY0 тысяч
            thousandsRus
        }

        if (digits[0] != 0 || digits[1] != 0 || digits[2] != 0)
            result += " "
    }

    // 100..999
    if (digits.size > 2 && digits[2] >= 1) {
        result += hundreds[digits[2]]
        if (digits[1] > 0 || digits[0] > 0) result += " "
    }

    // 10, 20..99
    if (digits.size > 1 && (digits[1] > 1 || digits[1] == 1 && digits[0] == 0)) {
        result += tens[digits[1]]
        if (digits[0] > 0) result += " "
    }

    // 1..9, 11..19
    if (digits[0] >= 1) {
        result += if (digits.size > 1 && digits[1] == 1) {
            // 11..19
            ones[digits[1] * 10 + digits[0]]
        } else {
            // 1..9
            ones[digits[0]]
        }
    }

    return result

}