package multiset

import util.ThreadID
import scala.util.Random
import org.scalatest.{FunSpec, Matchers}

/** @author
  *   Ilya Sergey
  */
trait HashCollisionTests extends FunSpec with Matchers {

  def mkCollisionFreeSet: ConcurrentMultiSet[MyObject]

  describe(s"Concurrent multi-set ${this.getClass.getName}") {
    it("should handle different elements with same hashes") {
      // TODO: Implement me by overloading .hashCode in MyObject
      ThreadID.reset()
      val printStats = true

      val NUM_THREADS = 4
      val INPUT_SIZE = 6000
      val set = mkCollisionFreeSet

      val inputs = (for (i <- 0 until NUM_THREADS) yield {
        // Create a list whose size is INPUT_SIZE and fill it with
        // random numbers between 1 and 10.
        // This makes sure that the list has duplicates
        List.fill(INPUT_SIZE)(new MyObject(i, Random.nextInt(10) + 1))
      }).toList

      val adders = for (in <- inputs) yield new Adder(set, in)
      val checkers = for (in <- inputs) yield new Checker(set, in)
      val remover = new Remover(set, inputs.flatten)

      val t1 = System.currentTimeMillis()
      for (t <- adders) t.start()
      for (t <- adders) t.join()

      for (t <- remover :: checkers) t.start()
      for (t <- remover :: checkers) t.join()
      val t2 = System.currentTimeMillis()

      set.printList()

      val allWitnessed = checkers.map(_.witnessed).toSet.flatten
      val allRemoved = remover.removed

      for (r <- allRemoved) {
        assert(!set.contains(r))
      }

      for (r <- allWitnessed if !allRemoved.contains(r)) {
        assert(set.contains(r))
      }

      val input = inputs.flatten
      for (e <- input) {
        assert(
          set.contains(e) && !allRemoved.contains(e) ||
            allRemoved.contains(e) && !set.contains(e)
        )
      }

      val formatter = java.text.NumberFormat.getIntegerInstance

      val timeAdd = adders.map(_.time).sum
      val timeRemove = remover.time
      val timeCheck = checkers.map(_.time).sum

      if (printStats) {
        println()
        println(s"Statistics for ${set.getClass.getName}, multi-set:")
        println(s"Number of threads: ${NUM_THREADS * 2 + 1}")
        println(s"Input size:        ${input.size}")
        println(s"Adding time:       ${formatter.format(timeAdd)} ms")
        println(s"Removing time:     ${formatter.format(timeRemove)} ms")
        println(s"Checking time:     ${formatter.format(timeCheck)} ms")
        println(s"Total time:        ${formatter.format(t2 - t1)} ms")
      }
    }
  }

  describe(s"Concurrent multi-set ${this.getClass.getName}") {
    it("should handle adding and removing at the same time") {
      // TODO: Implement me by overloading .hashCode in MyObject
      ThreadID.reset()
      val printStats = true

      val NUM_THREADS = 4
      val INPUT_SIZE = 6000
      val set = mkCollisionFreeSet

      val inputs = (for (i <- 0 until NUM_THREADS) yield {
        // Create a list whose size is INPUT_SIZE and fill it with
        // random numbers between 1 and 10.
        // This makes sure that the list has duplicates
        List.fill(INPUT_SIZE)(new MyObject(i, Random.nextInt(10) + 1))
      }).toList

      val adders = for (in <- inputs) yield new Adder(set, in)
      val checkers = for (in <- inputs) yield new Checker(set, in)
      val remover = new Remover(set, inputs.flatten)

      val t1 = System.currentTimeMillis()
      for (t <- adders) t.start()
      for (t <- remover :: checkers) t.start()

      for (t <- adders) t.join()
      for (t <- remover :: checkers) t.join()
      val t2 = System.currentTimeMillis()

      set.printList()

      val allWitnessed = checkers.map(_.witnessed).toSet.flatten
      val allRemoved = remover.removed

      for (r <- allRemoved) {
        assert(!set.contains(r))
      }

      for (r <- allWitnessed if !allRemoved.contains(r)) {
        assert(set.contains(r))
      }

      val input = inputs.flatten
      for (e <- input) {
        assert(
          set.contains(e) && !allRemoved.contains(e) ||
            allRemoved.contains(e) && !set.contains(e)
        )
      }

      val formatter = java.text.NumberFormat.getIntegerInstance

      val timeAdd = adders.map(_.time).sum
      val timeRemove = remover.time
      val timeCheck = checkers.map(_.time).sum

      if (printStats) {
        println()
        println(s"Statistics for ${set.getClass.getName}, multi-set:")
        println(s"Number of threads: ${NUM_THREADS * 2 + 1}")
        println(s"Input size:        ${input.size}")
        println(s"Adding time:       ${formatter.format(timeAdd)} ms")
        println(s"Removing time:     ${formatter.format(timeRemove)} ms")
        println(s"Checking time:     ${formatter.format(timeCheck)} ms")
        println(s"Total time:        ${formatter.format(t2 - t1)} ms")
      }
    }
  }

