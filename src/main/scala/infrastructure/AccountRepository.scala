package infrastructure

import domain.account.Account
import domain.{Client, Money}

import scala.concurrent.Future

sealed trait AccountRepository
object AccountRepository {
  trait AccountGetter extends AccountRepository {
    def getAccount(client: Client): Future[Account]
  }
  trait AccountAmountSetter extends AccountRepository {
    def setAccountAmount(client: Client, amountDiff: Money): Future[Account]
  }
}