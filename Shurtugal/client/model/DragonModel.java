/*
 ** 2011 December 10
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */

package Shurtugal.client.model;

import Shurtugal.common.entity.LifeStage;
import Shurtugal.client.model.anim.DragonAnimator;
import Shurtugal.common.entity.Dragon.EntityBaseDragon;
import Shurtugal.common.util.MathX;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

import org.lwjgl.opengl.GL11;

/**
 * Generic model for all winged tetrapod dragons.
 * 
 * @author Iamshortman
 */
public class DragonModel extends ModelBase
{

	// model constants
	public static int NECK_SIZE = 10;
	public static int TAIL_SIZE = 10;
	public static int VERTS_NECK = 6;
	public static int VERTS_TAIL = 12;
	public static int HEAD_OFS = -16;

	// model parts
	public ModelPart head;
	public ModelPart neck;
	public ModelPart neckScale;
	public ModelPart tail;
	public ModelPart tailHornLeft;
	public ModelPart tailHornRight;
	public ModelPart tailScaleLeft;
	public ModelPart tailScaleMiddle;
	public ModelPart tailScaleRight;
	public ModelPart jaw;
	public ModelPart body;
	public ModelPart back;
	public ModelPart forethigh;
	public ModelPart forecrus;
	public ModelPart forefoot;
	public ModelPart foretoe;
	public ModelPart hindthigh;
	public ModelPart hindcrus;
	public ModelPart hindfoot;
	public ModelPart hindtoe;
	public ModelPart wingArm;
	public ModelPart wingForearm;
	public ModelPart[] wingFinger = new ModelPart[4];

	// model attributes
	public ModelPartProxy[] neckProxy = new ModelPartProxy[VERTS_NECK];
	public ModelPartProxy[] tailProxy = new ModelPartProxy[VERTS_TAIL];
	public ModelPartProxy[] thighProxy = new ModelPartProxy[4];

	public int renderPass = -1;
	public float offsetX;
	public float offsetY;
	public float offsetZ;
	public float pitch;
	public float size;
	public EntityBaseDragon Dragon;

	public DragonModel()
	{
		this(0.0F);
	}

	public DragonModel(float scale)
	{
		textureWidth = 256;
		textureHeight = 256;

		setTextureOffset("body.body", 0, 0);
		setTextureOffset("body.scale", 0, 32);
		setTextureOffset("head.nostril", 48, 0);
		setTextureOffset("head.upperhead", 0, 0);
		setTextureOffset("head.upperjaw", 56, 88);
		setTextureOffset("head.lowerjaw", 0, 88);
		setTextureOffset("head.horn", 28, 32);
		setTextureOffset("forethigh.main", 112, 0);
		setTextureOffset("forecrus.main", 148, 0);
		setTextureOffset("forefoot.main", 210, 0);
		setTextureOffset("foretoe.main", 176, 0);
		setTextureOffset("hindthigh.main", 112, 29);
		setTextureOffset("hindcrus.main", 152, 29);
		setTextureOffset("hindfoot.main", 180, 29);
		setTextureOffset("hindtoe.main", 215, 29);
		setTextureOffset("neck.box", 112, 88);
		setTextureOffset("neck.scale", 0, 0);
		setTextureOffset("tail.box", 152, 88);
		setTextureOffset("tail.scale", 0, 0);
		setTextureOffset("tail.horn", 0, 117);
		setTextureOffset("wingarm.bone", 0, 152);
		setTextureOffset("wingarm.skin", 116, 232);
		setTextureOffset("wingfinger.bone", 0, 172);
		setTextureOffset("wingfinger.shortskin", -32, 224);
		setTextureOffset("wingfinger.skin", -49, 176);
		setTextureOffset("wingforearm.bone", 0, 164);

		buildBody(scale);
		buildNeck(scale);
		buildHead(scale);
		buildTail(scale);
		buildWing();
		buildLegs(scale);
	}

	private void buildHead(float scale)
	{
		head = new ModelPart(this, "head");
		head.addBox("upperjaw", -6, -1, -8 + HEAD_OFS, 12, 5, 16, 56, 88, scale);

		head.addBox("upperhead", -8, -8, 6 + HEAD_OFS, 16, 16, 16, 0, 0, scale);
		head.addBox("nostril", -5, -3, -6 + HEAD_OFS, 2, 2, 4, 48, 0, scale);
		head.mirror = true;
		head.addBox("nostril", 3, -3, -6 + HEAD_OFS, 2, 2, 4, 48, 0, scale);
		buildHorn(scale, false);
		buildHorn(scale, true);

		jaw = head.addChildBox("lowerjaw", -6, 0, -16, 12, 4, 16, 0, 88, scale);
		jaw.setRotationPoint(0, 4, 8 + HEAD_OFS);
	}

