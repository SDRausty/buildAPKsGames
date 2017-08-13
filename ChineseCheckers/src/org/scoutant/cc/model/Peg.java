/*
* Copyright (C) 2012- stephane coutant
*
* This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
* See the GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>
*/
package org.scoutant.cc.model;

public class Peg implements Comparable<Peg> {
	
	public Point point;
	public int color;

	public Peg( Point p) {
		this.point = p;
	}
	private Peg(int i, int j) {
		this( new Point(i,j));
	}
	public Peg(int i, int j, int color) {
		this(i,j);
		this.color = color;
	}
	
	public String toString(){
		return ""+point + ", color :" + color;
	}
	/**
	 * Shall not be used! Do sort with a PegComparator...
	 */
	@Override
	public int compareTo(Peg that) {
		throw new IllegalAccessError( "Peg shall not be compared directly! Do sort with a PegComparator...");
//		return -(this.point.j - that.point.j);
	}
	
	@Override
	public boolean equals(Object o) {
		Peg that = (Peg) o;
		if (that==null) return false;
		// consider color too?
		return this.point.equals(that.point);
	}

}
