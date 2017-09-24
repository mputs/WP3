
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object AIStest
{
	def main(args: Array[String])
	{
		val tfiles = "hdfs://namenode.ib.sandbox.ichec.ie:8020/" + args(0)

		val conf = new SparkConf()
		conf.setAppName("AIS-test")
		conf.setMaster("yarn-client")
		val sc = new SparkContext(conf)

		val data = sc.textFile(tfiles);
		val data1 = data.map(_.split(","));
		val data2 = data1.filter(x => x(0)=="235106484");
		data2.map(_.mkString(",")).saveAsTextFile("hdfs://namenode.ib.sandbox.ichec.ie:8020/user/itsala/output")
		//data2.map(_.mkString(",")).saveAsTextFile("file:////ichec/home/users/mputs/WP4/output")
		//println ("Lines in dataset: %d".format(data2.count()));
		sc.stop()
	}
}
