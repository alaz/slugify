package com.osinka.slugify

class Slugify(normalize: (String => String)) {
  protected val duplicateDashes = """-+""".r
  protected def dedupDashes(s: String) = duplicateDashes.replaceAllIn(s, "-")

  protected val space = """\p{Space}""".r
  protected def whitespaces(s: String) = space.replaceAllIn(s, "-")

  protected def preprocess(s: String) = s.trim

  protected val slugify = Function.chain( List(preprocess _, normalize, whitespaces _, dedupDashes _) )

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
  val ASCII = """Bulgarian-Latin/BGN; Any-Latin; Latin-ASCII; [^-_\p{Latin}\p{Digit}\p{Space}] Remove; Any-Lower"""

  def normalize(algo: String) = {
    import com.ibm.icu.text.Transliterator
    val tr = Transliterator.getInstance(algo)
    (s: String) => tr.transliterate(s)
  }

  def as(algo: String) = new Slugify(normalize(algo))

  lazy val default = as(ASCII)

  def apply(s: String) = default(s)
}