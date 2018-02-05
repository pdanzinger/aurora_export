import kotlin.browser.document
import kotlinx.html.dom.*
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.Document
import org.w3c.dom.asList
import kotlin.dom.clear

val body = document.body!!
val create = document.create

fun main(args: Array<String>) {
    document.querySelector("#comments_column_1")?.prepend(Main.buttonActivate)
}

object Main {
    val buttonActivate = create.button {
        text("Meine Arbeiten exportieren")
        onClickFunction = { start() }
        style = """
            background: lightgreen;
            padding: 15px 30px;
            color: white;
            margin-bottom: 10px;
            """
    }

    val cssNodes = document.querySelectorAll("link[rel='stylesheet']")
            .asList()
            .plus(create.link {rel="stylesheet"; href="/static/css/base.css"})
            .plus(create.link {rel="stylesheet"; href="/static/hijack/hijack-styles.css"})
            .plus(create.link {rel="stylesheet"; href="/static/css/challenge.css"})
            .plus(create.link {rel="stylesheet"; href="/static/trix/trix.css"})
            .plus(create.link {rel="stylesheet"; href="/static/Comments/comments.css"})
            .plus({
                val element = document.createElement("style")
                element.innerHTML = customCSS
                element
            }())

    var nick = ""
    var nickImageUrl = ""


    fun start() {
        document.head!!.clear()
        body.clear()
        body.append( create.div {
            img(src = "/static/img/auroraN@2x.jpg") {
                style = """
                    position: absolute;
                    top: 0;
                    right: 0;
                    bottom: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                """
            }
            div {
                style = """
                    width: 600px;
                    margin: 5em auto;
                    padding: 50px;
                    background-color: #fff;
                    border-radius: 1em;
                    position: relative;
                """
                h2 {
                    style = "font-size: 2em; font-weight: bold;"
                    +"Export wurde gestartet."
                }
                p {
                    +"Im Hintergrund werden jetzt alle abgegebenen Tasks geladen. Sobald dieser Vorgang abgeschlossen ist, wird diese Seite durch den Export ausgetaucht."
                    br()
                    br()
                    +"Diese Seite kann auf zwei Arten permanent gespeichert werden:"
                    ol {
                        li {+"(bevorzugt) Mit der Tastenkombination Strg+S erlauben es die meisten Browser, eine HTML-Seite samt Bildern lokal abzuspeichern. Dabei werden eine HTML-Datei und ein gleichnamiger Unterordner erzeugt."}
                        li {+"Ausdrucken. Mit der Tastenkombination Strg+P kann man einen Ausdruck der Seite erstellen. Oft wird dabei vom Betriebssystem auch ein 'PDF-Drucker' angeboten, mit dem man den Ausdruck in eine PDF-Datei umleiten kann. (Achtung: dauert recht lange)"}
                    }
                    br()
                    br()
                    +"Der Export sollte etwa 1-2 Minuten dauern; für jede Challenge und für jeden Task wird nacheinander je eine GET-Anfrage an den Server geschickt."
                    br()
                    +"Falls Probleme auftreten, am besten noch einmal mit Chrome probieren. Falls mit Chrome Probleme auftreten, ups :("
                    br()
                    br()
                    +"PS: Bei Langeweile kann man in der Entwicklerkonsole den Fortschritt mitverfolgen."
                }
            }
        } )

        // hack: remove window focus handler to prevent
        // polling of /comment/update/ with invalid POST data
        js("\$(window).unbind('focus');")
        js("COMMENTS.POLLING.stopped = true;")

        Log.log("Export script starting...", Log.STATUS_MESSAGE)
        FetchHtml("/course/dwi/challenge", ::gotChallenges)
    }

    fun gotChallenges(dom: Document) {
        val challenges = Challenge.extractFromChallengeList(dom)
        nick = dom.querySelector(".user_nick")!!.textContent!!
        nickImageUrl = dom.querySelector(".user_info")!!
                .getAttribute("style")!!
                .split("url('")[1]!!
                .split("')")[0]

        Log.log("Found "+challenges.size+" challenges", Log.STATUS_MESSAGE)
        FetchIterator(
                challenges,
                {challenge -> "/course/dwi/challenge/stack?id=" + challenge.id},
                {challenge, dom -> Challenge.refineFromChallengeDetail(challenge, dom)},
                ::gotChallengesWithPoints
        )
    }

