@file:Suppress("UNUSED_PARAMETER")

package lesson8.task2

import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max

fun even(n: Int) = n and 1 == 0

/**
 * Клетка шахматной доски. Шахматная доска квадратная и имеет 8 х 8 клеток.
 * Поэтому, обе координаты клетки (горизонталь row, вертикаль column) могут находиться в пределах от 1 до 8.
 * Горизонтали нумеруются снизу вверх, вертикали слева направо.
 */
data class Square(val column: Int, val row: Int) {
    /**
     * Пример
     *
     * Возвращает true, если клетка находится в пределах доски
     */
    fun inside(): Boolean = column in 1..8 && row in 1..8

    /**
     * Простая (2 балла)
     *
     * Возвращает строковую нотацию для клетки.
     * В нотации, колонки обозначаются латинскими буквами от a до h, а ряды -- цифрами от 1 до 8.
     * Для клетки не в пределах доски вернуть пустую строку
     */
    fun notation(): String = if (inside()) "${'a' + column - 1}$row" else ""
}

/**
 * Простая (2 балла)
 *
 * Создаёт клетку по строковой нотации.
 * В нотации, колонки обозначаются латинскими буквами от a до h, а ряды -- цифрами от 1 до 8.
 * Если нотация некорректна, бросить IllegalArgumentException
 */
fun square(notation: String): Square =
    if (notation.length == 2 && notation[0] in 'a'..'h' && notation[1] in '1'..'8')
        Square(notation[0] - 'a' + 1, notation[1] - '0') else
        throw IllegalArgumentException("Invalid notation format: $notation")


/**
 * Простая (2 балла)
 *
 * Определить число ходов, за которое шахматная ладья пройдёт из клетки start в клетку end.
 * Шахматная ладья может за один ход переместиться на любую другую клетку
 * по вертикали или горизонтали.
 * Ниже точками выделены возможные ходы ладьи, а крестиками -- невозможные:
 *
 * xx.xxххх
 * xх.хxххх
 * ..Л.....
 * xх.хxххх
 * xx.xxххх
 * xx.xxххх
 * xx.xxххх
 * xx.xxххх
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: rookMoveNumber(Square(3, 1), Square(6, 3)) = 2
 * Ладья может пройти через клетку (3, 3) или через клетку (6, 1) к клетке (6, 3).
 */
fun rookMoveNumber(start: Square, end: Square): Int =
    if (!(start.inside() && end.inside()))
        throw IllegalArgumentException("invalid format of argument(s)")
    else
        (if (start.column == end.column) 0 else 1) + (if (start.row == end.row) 0 else 1)

/**
 * Средняя (3 балла)
 *
 * Вернуть список из клеток, по которым шахматная ладья может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов ладьи см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры: rookTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          (здесь возможен ещё один вариант)
 *          rookTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(3, 3), Square(6, 3))
 *          (здесь возможен единственный вариант)
 *          rookTrajectory(Square(3, 5), Square(8, 5)) = listOf(Square(3, 5), Square(8, 5))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun rookTrajectory(start: Square, end: Square): List<Square> {
    val trajectory = mutableListOf(start)

    if (start.row != end.row)
        trajectory.add(Square(start.column, end.row))
    if (start.column != end.column)
        trajectory.add(Square(end.column, end.row))
    return trajectory
}

/**
 * Возвращает:
 *      true, если клетки first и second лежат на одной диагонали
 *      false, если клетки first и second лежат на разных диагоналях
 *
 * Бросает IllegalArgumentException в случае некорректности любой из клеток
 */
fun sameDiagonal(first: Square, second: Square) =
    if (!(first.inside() && second.inside()))
        throw IllegalArgumentException("Invalid argument(s)")
    else
        second.row - first.row == second.column - first.column

/**
 * Возвращает:
 *      true, если клетки first и second одного цвета
 *      false, если клетки first и second разного цвета
 *
 * Бросает IllegalArgumentException в случае некорректности любой из клеток
 */
fun sameColor(first: Square, second: Square) =
    if (!(first.inside() && second.inside()))
        throw IllegalArgumentException("Invalid argument(s)")
    else
        even(first.column + first.row) == even(second.column + second.row)


