/*
 * Copyright 2026 HM Revenue & Customs
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
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder
import uk.gov.hmrc.performance.conf.ServicesConfiguration

import java.util.UUID

object NotificationRequests extends ServicesConfiguration {

  private val baseUrl: String = baseUrlFor("universal-credit-notification")
  private val route: String   = "/notification"

  private val authToken: String    = AuthHelper.getAuthToken
  private val govUkOriginatorId: String = "gov-uk-originator-id"

  val randomCorrelationIDs: Iterator[Map[String, String]] =
    Iterator.continually(Map("correlationId" -> UUID.randomUUID().toString))

  def correlationIdFeeder: ChainBuilder = feed(randomCorrelationIDs)

  private val nationalInsuranceNumber: String = "AE001474"

  def notificationBody: String =
    s"""
       |{
       |  "nationalInsuranceNumber": "$nationalInsuranceNumber",
       |  "universalCreditRecordType": "{{universalCreditRecordType}}",
       |  "universalCreditAction": "{{universalCreditAction}}",
       |{% if universalCreditAction == "Insert" %}  "dateOfBirth": "2002-10-10",\n{% endif %}
       |{% if universalCreditAction == "Terminate" %}  "liabilityEndDate": "2025-08-19",\n{% endif %}
       |  "liabilityStartDate": "2025-08-19"
       |}
       |""".stripMargin

  val sendNotification: HttpRequestBuilder =
    http("Send Universal Credit Liability Notification - #{universalCreditAction}")
      .post(s"$baseUrl$route")
      .headers(
        Map(
          "authorization"        -> authToken,
          "content-type"         -> "application/json",
          "gov-uk-originator-id" -> govUkOriginatorId,
          "correlationId"        -> "#{correlationId}" // Gatling Expression Language extracts correlationId from Feeder
        )
      )
      .body(PebbleStringBody(notificationBody))
      .check(status.is(204))

}
