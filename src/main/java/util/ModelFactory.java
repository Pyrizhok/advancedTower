package util;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import component.Model;

public class ModelFactory {

	private final AssetManager assetManager;

	public ModelFactory(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public Spatial create(String name) {

		Box b = new Box(1, 1, 1);
		Geometry geom = new Geometry("Box", b);
		Material mat = new Material(assetManager, "assets/MatDefs/FogUnshaded.j3md");
		ColorRGBA colorRGBA;
		Node visual = new Node("Visual");
		switch (name) {
			case Model.DEFENDER:
				visual.attachChild(defineDefenderGeometry());
				break;
			case Model.INVADER:
				visual.attachChild(defineInvaderGeometry());
				break;
			case Model.BULLET:
				visual.attachChild(defineBulletNode(name));
				break;
			case Model.CURSOR:
				visual.attachChild(defineCursorGeometry());
				break;
			default:
				colorRGBA = ColorRGBA.Pink;
				geom.setMaterial(mat);
				mat.setColor("Color", colorRGBA);
				visual.attachChild(geom);
				break;
		}
		return visual;
	}

	private Geometry defineCursorGeometry() {
		Box box = new Box(1, 1, 1);
		Geometry geometry = new Geometry("Box", box);
		Material material = new Material(assetManager, "assets/MatDefs/FogUnshaded.j3md");
		ColorRGBA colorRGBA = ColorRGBA.Yellow;
		material.setColor("Color", colorRGBA);
		geometry.setMaterial(material);
		return geometry;
	}

	private Node defineBulletNode(String name) {
		return (Node) assetManager.loadModel("Models/" + name + ".j3o");
	}

	private Geometry defineInvaderGeometry() {
		Box box = new Box(Vector3f.ZERO, Vector3f.UNIT_XYZ);
		Geometry geometry = new Geometry("Box", box);
		Material material = new Material(assetManager, "assets/MatDefs/FogUnshaded.j3md");
		ColorRGBA colorRGBA = ColorRGBA.Green;
		material.setColor("Color", colorRGBA);
		geometry.setMaterial(material);
		return geometry;
	}

	private Geometry defineDefenderGeometry() {
		Material material = new Material(assetManager, "assets/MatDefs/FogUnshaded.j3md");
		ColorRGBA colorRGBA= ColorRGBA.DarkGray;
		Box box = new Box(Vector3f.ZERO, Vector3f.UNIT_XYZ);
		Geometry defenderGeometry = new Geometry("Box", box);
		material.setColor("Color", colorRGBA);
		defenderGeometry.setMaterial(material);
		return defenderGeometry;
	}
}