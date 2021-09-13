package application

import application.bankservice.BankService.{DepositAble, HistoryAble, WithdrawalAble}
import application.bankservice.BankService
import domain.account.Account
import domain.{Client, ClientId, Money}
import domain.account.Account.Factory
import infrastructure.AccountRepository
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AsyncFeatureSpec

trait BankServiceSpec[BSImplInput <: BankServiceSpec.USSpec,
                       AccntInput <: Account,
                   AccntRepoInput <: AccountRepository,
                      ClientInput <: Client] { self: AsyncFeatureSpec with GivenWhenThen =>
  implicit def accountFactoryInput: domain.account.Account.Factory[AccntInput]
  def bSImplBuilderInput: (ClientInput, AccntInput, AccntRepoInput) => Fixture.BSImpl
  def clientBuilderInput: ClientId => ClientInput
  def accntRepoBuilderInput: ClientInput => AccntInput => AccntRepoInput

  object Fixture {

    /** Generic spec */
    type US[BSI >: BSImplInput] = Prerequisites[AccntInput, AccntRepoInput] with Specified[BSI with BSImplInput]

    /** Every spec for BankService */
    type US1 = US[   DepositAble]
    type US2 = US[WithdrawalAble]
    type US3 = US[   HistoryAble]

    /** BankService test holder */
    trait Specified[+BS <: BSImpl] {
      def bankService: BS
    }
    /** Implementation(s) of BankService */
    type BSImpl = BSImplInput
    val  bSImplBuilder: (ClientInput, AccntInput, AccntRepoInput) => Fixture.BSImpl = bSImplBuilderInput

    /** Dependencies holder of BankService being tested */
    abstract class Prerequisites[+A  <: AccntInput : Factory,
                                 +AR <: AccntRepoInput] {
      def dummyClient           : ClientInput
      def dummyAccount          : A
      def dummyAccountRepository: AR
    }

    /** TP for getting Specified well parameterized for all scenarii */
    private[BankServiceSpec] abstract class Completion[A  <: AccntInput : Factory,
                                                       AR <: AccntRepoInput,
                                                       BS <: BSImpl] {
      def apply(p: Prerequisites[A, AR]): Prerequisites[A, AR] with Specified[BS]
    }

    /** default and sole implementation Completion to test */
    val bkC = new Completion[AccntInput, AccntRepoInput, BSImpl with DepositAble] {
      override def apply(p: Prerequisites[AccntInput, AccntRepoInput]) =
        new Prerequisites[AccntInput, AccntRepoInput] with Specified[BSImpl] {
          lazy val dummyClient = p.dummyClient
          lazy val dummyAccount = p.dummyAccount
          lazy val dummyAccountRepository = p.dummyAccountRepository

          lazy val bankService: BSImpl = bSImplBuilderInput(dummyClient, dummyAccount, dummyAccountRepository)
        }
    }

    def apply[BS <: BSImpl](operationsAmounts: Money*) =
      new Fixture[AccntInput, AccntRepoInput].Generator(
        clientBuilderInput(1L),
        (c: ClientInput) => Account.addAccountOperations(Account.blank(c), operationsAmounts: _*),
        accountRepositoryGen = accntRepoBuilderInput
      ).apply(bkC)

    lazy val dummyAmounts: Seq[Money] = Seq(70, 4535, -46, -100)

    lazy val withDummyAmounts = apply(Fixture.dummyAmounts: _*)

  }
  class Fixture[+A <: AccntInput : Factory, +AR <: AccntRepoInput] {
    import Fixture._

    case class Generator[AA >: A  <: AccntInput : Factory,
                        AAR >: AR <: AccntRepoInput,
                         BS <: BSImpl](clientGen: ClientInput,
                         accountGen          : ClientInput => AA,
                         accountRepositoryGen: ClientInput => AA => AAR) {
      def apply(implicit completion: Completion[AA, AAR, BS]) =
        completion(new Prerequisites[AA, AAR] {
          lazy val dummyAccountRepository = accountRepositoryGen(dummyClient)(dummyAccount)
          lazy val dummyClient = clientGen
          lazy val dummyAccount = accountGen(clientGen)
        })
    }
  }

  Feature("Spec \"US1\"") {

    lazy val us1: Fixture.US1 = Fixture.apply()
    val few: Money = 4
    lazy val newAccount = us1.bankService.deposit(us1.dummyClient, few)

    info("In order to save money")
    info("As a bank client")
    info("I want to make a deposit in my account")
    Scenario(s"I have got few money to save to my new account") {
      newAccount.map { account =>
        Given("a blank account of mine with no saving on it")
        // us1dummyAccount at 0
        When(s"I deposit few money")
        Then(s"account should have a balance of this few money AND\n" +
          " account should have a last operation of this few money as a credited amount AND\n" +
          " account should have a last operation with same balance as amount as this few money AND\n" +
          " account should have just one operation")
        assert(
          account.balance == few &&
          account.statement.headOption.exists(_.amount == few) &&
          account.statement.headOption.exists(op => op.amount  == op.balance && op.balance == few && op.amount == few) &&
          account.statement.size == 1
        )
      }
    }
    lazy val us1FixtureModified: Fixture.US1 = Fixture(few)
    val aLotOf: Money = 55555
    val twicedUsedAccount = us1FixtureModified.bankService.deposit(us1FixtureModified.dummyClient, aLotOf)

    Scenario(s"I have got a lot of amount to save to my once used account") {
      twicedUsedAccount.map { account =>
      Given("a once used account of mine with few saving on it")
      // us1dummyAccount at 44448
      When(s"I deposit a lot of amount")
      Then(s"account should have a balance added by a lot of money AND\n " +
        "account should have a last operation of a lot of money as credited amount AND\n " +
        "account should have a just two operations")
      assert(
        account.balance == aLotOf + few &&
        account.statement.headOption.exists(_.amount == aLotOf) &&
        account.statement.size == 2
        )
      }
    }
  }

  Feature("Spec \"US2\"") {

    lazy val us2Fixture: Fixture.US2 = Fixture.withDummyAmounts
    val few: Money = 4
    val lastAccount = us2Fixture.dummyAccount
    lazy val filledAccount = us2Fixture.bankService.withdrawal(us2Fixture.dummyClient, few)

    info("In order to retrieve some or all of my savings")
    info("As a bank client")
    info("I want to make a withdrawal from my account")
    Scenario("I retrieve few of my savings") {
      filledAccount.map { account =>
        Given("a filled account of mine with some saving on it")
        When(s"I deposit few money") // done on filledAccount
        Then(s"account has a balance of previous balance minus few money AND\n" +
          "account has a last operation of minus few money as credited amount AND\n " +
          "account has a one more operation"
        )
        assert(
          account.balance == lastAccount.balance - few &&
          account.statement.headOption.exists(_.amount == -few) &&
          account.statement.size == lastAccount.statement.size + 1
        )
      }
    }
  }

  Feature("Spec \"US3\"") {

    note("In order to check my operations")
    note("As a bank client")
    note("I want to see the history (operation, date, amount, balance) of my operations")
    lazy val us3Fixture: Fixture.US3 = Fixture.withDummyAmounts

    val nbOperations = Fixture.dummyAmounts.size
    lazy val filledHistory = us3Fixture.bankService.history(us3Fixture.dummyClient)
    Scenario(s"I check a history of $nbOperations operations") {
      filledHistory.map { statementPrinted =>
        Given("a filled account of mine with some operations on it")
        // us1dummyAccount 4459 €
        When(s"I check history")
        info("\n" + statementPrinted)
        Then(s"account has the same number of lines as its operations with one for headers")
        assert(statementPrinted.split(us3Fixture.bankService.operationsSeparator).length
          == us3Fixture.dummyAccount.statement.size + 1)
      }

    }

  }

}

object BankServiceSpec {
  type USSpec = BankService with DepositAble with WithdrawalAble with HistoryAble
}