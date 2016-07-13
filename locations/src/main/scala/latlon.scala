import java.lang.Math._

class Location(lattitude: Double, longitude: Double) {
	val lat = lattitude;
	val lon = longitude;
	

	def this(loc: Array[Double]) = 
	{
		this(loc(0), loc(1));
	}
	
	def +(operand: Location): Location = 
	{
		new Location(lat + operand.lat, lon + operand.lon);
	}

	def -(operand: Location): Location = 
	{
		new Location(lat -operand.lat, lon - operand.lon);
	}
	
	def /(operand: Double): Location = 
	{
		new Location(lat / operand, lon / operand);
	}

	override def toString(): String = "(" + lat + ", " + lon + ")";
	def apply(i: Int): Double = 
	{
		if (i==0) 
		{
			return lat;
		}
		else if (i==1)
		{
			return lon;
		}
		throw new ArrayIndexOutOfBoundsException("use 0 for lat and 1 for lon");
	}
	
}

class LatLonGrid(nw: Location, se: Location) {
	var mid = (nw + se) / 2

	var radius = new Array[Double](2)
	radius(0) = 6378.1; radius(1)= 6356.8

	def Rphi(lat: Double): Double = 
	{
		var l = lat / 90;
		return l*radius(0) + (1-l)*radius(1)
	}


	var dl = 10/(Rphi(mid(0))*cos(mid(0)*PI/180)*PI/180)
	var f = List(mid(0));

	var flast = mid(0);
	while (flast > se(0) )
	{
		var df = 100*(pow(180,2))/(pow(Rphi(flast),2) * cos(flast*PI/180) * dl * pow(PI,2));
		flast = flast - df
		f = List(flast):::f
		
	}
	var southbound = flast

	flast = mid(0);
	while (flast < nw(0) )
	{
		
		var df = 100*(pow(180,2))/(pow(Rphi(flast),2) * cos(flast*PI/180) * dl * pow(PI,2));
		flast = flast + df
		f = f:::List(flast)
	}
	var northbound = flast 

	var eastbound = se(1)+dl*((mid(1) - se(1))/dl).toInt
	var westbound = mid(1)+dl*((nw(1) - mid(1))/dl).toInt
	var latlen = ((se(1)-nw(1))/dl).toInt;

	def getlonidx(lon: Double): Integer = 
	{
		return ((f.take(f.length-1) zip f.takeRight(f.length-1) zip (0 to f.length-1)).filter{case((a,b),idx)=>(a<=lon && b>lon)})(0)._2
	}
	def getlatidx(lat: Double): Integer = 
	{
		var idx = ((lat-westbound)/dl).toInt
		if (idx <= latlen )
		{
			return idx
		}
		else
		{
			return -1
		}
	}
	def getbounds(idxlat: Integer, idxlon: Integer): (Location, Location) = 
	{
		val lat1 = westbound+dl*idxlat;
		val lat2 = lat1 + dl;
		val lon1 = f(idxlon);
		val lon2 = f(idxlon+1);
		return (new Location(lat1, lon1), new Location(lat2,lon2));
		 
	}

	override def toString(): String = "[("+ westbound + ", " + northbound + "), (" + eastbound + ", " + southbound + ")]\n" + ((se(1)-nw(1))/dl).toInt + "x" + f.length + " cells.";

	
}


