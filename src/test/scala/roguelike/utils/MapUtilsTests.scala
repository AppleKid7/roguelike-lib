package indigo.lib.roguelike.utils

import indigo._
import indigo.lib.roguelike.DfTiles
import indigo.lib.roguelike.terminal.MapTile

class MapUtilsTests extends munit.FunSuite {
  test("should be able to get the correct indices from given Points") {
    val gridWidth = 4
    val pts = List(Point(0, 0), Point(1, 2), Point(3, 3))
    val expected = List(0, 6, 15)
    val actual = pts.map(pt => MapUtils.toListIndex(pt.x, pt.y, gridWidth))

    assertEquals(expected, actual)
  }

  test("should be able to get correct positions on grid from a given Rectangle within bounds, starting at the upper left corner of the grid"){
    val grid = List.fill(16)("_")
    val width = 4
    val rect = Rectangle.fromTwoPoints(Point(0, 0), Point(1, 1))
    val expected = List(0, 1, 4, 5)
    val actual = MapUtils.findRectOnGrid(rect, grid, width)
    
    assertEquals(expected, actual)
  }

  test("should be able to get correct positions on grid from a given Rectangle within bounds, more towards the center of the grid"){
    val grid = List.fill(16)("_")
    val width = 4
    val rect = Rectangle.fromTwoPoints(Point(1, 1), Point(2, 2))
    val expected = List(5, 6, 9, 10)
    val actual = MapUtils.findRectOnGrid(rect, grid, width)
    
    assertEquals(expected, actual)
  }

  test("should be able to get correct positions on grid from a given Rectangle not entirely within bounds"){
    val grid = List.fill(16)("_")
    val width = 4
    val rect = Rectangle.fromTwoPoints(Point(1, 2), Point(2, 4))
    val expected = List(6, 7, 10, 11)
    val actual = MapUtils.findRectOnGrid(rect, grid, width) 
    
    assertEquals(expected, actual)
  }

  test("should return false if point is not inside a Rectangle within the grid"){
    val grid = List.fill(16)("_")
    val width = 4
    val rect = Rectangle.fromTwoPoints(Point(1, 2), Point(2, 4))
    val expected = false
    val actual = MapUtils.checkOverlap(Point(0, 0), rect, grid, width)

    assertEquals(expected, actual)
  }

  test("should return true if point is inside a Rectangle within the grid"){
    val grid = List.fill(16)("_")
    val width = 4
    val rect = Rectangle.fromTwoPoints(Point(0, 0), Point(1, 1))
    val expected = true
    val actual = MapUtils.checkOverlap(Point(0, 0), rect, grid, width)

    assertEquals(expected, actual)
  }

  test("should return false if point is not even inside the grid"){
    val grid = List.fill(16)("_")
    val width = 4
    val rect = Rectangle.fromTwoPoints(Point(0, 0), Point(2, 2))
    val expected = false
    val actual = MapUtils.checkOverlap(Point(4, 1), rect, grid, width)

    assertEquals(expected, actual)
  }

  test("should return the two points where the two rects intersect"){
    val grid = List.fill(16)("_")
    val width = 4
    val rect1 = Rectangle.fromTwoPoints(Point(0, 0), Point(2, 1))
    val rect2 = Rectangle.fromTwoPoints(Point(1, 1), Point(2, 2))
    val expected = List(5, 9)
    val actual = MapUtils.checksIntersection(rect1, rect2, grid, width)

    assertEquals(expected, actual)
  }

  test("should return the one point where the two rects intersect"){
    val grid = List.fill(16)("_")
    val width = 4
    val rect1 = Rectangle.fromTwoPoints(Point(0, 0), Point(1, 1))
    val rect2 = Rectangle.fromTwoPoints(Point(1, 1), Point(2, 2))
    val expected = List(5)
    val actual = MapUtils.checksIntersection(rect1, rect2, grid, width)

    assertEquals(expected, actual)
  }

  test("should return an empty list when the two rects do not intersect"){
    val grid = List.fill(16)("_")
    val width = 4
    val rect1 = Rectangle.fromTwoPoints(Point(0, 0), Point(2, 1))
    val rect2 = Rectangle.fromTwoPoints(Point(3, 0), Point(4, 1))
    val expected = List.empty[Int]
    val actual = MapUtils.checksIntersection(rect1, rect2, grid, width)

    assertEquals(expected, actual)
  }

  test("bresenhams line - 1x1") {
    val actual =
      MapUtils.bresenhamLine(Point(0, 0), Point(1, 1))

    val expected =
      List(Point(1, 1), Point(0, 0))

    assertEquals(actual, expected)
  }

  test("bresenhams line - vertical") {
    val actual =
      MapUtils.bresenhamLine(Point(10, 13), Point(10, 21))

    val expected =
      List(
        Point(10, 21),
        Point(10, 20),
        Point(10, 19),
        Point(10, 18),
        Point(10, 17),
        Point(10, 16),
        Point(10, 15),
        Point(10, 14),
        Point(10, 13)
      )

    assertEquals(actual, expected)
  }

  test("bresenhams line - horizontal") {
    val actual =
      MapUtils.bresenhamLine(Point(7, 13), Point(12, 13))

    val expected =
      List(
        Point(12, 13),
        Point(11, 13),
        Point(10, 13),
        Point(9, 13),
        Point(8, 13),
        Point(7, 13)
      )

    assertEquals(actual, expected)
  }

  test("bresenhams line - diagonal") {
    val actual =
      MapUtils.bresenhamLine(Point(0, 1), Point(6, 4))

    val expected =
      List(
        Point(6, 4),
        Point(5, 4),
        Point(4, 3),
        Point(3, 3),
        Point(2, 2),
        Point(1, 2),
        Point(0, 1)
      )

    assertEquals(actual, expected)
  }

  test("bresenhams line - same") {
    val actual =
      MapUtils.bresenhamLine(Point(0, 1), Point(0, 1))

    val expected =
      List(
        Point(0, 1)
      )

    assertEquals(actual, expected)
  }

  test("bresenhams line - tricky - misses target") {
    val actual =
      MapUtils.bresenhamLine(Point(8, 3), Point(5, 5))

    val expected =
      List(
        Point(8, 3),
        Point(7, 4),
        Point(6, 5)
      ).reverse

    assertEquals(actual, expected)
  }
}
