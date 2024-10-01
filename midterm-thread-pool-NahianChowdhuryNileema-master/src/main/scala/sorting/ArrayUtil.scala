package sorting

import scala.collection.mutable.Map
import scala.reflect.ClassTag

/**
  * Utility functions for testing implementations with arrays
  */
object ArrayUtil {


  /**
    * Swaps elements with indices `i` and `j` in an array `a`.
    *
    * Ensures that both `i` and `j` are in `a`'s range and throws an
    * exception otherwise.
    */
  def swap[T](a: Array[T], i: Int, j: Int): Unit = {
    // TODO: Implement me according to the specification above

    // Handle exceptions
    if (i < 0 || i >= a.length || j < 0 || j >= a.length) {
      throw new IndexOutOfBoundsException
    }

    // Swap
    val temp = a(i)
    a(i) = a(j)
    a(j) = temp
  }

  /**
    * The name says it all. Returns an array of a given size
    * populated with random integers. 
    */
  def generateRandomArrayOfInts(size: Int): Array[Int] = {
    // TODO: Implement me!
    Array.fill(size)(generateRandomIntBetween(size * -1, size))
  }

  /**
    * Generates a random integer. Both ends are inclusive. 
    */
  def generateRandomIntBetween(low: Int, high: Int): Int = {
    assert(low <= high, "Low end should be less then high end.")
    val r = math.random()
    val range = high - low
    (range * r).toInt + low
  }

  // This is necessary to enable implicit `Ordering` on `Int`s
  // Do not remove this import!
  import Ordering.Implicits._

  /**
    * Check if an array `a` is sorted 
    */
  def checkSorted[T: Ordering](a: Array[T]): Boolean = {
    // TODO: Implement me by relying on the standard comparison operations.
    // Operators like <, >, <= etc are provided by the `Ordering` constraint

    for (i <- 0 until a.length - 1) {
      if (a(i) > a(i + 1)) {
        return false
      }
    }
    true
  }

  /**
    * Check if an array `a` has the same elements as an array `b`
    */
  def checkSameElements[T: Ordering](a: Array[T], b: Array[T]): Boolean = {
    // TODO: Implement in any convenient way
    // TODO: Try to get better-than-O(n^2) implementation

    // Return false if the size are not the same
    if (a.length != b.length) return false

    // Create a map to store counts of each element
    val count = Map.empty[T, Int]

    // Store the elements of `a` and their counts in the map
    for (i <- a) {
      count(i) = if (count.contains(i)) count(i) + 1 else 1
    }

    // Traverse through `b` and compare the elements and its count with the elements of `a`
    for (i <- b) {
      // Return false if the element is not in count or if any element appears more times than in `a`
      if (!count.contains(i) || count(i) == 0) {
        return false
      } else {
        // If element is found, decrement its value in the map
        count(i) -= 1
      }
    }

    true
  }

  /**
    * Copies an array `a` into a new one and returns it
    */
  def arrayCopy[T: ClassTag](a: Array[T]): Array[T] = {
    val aCopy = new Array[T](a.length)
    a.copyToArray(aCopy)
    aCopy
  }

  def main(args: Array[String]): Unit = {
    val _array = generateRandomArrayOfInts(10)
    println(_array.mkString("[", ", ", "]"))

    println(checkSorted(Array(1)))
  }
}
