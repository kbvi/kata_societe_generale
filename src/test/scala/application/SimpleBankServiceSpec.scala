package application

import application.bankservice.SimpleBankService
import domain.Client
import domain.account.Account
import infrastructure.AccountRepository
import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AsyncFeatureSpec

trait SimpleBankServiceSpec[BSImplInput <: BankServiceSpec.USSpec with SimpleBankService,
                             AccntInput <: Account,
                         AccntRepoInput <: AccountRepository,
                            ClientInput <: Client]
  extends BankServiceSpec[BSImplInput, AccntInput, AccntRepoInput, ClientInput] {
  self: AsyncFeatureSpec with GivenWhenThen =>
}
