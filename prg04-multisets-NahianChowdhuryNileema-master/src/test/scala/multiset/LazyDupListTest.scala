package multiset

/**
  * @author Ilya Sergey
  */
class LazyDupListTest extends MultiSetTests with HashCollisionTests {
  override def mkSet = new LazyDupList[Int]

  override def mkCollisionFreeSet = new LazyDupList[MyObject]
}
