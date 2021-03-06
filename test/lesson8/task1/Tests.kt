package lesson8.task1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.lang.Math.ulp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.system.exitProcess

class Tests {
    @Test
    @Tag("Example")
    fun pointDistance() {
        assertEquals(0.0, Point(0.0, 0.0).distance(Point(0.0, 0.0)), 1e-5)
        assertEquals(5.0, Point(3.0, 0.0).distance(Point(0.0, 4.0)), 1e-5)
        assertEquals(50.0, Point(0.0, -30.0).distance(Point(-40.0, 0.0)), 1e-5)
    }

    @Test
    @Tag("Example")
    fun halfPerimeter() {
        assertEquals(6.0, Triangle(Point(0.0, 0.0), Point(0.0, 3.0), Point(4.0, 0.0)).halfPerimeter(), 1e-5)
        assertEquals(2.0, Triangle(Point(0.0, 0.0), Point(0.0, 1.0), Point(0.0, 2.0)).halfPerimeter(), 1e-5)
    }

    @Test
    @Tag("Example")
    fun triangleArea() {
        assertEquals(6.0, Triangle(Point(0.0, 0.0), Point(0.0, 3.0), Point(4.0, 0.0)).area(), 1e-5)
        assertEquals(0.0, Triangle(Point(0.0, 0.0), Point(0.0, 1.0), Point(0.0, 2.0)).area(), 1e-5)
    }

    @Test
    @Tag("Example")
    fun triangleContains() {
        assertTrue(Triangle(Point(0.0, 0.0), Point(0.0, 3.0), Point(4.0, 0.0)).contains(Point(1.5, 1.5)))
        assertFalse(Triangle(Point(0.0, 0.0), Point(0.0, 3.0), Point(4.0, 0.0)).contains(Point(2.5, 2.5)))
    }

    @Test
    @Tag("Example")
    fun segmentEquals() {
        val first = Segment(Point(1.0, 2.0), Point(3.0, 4.0))
        val second = Segment(Point(1.0, 2.0), Point(3.0, 4.0))
        val third = Segment(Point(3.0, 4.0), Point(1.0, 2.0))
        assertEquals(first, second)
        assertEquals(second, third)
        assertEquals(third, first)
    }

    private fun approxEquals(expected: Line, actual: Line, delta: Double): Boolean =
        abs(expected.angle - actual.angle) <= delta && abs(expected.b - actual.b) <= delta

    private fun assertApproxEquals(expected: Line, actual: Line, delta: Double = ulp(10.0)) {
        assertTrue(approxEquals(expected, actual, delta))
    }

    private fun assertApproxNotEquals(expected: Line, actual: Line, delta: Double = ulp(10.0)) {
        assertFalse(approxEquals(expected, actual, delta))
    }

    private fun approxEquals(expected: Point, actual: Point, delta: Double): Boolean =
        expected.distance(actual) <= delta

    private fun assertApproxEquals(expected: Point, actual: Point, delta: Double = ulp(10.0)) {
        assertTrue(approxEquals(expected, actual, delta))
    }

    // fixed invalid test. Don't ban me please
    private fun approxEquals(expected: Segment, actual: Segment, delta: Double): Boolean =
        expected.begin.distance(actual.begin) <= delta && expected.end.distance(actual.end) <= delta ||
                expected.begin.distance(actual.end) <= delta && expected.end.distance(actual.begin) <= delta

    private fun assertApproxEquals(expected: Segment, actual: Segment, delta: Double = ulp(10.0)) {
        assertTrue(approxEquals(expected, actual, delta))
    }

    private fun approxEquals(expected: Circle, actual: Circle, delta: Double): Boolean =
        expected.center.distance(actual.center) <= delta && abs(expected.radius - actual.radius) <= delta

    private fun assertApproxEquals(expected: Circle, actual: Circle, delta: Double = ulp(10.0)) {
        assertTrue(approxEquals(expected, actual, delta))
    }

    @Test
    @Tag("Example")
    fun lineEquals() {
        run {
            val first = Line(Point(0.0, 0.0), 0.0)
            val second = Line(Point(3.0, 0.0), 0.0)
            val third = Line(Point(-5.0, 0.0), 0.0)
            val fourth = Line(Point(3.0, 1.0), 0.0)
            assertApproxEquals(first, second)
            assertApproxEquals(second, third)
            assertApproxEquals(third, first)
            assertApproxNotEquals(fourth, first)
        }
        run {
            val first = Line(Point(0.0, 0.0), PI / 2)
            val second = Line(Point(0.0, 3.0), PI / 2)
            val third = Line(Point(0.0, -5.0), PI / 2)
            val fourth = Line(Point(1.0, 3.0), PI / 2)
            assertApproxEquals(first, second)
            assertApproxEquals(second, third)
            assertApproxEquals(third, first)
            assertApproxNotEquals(fourth, first)
        }
        run {
            val first = Line(Point(0.0, 0.0), PI / 4)
            val second = Line(Point(3.0, 3.0), PI / 4)
            val third = Line(Point(-5.0, -5.0), PI / 4)
            val fourth = Line(Point(3.00001, 3.0), PI / 4)
            assertApproxEquals(first, second)
            assertApproxEquals(second, third)
            assertApproxEquals(third, first)
            assertApproxNotEquals(fourth, first)
        }
    }

