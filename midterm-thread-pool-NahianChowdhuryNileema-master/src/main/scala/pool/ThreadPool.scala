package pool

import java.util.concurrent.locks.ReentrantLock
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
    var workerList = Array.empty[Worker]
    for (i <- 0 until n) {
      workerList = workerList :+ new Worker(i)
    }
    workerList
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
  private val lock = new ReentrantLock()
  private val taskAdded = lock.newCondition()

  {
    // TODO: Don't forget to start all the pooled threads when creating the object
  }

  private class Worker(val id: Int) extends Thread {
    @volatile var hasStarted = false

    override def run(): Unit = {
      hasStarted = true
      // Next task
      var task: Unit => Unit = null
      val i = util.ThreadID.get

      try {
        while (true /* TODO: What's the condition here? */) {
          // TODO: [prelude] try to fetch the new task from the task queue
          //       This part requires mutually exclusive access to the ThreadPool state to check
          //       the status of the queue
          //       A thread can be interrupted while waiting, so implement the graceful shutdown.
          //       Do we need some extra bookkeeping?

          lock.lock()
          while (taskQueue.isEmpty) {
            taskAdded.await()
          }

          task = taskQueue.dequeue()
          lock.unlock()

          // TODO: [body] work on the task.
          // Execute the task
          workInProgress(id) = true
          val result: Any = task()

          workInProgress(id) = false
          // TODO: [epilogue] Mark the thread as awaiting the next task
          //       Beware of deadlocks (or, in this case, wait-locks :-).

          // TODO: The process now repeats, hence the while-loop?
        }
      } catch {
        case e: InterruptedException => // TODO: What should we catch there?
          println(s"Thread $i has been interrupted, terminating now.")
      } finally {
        workInProgress(id) = false
        hasStarted = false
//        lock.unlock()
      }
    }
  }

//  private class AsyncHandler(task: Unit => Unit) extends Thread {
//    override def run(): Unit = {
//      task()
//    }
//  }

  /**
    * Shuts down the thread pool by interrupting all its worker threads.
    * After this the thread pool is no longer usable.
    */
  def shutdown(): Unit = {
    if (isShutDown) {
      println(s"Thread pool is already shut down")
      return
    }

    // TODO: implement via `interrupt()` method of a Thread class
    for (worker <- workers) {
      worker.interrupt()
    }

    isShutDown = true
  }


  /**
    * Schedule a new task for execution by some thread in the thread pool.
    * `async()` does not block the caller, but neither does it guarantee that
    * the task will be completed before `async()` returns.
    *
    * @param task a task to execute concurrently
    */
  def async(task: Unit => Unit): Unit = {
    // TODO: Implement me!
    //       What's going to happen if the pool is shut down already?
    //       How do we handle the task queue?
    //       How does this method interact with worker threads, letting them know
    //       that a new task has been enqueued?

    if (isShutDown) throw ThreadPoolException("Thread pool is no longer active")

    // Start all the workers if they are not already started
    for (worker <- workers) {
      Thread.sleep(1) // NOTE: This is preventing the terminated threads to start again
      if (!worker.hasStarted) {
        worker.start()
      }
    }

    lock.lock()
    try {
      taskQueue.enqueue(task)
      taskAdded.signal()
    } catch {
      case e: IllegalThreadStateException => println(s"Error: ${e.getMessage}")
    } finally {
      lock.unlock()
    }
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

    if (isShutDown) isShutDown = false

    // Wait for this thread to finish
    try {
      task()
      while (workInProgress.contains(true)) {}
    } catch {
      case e: Exception => {
        println(s"Error: ${e.getMessage}")
        throw ThreadPoolException(e.getMessage)
      }
    }
  }

}

/**
  * An exception used to indicate the invalid state of this thread pool 
  */
case class ThreadPoolException(msg: String) extends Exception(msg)
