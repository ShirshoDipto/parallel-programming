package multiset

/** @author
  *   Ilya Sergey
  */
class OptimisticDupListTest extends MultiSetTests with HashCollisionTests {
  override def mkSet = new OptimisticDupList[Int]

  override def mkCollisionFreeSet = new OptimisticDupList[MyObject]
}
