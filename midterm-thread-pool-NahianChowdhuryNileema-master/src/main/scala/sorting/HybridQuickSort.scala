package sorting

import pool.ThreadPool

/**
  * Hybrid sorting. Unleash your creativity here!
  */
object HybridQuickSort extends Sorting {

  /**
    * Returns the name of the sorting method 
    */
  override def getName = "HybridSort"

  def sort[T: Ordering](arr: Array[T]): Unit = {
    // TODO: The Hybrid sorting should take the best out of concurrent and sequential worlds
    //       Feel free to experiment with the design and make sure to describe your intuition

    class Sorter[T: Ordering](arr: Array[T], lo: Int, hi: Int) extends Thread {
      override def run(): Unit = {
        // Get my thread id
        // val id = Thread.currentThread().getId
        SimpleQuickSort.sortHelper(arr, lo, hi)
      }
    }

    // Partition the array
    val pivot = SimpleQuickSort.partition(arr, 0, arr.length - 1)

    val sorter = new Sorter(arr, 0, pivot - 1)
    sorter.start()
    SimpleQuickSort.sortHelper(arr, pivot + 1, arr.length - 1)
    sorter.join()
  }
}
