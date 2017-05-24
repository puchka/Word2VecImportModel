Word2VecImportModel
======================

I isolated in this project the code attempting to load
Google original binary format Word2Vec model.

I did it to isolate the bug encountered when loading large
model such as [GoogleNews-vectors-negative300.bin](https://drive.google.com/file/d/0B7XkCwpI5KDYNlNUTTlSS21pQmM/edit?usp=sharing]) as signaled
here : [[SPARK-15328][MLLIB][ML] Word2Vec import for original binary format](https://github.com/apache/spark/pull/13735)

Reproduce the bug with Spark Shell

```
spark-shell --driver-memory 10g --executor-memory 10g --jars ./target/scala-2.11/word2vecimportmodel_2.11-1.0.jar
spark shell> import org.apache.spark.examples.ml.feature._
spark shell> val path = "file:///media/data/repo/deeplearning4j/GoogleNews-vectors-negative300.bin.gz"
spark shell> val word = "spark"
spark shell> val gModel = Word2VecImportModel.loadGoogleModel(sc, path)
spark shell> println(gModel.transform(word).toArray.mkString(", "))
```