    @Test
    @Tag("Example")
    fun triangleEquals() {
        val first = Triangle(Point(0.0, 0.0), Point(3.0, 0.0), Point(0.0, 4.0))
        val second = Triangle(Point(0.0, 0.0), Point(0.0, 4.0), Point(3.0, 0.0))
        val third = Triangle(Point(0.0, 4.0), Point(0.0, 0.0), Point(3.0, 0.0))
        val fourth = Triangle(Point(0.0, 4.0), Point(0.0, 3.0), Point(3.0, 0.0))
        assertEquals(first, second)
        assertEquals(second, third)
        assertEquals(third, first)
        assertNotEquals(fourth, first)
    }

    @Test
    @Tag("2")
    fun circleDistance() {
        assertEquals(0.0, Circle(Point(0.0, 0.0), 1.0).distance(Circle(Point(1.0, 0.0), 1.0)), 1e-5)
        assertEquals(0.0, Circle(Point(0.0, 0.0), 1.0).distance(Circle(Point(0.0, 2.0), 1.0)), 1e-5)
        assertEquals(1.0, Circle(Point(0.0, 0.0), 1.0).distance(Circle(Point(-4.0, 0.0), 2.0)), 1e-5)
        assertEquals(2.0 * sqrt(2.0) - 2.0, Circle(Point(0.0, 0.0), 1.0).distance(Circle(Point(2.0, 2.0), 1.0)), 1e-5)
    }

    @Test
    @Tag("1")
    fun circleContains() {
        val center = Point(1.0, 2.0)
        assertTrue(Circle(center, 1.0).contains(center))
        assertFalse(Circle(center, 2.0).contains(Point(0.0, 0.0)))
        assertTrue(Circle(Point(0.0, 3.0), 5.01).contains(Point(-4.0, 0.0)))
    }

    @Test
    fun convexHull() {
        val pts1 = listOf(
            Point(5.0, 0.0),
            Point(3.0, 1.0),
            Point(0.0, 5.0),
            Point(-5.0, 0.0),
            Point(0.0, 0.0),
        )
        assertEquals(listOf(pts1[3], pts1[0], pts1[2]), convexHull(pts1))

        val pts2 = listOf(
            Point(5.0, 0.0),
            Point(3.0, 1.0),
            Point(0.0, 5.0),
            Point(-5.0, 0.0),
            Point(0.0, 0.0),
            Point(6.0, 0.0),
        )
        assertEquals(listOf(pts2[3], pts2[5], pts2[2]), convexHull(pts2))

        val pts3 = listOf(
            Point(5.0, 0.0),
            Point(3.0, 1.0),
            Point(0.0, 5.0),
            Point(-5.0, 0.0),
            Point(0.0, 0.0),
            Point(6.0, -1.0),
        )
        assertEquals(listOf(pts3[5], pts3[2], pts3[3]), convexHull(pts3))
    }

    @Test
    fun orientation() {
        assertTrue(
            Orientation.COUNTERCLOCKWISE == orientation(
                Point(-5.0, 0.0),
                Point(6.0, -1.0),
                Point(3.0, 1.0)
            )
        )

        assertTrue(
            Orientation.CLOCKWISE == orientation(
                Point(-5.0, 0.0),
                Point(3.0, 1.0),
                Point(6.0, -1.0)
            )
        )

        assertTrue(
            Orientation.COLLINEAR == orientation(
                Point(-5.0, 0.0),
                Point(3.0, 1.0),
                Point(11.0, 2.0)
            )
        )

        assertTrue(
            Orientation.CLOCKWISE == orientation(
                Point(0.0, 0.0),
                Point(-3.0, 1.0),
                Point(-3.0, 2.0)
            )
        )

        assertTrue(
            Orientation.COUNTERCLOCKWISE == orientation(
                Point(0.0, 0.0),
                Point(-3.0, 1.0),
                Point(-4.0, 1.0)
            )
        )
    }


