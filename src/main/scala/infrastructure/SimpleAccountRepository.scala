package infrastructure

import domain.account.Account
import domain.account.Account.Factory
import domain.{Client, Money}
import infrastructure.AccountRepository.{AccountAmountSetter, AccountGetter}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Minimalist implementation with:
  *   - a private var as account data mean of access
  *   - just one Client to one Account
  *   - no real concurrency or reactive management
  * (instead of any casual web service or database)
  *
  * @param account mutating account data
  */
class SimpleAccountRepository[A <: Account: Factory](private[this] var account: A)
                                                    (implicit ec: ExecutionContext)
  extends AccountGetter with AccountAmountSetter {
  def getAccount(client: Client) = oneClientFutureMade(client, account)

  override def setAccountAmount(client: Client, newAmount: Money) =
    oneClientFutureMade(client, Account.addAccountOperations(account, newAmount))

  private def oneClientFutureMade(client: Client, accountProcess: => Account) = client.id match {
    case id if id == account.client.id => Future(accountProcess)
    case _ => Future.failed(new IllegalArgumentException("Wrong user id"))
  }
}
object SimpleAccountRepository