	private void buildHorn(float scale, boolean mirror)
	{
		int hornThick = 3;
		int hornLength = 12;

		float hornOfs = -(hornThick / 2f);

		float hornPosX = -5;
		float hornPosY = -8;
		float hornPosZ = 0;

		float hornRotX = MathX.toRadians(30);
		float hornRotY = MathX.toRadians(-30);
		float hornRotZ = 0;

		if (mirror)
		{
			hornPosX *= -1;
			hornRotY *= -1;
		}

		head.mirror = mirror;
		ModelPart horn = head.addChildBox("horn", hornOfs, hornOfs, hornOfs, hornThick, hornThick, hornLength, 28, 32, scale);
		horn.setRotationPoint(hornPosX, hornPosY, hornPosZ);
		horn.setAngles(hornRotX, hornRotY, hornRotZ);
	}

	public void buildNeck(float scale)
	{
		neck = new ModelPart(this, "neck");
		neck.addBox("box", -5, -5, -5, NECK_SIZE, NECK_SIZE, NECK_SIZE, 112, 88, scale);
		neckScale = neck.addChildBox("scale", -1, -7, -3, 2, 4, 6, 0, 0, scale);

		// initialize model proxies
		for (int i = 0; i < neckProxy.length; i++)
		{
			neckProxy[i] = new ModelPartProxy(neck);
		}
	}

	private void buildTail(float scale)
	{
		tail = new ModelPart(this, "tail");
		tail.addBox("box", -5, -5, -5, TAIL_SIZE, TAIL_SIZE, TAIL_SIZE, 152, 88, scale);
		float scaleRotZ = MathX.toRadians(45);
		tailScaleLeft = tail.addChildBox("scale", -1, -8, -3, 2, 4, 6, 0, 0, scale).setAngles(0, 0, scaleRotZ);
		tailScaleMiddle = tail.addChildBox("scale", -1, -8, -3, 2, 4, 6, 0, 0, scale).setAngles(0, 0, 0);
		tailScaleRight = tail.addChildBox("scale", -1, -8, -3, 2, 4, 6, 0, 0, scale).setAngles(0, 0, -scaleRotZ);

		// tailScaleMiddle.showModel = false;
		tailScaleLeft.showModel = false;
		tailScaleRight.showModel = false;

		buildTailHorn(scale, false);
		buildTailHorn(scale, true);

		// initialize model proxies
		for (int i = 0; i < tailProxy.length; i++)
		{
			tailProxy[i] = new ModelPartProxy(tail);
		}
	}

	private void buildTailHorn(float scale, boolean mirror)
	{
		int hornThick = 3;
		int hornLength = 32;

		float hornOfs = -(hornThick / 2f);

		float hornPosX = 0;
		float hornPosY = hornOfs;
		float hornPosZ = TAIL_SIZE / 2f;

		float hornRotX = MathX.toRadians(-15);
		float hornRotY = MathX.toRadians(-145);
		float hornRotZ = 0;

		if (mirror)
		{
			hornPosX *= -1;
			hornRotY *= -1;
		}

		tail.mirror = mirror;
		ModelPart horn = tail.addChildBox("horn", hornOfs, hornOfs, hornOfs, hornThick, hornThick, hornLength, 0, 117, scale);
		horn.setRotationPoint(hornPosX, hornPosY, hornPosZ);
		horn.setAngles(hornRotX, hornRotY, hornRotZ);
		horn.isHidden = true;

		if (mirror)
		{
			tailHornLeft = horn;
		}
		else
		{
			tailHornRight = horn;
		}
	}

	private void buildBody(float scale)
	{
		body = new ModelPart(this, "body");
		body.setRotationPoint(0, 4, 8);
		body.addBox("body", -12, 0, -16, 24, 24, 64, 0, 0, scale);
		body.addBox("scale", -1, -6, 10, 2, 6, 12, 0, 32, scale);
		body.addBox("scale", -1, -6, 30, 2, 6, 12, 0, 32, scale);

		back = body.addChildBox("scale", -1, -6, -10, 2, 6, 12, 0, 32, scale);
	}

