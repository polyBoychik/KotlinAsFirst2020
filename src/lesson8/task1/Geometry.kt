@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import java.util.*
import kotlin.math.*

// Урок 8: простые классы
// Максимальное количество баллов = 40 (без очень трудных задач = 11)

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая (2 балла)
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double = max(0.0, center.distance(other.center) - radius - other.radius)

    /**
     * Тривиальная (1 балл)
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean = center.distance(p) <= radius

    /**
     * Возвращает true, если окружность содержит все точки points, иначе возвращает false
     */
    fun containsAll(points: List<Point>): Boolean {
        for (p in points)
            if (!this.contains(p))
                return false
        return true
    }
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    override fun equals(other: Any?) =
        other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        begin.hashCode() + end.hashCode()
}

/**
 * Возвращает Point с координатами, равными среднему арифметическому координат points
 */
fun meanPoint(points: List<Point>): Point {
    var x = 0.0
    var y = 0.0

    for ((x1, y1) in points) {
        x += x1
        y += y1
    }

    return Point(x / points.size, y / points.size)
}

fun rotate(a: Point, b: Point, c: Point): Double = (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x)

/**
 * Возвращает список точек, образующих минимальную выпуклую оболочку
 * (Graham scan)
 */
fun convexHull(points: List<Point>): List<Point> {
    var p0 = points[0]

    // Взятие самой нижней правой точки за начальную
    for (p in points) {
        if (p.y < p0.y || p0.y == p.y && p0.x > p.x) {
            p0 = p
        }
    }

    // Сортировка точек по полярному углу
    val sorted = points.minus(p0).sortedBy { (it.y - p0.y) / (it.x - p0.x) }
    val pts = Stack<Point>()
    pts.push(p0)
    pts.push(sorted[0])

    for (p in sorted.slice(1..sorted.lastIndex)) {
        if (rotate(pts[pts.lastIndex - 1], pts.peek(), p) < 0) {
            pts.pop()
        }
        pts.push(p)
    }

    return pts.toList()
}


/**
 * Средняя (3 балла)
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {

    if (points.size < 2)
        throw IllegalArgumentException("Not enough points")

    var p1 = 0
    var p2 = 0

    var diameter = 0.0

    for (i in points.indices) {
        for (j in i + 1..points.lastIndex) {
            val d = points[i].distance(points[j])
            if (d > diameter) {
                diameter = d
                p1 = i
                p2 = j
            }
        }
    }
    return Segment(points[p1], points[p2])
}

/**
 * Простая (2 балла)
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle =
    Circle(
        meanPoint(listOf(diameter.begin, diameter.end)),
        diameter.begin.distance(diameter.end) / 2
    )

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        require(angle >= 0 && angle < PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(point.y * cos(angle) - point.x * sin(angle), angle)

    /**
     * Средняя (3 балла)
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        val thisSin = sin(this.angle)
        val thisCos = cos(this.angle)
        val otherSin = sin(other.angle)
        val otherCos = cos(other.angle)

        require(!(this.angle == PI / 2 && other.angle == PI / 2)) { "There are infinite count of points" }

        // solution of the first equation
        val x = (-this.b * otherCos + other.b * thisCos) / (thisSin * otherCos - otherSin * thisCos)
        // solution of the second equation
        val y = if (this.angle != PI / 2)
            (x * thisSin + this.b) / thisCos
        else
            (x * otherSin + other.b) / otherCos

        return Point(x, y)
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

/**
 * Возвращает угол (в рад.) между прямой, соединяющей точки a и b, и  Ох
 */
fun angleBetweenPointsAndOx(a: Point, b: Point) = atan((b.y - a.y) / (b.x - a.x))

/**
 * Средняя (3 балла)
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line =
    Line(
        s.begin,
        if (s.end.x - s.begin.x != 0.0) (angleBetweenPointsAndOx(s.begin, s.end) + PI) % PI else PI / 2
    )

/**
 * Средняя (3 балла)
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line = lineBySegment(Segment(a, b))

/**
 * Сложная (5 баллов)
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line = Line(meanPoint(listOf(a, b)), (angleBetweenPointsAndOx(a, b) + PI / 2) % PI)

/**
 * Средняя (3 балла)
 *
 * Задан список из n окружностей на плоскости.
 * Найти пару наименее удалённых из них; расстояние между окружностями
 * рассчитывать так, как указано в Circle.distance.
 *
 * При наличии нескольких наименее удалённых пар,
 * вернуть первую из них по порядку в списке circles.
 *
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> {
    if (circles.size < 2) throw IllegalArgumentException("Not enough circles")

    var firstCircle = circles[0]
    var secondCircle = circles[1]
    var minDist = Double.POSITIVE_INFINITY
    for (i in circles.indices) {
        for (j in i + 1..circles.lastIndex) {
            val currentDist = circles[i].distance(circles[j])
            if (currentDist < minDist) {
                minDist = currentDist
                firstCircle = circles[i]
                secondCircle = circles[j]
            }
        }
    }

    return Pair(firstCircle, secondCircle)
}

/**
 * Сложная (5 баллов)
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle {
    val firstLine = Line(meanPoint(listOf(a, b)), (angleBetweenPointsAndOx(a, b) + PI / 2) % PI)
    val secondLine = Line(meanPoint(listOf(b, c)), (angleBetweenPointsAndOx(b, c) + PI / 2) % PI)

    val center = firstLine.crossPoint(secondLine)

    return Circle(center, center.distance(a))
}

/**
 * Очень сложная (10 баллов)
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
fun minContainingCircle(vararg points: Point): Circle {
    if (points.isEmpty()) throw IllegalArgumentException("Parameter's list hasn't be empty")

    if (points.size == 1)
        return Circle(points[0], 0.0)
    if (points.size == 2)
        return Circle(meanPoint(points.toList()), points[0].distance(points[1]) / 2)

    val convexHull = convexHull(points.toList())
    var minCircle = Circle(Point(0.0, 0.0), Double.POSITIVE_INFINITY)

    for (i in convexHull.indices) {
        for (j in i + 1..convexHull.lastIndex) {
            val circle = circleByDiameter(Segment(convexHull[i], convexHull[j]))

            if (circle.containsAll(convexHull) && circle.radius < minCircle.radius)
                minCircle = circle
        }
    }

    for (i in convexHull.indices) {
        for (j in i + 1..convexHull.lastIndex) {
            for (k in j + 1..convexHull.lastIndex) {
                if (lineByPoints(convexHull[i], convexHull[j]).angle != lineByPoints(convexHull[i], convexHull[k]).angle) {
                    val circle = circleByThreePoints(convexHull[i], convexHull[j], convexHull[k])

                    if (circle.containsAll(convexHull) && circle.radius < minCircle.radius)
                        minCircle = circle
                }
            }
        }
    }

    return minCircle
}

