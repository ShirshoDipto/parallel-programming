package futures

import futures.PalindromeSearch.{firstPalindromePrime, isPalindrome}
import org.scalatest.AsyncFlatSpec

/**
  * @author Ilya Sergey
  */
class PalindromeTests extends AsyncFlatSpec {
  
  behavior of "PalindromeSearch"
  it should "correctly find palindromes in range where they exist" in {
    // TODO: Implement me!
    //       Please, try some large numbers
    val start = 10000000
    val end = 999999999
    val workers = 12
    val f = firstPalindromePrime(start, end, workers)
    for (pp <- f) yield {
      println(s"\nPalindrome prime: $pp\n")
      assert(start <= pp && pp <= end)
    }
  }

  it should "Report failure if there is no palindrome" in {
    // TODO: Implement me!
    val f = firstPalindromePrime(1000, 10000)
    for (pp <- f) yield {
      assert(pp == -1)
    }
  }
}
