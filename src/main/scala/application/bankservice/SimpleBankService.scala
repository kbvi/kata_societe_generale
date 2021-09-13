package application.bankservice

import application.bankservice.BankService._
import domain.country.Country
import application.i18n.TextualHistoryFormat
import domain.account.Account
import domain.{Client, Currency, Money}
import infrastructure.AccountRepository.{AccountAmountSetter, AccountGetter}

import scala.concurrent.{ExecutionContext, Future}

/**
  * US1, US2 and US3 specs straightforward implementation
  */
abstract class SimpleBankService(val accountRepository: AccountGetter with AccountAmountSetter)
                                (implicit val ec: ExecutionContext)
    extends DepositAble with WithdrawalAble with HistoryAble {
  override def deposit   (client: Client, amount: Money): Future[Account] =
    setAccountByAmount(client, amount, identity)

  override def withdrawal(client: Client, amount: Money): Future[Account] =
    setAccountByAmount(client, amount, -_      )

  private def setAccountByAmount(client: Client, amount: Money, signing: Money => Money): Future[Account] =
    accountRepository.setAccountAmount(client, signing(amount))

  override def history(client: Client): Future[String] =
    accountRepository.getAccount(client).map(printedStatement)

  def printedStatement(account: Account) = {
    headers + "\n" + account.statement.zipWithIndex.map { case(op, idx) => op -> (account.statement.size - idx) }
      .map(operationFormat.tupled).mkString(operationsSeparator) + "\n"
  }
  def country: Country
  lazy val currency: Currency = country.currency
  override lazy val currencyFormat = s"$currency"
}
