package application.bankservice

import domain.country.Country
import application.i18n.FrenchHistoryFormat
import infrastructure.AccountRepository.{AccountAmountSetter, AccountGetter}

import scala.concurrent.ExecutionContext

class FrenchBankService(override val accountRepository: AccountGetter with AccountAmountSetter)
                       (implicit override val ec: ExecutionContext)
  extends SimpleBankService(accountRepository) with FrenchHistoryFormat {
  override lazy val country: Country = Country.France
}
