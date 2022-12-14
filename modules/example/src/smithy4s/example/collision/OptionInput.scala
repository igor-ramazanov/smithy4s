package smithy4s.example.collision

import smithy4s.Hints
import smithy4s.Schema
import smithy4s.ShapeId
import smithy4s.ShapeTag
import smithy4s.schema.Schema.string
import smithy4s.schema.Schema.struct

case class OptionInput(value: Option[String] = None)
object OptionInput extends ShapeTag.Companion[OptionInput] {
  val id: ShapeId = ShapeId("smithy4s.example.collision", "OptionInput")

  val hints : Hints = Hints(
    smithy.api.Input(),
  )

  implicit val schema: Schema[OptionInput] = struct(
    string.optional[OptionInput]("value", _.value),
  ){
    OptionInput.apply
  }.withId(id).addHints(hints)
}