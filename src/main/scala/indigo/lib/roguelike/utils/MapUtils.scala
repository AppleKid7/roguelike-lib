package indigo.lib.roguelike.utils

import indigo._

import scala.annotation.tailrec

object MapUtils {
  // Convert the x and y coordinates of a point to an index on the grid
  def toListIndex(x: Int, y: Int, width: Int): Int =
    x * width + y

  // Find the positions on the grid corresponding to a given rectangle
  def findRectOnGrid[T](rect: Rectangle, grid: List[T], gridWidth: Int): List[Int] =
    val from = rect.topLeft
    val to = rect.bottomRight
    val gridHeight = grid.length / gridWidth
    val yLimit = Math.min(gridHeight - 1, to.y)
    val xLimit = Math.min(to.x, gridWidth - 1)
    @tailrec
    def rec(y: Int, acc: Set[Point]): Set[Point] =
      if y <= yLimit then
        val xCoords = Math.max(from.x, 0) to xLimit
        rec(y + 1, acc ++ xCoords.map(x => Point(x, y)))
      else acc
    val points = rec(from.y, Set.empty[Point])
    val toVec = grid.toVector
    points.toVector.map(pt => toListIndex(pt.x, pt.y, gridWidth)).filter(idx => toVec.lift(idx).nonEmpty).toList.sorted

  // Checks if a position lies inside rectangle within the grid
  def checkOverlap[T](position: Point, rect: Rectangle, grid: List[T], gridWidth: Int): Boolean =
    val index = toListIndex(position.x, position.y, gridWidth)
    val rectOnGrid = findRectOnGrid(rect, grid, gridWidth)
    rectOnGrid.contains(index)

  // Uses the first two functions to decide is any positions are shared between to rectangles on the grid.
  def checksIntersection[T](rect1: Rectangle, rect2: Rectangle, grid: List[T], gridWidth: Int): List[Int] =
    val rectOnGrid1 = findRectOnGrid(rect1, grid, gridWidth)
    val rectOnGrid2 = findRectOnGrid(rect2, grid, gridWidth)
    rectOnGrid1 intersect rectOnGrid2

  def bresenhamLine(from: Point, to: Point): List[Point] =
    val x0: Int = from.x
    val y0: Int = from.y
    val x1: Int = to.x
    val y1: Int = to.y
    val dx      = Math.abs(x1 - x0)
    val dy      = Math.abs(y1 - y0)
    val sx      = if x0 < x1 then 1 else -1
    val sy      = if y0 < y1 then 1 else -1

    @tailrec
    def rec(x: Int, y: Int, err: Int, acc: List[Point]): List[Point] =
      val next = Point(x, y)
      if (x == x1 && y == y1) || next.distanceTo(to) <= 1 then next :: acc
      else
        var e  = err
        var x2 = x
        var y2 = y

        if err > -dx then
          e -= dy
          x2 += sx

        if e < dy then
          e += dx
          y2 += sy

        rec(x2, y2, e, next :: acc)

    rec(x0, y0, (if dx > dy then dx else -dy) / 2, Nil)
}
