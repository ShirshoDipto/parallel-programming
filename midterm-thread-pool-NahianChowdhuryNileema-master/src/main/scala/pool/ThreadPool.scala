package pool

import scala.collection.mutable

/**
  * A simplistic thread pool implementation
  */
class ThreadPool(private val n: Int) {

  // A queue of pending tasks to be implemented
  // TODO: Notice that this is a simple sequential (non-concurrent) queue.
  //       Can you explain why it is made this way?
  private val taskQueue = new mutable.Queue[Unit => Unit]()

  // An array of worker threads that are allocated to execute tasks in parallel.
  private val workers: Array[Worker] = {
    // TODO: Populate the array by the Worker thread instances 
    //       with the id's corresponding to their positions in the array
    ???
  }

  // An array of flags indicating which threads are currently active
  // TODO: Notice that this is a sequential, non-atomic array. This is intentional.
  //       Can you explain why?
  private val workInProgress = Array.fill(n)(false)

  // A flag indicating that the thread-pool is no longer usable
  // It is annotated as @volatile. Make sure to use it as such.
  @volatile
  private var isShutDown = false

  // TODO: feel free to add other fields if you need them

  {
    // TODO: Don't forget to start all the pooled threads when creating the object
  }



  private class Worker(val id: Int) extends Thread {
    override def run() = {
      // Next task
      var task: Unit => Unit = null

      try {
        while (??? /* RODO: What's the condition here? */) {
          // TODO: [prelude] try to fetch the new task from the task queue
          //       This part requires mutually exclusive access to the ThreadPool state to check
          //       the status of the queue
          //       A thread can be interrupted while waiting, so implement the graceful shutdown.
          //       Do we need some extra bookkeeping?
          
          // TODO: [body] work on the task.
          
          // TODO: [epilogue] Mark the thread as awaiting the next task
          //       Beware of deadlocks (or, in this case, wait-locks :-).
          
          // TODO: The process now repeats, hence the while-loop?
        }
      } catch {
        case _ => // TODO: What should we catch there?
        // println(s"Thread $i is now stopped.")
      }

    }
  }

  

  /**
    * Shuts down the thread pool by interrupting all its worker threads.
    * After this the thread pool is no longer usable.
    */
  def shutdown(): Unit = {
    if (isShutDown) throw ThreadPoolException("Thread pool is no longer active")
    // TODO: implement via `interrupt()` method of a Thread class
  }


  /**
    * Schedule a new task for execution by some thread in the thread pool.
    * `async()` doe not block the caller, but neither does it guarantee that 
    * the task will be completed before `async()` returns.
    *
    * @param task a task to executre concurrently
    */
  def async(task: Unit => Unit): Unit = {
    // TODO: Implement me!
    //       What's going to happen if the pool is shut down already?
    //       How do we handle the task queue?
    //       How does this method interact with worker threads, letting them know
    //       that a new task has been enqueued?
  }


  /**
    * Takes an initial tasks and blocks until all threads in the pool finish their work.
    * That is, the other threads may allocate future tasks. Yet, the thread that
    * invoked this method will be blocked until the process of creating tasks
    * and completing them reaches a "quiescent moment". Make sure to understand how
    * the "quiescence" for the thread pool is defined.
    *
    * @param task an initial task to executed by some thread in the pool
    */
  def startAndWait(task: Unit => Unit): Unit = {
    // TODO: Implement according to the specification.
    //       How would we know when to unblock?   
    //       Handle if is in `isShutDown` mode.
  }

}

/**
  * An exception used to indicate the invalid state of this thread pool 
  */
case class ThreadPoolException(msg: String) extends Exception(msg)
