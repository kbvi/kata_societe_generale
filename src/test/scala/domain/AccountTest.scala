package domain

import domain.Client.SimpleClient
import domain.account.SimpleAccount.factory
import domain.account.Account
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

// last operation equal balance
// sum of all operations = balance
// sum of n last operation = balance + n * amountDiff + balance of nieme operation

  // balance andaccount should not be updated alone

class AccountTest extends AnyFeatureSpec with GivenWhenThen {
  def dummyClient: Client = SimpleClient(1L)

  Feature("Account life cycle") {
    Scenario("A new account is empty of operation and Money") {
      Given("A brand new account of a client")
      val dummyNewAccount: Account = Account.blank(dummyClient)

      Then("It has no operations")
      assert(dummyNewAccount.statement.isEmpty)

      Then("Neither has it balance")
      assert(0 == dummyNewAccount.balance)
    }
  }

  //When adding a positive amount to it
  // removing several time same amount

  // Given a blank account
  // adding several time same amount
  // removing several time same amount
  // then account number is
  }
