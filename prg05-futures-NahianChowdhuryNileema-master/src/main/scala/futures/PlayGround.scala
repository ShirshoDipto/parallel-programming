package futures

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.Source
import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

/** Problem 1
 *


 Using Scala's Futures and Promises, implement three functions:

 * `askForUrl(): Future[String]` asks the user for a URL
 * `fetchUrl(url: String): Future[String]` reads the web page at that URL
 * `getLinks(doc: String): Future[List[String]]` returns all the hyperlinks from the text of the page

 Each function should use a separate Future for each of these three steps. Test your result in the `main` method

 Hints:

 * Make sure to handle possible failures via `recover` method

 * In case of getting `java.nio.charset.MalformedInputException`, check this post:
 https://stackoverflow.com/questions/29987146/using-result-from-scalas-fromurl-throws-exception

 * You can detect links via scala regular expressions:

 val pattern: Regex = """href="(http[^"]+)"""".r // Defines the pattern
  Next, find out (e.g., via Google) how to find regex patterns in Scala strings


 */
object PlayGround {

  private implicit val ec: ExecutionContext = ExecutionContext.Implicits.global

  def getLinks(doc: String): Future[List[String]] = Future {
    // TODO: Implement me!
    val pattern: Regex = """href="(http[^"]+)"""".r
    val links = pattern.findAllMatchIn(doc).map(_.group(1)).toList
    println(f"Found ${links.length} links")
    println(s"Links: ${links.mkString("\n")}\n")
    links
  }

  def fetchUrl(url: String): Future[String] = Future {
    // TODO: Implement me!
    println(f"Fetching $url")
    val f = Source.fromURL(url)("ISO-8859-1")
    try {
      val content = f.mkString
      println(f"\nSuccessfully fetched $url")
      println(f"Content length: ${content.length}")
      content
    } finally {
      f.close()
    }
  }.recover({
    case ex => {
      println(s"Failed to fetch $url")
      println(ex.getMessage)
      throw ex
    }
  })

  def askForUrl(): Future[String] = Future {
    // TODO: Implement me!
    println("Enter a valid URL:")
    scala.io.StdIn.readLine()
  }

  def runOperation(url: String): Future[List[String]] = {
    for {
      content <- fetchUrl(url)
      links <- getLinks(content)
    } yield links
  }

  def main(args: Array[String]): Unit = {
    // TODO: Implement me (concisely)!
    val callback: Try[List[String]] => Unit = {
      case Success(value) =>
        println(s"Success...")
      case Failure(exception) =>
        println(s"Failure: ${exception.toString}")
    }

    val f: Future[List[String]] = runOperation("https://www.google.com")

    f.onComplete(callback)
    Await.result(f, Duration.create(10, TimeUnit.SECONDS))
  }
}