if not exist "classes" mkdir classes
scalac -d classes paddle.scala
scala -classpath classes sanity_check.scala
