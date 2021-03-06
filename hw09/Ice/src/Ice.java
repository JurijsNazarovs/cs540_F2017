
import java.util.*;
import java.io.*;

public class Ice{
	
	static public void main(String[] args){
		//String[] args = {"200"};
		//String[] args = {"400", "400", "0.1"};
		//String[] args = {"500", "1e-6", "5"};
		//String[] args = {"700", "2020"};
		//String[] args = {"900", "0.1", "4"};
		int flag = Integer.valueOf(args[0]);
	
		Data data = new Data();
		if (flag == 100) {
			data.print();
		}
		
		if (flag == 200) {
			System.out.println(data.size);
			System.out.println(String.format("%.2f", Mean(data.y)));
			System.out.println(String.format("%.2f", Std(data.y)));
		}
		
		if (flag == 300) {
			double beta0 = Double.valueOf(args[1]);
			double beta1 = Double.valueOf(args[2]);
			
			System.out.println(String.format("%.2f", MSE(data, beta0, beta1)));
		}
		
		if (flag == 400) {
			double beta0 = Double.valueOf(args[1]);
			double beta1 = Double.valueOf(args[2]);
			
			ArrayList<Double> gradient = new ArrayList<>();
			gradient = Gradient(data, beta0, beta1);
			System.out.println(String.format("%.2f", gradient.get(0)));
			System.out.println(String.format("%.2f", gradient.get(1)));
		}
		
		if (flag == 500 || flag == 800) {
			if (flag == 800) {
				// Normalization
				ArrayList<Double> xNorm = Normalization(data.x);
				data.x.clear();
				data.x.addAll(xNorm);
			}
			
			double eta = Double.valueOf(args[1]);
			double T = Double.valueOf(args[2]);
			
			double beta0 = 0, beta1 = 0;
			
			ArrayList<Double> gradient = new ArrayList<>();
			
			for (int t = 1; t <= T; t++) {
				gradient = Gradient(data, beta0, beta1);

				beta0 -= eta*gradient.get(0);
				beta1 -= eta*gradient.get(1);
				System.out.println(t + " " 
								  + String.format("%.2f", beta0) + " "
								  + String.format("%.2f", beta1 ) + " "
								  + String.format("%.2f", MSE(data, beta0, beta1)));
			}
		}
		
		if (flag == 600 || flag == 700) {
			double enumerator = 0, denominator = 0;
			
			double xMean = Mean(data.x);
			double yMean = Mean(data.y);
			for (int i = 0; i < data.size; i++) {
				enumerator += (data.x.get(i) - xMean)*(data.y.get(i) - yMean);
				denominator += Math.pow(data.x.get(i) - xMean, 2);
			}
			
			double beta1Hat = enumerator/denominator;
			double beta0Hat = yMean - beta1Hat*xMean;
			
			if (flag == 600)
				System.out.println(String.format("%.2f", beta0Hat) + " "
								  + String.format("%.2f", beta1Hat) + " "
								  + String.format("%.2f", MSE(data, beta0Hat, beta1Hat)));
			if (flag == 700) {
				int year = Integer.valueOf(args[1]);
				System.out.println(String.format("%.2f", beta0Hat + beta1Hat*year));
			}
		}
		
		
		if (flag == 900) {
			// Normalization
			ArrayList<Double> xNorm = Normalization(data.x);
			data.x.clear();
			data.x.addAll(xNorm);
			
			// Main part
			double eta = Double.valueOf(args[1]);
			double T = Double.valueOf(args[2]);
			
			double beta0 = 0,  beta1 = 0;
			double grad0, grad1;
			Random rng = new Random();
			
			for (int t = 1; t <= T; t++) {
				int randomIter = rng.nextInt(data.size);
				grad0 = 2*(beta0 + beta1*data.x.get(randomIter) - data.y.get(randomIter));
				grad1 = grad0*data.x.get(randomIter);
				
				beta0 -= eta*grad0;
				beta1 -= eta*grad1;
				System.out.println(t + " "
								  + String.format("%.2f", beta0) + " "
								  + String.format("%.2f", beta1 ) + " "
								  + String.format("%.2f", MSE(data, beta0, beta1)));
			}
		}
		
	    return;
	}
	
	// My functions
	static public double Mean(ArrayList<Double> data) {
		int sum = 0;
		int size = data.size();
		for (int i = 0; i < size; i++) {
			sum += data.get(i);
		}
		return (double) sum/size;
	}
	
	static public double Std(ArrayList<Double> data) {
		double mean = Mean(data);
		double ss = 0;
		
		int size = data.size();
		for (int i = 0; i < size; i++) {
			ss += Math.pow(data.get(i) - mean, 2);
		}
		return Math.sqrt(ss/(size - 1));
	}
	
	static public double MSE(Data data, double beta0, double beta1) {
		double sum = 0;
		for (int i = 0; i < data.size; i++) {
			sum += Math.pow(beta0 + beta1*data.x.get(i) - data.y.get(i), 2);
		}
		
		return sum/data.size;
	}
	
