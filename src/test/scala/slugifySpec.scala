package com.osinka.slugify

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

class slugifySpec extends FunSpec with ShouldMatchers {
  val slugify = Slugify.default
  describe("Slugify") {
    it("converts deutsch ß to ss") {
      slugify("ß") should equal("ss")
    }
    it("keeps _ -") {
      slugify("--__-") should equal("-__-")
    }
    it("replaces any number of whitespaces with minus") {
      slugify("1  3  6") should equal("1-3-6")
    }
    it("drops heading or ending spaces") {
      slugify("   6    ") should equal("6")
    }
    it("transliterates cyrillic characters") {
      slugify("ава ук ефы") should equal("ava-uk-efy")
    }
    it("drops accents") {
      slugify("Ȳá") should equal("Ya")
    }
    it("drops non-characters") {
      // there is a lot of characters that cannot be translated into latin
      // mostly greek, math, etc.
      slugify("-ҥѶƇ-") should equal("-")
    }
  }
}
