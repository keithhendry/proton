package proton.users

import java.util.UUID

import com.unboundid.ldap.sdk._

import scala.collection.JavaConverters._

class Ldap(connectionPool: LDAPConnectionPool, baseDN: String) {
  import Models._

  def getUsers: List[User] = {
    val searchResult = connectionPool.search("ou=users," + baseDN, SearchScope.ONE,
      "(objectClass=organizationalPerson)", "entryUUID", "uid", "cn", "mail")

    var users = List[User]()

    searchResult.getSearchEntries.asScala.foreach(entry => {
      users = users :+ User(UUID.fromString(entry.getAttributeValue("entryUUID")), entry.getAttributeValue("uid"),
        entry.getAttributeValue("cn"), entry.getAttributeValue("mail"))
    })

    users
  }
}
