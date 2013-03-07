package com.osinka.slugify

class Slugify(algo: String) {
  protected val tr = {
    import com.ibm.icu.text.Transliterator
    Transliterator.getInstance(algo)
  }
  protected def normalize(s: String) = tr transliterate s

  protected val duplicateDashes = """-+""".r
  protected def dedupDashes(s: String) = duplicateDashes.replaceAllIn(s, "-")

  protected def whitespaces(s: String) = s.replaceAll(" ", "-")

  protected def preprocess(s: String) = s.trim

  protected val slugify = Function.chain( List(preprocess _, normalize _, whitespaces _, dedupDashes _) )

  def apply(s: String) = slugify(s)
}

/** This makes pretty slugs out of any string in any charset.
  *
  * There is an alternate implementation at https://github.com/slugify/slugify , but
  * - it has GNU license
  * - it just passes cyrillic letters along!
  *
  * Most reasonable seems to use Transliterator from ICU4J library with custom mappings.
  * This is very likely quite slow: needs benchmarking.
  *
  * Credits:
  * http://stackoverflow.com/questions/10188575/convert-latin-characters-to-normal-text-in-java
  * http://stackoverflow.com/questions/1657193/java-code-library-for-generating-slugs-for-use-in-pretty-urls
  */
object Slugify {
  val ASCII = """Any-Latin; Latin-ASCII; NFD; [:Nonspacing Mark:] Remove; NFC; [^-_\p{Latin}\p{Digit}\p{Space}] Remove; Any-Lower"""

  def as(algo: String) = new Slugify(algo)

  lazy val default = as(ASCII)

  def apply(s: String) = default(s)
}