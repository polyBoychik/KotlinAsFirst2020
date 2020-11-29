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


enum class Orientation {
    COLLINEAR, CLOCKWISE, COUNTERCLOCKWISE
}

fun orientation(a: Point, b: Point, c: Point): Orientation {
    val value = (b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y)
    if (value equalsAccuracy 0.0)
        return Orientation.COLLINEAR
    return if (value > 0) Orientation.CLOCKWISE else Orientation.COUNTERCLOCKWISE
}

/**
 * Возвращает список точек, образующих минимальную выпуклую оболочку
 * (Graham scan)
 */
fun convexHull(points: List<Point>): List<Point> {
    var p0 = points[0]

    // Взятие самой левой точки за начальную
    for (p in points) {
        if (p.y < p0.y || p.y == p0.y && p.x < p0.x) {
            p0 = p
        }
    }

    // Сортировка точек по полярному углу
    val sorted = points.minus(p0).sortedWith { p1: Point, p2: Point ->
        when (orientation(p0, p1, p2)) {
            Orientation.COLLINEAR -> {
                val dif = p0.distance(p2) - p0.distance(p1)
                when {
                    dif > 0.0 -> -1
                    dif == 0.0 -> 0
                    else -> 1
                }
            }

            Orientation.COUNTERCLOCKWISE -> -1
            else -> 1
        }
    }

    val pts = Stack<Point>()
    pts.push(p0)
    pts.push(sorted[0])

    for (p in sorted.slice(1..sorted.lastIndex)) {
        while (pts.size > 1 && orientation(pts[pts.lastIndex - 1], pts.peek(), p) != Orientation.COUNTERCLOCKWISE) {
            pts.pop()
        }
        pts.push(p)
    }

    return pts.toList()
}


fun area(a: Point, b: Point, c: Point) = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)

fun intersect1(a: Double, b: Double, c: Double, d: Double) = max(min(a, b), min(c, d)) <= min(max(a, b), max(c, d))

fun intersect(a: Segment, b: Segment) = intersect1(a.begin.x, a.end.x, b.begin.x, b.end.x)
        && intersect1(a.begin.y, a.end.y, b.begin.y, b.end.y)
        && area(a.begin, a.end, b.begin) * area(a.begin, a.end, b.end) <= 0
        && area(b.begin, b.end, a.begin) * area(b.begin, b.end, a.end) <= 0;


fun List<Point>.nextIdx(i: Int) = (i + 1) % this.size
fun List<Point>.prevIdx(i: Int) = (i - 1 + this.size) % this.size
fun List<Point>.next(i: Int) = this[this.nextIdx(i)]
fun List<Point>.prev(i: Int) = this[this.prevIdx(i)]

infix fun Double.equalsAccuracy(other: Double): Boolean = abs(this - other) <= Math.ulp(max(this, other))


fun getAntipodalPoints(convexHull: List<Point>): List<Pair<Point, Point>> {
    val antipodals = mutableListOf<Pair<Point, Point>>()

    // the most left
    var i = convexHull.indexOf(convexHull.minByOrNull { it.x }!!)
    // the most right
    var j = convexHull.indexOf(convexHull.maxByOrNull { it.x }!!)

    val stop = i

    antipodals.add(Pair(convexHull[i], convexHull[j]))

    do {

        // skip
        while (intersect(
                // Но как-то страшно оставлять такой вызов
                Segment(
                    Point((convexHull.prev(i).x - convexHull[i].x) / convexHull.prev(i).distance(convexHull[i]),
                        (convexHull.prev(i).y - convexHull[i].y) / convexHull.prev(i).distance(convexHull[i])),
                    Point((convexHull.next(i).x - convexHull[i].x) / convexHull.next(i).distance(convexHull[i]),
                        (convexHull.next(i).y - convexHull[i].y) / convexHull.next(i).distance(convexHull[i]))),
                Segment(
                    Point((convexHull.prev(j).x - convexHull[j].x) / convexHull.prev(j).distance(convexHull[j]),
                        (convexHull.prev(j).y - convexHull[j].y) / convexHull.prev(j).distance(convexHull[j])),
                    Point((convexHull.next(j).x - convexHull[j].x) / convexHull.next(j).distance(convexHull[j]),
                        (convexHull.next(j).y - convexHull[j].y) / convexHull.next(j).distance(convexHull[j])))
            ))
                j = convexHull.nextIdx(j)

        // а-ля нормализация отрезков относительно i, j точек
        while (!intersect(
                // Но как-то страшно оставлять такой вызов
                Segment(
                    Point((convexHull.prev(i).x - convexHull[i].x) / convexHull.prev(i).distance(convexHull[i]),
                        (convexHull.prev(i).y - convexHull[i].y) / convexHull.prev(i).distance(convexHull[i])),
                    Point((convexHull.next(i).x - convexHull[i].x) / convexHull.next(i).distance(convexHull[i]),
                        (convexHull.next(i).y - convexHull[i].y) / convexHull.next(i).distance(convexHull[i]))),
                Segment(
                    Point((convexHull.prev(j).x - convexHull[j].x) / convexHull.prev(j).distance(convexHull[j]),
                        (convexHull.prev(j).y - convexHull[j].y) / convexHull.prev(j).distance(convexHull[j])),
                    Point((convexHull.next(j).x - convexHull[j].x) / convexHull.next(j).distance(convexHull[j]),
                        (convexHull.next(j).y - convexHull[j].y) / convexHull.next(j).distance(convexHull[j])))
            )
        ) {
            antipodals.add(Pair(convexHull[i], convexHull[j]))
            j = convexHull.nextIdx(j)

        }
        i = convexHull.nextIdx(i)
        j = convexHull.prevIdx(j)
    } while (i != stop)

    return antipodals
}

