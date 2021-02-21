package com.deange.mechnotifier.model

import com.deange.mechnotifier.model.PostType.SELLING
import com.google.common.truth.Truth.assertWithMessage
import org.junit.Test

class PostTest {

  @Test
  fun `types for selling posts`() {
    assertType(
        SELLING,
        "[CA-ON] [H] GMK Olivia Dark base kit, Candybar Premium 40% keyboard R2 Frosted PC [W] Paypal",
        "[CA-ON][H]XD64 case, BM60 hotswap PCB, ePBT Sushi[W]local cash/paypal",
        "[EU-SE][H]Rama U-80 Kuro w/ Internal weight [W]Paypal",
        "[EU-CZ] [H] QUICK SALE UT47v1 [W] Paypal, hotswap 65+ unbuild",
        "[US-NY][H]Project Keyboards Sirius, Norbaforce MK1, Lubrigante, Realforce 87ub [W]Paypal"
    )
  }

  private fun assertType(
    postType: PostType,
    vararg postTitles: String
  ) {
    postTitles.forEach { title ->
      assertWithMessage(title)
          .that(post(title).type())
          .isEqualTo(postType)
    }
  }

  private fun post(title: String): Post = Post(
      id = "id",
      url = "url",
      title = title,
      flair = null,
      createdSeconds = 0L
  )
}
