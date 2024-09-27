package sorting

import sorting.ArrayUtil.swap

/**
  * Simple sequential in-place QuickSort
  */
object SimpleQuickSort extends Sorting {

  // This is necessary to enable implicit `Ordering` on `Int`s
  // Do not remove this import!
  import Ordering.Implicits._

  /**
    * A standard sub-routine of QuickSort that partitions an array
    * into two parts with regard to the pivot 
    * (<= pivot and > picot, correspondingly ).
    */
  def partition[T: Ordering](arr: Array[T], lo: Int, hi: Int): Int = {
    // TODO: Implement partition of a sub-array via a pivot
    // TODO: use `T`'s `<`, `>=` and similar operations to implement comparison 

    // Make a randomized pivot point
    val pivot = ArrayUtil.generateRandomIntBetween(low = lo, high = hi)

    // Put the pivot element at the end
    ArrayUtil.swap(arr, pivot, hi)
    var i = lo
    val pivotElem = arr(hi)

    // Start partitioning
    for (j <- lo until hi) {
      if (arr(j) <= pivotElem) {
        ArrayUtil.swap(arr, i, j)
        i += 1
      }
    }
    ArrayUtil.swap(arr, i, hi)
    i
  }

  def sortHelper[T: Ordering](arr: Array[T], lo: Int, hi: Int): Unit = {
    if (lo >= hi) return

    val pivot = partition(arr, lo, hi)
    sortHelper(arr, lo, pivot - 1)
    sortHelper(arr, pivot + 1, hi)
  }

  def sort[T: Ordering](arr: Array[T]): Unit = {
    // TODO: Implement by relying on the `partition` procedure.
    sortHelper(arr, 0, arr.length - 1)
  }

  /**
    * Returns the name of the sorting method 
    */
  override def getName = "SimpleSort"
}
