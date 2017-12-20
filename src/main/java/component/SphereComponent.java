package component;

import com.simsilica.es.EntityComponent;

public class SphereComponent implements EntityComponent {

	private float radius;

	public SphereComponent(float radius) {
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}
}