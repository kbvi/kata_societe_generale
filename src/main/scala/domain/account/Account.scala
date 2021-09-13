package domain.account

import domain.{Client, Date, Money, now}

abstract class Account private[account]() {
  def balance: Money
  def statement: Iterable[Account.Operation]
  def client: Client
}
object         Account {
  def blank[Specific <: Account](client: Client)(implicit accountFactory: Factory[Specific]): Specific =
    accountFactory.blank(client)

  final case class Operation private[Account](date: Date, amount: Money, balance: Money)
  object           Operation {
    /** create an operation of an account by some input amount */
    def apply(account: Account, amountDiff: Money): Operation = Operation(now, amountDiff, account.balance + amountDiff)
  }

  /** create a account with added operations on its statement */
  def addAccountOperations[Specific <: Account](accountToOperateOn: Specific, operationsAmounts: Money*)
                                               (implicit accountFactory: Factory[Specific]): Specific = {
    operationsAmounts.foldLeft(accountToOperateOn) { case (accnt, opAmnt) =>
      accountFactory.updateAccountByBalanceAndStatement(accnt,
        accnt.balance + opAmnt,
        Iterable(Operation(accnt, opAmnt)) ++ accnt.statement)
    }
  }

  /** Access-limited means to build an account in order to encapsulate most of its logic without misuse */
  trait Factory[SubAccount <: Account] {
    /** empty and brand new account */
    protected[account] def blank(client: Client): SubAccount
    /** this update should be on both fields balance and statement since they are logically dependent */
    protected[account] def updateAccountByBalanceAndStatement: (SubAccount, Money, Iterable[Operation]) => SubAccount
  }
}