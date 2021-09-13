package application.i18n

import domain.Date
import domain.account.Account.Operation

trait TextualHistoryFormat {
  def currencyFormat: String
  def operationLabel: String
  lazy val operationFieldsSeparator: String = "\t\t"
  lazy val operationHeadersSeparator: String = operationFieldsSeparator
  def amount: String
  def balance: String
  def date: String
  lazy val headers: String = Seq(s"$operationLabel",
                            s"$date                        ",
                            amount,
                            balance).mkString(operationHeadersSeparator)
  lazy val operationsSeparator = "\n"
  lazy val dateFormat: Date => String = _.toString
  lazy val operationFormat = (op: Operation, idx: Int) =>
    Seq(idx,
      dateFormat(op.date),
      s"${op.amount} €",
      s"${op.balance} €").mkString(operationFieldsSeparator)
}
