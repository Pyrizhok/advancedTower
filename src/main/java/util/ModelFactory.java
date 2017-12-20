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

	private static final String ASSETS_MAT_DEFS_FOG_UNSHADED_J3MD = "assets/MatDefs/FogUnshaded.j3md";
	private final AssetManager assetManager;

	public ModelFactory(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public Spatial create(String name) {

		Box b = new Box(1, 1, 1);
		Geometry geom = new Geometry("Box", b);
		Material mat = new Material(assetManager, ASSETS_MAT_DEFS_FOG_UNSHADED_J3MD);
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
			case Model.AREA:
				visual.attachChild(defineAreaGeometry());
				break;
			case Model.GATE:
				visual.attachChild(defineGateGeometry());
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

	private Spatial defineGateGeometry() {
		Box box = new Box(Vector3f.ZERO, Vector3f.UNIT_XYZ);
		Geometry geometry = new Geometry("Box", box);
		Material material = new Material(assetManager, ASSETS_MAT_DEFS_FOG_UNSHADED_J3MD);
		ColorRGBA colorRGBA = ColorRGBA.Cyan;
		material.setColor("Color", colorRGBA);
		geometry.setMaterial(material);
		return geometry;
	}

	private Spatial defineAreaGeometry() {
		Box box = new Box(Vector3f.ZERO, new Vector3f(50, 50, -1));
		Geometry geometry = new Geometry("Box", box);
		geometry.center();
		Material material = new Material(assetManager, ASSETS_MAT_DEFS_FOG_UNSHADED_J3MD);
		ColorRGBA colorRGBA = ColorRGBA.Brown;
		material.setColor("Color", colorRGBA);
		geometry.setMaterial(material);
		return geometry;
	}

	private Geometry defineCursorGeometry() {
		Box box = new Box(1, 1, 1);
		Geometry geometry = new Geometry("Box", box);
		Material material = new Material(assetManager, ASSETS_MAT_DEFS_FOG_UNSHADED_J3MD);
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
		Material material = new Material(assetManager, ASSETS_MAT_DEFS_FOG_UNSHADED_J3MD);
		ColorRGBA colorRGBA = ColorRGBA.Green;
		material.setColor("Color", colorRGBA);
		geometry.setMaterial(material);
		return geometry;
	}

	private Spatial defineDefenderGeometry() {
		Node result = new Node("defender");
		Material material = new Material(assetManager, ASSETS_MAT_DEFS_FOG_UNSHADED_J3MD);
		ColorRGBA colorRGBA = ColorRGBA.DarkGray;
		Box box = new Box(Vector3f.ZERO, Vector3f.UNIT_XYZ);
		Geometry defenderGeometry = new Geometry("Box", box);
		material.setColor("Color", colorRGBA);
		defenderGeometry.setMaterial(material);

		result.attachChild(defenderGeometry);
		result.attachChild(SpatialUtils.attachCoordinateAxes(Vector3f.UNIT_XYZ, assetManager));

		return result;
	}
}