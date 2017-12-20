package util;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;

public class SpatialUtils {

	public static Node attachCoordinateAxes(Vector3f pos, AssetManager assetManager) {
		Node result = new Node("coordinateAxes");
		Geometry gx = getGeometry(assetManager, Vector3f.UNIT_X, pos, ColorRGBA.Red);
		Geometry gy = getGeometry(assetManager, Vector3f.UNIT_Y, pos, ColorRGBA.Green);
		Geometry gz = getGeometry(assetManager, Vector3f.UNIT_Z, pos, ColorRGBA.Blue);

		result.attachChild(gx);
		result.attachChild(gy);
		result.attachChild(gz);
		return result;
	}

	private static Geometry getGeometry(AssetManager assetManager, Vector3f vector3f, Vector3f pos, ColorRGBA color) {
		Arrow arrow = new Arrow(vector3f);
		putShape(assetManager, arrow, color).setLocalTranslation(pos);
		Geometry gx = new Geometry("gx", arrow);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.getAdditionalRenderState().setLineWidth(4);
		mat.setColor("Color", color);
		gx.setMaterial(mat);
		return gx;
	}

	private static Geometry putShape(AssetManager assetManager, Mesh shape, ColorRGBA color) {
		Geometry g = new Geometry("coordinate axis", shape);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.getAdditionalRenderState().setWireframe(true);
		mat.setColor("Color", color);
		g.setMaterial(mat);
		return g;
	}

}
