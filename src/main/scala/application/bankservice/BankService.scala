package application.bankservice

import application.i18n.TextualHistoryFormat
import domain.{Client, Money}
import domain.account.Account

import scala.concurrent.Future

sealed trait BankService
object BankService {
  trait DepositAble extends BankService {
    def deposit   (client: Client, amount: Money): Future[Account]
  }
  trait WithdrawalAble extends BankService {
    def withdrawal(client: Client, amount: Money): Future[Account]
  }
  trait HistoryAble extends BankService with TextualHistoryFormat {
    def history(client: Client): Future[String]
  }
}
