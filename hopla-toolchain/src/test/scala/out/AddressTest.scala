package out

/**
 * Created by JIN Benli on 07/05/14.
 */

import org.junit._
import org.scalatest.junit.JUnitSuite
import org.scalatest.Matchers
import address.Tags

@Test
class AddressNormalTest extends Tags {
  @Test
  def normalTest() {
    val res1 = Address(Recipient("jin"), House("Fil"), Town(Nom("v2")), PostCode("59650"))
    val expect1 = "<Address><Recipient>jin</Recipient><House>Fil</House><Town><Nom>v2</Nom></Town><PostCode>59650</PostCode></Address>"

    assert(res1.toString == expect1)
  }
}

class AddressExceptionTest extends JUnitSuite with Matchers with Tags {
  @Test
  def relationException() {
    evaluating {
      val res2 = Recipient(Address())
      res2.toString
    } should produce[IllegalArgumentException]
  }
}

