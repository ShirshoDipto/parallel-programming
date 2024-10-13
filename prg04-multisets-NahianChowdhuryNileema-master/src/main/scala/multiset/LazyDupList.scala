package multiset

import java.util.concurrent.locks.ReentrantLock

/**
  * An implementation of a concurrent multi-set  
  * as a lazy list allowing for duplicates  
  */
class LazyDupList[T] extends ConcurrentMultiSet[T] {

  private val head: Node = new Node(Integer.MIN_VALUE)
  head.next = new Node(Integer.MAX_VALUE)

  protected def validate(pred: Node, curr: Node): Boolean = {
    // TODO: Implement me!
    !pred.marked && !curr.marked && (pred.next eq curr)
  }

  /**
    * Add another element equal to item
    *
    * @return true iff element was not there already
    */
  def add(item: T): Boolean = {
    // TODO: Implement me!
    val key = item.hashCode
    while (true) {
      var pred = this.head
      var curr = head.next
      while (curr.key < key) {
        pred = curr
        curr = curr.next
      }
      pred.lock()
      try {
        curr.lock()
        try {
          if (validate(pred, curr)) {
            val Node = new Node(item)
            Node.next = curr
            pred.next = Node
            return true
          }
        } finally {
          curr.unlock()
        }
      } finally pred.unlock()
    }
    true
  }

  /**
    * Remove a single element equal to item
    *
    * @param item element to remove
    * @return true iff element's count >= 1
    */
  def remove(item: T): Boolean = {
    // TODO: Implement me!
    val key = item.hashCode
    while (true) {
      var pred = this.head
      var curr = head.next
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
              if (curr.key != key || curr.item != item) return false
              else { // absent
                curr.marked = true // logically remove
                pred.next = curr.next // physically remove
                return true
              }
            }
          } finally {
            curr.unlock()
          }
        } finally {
          // always unlock pred
          pred.unlock()
        }
      }
    }
    false
  }

  /**
    * Test whether element's count is >= 1
    */
  def contains(item: T): Boolean = {
    // TODO: Implement me!
    val key = item.hashCode
    var curr = this.head
    while (curr != null && (curr.key != key || curr.item != item)) {
      curr = curr.next
    }
    if (curr != null) {
      curr.key == key && !curr.marked && curr.item == item
    } else {
      false
    }
  }

  override def count(item: T): Int = {
    val key = item.hashCode()
    var count = 0
    var curr = head.next

    while (curr.key <= key) {
      if (curr.key == key && !curr.marked && curr.item == item) {
        count += 1
      }
      curr = curr.next
    }
    count
  }

  def printList(): Unit = {
    // TODO: Implement me!
    var _list = List[T]()
    var current = head.next
    while (current.key != Integer.MAX_VALUE) {
      _list = _list :+ current.item
      current = current.next
    }
    println(_list)
  }

  /**
    * list Node
    */
  private class Node(val item: T) {
    private val myLock = new ReentrantLock()

    private var _key: Option[Int] = None

    def key = if (_key.isEmpty) item.hashCode() else _key.get

    @volatile
    var next: Node = _
    var marked: Boolean = false

    /**
      * Constructor for sentinel Node
      *
      * @param key should be min or max int value
      */
    def this(key: Int) {
      this(null.asInstanceOf[T])
      this._key = Some(key)
    }

    def lock(): Unit = myLock.lock()

    def unlock(): Unit = myLock.unlock()

  }

}