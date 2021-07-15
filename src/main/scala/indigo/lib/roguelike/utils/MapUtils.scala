package indigo.lib.roguelike.utils

import indigo._
import indigoextras.geometry.BoundingBox
import indigoextras.trees.QuadTree
import indigoextras.trees.QuadTree.{QuadBranch, QuadEmpty, QuadLeaf}

import scala.annotation.tailrec

object MapUtils {
  def calculateFOV[T <: GameTileInterface](radius: Int, center: Point, tileMap: QuadTree[T]): List[Point] =
    val bounds: Rectangle =
      Rectangle(
        (center - radius).max(0),
        (Size(center.x, center.y) + radius).max(1)
      )
    val tiles =
      searchByBounds(tileMap, bounds).filter(pt => center.distanceTo(pt) <= radius)

    @tailrec
    def visibleTiles(remaining: List[Point], acc: List[Point]): List[Point] =
      remaining match
        case Nil => acc
        case pt :: pts =>
          val lineOfSight = bresenhamLine(pt, center)
          if lineOfSight.forall(tiles.contains) then
            visibleTiles(
              pts,
              pt :: acc
            )
          else visibleTiles(pts, acc)
    visibleTiles(tiles, Nil)

  def searchByBounds[T <: GameTileInterface](quadTree: QuadTree[T], bounds: Rectangle): List[Point] =
    val boundingBox: BoundingBox = BoundingBox.fromRectangle(bounds)

    @tailrec
    def rec(remaining: List[QuadTree[T]], acc: List[Point]): List[Point] =
      remaining match
        case Nil => acc
        case x :: xs =>
          x match
            case QuadBranch(bounds, a, b, c, d) if boundingBox.overlaps(bounds) =>
              rec(a :: b :: c :: d :: xs, acc)
            case QuadLeaf(_, exactPosition, value) if boundingBox.contains(exactPosition) =>
              rec(xs, exactPosition.toPoint :: acc)
            case _ => rec(xs, acc)
    rec(List(quadTree), Nil)

  def bresenhamLine(from: Point, to: Point): List[Point] =
    val x0: Int = from.x
    val y0: Int = from.y
    val x1: Int = to.x
    val y1: Int = to.y
    val dx = Math.abs(x1 - x0)
    val dy = Math.abs(y1 - y0)
    val sx = if x0 < x1 then 1 else -1
    val sy = if y0 < y1 then 1 else -1

    @tailrec
    def rec(x: Int, y: Int, err: Int, acc: List[Point]): List[Point] =
      val next = Point(x, y)
      if (x == x1 && y == y1) || next.distanceTo(to) <= 1 then next :: acc
      else
        var e = err
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
