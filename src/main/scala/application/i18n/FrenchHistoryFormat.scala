package application.i18n

import java.text.SimpleDateFormat

trait FrenchHistoryFormat extends TextualHistoryFormat {
  lazy val amount: String = "montant"
  lazy val balance: String = "solde"
  lazy val date: String = "date"
  lazy val operationLabel = "opération n°"
  override lazy val dateFormat = new SimpleDateFormat("'Année' yyyy 'le' dd/MM HH:mm:ss").format
}
