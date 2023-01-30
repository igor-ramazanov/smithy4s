/*
 *  Copyright 2021-2022 Disney Streaming
 *
 *  Licensed under the Tomorrow Open Source Technology License, Version 1.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     https://disneystreaming.github.io/TOST-1.0.txt
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package smithy4s.codegen.internals

final class RendererSpec extends munit.FunSuite {
  import TestUtils._

  test("list member hints should be preserved") {
    val smithy =
      """
        |$version: "2.0"
        |
        |namespace smithy4s
        |
        |list ListWithMemberHints {
        |  @documentation("listFoo")
        |  @deprecated
        |  member: String
        |}
        |""".stripMargin

    val contents = generateScalaCode(smithy)
    val definition =
      contents.find(_.contains("object ListWithMemberHints")) match {
        case None =>
          fail(
            "No generated scala file contains valid ListWithMemberHints definition"
          )
        case Some(code) => code
      }

    val memberSchemaString =
      """string.addHints(smithy.api.documentation("listFoo"), smithy.api.deprecated(message = None, since = None))"""
    val requiredString =
      s"""val underlyingSchema: Schema[List[String]] = list($memberSchemaString)"""
    assert(definition.contains(requiredString))
  }

  test("map member hints should be preserved") {
    val smithy =
      """
        |$version: "2.0"
        |
        |namespace smithy4s
        |
        |map MapWithMemberHints {
        |  @documentation("mapFoo")
        |  key: String
        |
        |  @documentation("mapBar")
        |  @deprecated
        |  value: Integer
        |}
        |""".stripMargin

    val contents = generateScalaCode(smithy)
    val definition =
      contents.find(_.contains("object MapWithMemberHints")) match {
        case None =>
          fail(
            "No generated scala file contains valid MapWithMemberHints definition"
          )
        case Some(code) => code
      }

    val keySchemaString =
      """string.addHints(smithy.api.documentation("mapFoo"))"""
    val valueSchemaString =
      """int.addHints(smithy.api.documentation("mapBar"), smithy.api.deprecated(message = None, since = None))"""
    val requiredString =
      s"""val underlyingSchema: Schema[Map[String, Int]] = map($keySchemaString, $valueSchemaString)"""
    assert(definition.contains(requiredString))
  }
}