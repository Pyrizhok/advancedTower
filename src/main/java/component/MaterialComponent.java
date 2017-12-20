package component;

import com.jme3.material.Material;
import com.simsilica.es.EntityComponent;

public class MaterialComponent implements EntityComponent {

	private Material mat;

	public MaterialComponent(Material mat) {
		this.mat = mat;
	}

	public Material getMat() {
		return mat;
	}
}