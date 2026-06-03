package com.example

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ServerTest {

//    @Test
//    fun `test root endpoint`() = testApplication {
//        // loads default configuration
//        configure()
//        // verify server root returns 200
//        assertEquals(HttpStatusCode.OK, client.get("/").status)
//    }
//
//    @Test
//    fun `test new endpoint`() = testApplication {
//        configure()
//
//        val response = client.get("/test1")
//
//        assertEquals(HttpStatusCode.OK, response.status)
//        assertEquals("html", response.contentType()?.contentSubtype)
//        assertEquals(response.bodyAsText(), "Hello From Ktor")
//    }

    @Test
    fun newTasksCanBeAdded() = testApplication {

        configure()

        val response1 = client.post("/tasks"){

            header(
                HttpHeaders.ContentType,
                ContentType.Application.FormUrlEncoded.toString()
            )
            setBody(
                listOf(
                    "name" to "swimming",
                    "description" to "Go to the beach",
                    "priority" to "Low"
                ).formUrlEncode()
            )

        }
        assertEquals(HttpStatusCode.NoContent, response1.status)//actual 302Found

        val response2 = client.get("/tasks")

        assertEquals(HttpStatusCode.OK, response2.status)

        val body = response2.bodyAsText()

        assertContains(body, "swimming")
        assertContains(body, "Go to the beach")

    }

    @Test
    fun taskCanBeFoundByPriority() = testApplication {

        configure()

        val response = client.get("/tasks/byPriority/Medium")
        val body = response.bodyAsText()



        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue { body.contains("Mow the lawn") }
        assertTrue { body.contains("Paint the fence") }
//        assertEquals(body, "Mow the lawn")
//        assertEquals(body, "Paint the fence")

    }

    @Test
    fun invalidPriorityProduces400() = testApplication {

        configure()

        val response = client.get("/tasks/byPriority/Invalid")

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun unusedPriorityProduces404() = testApplication {

        configure()

        val response = client.get("/tasks/byPriority/Vital")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

}
