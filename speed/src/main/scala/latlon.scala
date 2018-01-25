package latlon

import math._

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

class LatLonGrid(nw: Location, se: Location, cellsize: Double) {
	var mid = (nw + se) / 2


	var radius = new Array[Double](2)
	radius(0) = 6378.1; radius(1)= 6356.8

	def Rphi(lat: Double): Double = 
	{
		var l = lat / 90;
		return l*radius(0) + (1-l)*radius(1)
	}


	var dl = math.sqrt(cellsize)/(Rphi(mid(0))*math.cos(mid(0)*math.Pi/180)*math.Pi/180)
	var f = List(mid(0));

	var flast = mid(0);
	while (flast > se(0) )
	{
		var df = cellsize*(math.pow(180,2))/(math.pow(Rphi(flast),2) * math.cos(flast*math.Pi/180) * dl * math.pow(math.Pi,2));
		flast = flast - df
		f = List(flast):::f
		
	}
	var southbound = flast

	flast = mid(0);
	while (flast < nw(0) )
	{
		
		var df = cellsize*(math.pow(180,2))/(math.pow(Rphi(flast),2) * math.cos(flast*math.Pi/180) * dl * math.pow(math.Pi,2));
		flast = flast + df
		f = f:::List(flast)
	}
	var northbound = flast 

	var eastbound = se(1)+dl*((mid(1) - se(1))/dl).toInt
	var westbound = mid(1)+dl*((nw(1) - mid(1))/dl).toInt
	var lonlen = ((se(1)-nw(1))/dl).toInt;

	def getlatidx(lat: Double): Int = 
	{
		if (lat >= southbound & lat <=northbound)
			return ((f.take(f.length-1) zip f.takeRight(f.length-1) zip (0 to f.length-1)).filter{case((a,b),idx)=>(a<=lat && b>lat)})(0)._2
		else
			return -1;
	}
	def getlonidx(lon: Double): Int = 
	{
		if (lon >= westbound && lon <=eastbound)
		{
			var idx = ((lon-westbound)/dl).toInt
			if (idx <= lonlen )
			{
				return idx
				}
			else
			{
				return -1
			}
		}
		else
			return -1
	}
	def getbounds(idxlat: Int, idxlon: Int): (Location, Location) = 
	{
		val lon1 = westbound+dl*idxlon;
		val lon2 = lon1 + dl;
		val lat1 = f(idxlat);
		val lat2 = f(idxlat+1);
		return (new Location(lat1, lon1), new Location(lat2,lon2));
		 
	}
	def getcenter(idxlat: Int, idxlon: Int): Location = 
	{
		val x = getbounds(idxlat, idxlon);
		return (x._1 + x._2)/2
	}

	override def toString(): String = "[("+ westbound + ", " + northbound + "), (" + eastbound + ", " + southbound + ")]\n" + ((se(1)-nw(1))/dl).toInt + "x" + f.length + " cells.";

	
}



