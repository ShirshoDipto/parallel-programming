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

    ???
  }

  /**
    * Returns the name of the sorting method 
    */
  override def getName = "PooledSort"
}
