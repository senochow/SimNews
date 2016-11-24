package com.senochow.simnews.utils;

public class WeightPair implements Comparable<WeightPair>{
	public String first;
	public double second;
	
	public WeightPair (String hval, double w) {
		this.first = hval;
		this.second = w;
	}

	@Override
	public int compareTo(WeightPair o) {
		// TODO Auto-generated method stub
		if (this.second < o.second) return 1;
		else if (this.second == o.second) {
//			return this.first.compareTo(o.first);
			return 0;
		}
		else return -1;
	}

	@Override
	public String toString() {
		return "WeightPair [first=" + first + ", second=" + second + "]";
	}
	
}