/**
 * Простая (2 балла)
 *
 * Определить число ходов, за которое шахматный слон пройдёт из клетки start в клетку end.
 * Шахматный слон может за один ход переместиться на любую другую клетку по диагонали.
 * Ниже точками выделены возможные ходы слона, а крестиками -- невозможные:
 *
 * .xxx.ххх
 * x.x.xххх
 * xxСxxxxx
 * x.x.xххх
 * .xxx.ххх
 * xxxxx.хх
 * xxxxxх.х
 * xxxxxхх.
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если клетка end недостижима для слона, вернуть -1.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Примеры: bishopMoveNumber(Square(3, 1), Square(6, 3)) = -1; bishopMoveNumber(Square(3, 1), Square(3, 7)) = 2.
 * Слон может пройти через клетку (6, 4) к клетке (3, 7).
 */
fun bishopMoveNumber(start: Square, end: Square): Int =
    if (!(start.inside() && end.inside()))
        throw IllegalArgumentException("Invalid argument(s)")
    else
        if (sameColor(start, end))
            if (sameDiagonal(start, end))
                if (start == end) 0 else 1
            else 2
        else -1

/**
 * Сложная (5 баллов)
 *
 * Вернуть список из клеток, по которым шахматный слон может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов слона см. предыдущую задачу.
 *
 * Если клетка end недостижима для слона, вернуть пустой список.
 *
 * Если клетка достижима:
 * - список всегда включает в себя клетку start
 * - клетка end включается, если она не совпадает со start.
 * - между ними должны находиться промежуточные клетки, по порядку от start до end.
 *
 * Примеры: bishopTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          bishopTrajectory(Square(3, 1), Square(3, 7)) = listOf(Square(3, 1), Square(6, 4), Square(3, 7))
 *          bishopTrajectory(Square(1, 3), Square(6, 8)) = listOf(Square(1, 3), Square(6, 8))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun bishopTrajectory(start: Square, end: Square): List<Square> {
    val trajectory = mutableListOf<Square>()

    if (sameColor(start, end)) {
        trajectory.add(start)

        val dMove = (end.column - start.column + end.row - start.row) / 2

        if (dMove != 0) {
            val dColumn = if (start.column + dMove > 8) -dMove else dMove
            val dRow = if (start.row + dMove > 8) -dMove else dMove

            trajectory.add(Square(start.column + dColumn, start.row + dRow))
        }

        if (trajectory.last() != end)
            trajectory.add(end)
    }
    return trajectory
}

/**
 * Средняя (3 балла)
 *
 * Определить число ходов, за которое шахматный король пройдёт из клетки start в клетку end.
 * Шахматный король одним ходом может переместиться из клетки, в которой стоит,
 * на любую соседнюю по вертикали, горизонтали или диагонали.
 * Ниже точками выделены возможные ходы короля, а крестиками -- невозможные:
 *
 * xxxxx
 * x...x
 * x.K.x
 * x...x
 * xxxxx
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: kingMoveNumber(Square(3, 1), Square(6, 3)) = 3.
 * Король может последовательно пройти через клетки (4, 2) и (5, 2) к клетке (6, 3).
 */
fun kingMoveNumber(start: Square, end: Square): Int =
    if (!(start.inside() && end.inside()))
        throw IllegalArgumentException("Invalid argument(s)")
    else
        max(abs(end.column - start.column), abs(end.row - start.row))

