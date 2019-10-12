package pureconfig.module.magnolia.auto

import scala.language.experimental.macros
import scala.reflect.ClassTag

import magnolia._
import pureconfig.ConfigWriter
import pureconfig.generic.{ CoproductHint, ProductHint }
import pureconfig.module.magnolia.MagnoliaConfigWriter

/**
 * An object that, when imported, provides implicit `ConfigWriter` instances for value classes, tuples, case classes and
 * sealed traits. The generation of `ConfigWriter`s is done by Magnolia.
 */
object writer {
  type Typeclass[A] = ConfigWriter[A]

  def combine[A: ProductHint](ctx: CaseClass[ConfigWriter, A]): ConfigWriter[A] =
    MagnoliaConfigWriter.combine(ctx)

  def dispatch[A: ClassTag: CoproductHint](ctx: SealedTrait[ConfigWriter, A]): ConfigWriter[A] =
    MagnoliaConfigWriter.dispatch(ctx)

  implicit def exportWriter[A]: ConfigWriter[A] = macro Magnolia.gen[A]
}
