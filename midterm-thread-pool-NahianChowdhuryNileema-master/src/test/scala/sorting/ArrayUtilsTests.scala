package sorting
import org.scalatest.{FunSpec, Matchers}

class ArrayUtilsTests extends FunSpec with Matchers{

  describe(s"Tests the utilities related to sorting") {
    it ("Checks if the array is sorted") {
      val testCases = Seq(
        (Array(1, 2, 3, 4, 5), true),
        (Array(5, 4, 3, 2, 1), false),
        (Array(1, 1, 1, 1, 1), true),
        (Array(1, 2, 2, 3, 4), true),
        (Array(1, 3, 2, 4, 5), false),
        (Array.empty[Int], true),
        (Array(1), true)
      )

      for ((array, expected) <- testCases) {
        val result = ArrayUtil.checkSorted(array)
        assert(result == expected, s"Test failed for input: ${array.mkString(",")}. Expected: $expected, Got: $result")
      }
      println("checkSorted: All test cases passed!")
    }

    it ("Verifies whether two arrays have the same elements") {
      val testCases = Seq(
        (Array(1, 2, 3), Array(3, 2, 1), true),
        (Array(1, 2, 2), Array(2, 1, 2), true),
        (Array(1, 2, 3), Array(1, 2, 2), false),
        (Array(1, 2, 3), Array(1, 2, 3, 4), false),
        (Array.empty[Int], Array.empty[Int], true),
        (Array(1), Array(1), true),
        (Array(1), Array(2), false)
      )

      for ((a, b, expected) <- testCases) {
        val result = ArrayUtil.checkSameElements(a, b)
        assert(result == expected, s"Test failed for input: (${a.mkString(",")}, ${b.mkString(",")}). Expected: $expected, Got: $result")
      }
      println(s"checkSameElements: All test cases passed!")
    }
  }
}
