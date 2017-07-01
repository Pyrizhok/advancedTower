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
		if (Model.DEFENDER.equals(name)) {
			Geometry defenderGeometry = defineDefenderGeometry();
			visual.attachChild(defenderGeometry);
		} else if (Model.INVADER.equals(name)) {
			colorRGBA = ColorRGBA.Green;
			mat.setColor("Color", colorRGBA);
			geom.setMaterial(mat);
			visual.attachChild(geom);
		} else if (Model.BULLET.equals(name)) {
			Node model = (Node) assetManager.loadModel("Models/" + name + ".j3o");
			visual.attachChild(model);
		} else if (Model.CURSOR.equals(name)) {
			colorRGBA = ColorRGBA.Yellow;
			mat.setColor("Color", colorRGBA);
			geom.setMaterial(mat);
			visual.attachChild(geom);
		} else {
			colorRGBA = ColorRGBA.Pink;
			geom.setMaterial(mat);
			mat.setColor("Color", colorRGBA);
			visual.attachChild(geom);
		}
		return visual;
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