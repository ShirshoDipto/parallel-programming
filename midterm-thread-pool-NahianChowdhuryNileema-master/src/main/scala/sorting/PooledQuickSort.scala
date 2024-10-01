package sorting

import pool.ThreadPool

/**
  * In-place QuickSort using Thread Pool
  */
object PooledQuickSort extends Sorting {

  def sort[T: Ordering](arr: Array[T]): Unit = {
    // TODO: Implement a version of sort that, instead of spawning/joining new threads
    //       allocates tasks in the Thread Pool. 
    
    // TODO: Use `startAndWait()` to make sure that the task (and its sub-tasks) are completed
    //       by the time this call returns
    
    // TODO: Use `async()` to allocate create more non-blocking tasks.

    // TODO: Do not forget to shut down the pool after the array is sorted

    val pool = new ThreadPool(4)

    def sortHelper(start: Int, end: Int): Unit = {
      if (start < end) {
        val pivot = SimpleQuickSort.partition(arr, start, end)
        pool.async(_ => sortHelper(start, pivot - 1))
        pool.async(_ => sortHelper(pivot + 1, end))
      }
    }

    def runIncrements(): Unit = {
      sortHelper(0, arr.length - 1)
    }

    try {
      pool.startAndWait(_ => runIncrements())
    } finally {
      pool.shutdown()
      println(s"${getName} Done.")
    }

//    --------------- Alternate Implementation -----------------
    //    // Partition the array
    //    val pivot = SimpleQuickSort.partition(arr, 0, arr.length - 1)
    //
    //    // Create an async task with the left partition
    //    // Create another async task with the right partition
    //
    //    def sortingTask(): Unit = {
    //      pool.async(_ => SimpleQuickSort.sortHelper(arr, 0, pivot - 1))
    //      pool.async(_ => SimpleQuickSort.sortHelper(arr, pivot + 1, arr.length - 1))
    //    }
    //
    //    try {
    //      pool.startAndWait(_ => sortingTask())
    //    } finally {
    //      // Let's not forget to clean up by shutting down all the threads
    //      pool.shutdown()
    //      println(s"${getName} Done.")
    //    }
  }

  /**
    * Returns the name of the sorting method
    */
  override def getName = "PooledSort"
}
