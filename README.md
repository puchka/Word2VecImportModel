Word2VecImportModel
======================

I isolated in this project the code attempting to load
Google original binary format Word2Vec model.

I did it to isolate the bug encountered when loading large
model such as [GoogleNews-vectors-negative300.bin](https://drive.google.com/file/d/0B7XkCwpI5KDYNlNUTTlSS21pQmM/edit?usp=sharing]) as signaled
here : [[SPARK-15328][MLLIB][ML] Word2Vec import for original binary format](https://github.com/apache/spark/pull/13735)
