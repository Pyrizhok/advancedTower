package component;

import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;

public class Direction implements EntityComponent {

	private final Vector3f location;

	public Direction(Vector3f location) {
		this.location = location;
	}

	public Vector3f getLocation() {
		return location;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + location + "]";
	}

}