    @Test
    @Tag("3")
    fun diameter() {

        assertApproxEquals(
            Segment(Point(0.8701945142793052, -632.0), Point(-632.0, 0.997298287394081)),
            diameter(
                Point(-632.0, -2.220446049250313e-16),
                Point(-2.220446049250313e-16, -632.0),
                Point(0.8701945142793052, -632.0),  // this
                Point(0.865507779340732, 0.9779855755331297),
                Point(-632.0, 0.997298287394081),  // this
                Point(0.9739555109765072, 0.6359091955606937),
            )
        )


        assertApproxEquals(
            Segment(Point(0.0, -632.0), Point(-632.0, 0.23297111436084172)),
            diameter(
                Point(-632.0, -5e-324),
                Point(0.0, -632.0),
                Point(0.7137124476307832, 0.8518978203996326),
                Point(0.5545127347585456, -2.220446049250313e-16),
                Point(-632.0, -5e-324),
                Point(0.2982326795419925, -5e-324),
                Point(0.8565100884297777, 0.06200710893703443),
                Point(0.1921985181521363, -2.220446049250313e-16),
                Point(5e-324, 0.8585808556622184),
                Point(-2.220446049250313e-16, 0.0),
                Point(-632.0, 0.23297111436084172),
                Point(2.220446049250313e-16, -2.220446049250313e-16),
                Point(2.220446049250313e-16, 0.5497033537886443),
                Point(0.5776487387646324, 0.5822836266748097)
            )
        )

        assertApproxEquals(
            Segment(Point(0.06863429075739391, 0.9157408980960688), Point(-632.0, -632.0)), diameter(
                Point(0.0508300675391814, 0.9280277014800296),
                Point(0.514955733666317, 0.4306122987994334),
                Point(0.06721598633694625, 5e-324),
                Point(0.5762430625520272, -632.0),
                Point(-632.0, -632.0),
                Point(2.220446049250313e-16, 0.0),
                Point(2.220446049250313e-16, 0.06062887102925607),
                Point(0.06754957644406823, 0.013982560840221647),
                Point(0.07830507312075397, 0.8614318293286136),
                Point(-632.0, 0.0),
                Point(0.06863429075739391, 0.9157408980960688),
                Point(5e-324, 0.16739826154762183)
            )
        )

        val p1 = Point(0.0, 0.0)
        val p2 = Point(1.0, 4.0)
        val p3 = Point(-2.0, 2.0)
        val p4 = Point(3.0, -1.0)
        val p5 = Point(-3.0, -2.0)
        val p6 = Point(0.0, 5.0)
        assertApproxEquals(Segment(p5, p6), diameter(p1, p2, p3, p4, p5, p6))
        assertApproxEquals(Segment(p4, p6), diameter(p1, p2, p3, p4, p6))
        assertApproxEquals(Segment(p3, p4), diameter(p1, p2, p3, p4))
        assertApproxEquals(Segment(p2, p4), diameter(p1, p2, p4))
        assertApproxEquals(Segment(p1, p4), diameter(p1, p4))

        val p7 = Point(5e-324, -632.0)
        val p8 = Point(-5e-324, 0.652028744668454)
        val p9 = Point(-632.0, 5e-324)
        val p10 = Point(-632.0, 0.5822364757216367)
        val p11 = Point(-5e-324, -5e-324)
        val p12 = Point(-632.0, 0.35709954279466427)
        val p13 = Point(0.502180286987219, -632.0)

        assertApproxEquals(Segment(p10, p13), diameter(p7, p8, p9, p10, p11, p12, p13))

        val points = listOf(
            Point(5e-324, 0.0), Point(0.9449968331851336, 0.0),
            Point(0.26732562330170384, 0.35586165186524954), Point(-632.0, 0.3793568356893322),
            Point(-5e-324, 0.6972569151290643), Point(0.2609077619468463, 0.9889383359419361),
            Point(-2.220446049250313e-16, 0.12291931407993906), Point(0.7376893629343998, 0.011194103329788874),
            Point(2.220446049250313e-16, 0.2382458634034157), Point(0.618973723410756, -5e-324),
            Point(2.220446049250313e-16, -2.220446049250313e-16), Point(0.0, 0.014398583540163101),
            Point(0.0, 0.41570555328132275), Point(0.73743131743315, 0.5882693360110391),
            Point(0.12435589605192754, 5e-324)
        )
        assertApproxEquals(Segment(points[1], points[3]), diameter(*points.toTypedArray()))

        val pts = listOf(
            Point(0.0, -632.0),
            Point(0.19414965341753987, 2.220446049250313e-16),
            Point(2.220446049250313e-16, 2.220446049250313e-16),
            Point(0.19009099083558578, 0.3393260073959129),
            Point(0.5891467152659507, 0.0),
            Point(2.220446049250313e-16, 0.0),
            Point(0.8072002142329636, 0.4901115604439399),
            Point(2.220446049250313e-16, -5e-324),
            Point(-5e-324, 0.8288682617557516),
            Point(-5e-324, 0.4270604849310383),
            Point(0.33904032338632495, -2.220446049250313e-16),
            Point(0.28413032412486827, -5e-324),
            Point(-5e-324, 0.23139601829341128),
            Point(0.17860916977279306, 2.220446049250313e-16)
        )

        assertApproxEquals(Segment(pts[0], pts[8]), diameter(*pts.toTypedArray()))
    }