/**
 * Сложная (5 баллов)
 *
 * Вернуть список из клеток, по которым шахматный король может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов короля см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры: kingTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 *          (здесь возможны другие варианты)
 *          kingTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(4, 2), Square(5, 2), Square(6, 3))
 *          (здесь возможен единственный вариант)
 *          kingTrajectory(Square(3, 5), Square(6, 2)) = listOf(Square(3, 5), Square(4, 4), Square(5, 3), Square(6, 2))
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun kingTrajectory(start: Square, end: Square): List<Square> {
    val trajectory = mutableListOf(start)

    while (trajectory.last() != end) {
        val dColumn = end.column - trajectory.last().column
        val dRow = end.row - trajectory.last().row
        trajectory.add(
            Square(
                trajectory.last().column + if (dColumn != 0) dColumn / abs(dColumn) else 0,
                trajectory.last().row + if (dRow != 0) dRow / abs(dRow) else 0
            )
        )
    }
    return trajectory
}


/**
 * Сложная (6 баллов)
 *
 * Определить число ходов, за которое шахматный конь пройдёт из клетки start в клетку end.
 * Шахматный конь одним ходом вначале передвигается ровно на 2 клетки по горизонтали или вертикали,
 * а затем ещё на 1 клетку под прямым углом, образуя букву "Г".
 * Ниже точками выделены возможные ходы коня, а крестиками -- невозможные:
 *
 * .xxx.xxx
 * xxKxxxxx
 * .xxx.xxx
 * x.x.xxxx
 * xxxxxxxx
 * xxxxxxxx
 * xxxxxxxx
 * xxxxxxxx
 *
 * Если клетки start и end совпадают, вернуть 0.
 * Если любая из клеток некорректна, бросить IllegalArgumentException().
 *
 * Пример: knightMoveNumber(Square(3, 1), Square(6, 3)) = 3.
 * Конь может последовательно пройти через клетки (5, 2) и (4, 4) к клетке (6, 3).
 */
fun knightMoveNumber(start: Square, end: Square): Int {
    if (!(start.inside() && end.inside()))
        throw IllegalArgumentException("Invalid argument(s)")

    if (start == end)
        return 0

    val field = Array(8) { Array(8) { 255 } }

    val endCol = end.column - 1
    val endRow = end.row - 1

    var step = 0

    fun valid(row: Int, col: Int) = row in 0..7 && col in 0..7

    fun makeStep(row: Int, col: Int): Boolean {
        if (valid(row, col)) {
            if (field[row][col] == 255)
                field[row][col] = step + 1
            return row == endRow && col == endCol
        }
        return false
    }


    field[start.row - 1][start.column - 1] = 0

    while (true) {

        for (i in field.indices) {
            for (j in field[i].indices) {
                if (field[i][j] == step) {
                    if (makeStep(i + 2, j + 1)) return field[i + 2][j + 1]
                    if (makeStep(i + 2, j - 1)) return field[i + 2][j - 1]
                    if (makeStep(i - 2, j + 1)) return field[i - 2][j + 1]
                    if (makeStep(i - 2, j - 1)) return field[i - 2][j - 1]

                    if (makeStep(i + 1, j + 2)) return field[i + 1][j + 2]
                    if (makeStep(i + 1, j - 2)) return field[i + 1][j - 2]
                    if (makeStep(i - 1, j + 2)) return field[i - 1][j + 2]
                    if (makeStep(i - 1, j - 2)) return field[i - 1][j - 2]
                }
            }
        }
        step++
    }
}

/**
 * Очень сложная (10 баллов)
 *
 * Вернуть список из клеток, по которым шахматный конь может быстрее всего попасть из клетки start в клетку end.
 * Описание ходов коня см. предыдущую задачу.
 * Список всегда включает в себя клетку start. Клетка end включается, если она не совпадает со start.
 * Между ними должны находиться промежуточные клетки, по порядку от start до end.
 * Примеры:
 *
 * knightTrajectory(Square(3, 3), Square(3, 3)) = listOf(Square(3, 3))
 * здесь возможны другие варианты)
 * knightTrajectory(Square(3, 1), Square(6, 3)) = listOf(Square(3, 1), Square(5, 2), Square(4, 4), Square(6, 3))
 * (здесь возможен единственный вариант)
 * knightTrajectory(Square(3, 5), Square(5, 6)) = listOf(Square(3, 5), Square(5, 6))
 * (здесь опять возможны другие варианты)
 * knightTrajectory(Square(7, 7), Square(8, 8)) =
 *     listOf(Square(7, 7), Square(5, 8), Square(4, 6), Square(6, 7), Square(8, 8))
 *
 * Если возможно несколько вариантов самой быстрой траектории, вернуть любой из них.
 */
fun knightTrajectory(start: Square, end: Square): List<Square> = TODO()
