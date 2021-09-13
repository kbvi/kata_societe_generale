package application.i18n

import domain.account.Account.Operation

trait UnitedStateHistoryFormat extends TextualHistoryFormat {
  val operationLabel = "operation n°"
  val amount: String = "amount"
  val balance: String = "balance"
  val date: String = "date"
}
