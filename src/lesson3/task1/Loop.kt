@file:Suppress("UNUSED_PARAMETER")

package lesson3.task1

import kotlin.math.*

// Урок 3: циклы
// Максимальное количество баллов = 9
// Рекомендуемое количество баллов = 7
// Вместе с предыдущими уроками = 16/21

/**
 * Пример
 *
 * Вычисление факториала
 */
fun factorial(n: Int): Double {
    var result = 1.0
    for (i in 1..n) {
        result *= i // Please do not fix in master
    }
    return result
}

/**
 * Пример
 *
 * Проверка числа на простоту -- результат true, если число простое
 */
fun isPrime(n: Int): Boolean {
    if (n < 2) return false
    if (n == 2) return true
    if (n % 2 == 0) return false
    for (m in 3..sqrt(n.toDouble()).toInt() step 2) {
        if (n % m == 0) return false
    }
    return true
}

/**
 * Пример
 *
 * Проверка числа на совершенность -- результат true, если число совершенное
 */
fun isPerfect(n: Int): Boolean {
    var sum = 1
    for (m in 2..n / 2) {
        if (n % m > 0) continue
        sum += m
        if (sum > n) break
    }
    return sum == n
}

/**
 * Пример
 *
 * Найти число вхождений цифры m в число n
 */
fun digitCountInNumber(n: Int, m: Int): Int =
    when {
        n == m -> 1
        n < 10 -> 0
        else -> digitCountInNumber(n / 10, m) + digitCountInNumber(n % 10, m)
    }

/**
 * Простая (2 балла)
 *
 * Найти количество цифр в заданном числе n.
 * Например, число 1 содержит 1 цифру, 456 -- 3 цифры, 65536 -- 5 цифр.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun digitNumber(n: Int): Int =
    ceil(abs(log10(abs(n) + 0.5))).toInt()

/**
 * Простая (2 балла)
 *
 * Найти число Фибоначчи из ряда 1, 1, 2, 3, 5, 8, 13, 21, ... с номером n.
 * Ряд Фибоначчи определён следующим образом: fib(1) = 1, fib(2) = 1, fib(n+2) = fib(n) + fib(n+1)
 */
fun fib(n: Int): Int {
    var fib1 = 0
    // fib2 инициализирован как первый элемент последовательности
    var fib2 = 1

    for (i in 2..n) {
        val fib = fib1 + fib2
        fib1 = fib2
        fib2 = fib
    }
    return fib2
}


/**
 * Простая (2 балла)
 *
 * Для заданного числа n > 1 найти минимальный делитель, превышающий 1
 */
fun minDivisor(n: Int): Int {
    val sqrt = sqrt(n.toDouble()).toInt() + 1
    if (n % 2 == 0)
        return 2

    var found = false

    var i = 3
    while (i <= sqrt && !found) {
        if (n % i == 0) {
            found = true
            break
        }
        // Пропуск четных чисел
        i += 2
    }

    return if (found) i else n
}

/**
 * Простая (2 балла)
 *
 * Для заданного числа n > 1 найти максимальный делитель, меньший n
 */
fun maxDivisor(n: Int): Int = n / minDivisor(n)

/**
 * Простая (2 балла)
 *
 * Гипотеза Коллатца. Рекуррентная последовательность чисел задана следующим образом:
 *
 *   ЕСЛИ (X четное)
 *     Xслед = X /2
 *   ИНАЧЕ
 *     Xслед = 3 * X + 1
 *
 * например
 *   15 46 23 70 35 106 53 160 80 40 20 10 5 16 8 4 2 1 4 2 1 4 2 1 ...
 * Данная последовательность рано или поздно встречает X == 1.
 * Написать функцию, которая находит, сколько шагов требуется для
 * этого для какого-либо начального X > 0.
 */
fun collatzSteps(x: Int): Int {
    var steps = 0
    var currentElement = x
    while (currentElement > 1) {
        if (currentElement % 2 == 0)
            currentElement /= 2
        else
            currentElement = 3 * currentElement + 1

        steps++
    }
    return steps
}

/**
 * Возвращает наибольший общий делитель m и n
 */
fun gcd(m: Int, n: Int): Int {
    var mutableM = m
    var mutableN = n

    while (mutableM != mutableN) {
        if (mutableM > mutableN)
            mutableM -= mutableN
        else
            mutableN -= mutableM
    }
    return mutableM
}

/**
 * Средняя (3 балла)
 *
 * Для заданных чисел m и n найти наименьшее общее кратное, то есть,
 * минимальное число k, которое делится и на m и на n без остатка
 */
fun lcm(m: Int, n: Int): Int = m * n / gcd(m, n)

/**
 * Средняя (3 балла)
 *
 * Определить, являются ли два заданных числа m и n взаимно простыми.
 * Взаимно простые числа не имеют общих делителей, кроме 1.
 * Например, 25 и 49 взаимно простые, а 6 и 8 -- нет.
 */
fun isCoPrime(m: Int, n: Int): Boolean = gcd(m, n) == 1

/**
 * Средняя (3 балла)
 *
 * Для заданных чисел m и n, m <= n, определить, имеется ли хотя бы один точный квадрат между m и n,
 * то есть, существует ли такое целое k, что m <= k*k <= n.
 * Например, для интервала 21..28 21 <= 5*5 <= 28, а для интервала 51..61 квадрата не существует.
 */
fun squareBetweenExists(m: Int, n: Int): Boolean {
    val minSqrt = ceil(sqrt(m.toDouble())).toInt()
    val maxSqrt = floor(sqrt(n.toDouble())).toInt()

    return maxSqrt - minSqrt >= 0

}

