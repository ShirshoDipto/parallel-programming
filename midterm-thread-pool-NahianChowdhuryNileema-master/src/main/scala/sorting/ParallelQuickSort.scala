package sorting

/**
  * Parallel in-place QuickSort
  */
object ParallelQuickSort extends Sorting {

  /**
    * Returns the name of the sorting method 
    */
  override def getName = "ParallelSort"

  class Sorter[T: Ordering](arr: Array[T], lo: Int, hi: Int) extends Thread {
    override def run(): Unit = {
      // Get my thread id
      // val id = Thread.currentThread().getId
      SimpleQuickSort.sortHelper(arr, lo, hi)
    }
  }

  def sort[T: Ordering](arr: Array[T]): Unit = {
    // TODO: Implement a version of sort that creates concurrent threads for
    //       sorting recursive sub-arrays in parallel.
    // TODO: You might want to reuse some functions from the sequential version

    val pivot = SimpleQuickSort.partition(arr, 0, arr.length - 1)
    val leftSorter = new Sorter(arr, 0, pivot - 1)
    val rightSorter = new Sorter(arr, pivot + 1, arr.length - 1)

    leftSorter.start()
    rightSorter.start()
    leftSorter.join()
    rightSorter.join()
  }
}
