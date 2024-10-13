package multiset

import java.util.concurrent.locks.ReentrantLock

import util.ThreadID

/** An implementation of a concurrent multi-set as an optimistic list allowing
  * for duplicates
  */
class OptimisticDupList[T] extends ConcurrentMultiSet[T] {

  // Add sentinels to start and end
  private val head: Node = new Node(Integer.MIN_VALUE)
  head.next = new Node(Integer.MAX_VALUE) // tail

  protected def validate(pred: Node, curr: Node): Boolean = {
    // TODO: Implement me!
//    println(s"Pred: ${pred.item}, Curr: ${curr.item}")
    var entry = head
    while (entry.key <= pred.key) {
      if (entry eq pred) {
        // Checking for reference equality
        return pred.next eq curr
      }
      entry = entry.next
    }
    false
  }

  /** Add another element equal to item
    *
    * @return
    *   true iff element was not there already
    */
  def add(item: T): Boolean = {
    // TODO: Implement me!
    val key = item.hashCode()
    while (true) {
      var pred = this.head
      var curr = pred.next
      while (curr.key < key) {
        pred = curr
        curr = curr.next
      }
      pred.lock()
      curr.lock()
      try {
        if (validate(pred, curr)) {
          val entry = new Node(item)
          entry.next = curr
          pred.next = entry
          return true
        }
      } finally { // always unlock
        pred.unlock()
        curr.unlock()
      }
    }
    true
  }

  /** Remove a single element equal to item
    *
    * @param item
    *   element to remove
    * @return
    *   true iff element's count >= 1
    */
  def remove(item: T): Boolean = {
    // TODO: Implement me!
    val key = item.hashCode()
    while (true) {
      var pred = head
      var curr = pred.next
      while (curr != null && (curr.key != key || curr.item != item)) {
        pred = curr
        curr = curr.next
      }
      if (curr != null) {
        pred.lock()
        try {
          curr.lock()
          try {
            if (validate(pred, curr)) {
              if (curr.key == key && curr.item == item) {
                pred.next = curr.next
                return true
              } else {
                return false
              }
            }
          } finally {
            curr.unlock()
          }
        } finally {
          pred.unlock()
        }
      }
    }
    false
  }

  /** Test whether element's count is >= 1
    */
  def contains(item: T): Boolean = {
    // TODO: Implement me!
//    val key = item.hashCode()
//    var curr = head.next
//
//    while (curr.key <= key) {
//      if (curr.key == key && curr.item == item) {
//        return true
//      }
//      curr = curr.next
//    }
//    false

    val key = item.hashCode()
    var pred = this.head
    var curr = pred.next
    while (curr != null && (curr.key != key || curr.item != item)) {
      pred = curr
      curr = curr.next
    }
    if (curr != null) {
      try {
        pred.lock()
        curr.lock()
        if (validate(pred, curr)) {
          return curr.key == key && curr.item == item
        } else {
          return false
        }
      } finally { // always unlock
        pred.unlock()
        curr.unlock()
      }
    }
    false
  }

  /** A count of items equals to item in the list
    */
  def count(item: T): Int = {
    // TODO: Implement me!
    val key = item.hashCode()
    var count = 0
    var curr = head.next

    while (curr.key <= key) {
      curr.lock()
      try {
        if (curr.key == key && curr.item == item) {
          count += 1
        }
        curr = curr.next
      } finally {
        curr.unlock()
      }
    }
    count
  }

  def printList(): Unit = {
    var _list = List[T]()
    var current = head.next
    while (current.key != Integer.MAX_VALUE) {
      _list = _list :+ current.item
      current = current.next
    }
    println(_list)
  }

  /** list Node
    */
  private class Node(val item: T) {
    private val myLock = new ReentrantLock()

    private var _key: Option[Int] = None

    def key = if (_key.isEmpty) item.hashCode() else _key.get

    @volatile
    var next: Node = _

    def lock(): Unit = myLock.lock()

    def unlock(): Unit = myLock.unlock()

    /** Constructor for sentinel Node
      *
      * @param key
      *   should be min or max int value
      */
    def this(key: Int) {
      this(null.asInstanceOf[T])
      this._key = Some(key)
    }
  }
}
