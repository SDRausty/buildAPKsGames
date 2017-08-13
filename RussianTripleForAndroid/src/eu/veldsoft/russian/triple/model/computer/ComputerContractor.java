package eu.veldsoft.russian.triple.model.computer;

import eu.veldsoft.russian.triple.model.Talon;
import eu.veldsoft.russian.triple.model.Contractor;

/**
 * Computer contractor.
 * 
 * @author Todor Balabanov
 */
public interface ComputerContractor extends Contractor {
	/**
	 * Keep reference to talon object.
	 * 
	 * @param talon Talone reference.
	 */
	public void talonLink(Talon talon);
}
