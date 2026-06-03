package com.example

import com.example.model.Priority
import com.example.model.Task
import com.example.model.TaskRepository
import com.example.model.tasksAsTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.*


////First chapter
//fun Application.configureRouting() {
//
//    install(StatusPages){
//        exception<IllegalStateException> {call, cause ->
//            call.respondText("App in illegal state as ${cause.message}")
//        }
//    }
//
//    routing {
//
//        get("/error-test"){
//            throw IllegalStateException("Too Busy")
//        }
//
//        get("/") {
//            call.respondText("Hello, World!")
//        }
//
//        staticResources("/content", "mycontent")
//
//        get("/test1") {
//            val text = "<h1>Hello From Ktor</h1>"
//            val type = ContentType.parse("text/html")
//            call.respondText(text, type)
//        }
//    }
//}

fun Application.configureRouting(){

    routing {

        staticResources("/task-ui", "task-ui")

        get("/task") {
            call.respondText(
                contentType = ContentType.parse("text/html"),
                    text = """
                <h3>TODO:</h3>
                <ol>
                    <li>A table of all the tasks</li>
                    <li>A form to submit new tasks</li>
                </ol>
                """.trimEnd()
            )
        }

        get("/tasks"){
            val tasks = TaskRepository.allTasks()
            call.respondText(
                contentType = ContentType.parse("text/html"),
                text = tasks.tasksAsTable()
            )
        }

        post("/tasks") {

            val formContent = call.receiveParameters()

            val params = Triple(
                formContent["name"] ?: "",
                formContent["description"] ?: "",
                formContent["priority"] ?: "",
            )

            if (params.toList().any {it.isEmpty()}){
                call.respondText(text = "Заполните все поля", status = HttpStatusCode.BadRequest)
                return@post
            }

            try {

                val priority = Priority.valueOf(params.third)

                TaskRepository.addTask(
                    task = Task(
                        name = params.first,
                        description = params.second,
                        priority = priority
                    )
                )

                    call.respondRedirect("/tasks")

            }catch (ex: IllegalArgumentException){
                call.respond(HttpStatusCode.BadRequest)
            }catch (ex: IllegalStateException){
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/tasks/byPriority/{priority?}"){

            val priorityAsText = call.parameters["priority"]

            if (priorityAsText == null){
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            try {

                val priority = Priority.valueOf(priorityAsText)
                val tasks = TaskRepository.tasksByPriority(priority)

                if (tasks.isEmpty()){
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respondText(
                    contentType = ContentType.parse("text/html"),
                    text = tasks.tasksAsTable()
                )


            }catch (e: IllegalArgumentException){
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}