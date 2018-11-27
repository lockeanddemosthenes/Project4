/**
 * This interface provides all the necessary functions to preform collisons
 * from these one can get the boarders of a given object 
 * (assuming it is rectangular)
 */
public interface Collidable {
	/**
	 * gets the center x of the object
	 * @return the center x
	 */
	public double getX();
	/**
	 * gets the center y of the object
	 * @return the center y
	 */
	public double getY();
	
	
	/**
	 * Gets the height of the object in order to form the borders
	 * @return the height of the collidable object
	 */
	public double getWidth();
	
	/**
	 * Gets the width of the object in order to form the borders
	 * @return the width of the collidable oblect
	 */
	public double getHeight();
}
