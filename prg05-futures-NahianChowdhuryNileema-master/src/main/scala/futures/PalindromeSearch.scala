package futures

import java.util.concurrent.TimeUnit
import scala.collection.concurrent.TrieMap
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future, Promise}
import scala.util.{Failure, Success}

/** Problem 3:

Solve the following problem using Scala's Futures and Promises. 
For a range of bit integers, find the first prime number in it that is a palindrome, 
or return -1 if no such number exists. To do so, split the range into sub-ranges that
will be searched concurrently by tasks enclosed into Futures. 
To implement cancellation, use a promise: all tasks should check from time to time if the
promise is completed, in which case they should stop their attempts.

Some hints:

* The parameter `workers` determines how many Futures running concurrently should be allocated

* Use `trySuccess` and `tryFailure` of the `Promise` for CAS-like installing of a result into the promise

* A good concurrent HashMap is provided by Scala's TrieMap     

* Think when the palindrome-seeking futures need to synchronize with each other.
  For instance, can futures for "later" ranges announce the results before "earlier" ones?

* Use `BigInt(x).isProbablePrime(10)` to check a number of primality with high probability

* Do not use explicit awaits in the futures!

* That said, a bit of spinning in individual futures (on some variable to be updated) is okay.

 */
object PalindromeSearch {

  private implicit val ec: ExecutionContext = ExecutionContext.Implicits.global

  def firstPalindromePrime(from: Int, upTo: Int,
                           workers: Int = Runtime.getRuntime.availableProcessors()): Future[Int] = {
    // TODO: Implement me!
    if (workers > upTo - from + 1) {
      throw new IllegalArgumentException("Number of workers should be less than the range")
    }

    val p = Promise[Int]()
    val f = p.future

    val range = upTo - from + 1
    val chunkSize = Math.max(1, range / workers)
    val results = new TrieMap[Int, Boolean]()

    val producer = Future {
      println(s"[Producer] promise completed: ${p.isCompleted}")
      val listOfFutures = (0 until workers).map { i =>
        val start = from + i * chunkSize
        val end = Math.min(upTo, start + chunkSize - 1)

        Future {
          println(s"[Producer] Searching for palindrome prime in range $start to $end")
          val result = searchPalindromePrime(start, end, results)
          if (result != -1 && !p.isCompleted) {
            println(s"[Producer] Found palindrome prime: $result")
            p.trySuccess(result)
          }
        }
      }
      Future.sequence(listOfFutures).onComplete {
        case Success(_) => {
          p.trySuccess(-1)
          println(s"[Producer] promise completed: ${p.isCompleted}")
          println("[Producer] Input completed")
        }
        case Failure(e) => {
          p.tryFailure(e)
          println(s"[Producer] promise completed: ${p.isCompleted}")
          println(s"[Producer] Input failed: ${e.getMessage}")
        }
      }
    }

    val consumer = Future {
      println("[Consumer] Waiting for the input\n")
    }.flatMap(_ => {
      while (!p.isCompleted) {}
      println(s"[Consumer] promise completed: ${p.isCompleted}")
      f
    })
    consumer
  }

  private def searchPalindromePrime(from: Int, upTo: Int, results: TrieMap[Int, Boolean]): Int = {
    for (num <- from to upTo) {
      if (isPalindrome(num) && BigInt(num).isProbablePrime(10)) {
        return num
      }
      // TODO: Doesn't seem like we need the TrieMap
//      results.put(num, false)
    }
    -1
  }

  def isPalindrome(n: Int): Boolean = {
    val s = n.toString
    s == s.reverse
  }

  def main(args: Array[String]): Unit = {
    // Use this for testing
    val search = firstPalindromePrime(1000000, 99999999)
    val r = Await.result(search, Duration.create(10, TimeUnit.SECONDS))
    println(r)
  }
}
