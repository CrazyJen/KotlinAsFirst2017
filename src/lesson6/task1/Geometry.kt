@file:Suppress("UNUSED_PARAMETER")

package lesson6.task1

import lesson1.task1.sqr
import java.lang.Math.*

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = Math.sqrt(sqr(x - other.x) + sqr(y - other.y))
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
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
        return Math.sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
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
     * Простая
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double {
        val result = this.center.distance(other.center) - (this.radius + other.radius)
        return if (result < 0.0) 0.0
        else result
    }

    /**
     * Тривиальная
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point): Boolean = this.center.distance(p) - this.radius <= 1e-15
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
 * Средняя
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    val input = points.toList()
    if (input.size < 2) throw IllegalArgumentException("Недостаточно точек")
    var result = Segment(input[0], input[1])
    for (point in input) {
        for (i in input.indexOf(point) + 1 until input.size) {
            if (point.distance(input[i]) > result.begin.distance(result.end))
                result = Segment(point, input[i])
        }
    }
    return result
}

/**
 * Простая
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle {
    val center = Point((diameter.begin.x + diameter.end.x) / 2.0,
            (diameter.begin.y + diameter.end.y) / 2.0)
    val radius = diameter.begin.distance(diameter.end) / 2.0
    return Circle(center, radius)
}

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        assert(angle >= 0 && angle < Math.PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(point.y * Math.cos(angle) - point.x * Math.sin(angle), angle)

    /**
     * Средняя
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        val yCoord = when {
            this.angle <= 1e-15 -> this.b
            other.angle <= 1e-15 -> other.b
            abs(this.angle - PI / 2) <= 1e-15 -> (this.b - (other.b * sin(this.angle)) / sin(other.angle)) /
                    (cos(this.angle) - sin(this.angle) / tan(other.angle))
            else -> (other.b - (this.b * sin(other.angle)) / sin(this.angle)) /
                    (cos(other.angle) - sin(other.angle) / tan(this.angle))
        }
        val xCoord = if (this.angle < 1e-15) (yCoord * cos(other.angle) - other.b) / sin(other.angle)
        else (yCoord * cos(this.angle) - this.b) / sin(this.angle)
        return Point(xCoord, yCoord)
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${Math.cos(angle)} * y = ${Math.sin(angle)} * x + $b)"
}

/**
 * Средняя
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment): Line {
    val endCoordY = maxOf(s.begin.y, s.end.y)
    val segment = if (abs(endCoordY - s.begin.y) <= 1e-15) Segment(s.end, s.begin)
    else s
    val angle = acos((segment.end.x - segment.begin.x) / (s.begin.distance(s.end))) % PI
    return Line(segment.begin, angle)
}

/**
 * Средняя
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line = lineBySegment(Segment(a, b))

/**
 * Сложная
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line {
    val segmentAngle = lineByPoints(a, b).angle
    val angle = when {
        (abs(a.y - b.y) <= 1e-15) -> PI / 2
        (abs(a.x - b.x) <= 1e-15) -> 0.0
        segmentAngle > PI / 2 -> segmentAngle - PI / 2
        else -> segmentAngle + PI / 2
    }
    return Line(Point((a.x + b.x) / 2, (a.y + b.y) / 2), angle)
}

/**
 * Средняя
 *
 * Задан список из n окружностей на плоскости. Найти пару наименее удалённых из них.
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> {
    val input = circles.toList()
    if (input.size < 2) throw IllegalArgumentException()
    var result = Pair(input[0], input[1])
    for (circle in input)
        for (i in input.indexOf(circle) + 1 until input.size) {
            if (circle.distance(input[i]) < result.first.distance(result.second))
                result = Pair(circle, input[i])
        }
    return result
}

/**
 * Сложная
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle {
    val center = bisectorByPoints(a, b).crossPoint(bisectorByPoints(b, c))
    val radius = center.distance(a)
    return Circle(center, radius)
}

/**
 * Очень сложная
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
    val input = points.toSet().toTypedArray() //Для удаления повторяющихся точек
    val diameter: Segment
    val result: Circle
    when (input.size) {
        0 -> throw IllegalArgumentException()
        1 -> return Circle(input[0], 0.0)
        2 -> return circleByDiameter(Segment(input[0], input[1]))
        3 -> return circleByThreePoints(input[0], input[1], input[2])
        else -> {
            diameter = diameter(*input)
            result = circleByDiameter(diameter)
        }
    }
    var maxDistance = 0.0
    var maxRemotePoint = result.center

    for (point in input) {
        if (!result.contains(point)) {
            val currentDistance = point.distance(result.center)
            if (currentDistance > maxDistance) {
                maxDistance = currentDistance
                maxRemotePoint = point
            }
        }
    }

    return if (maxDistance == 0.0) result
    else circleByThreePoints(diameter.begin, diameter.end, maxRemotePoint)
}