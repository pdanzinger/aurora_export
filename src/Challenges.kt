import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.asList
import org.w3c.dom.parsing.DOMParser
import kotlin.browser.document

data class Challenge(
        val id: Int,
        val name: String,
        val maxPoints: Int,
        val awardedPoints: Int,
        val handedIn: Boolean,
        val tasks: List<Task> = listOf()
)  {
    companion object {
        fun extractFromChallengeList(dom: Document): List<Challenge> {
            return dom.querySelectorAll("#detail_area_1 .stack").asList().map { chapter ->
                if (chapter is Element) {
                    Challenge(
                        name = chapter.querySelector(".title b")!!.textContent!!.withoutLastChar(),
                        id = chapter.getAttribute("id")!!.toInt(),
                        maxPoints = chapter.querySelector(".title")?.textContent
                                !!.split("max. ")[1].split(" Points")[0].toInt(),
                        awardedPoints = -1,
                        handedIn = chapter.querySelectorAll(".fa-spinner, .fa-check").length > 0
                    )
                } else null
            }.filterNotNull().filter { it.handedIn }
        }

        fun refineFromChallengeDetail(challenge: Challenge, dom: Document): Challenge {
            var newPoints = challenge.awardedPoints
            dom.querySelectorAll(".step.donow")
                    .asList()
                    .forEach {node ->
                        if (node.textContent?.contains("EVALUATED") == true && node is Element) {
                            newPoints = node.querySelector("span")!!.textContent!!.toInt()
                        }
                    }


            val tasks = dom.querySelectorAll(".card_list .card").asList().filter { it is Element }.map {
                val element = it as Element
                Task(
                        id = element.getAttribute("challenge_id")!!.toInt(),
                        name = element.querySelector(".card_headline")!!.textContent!!,
                        handedIn = "",//js("new Date(1970,01,01)"),
                        descriptionHtml = "",
                        workHtml = "",
                        backgroundImageUrl = element.getAttribute("style")!!
                                .split("url('")[1].split("')")[0],
                        fileURLs = listOf(),
                        imageURLs = mapOf()
                )
            }

            return challenge.copy(awardedPoints = newPoints, tasks = tasks)
        }

        private fun String.withoutLastChar(): String {
            return this.substring(0, this.length - 1)
        }
    }
}


data class Task(
        val id: Int,
        val name: String,
        val handedIn: String,
        val descriptionHtml: String,
        val workHtml: String,
        val backgroundImageUrl: String,
        val fileURLs: List<String>,
        val imageURLs: Map<String, String> // fileURL -> imageURL
) {

    companion object {
        fun refineFromTaskDetail(task: Task, dom: Document): Task {
            val fileURLs = dom.querySelectorAll(".revision_section .file_upload_not:first-of-type a")
                    .asList()
                    .map { link -> (link as Element).getAttribute("href") ?: "" }
                    .filterNot { it == "" }

            return task.copy(
                    descriptionHtml = dom.querySelector(".challenge_description_text")!!.innerHTML,
                    workHtml = {
                        val encoded = (dom.querySelector(".revision_section .trix-content")?.innerHTML
                                ?: dom.querySelector("#editor_challenge")!!.innerHTML)
                        val elem = document.createElement("textarea")
                        elem.innerHTML = encoded
                        elem.textContent!!
                    }(),
                    handedIn = dom.querySelector(".challenge > a")!!.getAttribute("title")!!.split(",")[0],
                    fileURLs = fileURLs,
                    imageURLs = fileURLs
                            .map {
                                Pair(
                                        it,
                                        (dom.querySelector("a[href=\""+ it +"\"] img") as Element).getAttribute("src")!!
                                )
                            }
                            .toMap()

            )
        }
    }

    val imageFileURLs = fileURLs.filter {
        it.endsWith("jpg") || it.endsWith("jpeg") || it.endsWith("png") || it.endsWith("bmp") || it.endsWith("gif")
    }
    val pdfFileURLs = fileURLs.filter { it.endsWith("pdf") }
}