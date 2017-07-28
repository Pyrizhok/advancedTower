package system;

import com.jme3.app.*;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.*;
import com.jme3.renderer.Camera;
import com.jme3.scene.*;
import com.jme3.scene.control.CameraControl;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.core.VersionedHolder;
import com.simsilica.lemur.input.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CameraState extends BaseAppState
		implements AnalogFunctionListener, StateFunctionListener {

	public static final String DEBUG_VALUE_VECTOR_3F_FORMAT = "%.2f, %.2f, %.2f";
	public static final String GROUP = "Camera";
	public static final FunctionId F_VERTICAL_MOVE = new FunctionId(GROUP, "Vertical Move");
	public static final FunctionId F_HORIZONTAL_MOVE = new FunctionId(GROUP, "Horizontal Move");
	public static final FunctionId F_START_PAN = new FunctionId(GROUP, "Start pan");
	public static final FunctionId F_VERTICAL_ROTATE = new FunctionId(GROUP, "Vertical Rotate");
	public static final FunctionId F_HORIZONTAL_ROTATE = new FunctionId(GROUP, "Horizontal Rotate");
	public static final FunctionId F_ZOOM = new FunctionId(GROUP, "Zoom");
	//num pad short cuts for camera POV
	public static final FunctionId F_POV_FRONT = new FunctionId(GROUP, "Front POV");
	public static final FunctionId F_POV_BACK = new FunctionId(GROUP, "Back POV");
	public static final FunctionId F_POV_LEFT = new FunctionId(GROUP, "Laft POV");
	public static final FunctionId F_POV_RIGHT = new FunctionId(GROUP, "Right POV");
	public static final FunctionId F_POV_TOP = new FunctionId(GROUP, "Top POV");
	public static final FunctionId F_POV_BOTTOM = new FunctionId(GROUP, "Bottom POV");
	public static final FunctionId F_POV_ROTATE_LEFT = new FunctionId(GROUP, "Rotate POV Left");
	public static final FunctionId F_POV_ROTATE_RIGHT = new FunctionId(GROUP, "Rotate POV Right");
	public static final FunctionId F_POV_ROTATE_TOP = new FunctionId(GROUP, "Rotate POV Top");
	public static final FunctionId F_POV_ROTATE_BOTTOM = new FunctionId(GROUP, "Rotate POV Bottom");
	private final static float ROT_FACTOR = 0.1f;
	static Logger log = LoggerFactory.getLogger(CameraState.class);
	float time = 0;
	Quaternion tmpQuat = new Quaternion();
	private VersionedHolder<String> cameraPositionDebugDisplay;
	private VersionedHolder<String> cameraTargetPositionDebugDisplay;
	private Camera camera;
	private Node target;
	private CameraNode cameraNode;
	private Quaternion futureTargetRot;
	private Quaternion startTargetRot;
	private Vector3f futureTargetPos;
	private Vector3f startTargetPos;
	private float delay = 0.25f;
	protected Vector3f xDelta = new Vector3f();
	protected Vector3f yDelta = new Vector3f();
	protected Vector2f lastCursor = new Vector2f();
	private Vector3f tmpVec = new Vector3f();
	private Vector3f tmpVec2 = new Vector3f();
	private Quaternion tmpRot = new Quaternion();
	private Quaternion tmpRot2 = new Quaternion();
	private Quaternion tmpRot3 = new Quaternion();

	private InputMapper inputMapper;

	public CameraState() {
		this(true);
	}

	public CameraState(boolean enabled) {
		setEnabled(enabled);
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	@Override
	protected void initialize(Application app) {

		if (this.camera == null) {
			this.camera = app.getCamera();
		}

		target = new Node("camera target");
		cameraNode = new CameraNode("camera holder", camera);
		cameraNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
		cameraNode.setEnabled(false);
		target.attachChild(cameraNode);

		if (inputMapper == null) {
			inputMapper = GuiGlobals.getInstance().getInputMapper();
		}

		InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();
		inputMapper.addAnalogListener(this
				, F_VERTICAL_MOVE
				, F_HORIZONTAL_MOVE
				, F_VERTICAL_ROTATE
				, F_HORIZONTAL_ROTATE
				, F_ZOOM);

		if (!inputMapper.hasMappings(F_VERTICAL_ROTATE) && !inputMapper.hasMappings(F_HORIZONTAL_ROTATE)) {
			log.info("Initializing default mappings for:" + F_VERTICAL_ROTATE + " and " + F_HORIZONTAL_ROTATE);
			inputMapper.map(F_HORIZONTAL_ROTATE, Axis.MOUSE_X, Button.MOUSE_BUTTON2);
			inputMapper.map(F_VERTICAL_ROTATE, Axis.MOUSE_Y, Button.MOUSE_BUTTON2);
		}

		if (!inputMapper.hasMappings(F_ZOOM)) {
			log.info("Initializing default mappings for:" + F_ZOOM);
			inputMapper.map(F_ZOOM, Axis.MOUSE_WHEEL);
		}
	}

	@Override
	protected void cleanup(Application app) {
		inputMapper.removeAnalogListener(this
				, F_VERTICAL_MOVE
				, F_HORIZONTAL_MOVE
				, F_VERTICAL_ROTATE
				, F_HORIZONTAL_ROTATE
				, F_ZOOM);
	}

	@Override
	protected void onEnable() {
		log.info(getClass().getName() + " Enabled");
		inputMapper = GuiGlobals.getInstance().getInputMapper();
		inputMapper.activateGroup(GROUP);
		cameraNode.lookAt(target.getWorldTranslation(), Vector3f.UNIT_Y);
		cameraNode.setEnabled(true);

	}

	@Override
	protected void onDisable() {
		log.info(getClass().getName() + " Disabled");
		inputMapper.deactivateGroup(GROUP);

		target.removeFromParent();
		cameraNode.setEnabled(false);
	}

	@Override
	public void update(float tpf) {

		cameraNode.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		target.updateLogicalState(tpf);
		target.updateGeometricState();

		if (futureTargetRot != null) {
			time += tpf;
			if (time < delay) {
				tmpQuat.set(startTargetRot).nlerp(futureTargetRot, time / delay);
				target.setLocalRotation(tmpQuat);
			} else {
				time = 0;
				target.setLocalRotation(futureTargetRot);
				futureTargetRot = null;
			}
		}
		if (futureTargetPos != null) {
			time += tpf;
			if (time < delay) {
				Vector3f v = new Vector3f();
				v.set(startTargetPos);
				log.warn(startTargetPos.toString());
				log.warn(futureTargetPos.toString());
				v.interpolateLocal(futureTargetPos, time / delay);
				target.setLocalTranslation(v);
			} else {
				time = 0;
				target.setLocalTranslation(futureTargetPos);
				futureTargetPos = null;
			}
		}
	}

	@Override
	public void postRender() {
	}

	@Override
	public void valueActive(FunctionId func, double value, double tpf) {
		if (func == F_HORIZONTAL_MOVE || func == F_VERTICAL_MOVE) {
			Vector2f cursor = getApplication().getInputManager().getCursorPosition();
			float x = cursor.getX() - lastCursor.x;
			float y = cursor.getY() - lastCursor.y;

			target.move(xDelta.mult(-x).addLocal(yDelta.mult(-y)));

			lastCursor.set(cursor);
		} else if (func == F_HORIZONTAL_ROTATE || func == F_VERTICAL_ROTATE) {
			//Extract horizontal rotation
			//get left vector and cross product it with unitY (to "flatten" the direction on the horizontal plane)
			target.getLocalRotation().getRotationColumn(0, tmpVec).crossLocal(Vector3f.UNIT_Y);
			tmpRot.lookAt(tmpVec, Vector3f.UNIT_Y);

			//Extract vertical rotation
			//get the direction of the rotation
			//rotate it by the inverse horizontal rotation.
			//get the rotation from that vector.
			target.getLocalRotation().getRotationColumn(2, tmpVec);
			tmpRot3.set(tmpRot).inverseLocal().mult(tmpVec, tmpVec);
			//Finding the up axis (negating it when we go backward to be able to completely rotate aroudn the target)
			tmpVec2.set(Vector3f.UNIT_Y).multLocal(FastMath.sign(tmpVec.getZ()));
			tmpRot2.lookAt(tmpVec, tmpVec2);

			//computing the additional rotation and combining it the right orders
			if (func == F_HORIZONTAL_ROTATE) {
				//the incremental horizontal rotation.
				tmpRot3.fromAngleAxis((float) -value * ROT_FACTOR, Vector3f.UNIT_Y);
				//applying the horizontal incremental rotation on the horizontal rotation.
				tmpRot.multLocal(tmpRot3);
				//applying the vertical rotation on the resulting horizontal rotation.
				tmpRot.multLocal(tmpRot2);
			} else {
				//the incremental vertical rotation
				tmpRot3.fromAngleAxis((float) value * ROT_FACTOR, Vector3f.UNIT_X);
				//applying the incremental vertical rotation on the vertical rotation.
				tmpRot2.multLocal(tmpRot3);
				//Applying the resulting vertical rotation on the horizontal rotation.
				tmpRot.multLocal(tmpRot2);
			}

			//Setting the new rotation
			target.setLocalRotation(tmpRot);
		} else if (func == F_ZOOM) {
			float factor = Math.min(cameraNode.getLocalTranslation().z * 0.1f, 2);
			tmpVec.set(cameraNode.getLocalTranslation()).addLocal(0, 0, (float) -value * factor);
			tmpVec.z = Math.max(tmpVec.z, 1f);
			cameraNode.setLocalTranslation(tmpVec);
		}
	}

	@Override
	public void valueChanged(FunctionId func, InputState value, double tpf) {
		if (func == F_START_PAN && value == InputState.Positive) {

			Vector3f up = camera.getUp();
			Vector3f right = camera.getLeft().negate();

			Vector3f originScreen = camera.getScreenCoordinates(target.getWorldTranslation());
			Vector3f xScreen = camera.getScreenCoordinates(target.getWorldTranslation().add(right));
			Vector3f yScreen = camera.getScreenCoordinates(target.getWorldTranslation().add(up));

			float x = xScreen.x - originScreen.x;
			float y = yScreen.y - originScreen.y;

			xDelta.set(right).divideLocal(x);
			yDelta.set(up).divideLocal(y);

			lastCursor.set(getApplication().getInputManager().getCursorPosition());
		}
	}

}