//fun getAntipodalPoints(convexHull: List<Point>): List<Pair<Point, Point>> {
//    val antipodals = mutableListOf<Pair<Point, Point>>()
//
//    println("cohul: $convexHull")
//
//    var i = convexHull.prevIdx(convexHull.indexOf(convexHull.minByOrNull { it.x }!!))
//    var j = convexHull.prevIdx(convexHull.indexOf(convexHull.maxByOrNull { it.x }!!))
//
//    println("min: $i, max: $j")
//
//    antipodals.add(Pair(convexHull.next(i), convexHull.next(j)))
//
//    var offsetX = convexHull.next(j + 1).x - convexHull.next(i).x
//    var offsetY = convexHull.next(j + 1).y - convexHull.next(i).y
//
//    while (!intersect(
//            Segment(convexHull[i], convexHull.next(i + 1)),
//            Segment(
//                Point(convexHull.next(j).x - offsetX, convexHull.next(j).y - offsetY),
//                Point(convexHull.next(j + 2).x - offsetX, convexHull.next(j + 2).y - offsetY)
//            )
//        )
//    ) {
//        j = convexHull.nextIdx(j)
//
//        antipodals.add(Pair(convexHull.next(i), convexHull[j]))
//
//        offsetX = convexHull.next(j + 1).x - convexHull.next(i).x
//        offsetY = convexHull.next(j + 1).y - convexHull.next(i).y
//    }
//
//    println("after: $i, $j")
//    antipodals.add(Pair(convexHull.next(i), convexHull.next(j)))
//
//    i = convexHull.nextIdx(i)
//    val stop = i
//
//
//    offsetX = convexHull.next(j).x - convexHull.next(i).x
//    offsetY = convexHull.next(j).y - convexHull.next(i).y
//
//     do {
//        while (!intersect(
//                Segment(convexHull[i], convexHull.next(i + 1)),
//                Segment(
//                    Point(convexHull.next(j - 1).x - offsetX, convexHull.next(j - 1).y - offsetY),
//                    Point(convexHull.next(j + 1).x - offsetX, convexHull.next(j + 1).y - offsetY)
//                )
//            )
//        ) {
//            j = convexHull.nextIdx(j)
//
//            antipodals.add(Pair(convexHull.next(i), convexHull[j]))
//
//            offsetX = convexHull.next(j).x - convexHull.next(i).x
//            offsetY = convexHull.next(j).y - convexHull.next(i).y
//        }
//
//        i = convexHull.nextIdx(i)
//        offsetX = convexHull.next(j).x - convexHull.next(i + 1).x
//        offsetY = convexHull.next(j).y - convexHull.next(i + 1).y
//        antipodals.add(Pair(convexHull.next(i), convexHull[j]))
//    } while (i != stop)
//    return antipodals
//}

// TRASH. DO NOT SEE
//fun getAntipodalPoints(convexHull: List<Point>): List<Pair<Point, Point>> {
//    println("hull: $convexHull")
//
//    val antipodals = mutableListOf<Pair<Point, Point>>()
//    var i = 0
//    var j = 1
//
//    while (angle(
//            Segment(convexHull.next(i), convexHull[i]),
//            Segment(convexHull[j], convexHull.next(j))
//        ) < PI
//    ) {
//        j = convexHull.nextIdx(j)
//    }
//
//    antipodals.add(Pair(convexHull[i], convexHull[j]))
//
//
//    while (j != 1) {
//        val ang = angle(
//            Segment(convexHull.next(i), convexHull[i]),
//            Segment(convexHull[j], convexHull.next(j))
//        )
//
//        when {
//            ang equalsAccuracy PI -> {
//                val a = convexHull.nextIdx(i)
//                val b = convexHull.nextIdx(j)
//                antipodals.add(Pair(convexHull[a], convexHull[j]))
//                antipodals.add(Pair(convexHull[i], convexHull[b]))
//                antipodals.add(Pair(convexHull[a], convexHull[b]))
//
//                i = convexHull.nextIdx(i)
//                j = convexHull.nextIdx(j)
//            }
//            ang < PI -> {
//                antipodals.add(Pair(convexHull.next(i), convexHull[j]))
//                i = convexHull.nextIdx(i)
//            }
//            else -> {
//                antipodals.add(Pair(convexHull[i], convexHull.next(j)))
//                j = convexHull.nextIdx(j)
//            }
//        }
//    }
//    return antipodals
//}

/**
 * Средняя (3 балла)
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {

    if (points.toSet().size < 2)
        throw IllegalArgumentException("Not enough points")

    val convexHull = convexHull(points.toList())

    val antipodals = getAntipodalPoints(convexHull)

    val diameter = antipodals.maxByOrNull { it.first.distance(it.second) }!!

    return Segment(diameter.first, diameter.second)
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

//    val convexHull = convexHull(pts.toList())

    var circle = circleByDiameter(diameter(*pts.toTypedArray()))
    var minCircle = if (circle.containsAll(pts))
        circle
    else
        Circle(Point(0.0, 0.0), Double.POSITIVE_INFINITY)

    for (i in pts.indices) {
        for (j in i + 1..pts.lastIndex) {
            for (k in j + 1..pts.lastIndex) {

                if (!(lineByPoints(pts[i], pts[j]).angle equalsAccuracy lineByPoints(pts[j], pts[k]).angle)) {
                    circle = circleByThreePoints(pts[i], pts[j], pts[k])
                    if (circle.containsAll(pts) && circle.radius < minCircle.radius)
                        minCircle = circle
                }
            }
        }
    }

    return minCircle
}

