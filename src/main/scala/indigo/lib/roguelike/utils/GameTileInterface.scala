package indigo.lib.roguelike.utils

import indigo.lib.roguelike.terminal.MapTile

trait GameTileInterface {
  def lightMapTile: MapTile
  def darkMapTile: MapTile
  def blocked: Boolean
  def blockSight: Boolean
  def isBlocked: Boolean = blocked
}
