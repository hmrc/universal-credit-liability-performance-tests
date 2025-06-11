/*
 * Copyright 2025 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.universalcreditliabilityapi

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration

import java.util.UUID

object InsertionRequests extends ServicesConfiguration {

  val nationalInsuranceNumber: String = RandomNino.next()
  val authToken: String               = AuthHelper.getAuthToken

  val baseUrl: String       = baseUrlFor("universal-credit-notification")
  val route: String         = "/notification"
  val originatorId: String  = ""
  val correlationId: String = UUID.randomUUID().toString

  def insertionBody(): String =
    s"""
       |{
       |  "nationalInsuranceNumber": "$nationalInsuranceNumber",
       |  "universalCreditRecordType": "UC",
       |  "universalCreditAction": "Insert",
       |  "dateOfBirth": "2002-10-10",
       |  "liabilityStartDate": "2025-08-19"
       |}
       |""".stripMargin

  val insertion: HttpRequestBuilder =
    http("Send insertion notification")
      .post(s"$baseUrl$route")
      .headers(
        Map(
          "authorization"        -> authToken,
          "content-type"         -> "application/json",
          "gov-uk-originator-id" -> originatorId,
          "correlationId"        -> correlationId
        )
      )
      .body(StringBody(insertionBody()))
      .check(status.is(204))
}