	private void buildWing()
	{
		wingArm = new ModelPart(this, "wingarm");
		wingArm.setRotationPoint(-10, 5, 4);
		wingArm.setRenderScale(1.1f);
		wingArm.addBox("bone", -28, -3, -3, 28, 6, 6);
		wingArm.addBox("skin", -28, 0, 2, 28, 0, 24);

		wingForearm = new ModelPart(this, "wingforearm");
		wingForearm.setRotationPoint(-28, 0, 0);
		wingForearm.addBox("bone", -48, -2, -2, 48, 4, 4);
		wingArm.addChild(wingForearm);

		wingFinger[0] = buildWingFinger(false);
		wingFinger[1] = buildWingFinger(false);
		wingFinger[2] = buildWingFinger(false);
		wingFinger[3] = buildWingFinger(true);
	}

	private ModelPart buildWingFinger(boolean small)
	{
		ModelPart finger = new ModelPart(this, "wingfinger");
		finger.setRotationPoint(-47, 0, 0);
		finger.addBox("bone", -70, -1, -1, 70, 2, 2);
		if (small)
		{
			finger.addBox("shortskin", -70, 0, 1, 70, 0, 32);
		}
		else
		{
			finger.addBox("skin", -70, 0, 1, 70, 0, 48);
		}
		wingForearm.addChild(finger);

		return finger;
	}

	private void buildLegs(float scale)
	{
		buildLeg(scale, false);
		buildLeg(scale, true);

		// initialize model proxies
		for (int i = 0; i < 4; i++)
		{
			if (i % 2 == 0)
			{
				thighProxy[i] = new ModelPartProxy(forethigh);
			}
			else
			{
				thighProxy[i] = new ModelPartProxy(hindthigh);
			}
		}
	}

	private void buildLeg(float scale, boolean hind)
	{
		float baseLength = 26;
		String baseName = hind ? "hind" : "fore";

		// thigh variables
		float thighPosX = -11;
		float thighPosY = 18;
		float thighPosZ = 4;

		int thighThick = 9;
		int thighLength = (int) (baseLength * (hind ? 0.9f : 0.77f));

		if (hind)
		{
			thighThick++;
			thighPosY -= 5;
		}

		float thighOfs = -(thighThick / 2f);

		int Textofx = 112;
		int Textofy = hind ? 29 : 0;

		ModelPart thigh = new ModelPart(this, baseName + "thigh");
		thigh.setRotationPoint(thighPosX, thighPosY, thighPosZ);
		thigh.addBox("main", thighOfs, thighOfs, thighOfs, thighThick, thighLength, thighThick, Textofx, Textofy, scale);

		// crus variables
		float crusPosX = 0;
		float crusPosY = thighLength + thighOfs;
		float crusPosZ = 0;

		int crusThick = thighThick - 2;
		int crusLength = (int) (baseLength * (hind ? 0.7f : 0.8f));

		if (hind)
		{
			crusThick--;
			crusLength -= 2;
		}

		float crusOfs = -(crusThick / 2f);

		Textofx = hind ? 152 : 148;
		Textofy = hind ? 29 : 0;

		ModelPart crus = new ModelPart(this, baseName + "crus");
		crus.setRotationPoint(crusPosX, crusPosY, crusPosZ);
		crus.addBox("main", crusOfs, crusOfs, crusOfs, crusThick, crusLength, crusThick, Textofx, Textofy, scale);
		thigh.addChild(crus);

		// foot variables
		float footPosX = 0;
		float footPosY = crusLength + (crusOfs / 2f);
		float footPosZ = 0;

		int footWidth = crusThick + 2;
		int footHeight = 4;
		int footLength = (int) (baseLength * (hind ? 0.67f : 0.34f));

		float footOfsX = -(footWidth / 2f);
		float footOfsY = -(footHeight / 2f);
		float footOfsZ = footLength * -0.75f;

		Textofx = hind ? 180 : 210;
		Textofy = hind ? 29 : 0;

		ModelPart foot = new ModelPart(this, baseName + "foot");
		foot.setRotationPoint(footPosX, footPosY, footPosZ);
		foot.addBox("main", footOfsX, footOfsY, footOfsZ, footWidth, footHeight, footLength, Textofx, Textofy, scale);
		crus.addChild(foot);

		// toe variables
		int toeWidth = footWidth;
		int toeHeight = footHeight;
		int toeLength = (int) (baseLength * (hind ? 0.27f : 0.33f));

		float toePosX = 0;
		float toePosY = 0;
		float toePosZ = footOfsZ - (footOfsY / 2f);

		float toeOfsX = -(toeWidth / 2f);
		float toeOfsY = -(toeHeight / 2f);
		float toeOfsZ = -toeLength;

		Textofx = hind ? 215 : 176;
		Textofy = hind ? 29 : 0;

		ModelPart toe = new ModelPart(this, baseName + "toe");
		toe.setRotationPoint(toePosX, toePosY, toePosZ);
		toe.addBox("main", toeOfsX, toeOfsY, toeOfsZ, toeWidth, toeHeight, toeLength, Textofx, Textofy, scale);
		foot.addChild(toe);

		if (hind)
		{
			hindthigh = thigh;
			hindcrus = crus;
			hindfoot = foot;
			hindtoe = toe;
		}
		else
		{
			forethigh = thigh;
			forecrus = crus;
			forefoot = foot;
			foretoe = toe;
		}
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third
	 * float params here are the same second and third as in the
	 * setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase entity, float moveTime, float moveSpeed, float partialTicks)
	{
		setLivingAnimations((EntityBaseDragon) entity, moveTime, moveSpeed, partialTicks);
	}

