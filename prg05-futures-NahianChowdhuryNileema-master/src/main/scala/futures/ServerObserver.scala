package futures

import java.net.URL
import java.util.concurrent.TimeUnit

import scala.collection.immutable.Map
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Try
 
/** Problem 2

Implement a method `getLinkHttpServerCounts(url: String): Future[Map[String, Int]]`,
which, given a URL, reads the web page at that URL, finds all the hyperlinks, visits each of them concurrently, 
and locates the Server HTTP header for each of them. It then collects a map of which servers were
found how often. Use futures for visit each page and return its Server header.   
  
Hints:

* You may reuse some of the functionality from Problem 1

* Feel free to use the utility functions `fetchServerName` and `toUrl`. 
  
* Use `Future.sequence` utility method to combine a sequence of Futures
  into a future returning sequence.     
  
  */
object ServerObserver {

  private implicit val ec: ExecutionContext = ExecutionContext.Implicits.global

  import LinkExtractor._

  def getLinkHttpServerCounts(url: String): Future[Map[String, Int]] = {
    for {
      content <- fetchUrl(url)
      links <- getLinks(content)
      serverNames <- getServerNames(links)
    } yield {
      var mapOfServerNames = Map.empty[String, Int]
      for (name <- serverNames if name.isDefined) {
        val serverName = name.get
        if (mapOfServerNames.contains(serverName)) {
          val value = mapOfServerNames(serverName)
          mapOfServerNames = mapOfServerNames + (serverName -> (value + 1))
        } else {
          mapOfServerNames = mapOfServerNames + (serverName -> 1)
        }
      }
      mapOfServerNames
    }
  }.recover({
    case e: Exception =>
      println(s"Failed to get server names: ${e.getMessage}")
      Map.empty
  })

  private def getServerNames(links: List[String]): Future[List[Option[String]]] = {
    val listOfFutures: List[Future[Option[String]]] = links.map { link =>
      Future {
        val urlTry = toUrl(link)
        val serverName = fetchServerName(urlTry.get)
        serverName
      }
    }
    Future.sequence(listOfFutures)
  }

  private def fetchServerName(url: URL): Option[String] = {
    println(s"Fetching header for $url")
    val name = Option(url.openConnection().getHeaderField("Server"))
    if (name.isDefined) println(s"$url uses server: ${name.get}")
    else println(s"$url does not expose server name")
    name
  }

  def toUrl(link: String): Try[URL] = {
    Try(new URL(link.stripPrefix("href=\"").stripSuffix("\"")))
  }


  def main(args: Array[String]): Unit = {
    // TODO: Implement me (concisely)!
    val f: Future[Map[String, Int]] = getLinkHttpServerCounts("https://www.wikipedia.org ")

    val map = Await.result(f, Duration.create(20, TimeUnit.SECONDS))
//    println(s"gws servers: ${map.getOrElse("gws", 0)}")
//    println(s"ESF servers: ${map.getOrElse("ESF", 0)}")
//    println(s"sffe servers: ${map.getOrElse("sffe", 0)}")
    println(map.toList.mkString("\n"))
  }


}