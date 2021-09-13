package application

import application.bankservice.UnitedStateBankService
import domain.Client.SimpleClient
import domain.ClientId
import domain.account.SimpleAccount
import infrastructure.SimpleAccountRepository
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AsyncFeatureSpec

class UnitedStateBankServiceSpec
  extends AsyncFeatureSpec with GivenWhenThen
    with SimpleBankServiceSpec[UnitedStateBankService,
                               SimpleAccount,
                               SimpleAccountRepository[SimpleAccount],
                               SimpleClient] {
  override implicit lazy val accountFactoryInput = SimpleAccount
  override lazy val bSImplBuilderInput =
    (_, _, acR: SimpleAccountRepository[SimpleAccount]) => new UnitedStateBankService(acR)
  override lazy val clientBuilderInput: ClientId => SimpleClient = SimpleClient.apply _
  override lazy val accntRepoBuilderInput = _ => new SimpleAccountRepository[SimpleAccount](_)

}
