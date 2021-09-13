package application

import application.bankservice.FrenchBankService
import domain.Client.SimpleClient
import domain.ClientId
import domain.account.SimpleAccount
import infrastructure.SimpleAccountRepository
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AsyncFeatureSpec

class FrenchBankServiceSpec
  extends AsyncFeatureSpec with GivenWhenThen
    with SimpleBankServiceSpec[FrenchBankService,
                               SimpleAccount,
                               SimpleAccountRepository[SimpleAccount],
                               SimpleClient] {
  override implicit lazy val accountFactoryInput = SimpleAccount
  override lazy val bSImplBuilderInput =
    (_, _, acR: SimpleAccountRepository[SimpleAccount]) => new FrenchBankService(acR)
  override lazy val clientBuilderInput: ClientId => SimpleClient = SimpleClient.apply _
  override lazy val accntRepoBuilderInput = _ => new SimpleAccountRepository[SimpleAccount](_)
}
