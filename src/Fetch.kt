import org.w3c.dom.Document
import org.w3c.dom.parsing.DOMParser
import org.w3c.xhr.XMLHttpRequest

class FetchHtml(url: String, val callback: (Document)->Unit, method: String = "GET") {
    val request = XMLHttpRequest()

    init {
        request.open(method, url)
        request.onloadend = {interpret()}
        Log.log("GETting URL: " + url, Log.STATUS_DEBUG)
        request.send()
    }

    private fun interpret() {
        val dom = domParser.parseFromString(request.responseText, "text/html")
        callback(dom)
    }


    companion object {
        private val domParser = DOMParser()
    }
}

class FetchIterator<FROM, TO>(elements: List<FROM>,
                              val buildURL: (FROM) -> String,
                              val transformResult: (FROM, Document) -> TO,
                              val callback: (List<TO>) -> Unit) {

    val mutableElements = elements.toMutableList()
    val results = mutableListOf<TO>()
    init {
        processNextElementOrReturn()
    }

    fun processNextElementOrReturn() {
        if (mutableElements.size > 0) {
            startProcessingNextElement()
        } else {
            callback(results)
        }
    }

    fun startProcessingNextElement() {
        val element = mutableElements.removeAt(0)
        val url = buildURL(element)
        FetchHtml(url, {requestLoaded(element, it)})
    }

    fun requestLoaded(element: FROM, result: Document) {
        val transformed = transformResult(element, result)
        results.add(transformed)
        processNextElementOrReturn()
    }


}