    fun gotChallengesWithPoints(challenges: List<Challenge>) {
        Log.log("Refined challenges with data from the detail view", Log.STATUS_MESSAGE)
        loadTasksForChallenge(challenges)
    }

    fun loadTasksForChallenge(challenges: List<Challenge>, index: Int = 0) {
        // if index == challenges.size continue
        if (index == challenges.size) {
            renderResult(challenges)
        } else {
            Log.log("Getting tasks for challenge \"" + challenges[index].name + "\"", Log.STATUS_MESSAGE)
            FetchIterator(
                    challenges[index].tasks,
                    {task -> "/course/dwi/challenge/challenge?id=" + task.id},
                    {task, dom -> Task.refineFromTaskDetail(task, dom)},
                    {newTasks ->
                        val newList = challenges.toMutableList()
                        newList.set(index, challenges[index].copy(tasks = newTasks))
                        loadTasksForChallenge(newList, index+1)
                    }
            )
        }
    }


    fun renderResult(challenges: List<Challenge>) {
        Log.log("Finished downloading work. Rendering result.", Log.STATUS_MESSAGE)
        val cssPrefix = "aurora_export-"
        body.clear()
        cssNodes.forEach { body.append(it) }
        body.append {
            div(classes = cssPrefix + "container") {

                div(classes = cssPrefix + "intro-container") {
                    img(src = "/static/img/auroraN@2x.jpg")
                    div(classes = cssPrefix + "intro") {
                        div(classes = cssPrefix + "heading") { h2 { +"Denkweisen der Informatik" } }
                        div(classes = cssPrefix + "user") {
                            p { +("Portfolio von " + nick) }
                            img(src = nickImageUrl)
                        }
                    }
                }

                challenges.forEach { challenge ->

                    div(classes = cssPrefix + "challenge-container") {

                        div(classes = cssPrefix + "challenge") {
                            div(classes = cssPrefix + "general-container") {
                                div(classes = cssPrefix + "general") {
                                    h2 {+("Challenge: " + challenge.name)}
                                    p {
                                        if (challenge.awardedPoints >= 0) {
                                            +(challenge.awardedPoints.toString() + " von " + challenge.maxPoints + " Punkten")
                                        } else {
                                            +("[noch nicht bewertet]")
                                        }
                                    }
                                }
                            }

                            div(classes = cssPrefix + "tasks-container") {

                                div(classes = cssPrefix + "tasks") {
                                    challenge.tasks.forEach { task ->
                                        div(classes = cssPrefix + "task") {
                                            div(classes = cssPrefix + "title-container") {
                                                div(classes = cssPrefix + "title") {
                                                    h3 {+("Task: "+task.name)}
                                                    img {src = task.backgroundImageUrl}
                                                }
                                                div(classes = cssPrefix + "task-description challenge_description") {
                                                    div(classes = "challenge_description_text") {
                                                        unsafe { raw(task.descriptionHtml) }
                                                    }
                                                }
                                            }
                                            div(classes = cssPrefix + "task-content") {
                                                div(classes = cssPrefix + "task-images") {
                                                    var num = 1
                                                    task.fileURLs.forEach { url ->
                                                        div(classes = cssPrefix + "task-image-box " + if (url.endsWith("pdf")) "pdf" else "") {
                                                            a(href = url, target = "_blank") {
                                                                img(src = task.imageURLs[url], classes = "non-print")
                                                                img(src = url, classes = "print")
                                                            }
                                                            br()
                                                            span {
                                                                +("Fig. " + num)
                                                            }
                                                            // trick browsers into downloading our pdf on crtl+s
                                                            if (url.endsWith("pdf")) {
                                                                img {
                                                                    src = url
                                                                    style = "display: none;"
                                                                }
                                                            }
                                                        }
                                                        num++
                                                    }
                                                }
                                                div(classes = cssPrefix + "revision trix-content") {
                                                    unsafe { raw(task.workHtml) }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }


                }
            }
        }

    }

}
