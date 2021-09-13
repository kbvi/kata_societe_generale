package application.bankservice

import domain.country.Country
import application.i18n.UnitedStateHistoryFormat
import infrastructure.AccountRepository.{AccountAmountSetter, AccountGetter}

import scala.concurrent.ExecutionContext

class UnitedStateBankService(override val accountRepository: AccountGetter with AccountAmountSetter)
                            (implicit override val ec: ExecutionContext)
  extends SimpleBankService(accountRepository) with UnitedStateHistoryFormat {
  override lazy val country: Country = Country.UnitedState
}

