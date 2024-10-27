package futures

import futures.ServerObserver.getLinkHttpServerCounts
import futures.LinkExtractor.{askForUrl, fetchUrl, getLinks}
import org.scalatest.AsyncFlatSpec

import java.util.concurrent.TimeUnit
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success, Try}

/**
  * @author Ilya Sergey
  */
class UrlTests extends AsyncFlatSpec {
  
  behavior of "LinkExtractor"
  
  it should "correctly find some expected links" in {
    // TODO: Implement me!
    //       Try some well-known URLs

    val urls: List[String] = List(
      "https://www.scala-lang.org/api/2.12.4/scala/io/Source$.html",
      "https://google.com",
      "https://www.github.com",
      "https://www.stackoverflow.com",
      "https://www.reddit.com",
      "https://www.wikipedia.org"
    )

    val listOfFutures: List[Future[List[String]]] = urls.map { url =>
      for {
        content <- fetchUrl(url)
        links <- getLinks(content)
      } yield {
        links
      }
    }

    val results: Future[List[List[String]]] = Future.sequence(listOfFutures)
    for (links <- results) yield {
      assert(links(0).nonEmpty)
      assert(links(1).nonEmpty)
      assert(links(2).nonEmpty)
      assert(links(3).nonEmpty)
      assert(links(4).nonEmpty)
      assert(links(5).nonEmpty)
    }
  }

  it should "Fail gracefully" in {
    // TODO: Implement me!
    val urls: List[String] = List(
      "https://www.example-scala-docs.com/api/2.13.8/scala/concurrent/Future.html",
      "https://www.modon-vudai.com",
      "https://www.baler-pub.com",
      "https://www.stack-bainchod.com",
      "https://www.fictional-social-media.com",
      "https://www.lotir-sawal-encyclopedia.org"
    )

    val listOfFutures: List[Future[List[String]]] = urls.map { url =>
      for {
        content <- fetchUrl(url)
        links <- getLinks(content)
      } yield {
        links
      }
    }

    val results: Future[List[List[String]]] = Future.sequence(listOfFutures)
    for (links <- results) yield {
      println(links)
      assert(links(0).isEmpty)
      assert(links(1).isEmpty)
      assert(links(2).isEmpty)
      assert(links(3).isEmpty)
      assert(links(4).isEmpty)
      assert(links(5).isEmpty)
    }

  }

  behavior of "ServerObserver"

  it should "correctly find expected servers" in {
    // TODO: Implement me!
    //       Try some well-known URLs

    val urls: List[String] = List(
//      "https://www.scala-lang.org/api/2.12.4/scala/io/Source$.html",
      "https://google.com",
//      "https://www.github.com",
      "https://www.stackoverflow.com",
//      "https://www.reddit.com",
      "https://www.wikipedia.org"
    )

    val listOfFutures: List[Future[Map[String, Int]]] = urls.map { url =>
      for {
        servers <- getLinkHttpServerCounts(url)
      } yield {
        servers
      }
    }

    val results: Future[List[Map[String, Int]]] = Future.sequence(listOfFutures)
    for (links <- results) yield {
      println(links.mkString("\n"))

      // google.com servers
      assert(links(0)("gws") > 0)
      assert(links(0)("ESF") > 0)
      assert(links(0)("sffe") > 0)

      // github.com servers

      // stackoverflow.com servers
      assert(links(1)("cloudflare") > 0)
      assert(links(1)("Netlify") > 0)
      assert(links(1)("tsa_m") > 0)

      // reddit.com servers
//      assert(links(1)("snooserv") > 0)
//      assert(links(1)("cloudflare") > 0)

      // wikipedia.org servers
      assert(links(2)("daiquiri/5") > 0)
      assert(links(2)("ATS/9.2.5") > 0)
      assert(links(2)("cloudflare") > 0)
      assert(links(2)("Mastodon") > 0)
    }
  }

  it should "Fail gracefully" in {
    // TODO: Implement me!

    val urls: List[String] = List(
      //      "https://www.scala-lang.org/api/2.12.4/scala/io/Source$.html",
      "httpz://google.com",
      //      "https://www.github.com",
      "https://www.bolder-golpo.com",
      //      "https://www.reddit.com",
      //      "https://www.wikipedia.org"
    )

    val listOfFutures: List[Future[Map[String, Int]]] = urls.map { url =>
      for {
        servers <- getLinkHttpServerCounts(url)
      } yield {
        servers
      }
    }

    val results: Future[List[Map[String, Int]]] = Future.sequence(listOfFutures)
    for (links <- results) yield {
      println(links)

      // google.com servers
      assert(links(0).isEmpty)
//      assert(links(0)("ESF") > 0)
//      assert(links(0)("sffe") > 0)

      // stackoverflow.com servers
      assert(links(1).isEmpty)
//      assert(links(1)("Netlify") > 0)
//      assert(links(1)("tsa_m") > 0)
    }
  }

  
}