	public void setLivingAnimations(EntityBaseDragon dragon, float moveTime, float moveSpeed, float partialTicks)
	{
		DragonAnimator animator = dragon.getAnimator();
		animator.setPartialTicks(partialTicks);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(Entity entity, float moveTime, float moveSpeed, float ticksExisted, float lookYaw, float lookPitch, float scale)
	{
		render((EntityBaseDragon) entity, moveTime, moveSpeed, ticksExisted, lookYaw, lookPitch, scale);
	}

	public void render(EntityBaseDragon dragon, float moveTime, float moveSpeed, float ticksExisted, float lookYaw, float lookPitch, float scale)
	{
		this.Dragon = dragon;
		DragonAnimator animator = dragon.getAnimator();
		animator.setMovement(moveTime, moveSpeed);
		animator.setLook(lookYaw, lookPitch);
		animator.setTicksExisted(ticksExisted);
		animator.animate(this);

		size = dragon.getSize();

		renderModel(dragon, scale);
	}

	/**
	 * Renders the model after all animations are applied.
	 */
	public void renderModel(EntityBaseDragon dragon, float scale)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(offsetX, offsetY, offsetZ);
		GL11.glRotatef(-pitch, 1, 0, 0);

		if (renderPass == 5)
		{
			GL11.glScalef(1.1f, 1.1f, 1.1f);
			GL11.glTranslatef(0, -0.1f, 0);
			renderBody(scale);
		}
		else
		{
			renderHead(scale);
			renderNeck(scale);
			renderBody(scale);
			renderLegs(scale);
			renderTail(scale);
			renderWings(scale);
		}

		GL11.glPopMatrix();
	}

	protected void renderBody(float scale)
	{
		body.render(scale);
	}

	public void renderHead(float scale)
	{
		float headScale = 1.4f / (size + 0.4f);
		if (this.Dragon.lifeStage == LifeStage.ELDER)
		{
			headScale += 0.2f;
		}

		head.setRenderScale(headScale);
		head.render(scale);
	}

	protected void renderNeck(float scale)
	{
		for (ModelPartProxy proxy : neckProxy)
		{
			proxy.render(scale);
		}
	}

	protected void renderTail(float scale)
	{
		for (ModelPartProxy proxy : tailProxy)
		{
			proxy.render(scale);
		}
	}

	protected void renderWings(float scale)
	{
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_FRONT);

		for (int i = 0; i < 2; i++)
		{
			wingArm.render(scale);

			if (i == 0)
			{
				// mirror next wing
				GL11.glScalef(-1, 1, 1);
				// switch to back face culling
				GL11.glCullFace(GL11.GL_BACK);
			}
		}

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	protected void renderLegs(float scale)
	{
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		for (int i = 0; i < thighProxy.length; i++)
		{
			thighProxy[i].render(scale);

			if (i == 1)
			{
				// mirror next legs
				GL11.glScalef(-1, 1, 1);
				// switch to front face culling
				GL11.glCullFace(GL11.GL_FRONT);
			}
		}

		GL11.glCullFace(GL11.GL_BACK);
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
}