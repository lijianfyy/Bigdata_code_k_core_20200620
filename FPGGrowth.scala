import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.fpm.{ FPGrowth, FPGrowthModel }
object fpg{
  def main(args: Array[String]){

    //构建Spark对象
    val conf = new SparkConf().setAppName("fpg")
    val sc = new SparkContext(conf)
    Logger.getRootLogger.setLevel(Level.WARN)

    //读取数据
    val data_path = "file:///home/spark/Downloads/test.txt"
    val data = sc.textFile(data_path)
    val examples = data.map(_.split(" ")).cache()

    //构建模型
    val minSupport=0.2
    val numPartition = 10
    val model = new FPGrowth().
      setMinSupport(minSupport).
      setNumPartitions(numPartition).
      run(examples)
    println(s"Number of frequent items: ${model.freqItemsets.count()}")
    model.freqItemsets.collect().foreach{itemset =>
      println(itemset.items.mkString("[",",","]")+","+ itemset.freq )
    }
  }
}
