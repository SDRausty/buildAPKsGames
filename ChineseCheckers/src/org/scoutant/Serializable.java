package org.scoutant;

/**
 * @author scoutant
 * Human readable serialization. As opposite to standard Java binary object @see java.io.Serialization.
 * Any class implementing org.scoutant.Serialization is invited to provide either :<ul>
 * <li> a constructor with a String argument, 
 * <li>or a static method with a String argument, returning a newly constructed instance, good practice to name it deserialize(). 
 */
public interface Serializable {

	String serialize();
}
