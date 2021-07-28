package indigo.lib.roguelike.utils

import indigo._
import indigo.lib.roguelike.DfTiles
import indigo.lib.roguelike.terminal.MapTile

class UtilsTests extends munit.FunSuite {
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
}
