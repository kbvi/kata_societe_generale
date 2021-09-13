package domain

sealed abstract class Currency(val symbol: Char) {
  override lazy val toString = symbol.toString
}
object Currency {
  final case object Euro   extends Currency('€')
  final case object Dollar extends Currency('$')
}
