import java.util.Calendar

package object domain {
  type Money = BigDecimal
  implicit val nm: Numeric[Money] = implicitly[Numeric[BigDecimal]]
  type Date = java.util.Date
  type ClientId = Long
  def now: Date = Calendar.getInstance().getTime
}
