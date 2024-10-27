file://<WORKSPACE>/src/main/scala/futures/LinkExtractor.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 2083
uri: file://<WORKSPACE>/src/main/scala/futures/LinkExtractor.scala
text:
```scala
package futures

import java.util.concurrent.TimeUnit

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.io.Source
import scala.util.matching.Regex

/** Problem 1


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
object LinkExtractor {

  private implicit val ec: ExecutionContext = ExecutionContext.Implicits.global

  def getLinks(doc: String): Future[List[String]] = {
    // TODO: Implement me!
    val pattern: Regex = """href="(http[^"]+)"""".r
    pattern.findAllMatchIn(doc).map(_.group(1)).toList
  }

  def fetchUrl(url: String): Future[String] = {
    // TODO: Implement me!
    println(f"Fetching $url")

    val f = Source.fromURL(url, "UTF-8")
    try {
      val content = f.mkString
    } finally {
      f.close()
    }
    // Try(Source.fromURL(url, "UTF-8").mkString) match {
    //   case Success(content) => content
    //   case Failure(e) => throw new RuntimeException(s"Failed to fetch URL: $url", e)
    // }
  }

  def askForUrl(): Future[String] = Future {
    // TODO: Implement me!
    println("Enter a valid URL:")
    scala.io.StdIn.readLine()
  }

  def main(args: Array[String]): Unit = {
    // TODO: Implement me (concisely)!
    val callback: Try[[String@@]] => Unit = {
      case Success(value) =>
        println(s"Success: ${value}")
      case Failure(exception) =>
        println(s"Failure: ${exception.toString}")
    }

    val f: Future[List[String]] = for {
      url <- askForUrl()
      content <- fetchUrl(url)
      links <- getLinks(content)
    } yield links

    f.onComplete(callback)

    val links = Await.result(f, Duration.create(10, TimeUnit.SECONDS))
    println(links.mkString("\n"))
  }


}
```



#### Error stacktrace:

```
dotty.tools.dotc.core.SymDenotations$NoDenotation$.owner(SymDenotations.scala:2607)
	scala.meta.internal.pc.SignatureHelpProvider$.isValid(SignatureHelpProvider.scala:83)
	scala.meta.internal.pc.SignatureHelpProvider$.notCurrentApply(SignatureHelpProvider.scala:94)
	scala.meta.internal.pc.SignatureHelpProvider$.$anonfun$1(SignatureHelpProvider.scala:48)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile(LinearSeq.scala:280)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile$(LinearSeq.scala:278)
	scala.collection.immutable.List.dropWhile(List.scala:79)
	scala.meta.internal.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:48)
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:435)
```
#### Short summary: 

java.lang.AssertionError: NoDenotation.owner