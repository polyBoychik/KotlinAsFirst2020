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

    // Взятие самой левой точки за начальную
    for (p in points) {
        if (p.x < p0.x) {
            p0 = p
        }
    }

    // Сортировка точек по полярному углу
    val sorted = points.minus(p0).sortedBy { (it.y - p0.y) / (it.x - p0.x) }
    val pts = Stack<Point>()
    pts.push(p0)
    pts.push(sorted[0])

    for (p in sorted.slice(1..sorted.lastIndex)) {
        while (pts.size > 1 && rotate(pts[pts.lastIndex - 1], pts.peek(), p) <= 0) {
            pts.pop()
        }
        pts.push(p)
    }

    return pts.toList()
}

fun nextCycledIdx(what: Int, size: Int) = (what + 1) % size

/**
 * Средняя (3 балла)
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {

    fun angle(a: Segment, b: Segment): Double {
        val angle =
            PI - (atan2(b.end.y - b.begin.y, b.end.x - b.begin.x) - atan2(a.end.y - a.begin.y, a.end.x - a.begin.x))
        return (angle % (2 * PI))
    }

    if (points.size < 2)
        throw IllegalArgumentException("Not enough points")

    val convexHull = convexHull(points.toList())

    var i = 0
    var j = 1

    while (angle(
            Segment(convexHull[i], convexHull[nextCycledIdx(i, convexHull.size)]),
            Segment(convexHull[j], convexHull[nextCycledIdx(j, convexHull.size)])
        ) < PI
    ) {
        j = nextCycledIdx(j, convexHull.size)
    }

    var p1 = i
    var p2 = j

    var diameter = convexHull[i].distance(convexHull[j])

    while (j != 1) {
        val ang = 2 * PI - angle(
            Segment(convexHull[i], convexHull[nextCycledIdx(i, convexHull.size)]),
            Segment(convexHull[j], convexHull[nextCycledIdx(j, convexHull.size)])
        )

        when {
            ang == PI -> {
                val a = nextCycledIdx(i, convexHull.size)
                val b = nextCycledIdx(j, convexHull.size)
                var d1 = convexHull[a].distance(convexHull[j])
                if (d1 > diameter) {
                    p1 = a
                    p2 = j
                    diameter = d1
                }

                d1 = convexHull[i].distance(convexHull[b])
                if (d1 > diameter) {
                    p1 = i
                    p2 = b
                    diameter = d1
                }
                d1 = convexHull[a].distance(convexHull[b])
                if (d1 > diameter) {
                    p1 = a
                    p2 = b
                    diameter = d1
                }

                i = nextCycledIdx(i, convexHull.size)
                j = nextCycledIdx(j, convexHull.size)
            }
            ang < PI -> {
                val d1 = convexHull[nextCycledIdx(i, convexHull.size)].distance(convexHull[j])
                if (d1 > diameter) {
                    p1 = nextCycledIdx(i, convexHull.size)
                    p2 = j
                    diameter = d1
                }
                i = nextCycledIdx(i, convexHull.size)
            }
            else -> {
                val d1 = convexHull[i].distance(convexHull[nextCycledIdx(j, convexHull.size)])
                if (d1 > diameter) {
                    p1 = i
                    p2 = nextCycledIdx(j, convexHull.size)
                    diameter = d1
                }
                j = nextCycledIdx(j, convexHull.size)
            }
        }
    }

    return Segment(convexHull[p1], convexHull[p2])
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
fun angleBetweenPointsAndOx(a: Point, b: Point): Double {
    val angle = atan2(b.y - a.y, b.x - a.x)
    return if (angle <= 0.0) angle + PI else angle
}

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
fun bisectorByPoints(a: Point, b: Point): Line =
    Line(meanPoint(listOf(a, b)), (angleBetweenPointsAndOx(a, b) + PI / 2) % PI)

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

// center rotation
fun rotatedLine(a: Point, b: Point, angle: Double): Line =
    Line(meanPoint(listOf(a, b)), (angleBetweenPointsAndOx(a, b) + PI + angle) % PI)

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
    val firstLine = rotatedLine(a, b, PI / 2)
    val secondLine = rotatedLine(b, c, PI / 2)

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

    val pts = points.toSet().toList()

    if (pts.size == 1)
        return Circle(pts[0], 0.0)
    if (pts.size == 2)
        return Circle(meanPoint(pts.toList()), pts[0].distance(pts[1]) / 2)

    val convexHull = convexHull(pts.toList())

    var circle = circleByDiameter(diameter(*convexHull.toTypedArray()))
    var minCircle = if (circle.containsAll(pts))
        circle
    else
        Circle(Point(0.0, 0.0), Double.POSITIVE_INFINITY)

    for (i in convexHull.indices) {
        for (j in i + 1..convexHull.lastIndex) {
            for (k in j + 1..convexHull.lastIndex) {
                // Пришлось прибегать к eps, т.к. радианы неохотно сравнивались

                if (abs(
                        lineByPoints(convexHull[i], convexHull[j]).angle - lineByPoints(
                            convexHull[j],
                            convexHull[k]
                        ).angle
                    ) > 1e-10
                ) {
                    circle = circleByThreePoints(convexHull[i], convexHull[j], convexHull[k])
                    if (circle.containsAll(convexHull) && circle.radius < minCircle.radius)
                        minCircle = circle
                }
            }
        }
    }

    return minCircle
}