	static public ArrayList<Double> Gradient(Data data, double beta0, double beta1) {
		double gr0 = 0;
		double gr1 = 0;
		for (int i = 0; i < data.size; i++) {
			double tmp = beta0 + beta1*data.x.get(i) - data.y.get(i);
			gr0 += tmp;
			gr1 += tmp*data.x.get(i);
		}
		
		ArrayList<Double> gradient = new ArrayList <Double> ();
		gradient.add(2./data.size*gr0);
		gradient.add(2./data.size*gr1);

		return gradient;
	}
	
	static public ArrayList<Double> Normalization(ArrayList<Double> x){
		int size = x.size();
		double xMean = Mean(x);
		double xSd = Std(x);
		ArrayList <Double> xTmp = new ArrayList<>();
		
		for (int i = 0; i < size; i++) {
			xTmp.add((x.get(i) - xMean)/xSd);
		}
		
		return xTmp;
	}
}


class Data{
	ArrayList <Double> x = new ArrayList<>();
	ArrayList <Double> y = new ArrayList<>();
	Integer size = 0;
	
	public void print() {
		for (int i = 0; i < this.size; i++) {
			// x
			if (this.x.get(i) == (this.x.get(i).intValue()))
				System.out.print(this.x.get(i).intValue() + " ");
			else 
				System.out.print(this.x.get(i) + " ");
			
			// y
			if (this.y.get(i) == (this.y.get(i).intValue()))
				System.out.println(this.y.get(i).intValue());
			else 
				System.out.println(this.y.get(i));
		}
	}
	
