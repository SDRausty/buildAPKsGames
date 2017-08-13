package it.ecosw.dudo.games;

/**
 * This file is part of Dudo for Android software.
 *
 *  Dudo is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Dudo is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Dudo.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * This class is just a interface class between settings and real PlayerSet
 * @author Enrico Strocchi
 *
 */
public class PlayerInfo {
	
	private String name;
	
	private String save;
	
	/**
	 * Constructor
	 * @param name Player Name
	 * @param save Player last save status
	 */
	public PlayerInfo(String name, String save){
		this.name = name;
		this.save = save;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the save
	 */
	public String getSave() {
		return save;
	}
	
	/**
	 * Return playerset relative to this player
	 * @return PlayerSet
	 */
	public PlayerSet getPlayerSet(){
		if (save.length() == 5 ){
			if (save.equals("00000")) return new PlayerSet(name, false);
			return new PlayerSet(this,false);
		} else if (save.length() == 6) {
			if (save.equals("000000")) return new PlayerSet(name, true);
			return new PlayerSet(this,true);
		}
		return null;
	}

}
