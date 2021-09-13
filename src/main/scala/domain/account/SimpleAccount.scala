package domain.account

import domain.{Client, Money}
import domain.account.Account.{Factory, Operation}

final case class SimpleAccount private(client: Client, balance: Money, statement: List[Operation]) extends Account
object           SimpleAccount extends Account.Factory[SimpleAccount] {
  private def apply(client: Client, balance: Money, statement: List[Operation]): SimpleAccount =
    new SimpleAccount(client, balance, statement)

  protected[account] def blank(client: Client): SimpleAccount = apply(client, 0, Nil)

  protected[account] def updateAccountByBalanceAndStatement = (s, b, so) => s.copy(balance = b, statement = so.toList)

  implicit val factory: Factory[SimpleAccount] = this
}