/**
 * Средняя (3 балла)
 *
 * Поменять порядок цифр заданного числа n на обратный: 13478 -> 87431.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun revert(n: Int): Int {
    var result = 0
    var mutableN = n

    while (mutableN > 0) {
        result *= 10
        result += mutableN % 10
        mutableN /= 10
    }
    return result
}

/**
 * Средняя (3 балла)
 *
 * Проверить, является ли заданное число n палиндромом:
 * первая цифра равна последней, вторая -- предпоследней и так далее.
 * 15751 -- палиндром, 3653 -- нет.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun isPalindrome(n: Int): Boolean {
    val digitNum = digitNumber(n)
    for (i in 0 until digitNum) {
        if (n / pow(10, i) % 10 != n / pow(10, digitNum - i - 1) % 10)
            return false
    }
    return true
}

/**
 * Средняя (3 балла)
 *
 * Для заданного числа n определить, содержит ли оно различающиеся цифры.
 * Например, 54 и 323 состоят из разных цифр, а 111 и 0 из одинаковых.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun hasDifferentDigits(n: Int): Boolean {
    val rightDigit = n % 10
    // n без правой цифры, т.к. поиск отличной цифры происходит относительно этой цифры
    var mutableN = n / 10
    while (mutableN > 0) {
        if (mutableN % 10 != rightDigit)
            return true
        mutableN /= 10
    }

    return false
}

/**
 * Средняя (4 балла)
 *
 * Для заданного x рассчитать с заданной точностью eps
 * sin(x) = x - x^3 / 3! + x^5 / 5! - x^7 / 7! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю.
 * Подумайте, как добиться более быстрой сходимости ряда при больших значениях x.
 * Использовать kotlin.math.sin и другие стандартные реализации функции синуса в этой задаче запрещается.
 */
fun sin(x: Double, eps: Double): Double {
    var mutableX = x

    // how 2pi in x?
    if (x > 2 * PI)
        mutableX -= (2 * PI) * floor(x / (2 * PI))
    else if (x < -2 * PI)
        mutableX += (2 * PI) * floor(x / (2 * PI))

    val sqrX = mutableX * mutableX

    var a = mutableX
    var factorialBase = 1

    var sinx = 0.0

    do {
        sinx += a
        a *= -1.0 * sqrX / (2 * factorialBase) / (2 * factorialBase + 1)
        factorialBase += 1

    } while (abs(a) >= eps)
    return sinx
}

/**
 * Средняя (4 балла)
 *
 * Для заданного x рассчитать с заданной точностью eps
 * cos(x) = 1 - x^2 / 2! + x^4 / 4! - x^6 / 6! + ...
 * Нужную точность считать достигнутой, если очередной член ряда меньше eps по модулю
 * Подумайте, как добиться более быстрой сходимости ряда при больших значениях x.
 * Использовать kotlin.math.cos и другие стандартные реализации функции косинуса в этой задаче запрещается.
 */
fun cos(x: Double, eps: Double): Double {
    var mutableX = x

    // how 2pi in x?
    if (x > 2 * PI)
        mutableX -= (2 * PI) * floor(x / (2 * PI))
    else if (x < -2 * PI)
        mutableX += (2 * PI) * floor(x / (2 * PI))

    val sqrX = mutableX * mutableX

    var a = 1.0
    var factorialBase = 1

    var cosx = 0.0
    do {
        cosx += a
        a *= -1.0 * sqrX / (2 * factorialBase - 1) / (2 * factorialBase)
        factorialBase += 1

    } while (abs(a) >= eps)
    return cosx
}

/**
 * Сложная (4 балла)
 *
 * Найти n-ю цифру последовательности из квадратов целых чисел:
 * 149162536496481100121144...
 * Например, 2-я цифра равна 4, 7-я 5, 12-я 6.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun squareSequenceDigit(n: Int): Int {
    var digit = 0
    var base = 1

    var sqr = 1
    var sqrDigitsNum = 1

    while (digit + sqrDigitsNum < n) {
        digit += sqrDigitsNum

        base++
        sqr = base * base
        sqrDigitsNum = digitNumber(sqr)
    }
    return when (digit + sqrDigitsNum) {
        n -> sqr % 10
        else -> sqr / pow(10, digit + sqrDigitsNum - n) % 10
    }
}

/**
 * Возводит целое base в целую неотрицательную степень n
 */
fun pow(base: Int, power: Int): Int {
    if (power == 0)
        return 1
    var res = base
    var needPower = power
    while (needPower > 1) {
        res *= base
        needPower--
    }
    return res
}

/**
 * Сложная (5 баллов)
 *
 * Найти n-ю цифру последовательности из чисел Фибоначчи (см. функцию fib выше):
 * 1123581321345589144...
 * Например, 2-я цифра равна 1, 9-я 2, 14-я 5.
 *
 * Использовать операции со строками в этой задаче запрещается.
 */
fun fibSequenceDigit(n: Int): Int {
    var digit = 0

    // fib2 на момент инициализации - первый член последовательности Фибоначчи
    var fib1 = 0
    var fib2 = 1

    var fibDigitsNum = 1

    while (digit + fibDigitsNum < n) {
        digit += fibDigitsNum

        val fib = fib1 + fib2
        fib1 = fib2
        fib2 = fib

        fibDigitsNum = digitNumber(fib2)
    }
    return when (digit + fibDigitsNum) {
        n -> fib2 % 10
        else -> fib2 / pow(10, digit + fibDigitsNum - n) % 10
    }
}
