package domain

trait Client {
  val id: ClientId
}
object Client {
  case class SimpleClient(override val id: ClientId) extends Client
}