  describe(s"Concurrent multi-set ${this.getClass.getName}") {
    it("should handle adding and removing at the same time. Multiple adders, removers, and checkers") {
      // TODO: Implement me by overloading .hashCode in MyObject
      ThreadID.reset()
      val printStats = true

      val NUM_THREADS = 4
      val INPUT_SIZE = 6000
      val set = mkCollisionFreeSet

      val inputs = (for (i <- 0 until NUM_THREADS) yield {
        // Create a list whose size is INPUT_SIZE and fill it with
        // random numbers between 1 and 10.
        // This makes sure that the list has duplicates
        List.fill(INPUT_SIZE)(new MyObject(i, Random.nextInt(10) + 1))
      }).toList

      val adders = for (in <- inputs) yield new Adder(set, in)
      val checkers = for (in <- inputs) yield new Checker(set, in)
      val remover = for (in <- inputs) yield new Remover(set, in)

      val t1 = System.currentTimeMillis()
      for (t <- adders) t.start()
      for (t <- remover) t.start()
      for (t <- checkers) t.start()

      for (t <- adders) t.join()
      for (t <- remover) t.join()
      for (t <- checkers) t.join()
      val t2 = System.currentTimeMillis()

      set.printList()

      val allWitnessed = checkers.map(_.witnessed).toSet.flatten
      val allRemoved = remover.flatMap(_.removed).toSet

      for (r <- allRemoved) {
        assert(!set.contains(r))
      }

      for (r <- allWitnessed if !allRemoved.contains(r)) {
        assert(set.contains(r))
      }

      val input = inputs.flatten
      for (e <- input) {
        assert(
          set.contains(e) && !allRemoved.contains(e) ||
            allRemoved.contains(e) && !set.contains(e)
        )
      }

      val formatter = java.text.NumberFormat.getIntegerInstance

      val timeAdd = adders.map(_.time).sum
      val timeRemove = remover.map(_.time).sum
      val timeCheck = checkers.map(_.time).sum

      if (printStats) {
        println()
        println(s"Statistics for ${set.getClass.getName}, multi-set:")
        println(s"Number of threads: ${NUM_THREADS * 2 + 1}")
        println(s"Input size:        ${input.size}")
        println(s"Adding time:       ${formatter.format(timeAdd)} ms")
        println(s"Removing time:     ${formatter.format(timeRemove)} ms")
        println(s"Checking time:     ${formatter.format(timeCheck)} ms")
        println(s"Total time:        ${formatter.format(t2 - t1)} ms")
      }
    }
  }

  describe(s"Concurrent multi-set ${this.getClass.getName}") {
    it(s"should count the duplicates correctly for ${this.getClass.getName}") {
      ThreadID.reset()

      val NUM_THREADS = 4
      val INPUT_SIZE = 6000
      val set = mkCollisionFreeSet

      val inputs = (for (i <- 0 until NUM_THREADS) yield {
        // Create a list whose size is INPUT_SIZE and fill it with
        // random numbers between 1 and 10.
        // This makes sure that the list has duplicates
        List.fill(INPUT_SIZE)(new MyObject(i, Random.nextInt(10) + 1))
      }).toList

      val adders = for (in <- inputs) yield new Adder(set, in)
      for (t <- adders) t.start()
      for (t <- adders) t.join()

      // Count the duplicates
      val duplicateCounts = inputs.flatten.groupBy(_.getValue).mapValues(_.size)
      // Check duplicates using the count method
      for ((k, v) <- duplicateCounts) {
        assert(set.count(new MyObject(0, k)) == v)
      }
    }
  }

  // A class used to test multi-set in the presence of hash-code collisions
  class MyObject(val id: Int, val value: Int) {
    override def hashCode(): Int = {
      // TODO: Implement me
      value % 10 == 0 match {
        case true => 1
        case false => value
      }
    }

    def getID: Int = id
    def getValue: Int = value

    override def equals(obj: Any): Boolean = obj match {
      case null => false
      case that: MyObject => this.getValue == that.getValue
      case _ => false
    }

    override def toString: String = s"MyObject(id=$id, value=$value)"
  }

  // TODO: Add implementations of specific threads, used for testing
  class Adder(val set: ConcurrentMultiSet[MyObject], input: List[MyObject])
      extends Thread {
    var time: Long = 0

    override def run() = {
      val t1 = System.currentTimeMillis()
      // val perm = input.map(_.getValue).permutations.take(10000).next
      for (i <- input) {
        set.add(i)
      }
      val t2 = System.currentTimeMillis()
      time = t2 - t1
    }
  }

  class Remover(val set: ConcurrentMultiSet[MyObject], toRemove: List[MyObject])
      extends Thread {
    var removed: Set[MyObject] = Set.empty
    var time: Long = 0

    override def run() = {
      val t1 = System.currentTimeMillis()
      for (i <- toRemove) {
        if (set.remove(i)) {
          removed = removed + i
        }
        val t2 = System.currentTimeMillis()
        time = t2 - t1
      }
    }
  }

  class Checker(val set: ConcurrentMultiSet[MyObject], elems: List[MyObject])
      extends Thread {
    var witnessed: Set[MyObject] = Set.empty
    var time: Long = 0

    override def run() = {
      val t1 = System.currentTimeMillis()
      for (i <- elems) {
        if (set.contains(i)) {
          witnessed = witnessed + i
        }
      }
      val t2 = System.currentTimeMillis()
      time = t2 - t1
    }

  }

}
