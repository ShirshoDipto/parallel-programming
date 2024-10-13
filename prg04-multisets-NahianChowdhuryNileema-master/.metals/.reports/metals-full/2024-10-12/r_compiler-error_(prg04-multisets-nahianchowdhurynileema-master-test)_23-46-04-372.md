file://<WORKSPACE>/src/test/scala/multiset/MultiSetTests.scala
### scala.reflect.internal.FatalError: 
  ThisType(method mkThreads) for sym which is not a class
     while compiling: file://<WORKSPACE>/src/test/scala/multiset/MultiSetTests.scala
        during phase: globalPhase=<no phase>, enteringPhase=parser
     library version: version 2.12.16
    compiler version: version 2.12.16
  reconstructed args: -classpath <WORKSPACE>/.bloop/prg04-multisets-nahianchowdhurynileema-master/bloop-bsp-clients-classes/test-classes-Metals-0V8OfSQJTLaYPmDecKhT8w==:<HOME>/Library/Caches/bloop/semanticdb/com.sourcegraph.semanticdb-javac.0.10.0/semanticdb-javac-0.10.0.jar:<WORKSPACE>/.bloop/prg04-multisets-nahianchowdhurynileema-master/bloop-bsp-clients-classes/classes-Metals-0V8OfSQJTLaYPmDecKhT8w==:<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.12.16/scala-library-2.12.16.jar:<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scalatest/scalatest_2.12/3.0.1/scalatest_2.12-3.0.1.jar:<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/commons-io/commons-io/2.4/commons-io-2.4.jar:<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scalactic/scalactic_2.12/3.0.1/scalactic_2.12-3.0.1.jar:<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-reflect/2.12.16/scala-reflect-2.12.16.jar:<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-xml_2.12/1.0.5/scala-xml_2.12-1.0.5.jar:<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-parser-combinators_2.12/1.0.4/scala-parser-combinators_2.12-1.0.4.jar -Xplugin-require:semanticdb -Yrangepos -Ymacro-expand:discard -Ycache-plugin-class-loader:last-modified -Ypresentation-any-thread

  last tree to typer: TypeTree(class List)
       tree position: line 78 of file://<WORKSPACE>/src/test/scala/multiset/MultiSetTests.scala
            tree tpe: List[MultiSetTests.this.Checker]
              symbol: (sealed abstract) class List in package immutable
   symbol definition: sealed abstract class List[+A] extends AbstractSeq[A] with LinearSeq[A] with Product with GenericTraversableTemplate[A,List] with LinearSeqOptimized[A,List[A]] with Serializable (a ClassSymbol)
      symbol package: scala.collection.immutable
       symbol owners: class List
           call site: <none> in <none>