	public Data(){
		this.x.add(1855.); this.y.add(118.);
		this.x.add(1856.); this.y.add(151.);
		this.x.add(1857.); this.y.add(121.);
		this.x.add(1858.); this.y.add(96.);
		this.x.add(1859.); this.y.add(110.);
		this.x.add(1860.); this.y.add(117.);
		this.x.add(1861.); this.y.add(132.);
		this.x.add(1862.); this.y.add(104.);
		this.x.add(1863.); this.y.add(125.);
		this.x.add(1864.); this.y.add(118.);
		this.x.add(1865.); this.y.add(125.);
		this.x.add(1866.); this.y.add(123.);
		this.x.add(1867.); this.y.add(110.);
		this.x.add(1868.); this.y.add(127.);
		this.x.add(1869.); this.y.add(131.);
		this.x.add(1870.); this.y.add(99.);
		this.x.add(1871.); this.y.add(126.);
		this.x.add(1872.); this.y.add(144.);
		this.x.add(1873.); this.y.add(136.);
		this.x.add(1874.); this.y.add(126.);
		this.x.add(1875.); this.y.add(91.);
		this.x.add(1876.); this.y.add(130.);
		this.x.add(1877.); this.y.add(62.);
		this.x.add(1878.); this.y.add(112.);
		this.x.add(1879.); this.y.add(99.);
		this.x.add(1880.); this.y.add(161.);
		this.x.add(1881.); this.y.add(78.);
		this.x.add(1882.); this.y.add(124.);
		this.x.add(1883.); this.y.add(119.);
		this.x.add(1884.); this.y.add(124.);
		this.x.add(1885.); this.y.add(128.);
		this.x.add(1886.); this.y.add(131.);
		this.x.add(1887.); this.y.add(113.);
		this.x.add(1888.); this.y.add(88.);
		this.x.add(1889.); this.y.add(75.);
		this.x.add(1890.); this.y.add(111.);
		this.x.add(1891.); this.y.add(97.);
		this.x.add(1892.); this.y.add(112.);
		this.x.add(1893.); this.y.add(101.);
		this.x.add(1894.); this.y.add(101.);
		this.x.add(1895.); this.y.add(91.);
		this.x.add(1896.); this.y.add(110.);
		this.x.add(1897.); this.y.add(100.);
		this.x.add(1898.); this.y.add(130.);
		this.x.add(1899.); this.y.add(111.);
		this.x.add(1900.); this.y.add(107.);
		this.x.add(1901.); this.y.add(105.);
		this.x.add(1902.); this.y.add(89.);
		this.x.add(1903.); this.y.add(126.);
		this.x.add(1904.); this.y.add(108.);
		this.x.add(1905.); this.y.add(97.);
		this.x.add(1906.); this.y.add(94.);
		this.x.add(1907.); this.y.add(83.);
		this.x.add(1908.); this.y.add(106.);
		this.x.add(1909.); this.y.add(98.);
		this.x.add(1910.); this.y.add(101.);
		this.x.add(1911.); this.y.add(108.);
		this.x.add(1912.); this.y.add(99.);
		this.x.add(1913.); this.y.add(88.);
		this.x.add(1914.); this.y.add(115.);
		this.x.add(1915.); this.y.add(102.);
		this.x.add(1916.); this.y.add(116.);
		this.x.add(1917.); this.y.add(115.);
		this.x.add(1918.); this.y.add(82.);
		this.x.add(1919.); this.y.add(110.);
		this.x.add(1920.); this.y.add(81.);
		this.x.add(1921.); this.y.add(96.);
		this.x.add(1922.); this.y.add(125.);
		this.x.add(1923.); this.y.add(104.);
		this.x.add(1924.); this.y.add(105.);
		this.x.add(1925.); this.y.add(124.);
		this.x.add(1926.); this.y.add(103.);
		this.x.add(1927.); this.y.add(106.);
		this.x.add(1928.); this.y.add(96.);
		this.x.add(1929.); this.y.add(107.);
		this.x.add(1930.); this.y.add(98.);
		this.x.add(1931.); this.y.add(65.);
		this.x.add(1932.); this.y.add(115.);
		this.x.add(1933.); this.y.add(91.);
		this.x.add(1934.); this.y.add(94.);
		this.x.add(1935.); this.y.add(101.);
		this.x.add(1936.); this.y.add(121.);
		this.x.add(1937.); this.y.add(105.);
		this.x.add(1938.); this.y.add(97.);
		this.x.add(1939.); this.y.add(105.);
		this.x.add(1940.); this.y.add(96.);
		this.x.add(1941.); this.y.add(82.);
		this.x.add(1942.); this.y.add(116.);
		this.x.add(1943.); this.y.add(114.);
		this.x.add(1944.); this.y.add(92.);
		this.x.add(1945.); this.y.add(98.);
		this.x.add(1946.); this.y.add(101.);
		this.x.add(1947.); this.y.add(104.);
		this.x.add(1948.); this.y.add(96.);
		this.x.add(1949.); this.y.add(109.);
		this.x.add(1950.); this.y.add(122.);
		this.x.add(1951.); this.y.add(114.);
		this.x.add(1952.); this.y.add(81.);
		this.x.add(1953.); this.y.add(85.);
		this.x.add(1954.); this.y.add(92.);
		this.x.add(1955.); this.y.add(114.);
		this.x.add(1956.); this.y.add(111.);
		this.x.add(1957.); this.y.add(95.);
		this.x.add(1958.); this.y.add(126.);
		this.x.add(1959.); this.y.add(105.);
		this.x.add(1960.); this.y.add(108.);
		this.x.add(1961.); this.y.add(117.);
		this.x.add(1962.); this.y.add(112.);
		this.x.add(1963.); this.y.add(113.);
		this.x.add(1964.); this.y.add(120.);
		this.x.add(1965.); this.y.add(65.);
		this.x.add(1966.); this.y.add(98.);
		this.x.add(1967.); this.y.add(91.);
		this.x.add(1968.); this.y.add(108.);
		this.x.add(1969.); this.y.add(113.);
		this.x.add(1970.); this.y.add(110.);
		this.x.add(1971.); this.y.add(105.);
		this.x.add(1972.); this.y.add(97.);
		this.x.add(1973.); this.y.add(105.);
		this.x.add(1974.); this.y.add(107.);
		this.x.add(1975.); this.y.add(88.);
		this.x.add(1976.); this.y.add(115.);
		this.x.add(1977.); this.y.add(123.);
		this.x.add(1978.); this.y.add(118.);
		this.x.add(1979.); this.y.add(99.);
		this.x.add(1980.); this.y.add(93.);
		this.x.add(1981.); this.y.add(96.);
		this.x.add(1982.); this.y.add(54.);
		this.x.add(1983.); this.y.add(111.);
		this.x.add(1984.); this.y.add(85.);
		this.x.add(1985.); this.y.add(107.);
		this.x.add(1986.); this.y.add(89.);
		this.x.add(1987.); this.y.add(87.);
		this.x.add(1988.); this.y.add(97.);
		this.x.add(1989.); this.y.add(93.);
		this.x.add(1990.); this.y.add(88.);
		this.x.add(1991.); this.y.add(99.);
		this.x.add(1992.); this.y.add(108.);
		this.x.add(1993.); this.y.add(94.);
		this.x.add(1994.); this.y.add(74.);
		this.x.add(1995.); this.y.add(119.);
		this.x.add(1996.); this.y.add(102.);
		this.x.add(1997.); this.y.add(47.);
		this.x.add(1998.); this.y.add(82.);
		this.x.add(1999.); this.y.add(53.);
		this.x.add(2000.); this.y.add(115.);
		this.x.add(2001.); this.y.add(21.);
		this.x.add(2002.); this.y.add(89.);
		this.x.add(2003.); this.y.add(80.);
		this.x.add(2004.); this.y.add(101.);
		this.x.add(2005.); this.y.add(95.);
		this.x.add(2006.); this.y.add(66.);
		this.x.add(2007.); this.y.add(106.);
		this.x.add(2008.); this.y.add(97.);
		this.x.add(2009.); this.y.add(87.);
		this.x.add(2010.); this.y.add(109.);
		this.x.add(2011.); this.y.add(57.);
		this.x.add(2012.); this.y.add(87.);
		this.x.add(2013.); this.y.add(117.);
		this.x.add(2014.); this.y.add(91.);
		this.x.add(2015.); this.y.add(62.);
		this.x.add(2016.); this.y.add(65.);

		this.size = this.y.size();
	}
}