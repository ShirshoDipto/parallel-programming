package sorting

/**
  * @author Ilya Sergey
  */
class ParallelQuickSortTests extends SortingTests {
  
  override val ARRAY_SIZE: Int = 500000

  val sorter = ParallelQuickSort
}