    @Test
    @Tag("2")
    fun circleByDiameter() {
        assertApproxEquals(Circle(Point(0.0, 1.0), 1.0), circleByDiameter(Segment(Point(0.0, 0.0), Point(0.0, 2.0))))
        assertApproxEquals(Circle(Point(2.0, 1.5), 2.5), circleByDiameter(Segment(Point(4.0, 0.0), Point(0.0, 3.0))))
    }

    @Test
    @Tag("3")
    fun crossPoint() {
        assertApproxEquals(
            Point(2.0, 3.0),
            Line(Point(2.0, 0.0), PI / 2).crossPoint(
                Line(Point(0.0, 3.0), 0.0)
            ),
            1e-5
        )
        assertApproxEquals(
            Point(2.0, 2.0),
            Line(Point(0.0, 0.0), PI / 4).crossPoint(
                Line(Point(0.0, 4.0), 3 * PI / 4)
            ),
            1e-5
        )

        val p = Point(1.0, 3.0)

        assertApproxEquals(
            p,
            Line(p, 1.0).crossPoint(Line(p, 2.0)),
            1e-5
        )
    }

    @Test
    @Tag("3")
    fun lineBySegment() {
        assertApproxEquals(Line(Point(0.0, 0.0), 0.0), lineBySegment(Segment(Point(0.0, 0.0), Point(7.0, 0.0))))
        assertApproxEquals(Line(Point(0.0, 0.0), PI / 2), lineBySegment(Segment(Point(0.0, 0.0), Point(0.0, 8.0))))
        assertApproxEquals(Line(Point(1.0, 1.0), PI / 4), lineBySegment(Segment(Point(1.0, 1.0), Point(3.0, 3.0))))
    }

    // There is typo in the method's name:  lineByPoints
    @Test
    @Tag("3")
    fun lineByPoint() {
        assertApproxEquals(Line(Point(0.0, 0.0), PI / 2), lineByPoints(Point(0.0, 0.0), Point(0.0, 2.0)))
        assertApproxEquals(Line(Point(1.0, 1.0), PI / 4), lineByPoints(Point(1.0, 1.0), Point(3.0, 3.0)))
    }

    @Test
    @Tag("5")
    fun bisectorByPoints() {
        assertApproxEquals(Line(Point(2.0, 0.0), PI / 2), bisectorByPoints(Point(0.0, 0.0), Point(4.0, 0.0)))
        assertApproxEquals(Line(Point(1.0, 2.0), 0.0), bisectorByPoints(Point(1.0, 5.0), Point(1.0, -1.0)))
    }

    @Test
    @Tag("3")
    fun findNearestCirclePair() {
        val c1 = Circle(Point(0.0, 0.0), 1.0)
        val c2 = Circle(Point(3.0, 0.0), 5.0)
        val c3 = Circle(Point(-5.0, 0.0), 2.0)
        val c4 = Circle(Point(0.0, 7.0), 3.0)
        val c5 = Circle(Point(0.0, -6.0), 4.0)
        assertEquals(Pair(c1, c5), findNearestCirclePair(c1, c3, c4, c5))
        assertEquals(Pair(c2, c4), findNearestCirclePair(c2, c4, c5))
        assertEquals(Pair(c1, c2), findNearestCirclePair(c1, c2, c4, c5))
    }

    @Test
    @Tag("5")
    fun circleByThreePoints() {
        val actual = circleByThreePoints(Point(5.0, 0.0), Point(3.0, 4.0), Point(0.0, -5.0))
        val expected = Circle(Point(0.0, 0.0), 5.0)
        assertApproxEquals(expected, actual, 1e-5)

        assertApproxEquals(
            Circle(Point(5.0, 5.0), 2.0),
            circleByThreePoints(Point(5.0, 7.0), Point(3.0, 5.0), Point(7.0, 5.0))
        )
    }

    @Test
    @Tag("10")
    fun minContainingCircle() {
        val p1 = Point(0.0, 0.0)
        val p2 = Point(1.0, 4.0)
        val p3 = Point(-2.0, 2.0)
        val p4 = Point(3.0, -1.0)
        val p5 = Point(-3.0, -2.0)
        val p6 = Point(0.0, 5.0)
        val result = minContainingCircle(p1, p2, p3, p4, p5, p6)
        assertEquals(4.0, result.radius, 0.02)
        for (p in listOf(p1, p2, p3, p4, p5, p6)) {
            assertTrue(result.contains(p))
        }
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> { lesson8.task1.minContainingCircle() }
        assertApproxEquals(Circle(p1, 0.0), minContainingCircle(p1))
    }
}
