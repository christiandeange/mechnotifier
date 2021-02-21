package com.deange.mechnotifier.topics

enum class PublicType(val tagName: String) {
  INTEREST_CHECK("IC"),
  GROUP_BUY("GB"),
  ARTISAN("Artisan"),
  SERVICE("Service"),
  VENDOR("Vendor"),
  ;

  companion object {
    fun fromTagName(tagName: String): PublicType {
      return values().first { it.tagName == tagName }
    }

    fun tagNames(): Set<String> {
      return values().map { it.tagName }.toSet()
    }
  }
}
