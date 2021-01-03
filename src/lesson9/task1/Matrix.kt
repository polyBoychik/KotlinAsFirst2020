@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson9.task1

// Урок 9: проектирование классов
// Максимальное количество баллов = 40 (без очень трудных задач = 15)

/**
 * Ячейка матрицы: row = ряд, column = колонка
 */
data class Cell(val row: Int, val column: Int)

/**
 * Интерфейс, описывающий возможности матрицы. E = тип элемента матрицы
 */
interface Matrix<E> {
    /** Высота */
    val height: Int

    /** Ширина */
    val width: Int

    /**
     * Доступ к ячейке.
     * Методы могут бросить исключение, если ячейка не существует или пуста
     */
    operator fun get(row: Int, column: Int): E

    operator fun get(cell: Cell): E = get(cell.row, cell.column)

    /**
     * Запись в ячейку.
     * Методы могут бросить исключение, если ячейка не существует
     */
    operator fun set(row: Int, column: Int, value: E)

    operator fun set(cell: Cell, value: E) = set(cell.row, cell.column, value)
}

/**
 * Простая (2 балла)
 *
 * Метод для создания матрицы, должен вернуть РЕАЛИЗАЦИЮ Matrix<E>.
 * height = высота, width = ширина, e = чем заполнить элементы.
 * Бросить исключение IllegalArgumentException, если height или width <= 0.
 */
fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E> =
    if (height <= 0 || width <= 0)
        throw IllegalArgumentException("Invalid argument(s)")
    else
        MatrixImpl(height, width, e)

/**
 * Средняя сложность (считается двумя задачами в 3 балла каждая)
 *
 * Реализация интерфейса "матрица"
 */
class MatrixImpl<E>(
    override val height: Int,
    override val width: Int,
    initial: E
) : Matrix<E> {

    private val storage = MutableList(height) { MutableList(width) { initial } }


    override fun get(row: Int, column: Int): E =
        if (row in 0 until height && column in 0 until width)
            storage[row][column]
        else
            throw IndexOutOfBoundsException("Invalid index")


    override fun set(row: Int, column: Int, value: E) =
        if (row in 0 until height && column in 0 until width)
            storage[row][column] = value
        else
            throw IndexOutOfBoundsException("Invalid index")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Matrix<*>) return false
        if (this.height != other.height || this.width != other.width) return false

        for (i in storage.indices)
            for (j in storage[i].indices)
                if (this[i, j] != other[i, j])
                    return false
        return true
    }

    override fun toString(): String =
        storage.joinToString(separator = ", ", prefix = "[", postfix = "]") {
            it.joinToString(separator = ", ", prefix = "[", postfix = "]")
        }
}

