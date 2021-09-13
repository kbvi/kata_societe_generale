package domain.country

import domain.Currency

sealed trait Country {
  val currency: Currency
}
object Country {
  case object France      extends Country {
    val currency: Currency.Euro.type = Currency.Euro
  }
  case object UnitedState extends Country {
    val currency: Currency.Dollar.type = Currency.Dollar
  }

}
