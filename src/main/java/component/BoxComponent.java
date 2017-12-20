package component;

import com.jme3.math.Vector3f;
import com.simsilica.es.EntityComponent;

public class BoxComponent implements EntityComponent {

	private Vector3f size;

	public BoxComponent(Vector3f size) {
		this.size = size;
	}

	public BoxComponent() {
		this.size = new Vector3f();
	}

	public Vector3f getSize() {
		return size;
	}
}