== Source file context for tree position ==

    75       }).toList
    76 
    77       val adders = for (in <- inputs) yield new Adder(set, in)
    78       val checkers = for (in <- inputs) yield new Checker(se_CURSOR_t, in)
    79       val remover = new Remover(set, inputs.flatten)
    80       (inputs.flatten, adders, checkers, remover)
    81     }

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 2.12.16
Classpath:
<WORKSPACE>/.bloop/prg04-multisets-nahianchowdhurynileema-master/bloop-bsp-clients-classes/test-classes-Metals-0V8OfSQJTLaYPmDecKhT8w== [exists ], <HOME>/Library/Caches/bloop/semanticdb/com.sourcegraph.semanticdb-javac.0.10.0/semanticdb-javac-0.10.0.jar [exists ], <WORKSPACE>/.bloop/prg04-multisets-nahianchowdhurynileema-master/bloop-bsp-clients-classes/classes-Metals-0V8OfSQJTLaYPmDecKhT8w== [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.12.16/scala-library-2.12.16.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scalatest/scalatest_2.12/3.0.1/scalatest_2.12-3.0.1.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/commons-io/commons-io/2.4/commons-io-2.4.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scalactic/scalactic_2.12/3.0.1/scalactic_2.12-3.0.1.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-reflect/2.12.16/scala-reflect-2.12.16.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-xml_2.12/1.0.5/scala-xml_2.12-1.0.5.jar [exists ], <HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-parser-combinators_2.12/1.0.4/scala-parser-combinators_2.12-1.0.4.jar [exists ]
Options:
-Yrangepos -Xplugin-require:semanticdb


action parameters:
offset: 2424
uri: file://<WORKSPACE>/src/test/scala/multiset/MultiSetTests.scala
text:
```scala
package multiset

import org.scalatest.{FunSpec, Matchers}
import util.ThreadID
import scala.util.Random

trait MultiSetTests extends FunSpec with Matchers {
  val printStats = true

  val NUM_THREADS = 4
  val INPUT_SIZE = 100

  def mkSet: ConcurrentMultiSet[Int]

  describe(s"Concurrent multi-set ${this.getClass.getName}") {
    it("should behave correctly as a set") {
      ThreadID.reset()
      // Handling unique elements
      val set = mkSet
      val (input, adders, checkers, remover) = mkThreads(set)
      val threads = adders ++ checkers ++ List(remover)

      val t1 = System.currentTimeMillis()
      for (t <- adders) t.start()
      for (t <- adders) t.join()
      for (t <- remover :: checkers) t.start()
      for (t <- remover :: checkers) t.join()
      val t2 = System.currentTimeMillis()

      val allWitnessed = checkers.map(_.witnessed).toSet.flatten
      val allRemoved = remover.removed

      for (r <- allRemoved) {
        assert(!set.contains(r))
      }

      for (r <- allWitnessed if !allRemoved.contains(r)) {
        assert(set.contains(r))
      }

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
        println(s"Statistics for ${set.getClass.getName}:")
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
    it("should behave correctly as a multi-set") {
      // TODO: Implement me
      ThreadID.reset()
      val set = mkSet

      val inputs = (for (i <- 0 until NUM_THREADS) yield {
        List.fill(INPUT_SIZE)(Random.nextInt(10) + 1)
      }).toList

      val adders = for (in <- inputs) yield new Adder(set, in)
      val checkers = for (in <- inputs) yield new Checker(se@@t, in)
      val remover = new Remover(set, inputs.flatten)
      (inputs.flatten, adders, checkers, remover)
    }
  }

  def mkThreads(s: ConcurrentMultiSet[Int]) = {
    val inputs = (for (i <- 0 until NUM_THREADS) yield {
      val start = i * INPUT_SIZE
      val end = (i + 1) * INPUT_SIZE - 1
      (start to end).toList
    }).toList

    val adders = for (in <- inputs) yield new Adder(s, in)
    val checkers = for (in <- inputs) yield new Checker(s, in)
    val remover = new Remover(s, inputs.flatten)
    (inputs.flatten, adders, checkers, remover)
  }

  class Adder(val set: ConcurrentMultiSet[Int], input: List[Int])
      extends Thread {
    var time: Long = 0

    override def run() = {
      val t1 = System.currentTimeMillis()
      val perm = input.permutations.take(10000).next
      for (i <- perm) {
        set.add(i)
      }
      val t2 = System.currentTimeMillis()
      time = t2 - t1
    }
  }

  class Remover(val set: ConcurrentMultiSet[Int], toRemove: List[Int])
      extends Thread {
    var removed: Set[Int] = Set.empty
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

  class Checker(val set: ConcurrentMultiSet[Int], elems: List[Int])
      extends Thread {
    var witnessed: Set[Int] = Set.empty
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

```



#### Error stacktrace:

```
scala.reflect.internal.Reporting.abort(Reporting.scala:69)
	scala.reflect.internal.Reporting.abort$(Reporting.scala:65)
	scala.reflect.internal.SymbolTable.abort(SymbolTable.scala:28)
	scala.reflect.internal.Types$ThisType.<init>(Types.scala:1193)
	scala.reflect.internal.Types$UniqueThisType.<init>(Types.scala:1213)
	scala.reflect.internal.Types$ThisType$.apply(Types.scala:1217)
	scala.meta.internal.pc.AutoImportsProvider$$anonfun$autoImports$3.applyOrElse(AutoImportsProvider.scala:74)
	scala.meta.internal.pc.AutoImportsProvider$$anonfun$autoImports$3.applyOrElse(AutoImportsProvider.scala:60)
	scala.collection.immutable.List.collect(List.scala:315)
	scala.meta.internal.pc.AutoImportsProvider.autoImports(AutoImportsProvider.scala:60)
	scala.meta.internal.pc.ScalaPresentationCompiler.$anonfun$autoImports$1(ScalaPresentationCompiler.scala:306)
```
#### Short summary: 

scala.reflect.internal.FatalError: 
  ThisType(method mkThreads) for sym which is not a class
     while compiling: file://<WORKSPACE>/src/test/scala/multiset/MultiSetTests.scala
        during phase: globalPhase=<no phase>, enteringPhase=parser
     library version: version 2.12.16
    compiler version: version 2.12.16
  reconstructed args: -classpath <WORKSPACE>/.bloop/prg04-multisets-nahianchowdhurynileema-master/bloop-bsp-clients-classes/test-classes-Metals-0V8OfSQJTLaYPmDecKhT8w==:<HOME>/Library/Caches/bloop/semanticdb/com.sourcegraph.semanticdb-javac.0.10.0/semanticdb-javac-0.10.0.jar:<WORKSPACE>/.bloop/prg04-multisets-nahianchowdhurynileema-master/bloop-bsp-clients-classes/classes-Metals-0V8OfSQJTLaYPmDecKhT8w==:<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.12.16/scala-library-2.12.16.jar:<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scalatest/scalatest_2.12/3.0.1/scalatest_2.12-3.0.1.jar:<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/commons-io/commons-io/2.4/commons-io-2.4.jar:<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scalactic/scalactic_2.12/3.0.1/scalactic_2.12-3.0.1.jar:<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-reflect/2.12.16/scala-reflect-2.12.16.jar:<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-xml_2.12/1.0.5/scala-xml_2.12-1.0.5.jar:<HOME>/Library/Caches/Coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/modules/scala-parser-combinators_2.12/1.0.4/scala-parser-combinators_2.12-1.0.4.jar -Xplugin-require:semanticdb -Yrangepos -Ymacro-expand:discard -Ycache-plugin-class-loader:last-modified -Ypresentation-any-thread

  last tree to typer: TypeTree(class List)
       tree position: line 78 of file://<WORKSPACE>/src/test/scala/multiset/MultiSetTests.scala
            tree tpe: List[MultiSetTests.this.Checker]
              symbol: (sealed abstract) class List in package immutable
   symbol definition: sealed abstract class List[+A] extends AbstractSeq[A] with LinearSeq[A] with Product with GenericTraversableTemplate[A,List] with LinearSeqOptimized[A,List[A]] with Serializable (a ClassSymbol)
      symbol package: scala.collection.immutable
       symbol owners: class List
           call site: <none> in <none>

== Source file context for tree position ==

    75       }).toList
    76 
    77       val adders = for (in <- inputs) yield new Adder(set, in)
    78       val checkers = for (in <- inputs) yield new Checker(se_CURSOR_t, in)
    79       val remover = new Remover(set, inputs.flatten)
    80       (inputs.flatten, adders, checkers, remover)
    81     }