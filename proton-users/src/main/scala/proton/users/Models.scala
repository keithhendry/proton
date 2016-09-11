package proton.users

import java.util.UUID

object Models {
  case class User(id: UUID, uid: String, name: String, email: String)
}
