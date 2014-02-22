package com.osinka.slugify

import org.scalatest.{FunSpec, Matchers}
import org.scalatest.prop.PropertyChecks

class slugifySpec extends FunSpec with Matchers with PropertyChecks {
  val slugify = Slugify.default
  describe("Slugify") {
    it("converts deutsch ß to ss") {
      slugify("ß") should equal("ss")
    }
    it("keeps _") {
      slugify("* --__- *") should equal("__")
    }
    it("replaces any number of whitespaces with minus") {
      slugify("1  3  6") should equal("1-3-6")
    }
    it("drops heading or ending spaces") {
      slugify("   6    ") should equal("6")
    }
    it("transliterates cyrillic characters w/ BGN romanization") {
      slugify("а б в г д е ж з и й к л м н о п р с т у ф х ц ч ш щ э ю я ь ъ") should equal("a-b-v-g-d-e-zh-z-i-i-k-l-m-n-o-p-r-s-t-u-f-kh-ts-ch-sh-sht-e-yu-ya-u")
      slugify("Ольга") should equal("olga")
    }
    it("drops accents") {
      slugify("Ȳá") should equal("ya")
    }
    it("drops non-characters") {
      // there is a lot of characters that cannot be translated into latin
      // mostly greek, math, etc.
      slugify("ҥѶƇ") should equal("c")
    }
    it("replaces select punctuation with dashes") {
      slugify("""A+l[on]g-l(in)e@{wit}h "a'hu'as"bol&s""") should equal("a-l-on-g-l-in-e-wit-h-ahuasbol-s")
    }
    it("lowercases") {
      slugify("""ФЫВАЯYAUSL""") should equal("""fyvayayausl""")
    }
    it("meets the `slug` laws") {
      forAll { (str: String) =>
        val slug = slugify(str)
        slug should not(startWith("-") and endWith("-"))
        slug.count(_.isUpper) should equal(0)
        slug.count(_.isSpaceChar) should equal(0)
        slug should not(include("--"))
        slug should not(include("""[^-_\p{ASCII}\p{Digit}]"""))
        slug should not(include("""[^-_\p{Latin}\p{Digit}]"""))
      }
    }
  }
}
