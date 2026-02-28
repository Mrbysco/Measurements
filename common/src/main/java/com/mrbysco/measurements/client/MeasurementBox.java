package com.mrbysco.measurements.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mrbysco.measurements.config.TextColor;
import com.mrbysco.measurements.platform.Services;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Credit: MadeBaruna - https://github.com/MadeBaruna/BlockMeter/blob/master/src/main/java/win/baruna/blockmeter/MeasureBox.java
public class MeasurementBox {
	private final BlockPos startPos;
	private BlockPos endPos;
	public AABB box;
	private final ResourceKey<Level> dimensionKey;
	private boolean finished;
	private final DyeColor lineColor;
	private final DyeColor textX;
	private final DyeColor textY;
	private final DyeColor textZ;

	MeasurementBox(BlockPos block, ResourceKey<Level> dimensionKey) {
		this.startPos = block;
		this.endPos = block;
		this.dimensionKey = dimensionKey;
		this.finished = false;

		this.lineColor = Services.PLATFORM.getLineColor().getColor(BoxHandler.random);
		TextColor textColor = Services.PLATFORM.getTextColor();
		if (textColor == TextColor.XYZRGB) {
			this.textX = Services.PLATFORM.getTextColor().getColor(BoxHandler.random, Direction.Axis.X);
			this.textY = Services.PLATFORM.getTextColor().getColor(BoxHandler.random, Direction.Axis.Y);
			this.textZ = Services.PLATFORM.getTextColor().getColor(BoxHandler.random, Direction.Axis.Z);
		} else {
			DyeColor color = textColor.getColor(BoxHandler.random, null);
			this.textX = color;
			this.textY = color;
			this.textZ = color;
		}

		this.setBoundingBox();
	}

	private void setBoundingBox() {
		final int ax = this.startPos.getX();
		final int ay = this.startPos.getY();
		final int az = this.startPos.getZ();
		final int bx = this.endPos.getX();
		final int by = this.endPos.getY();
		final int bz = this.endPos.getZ();

		this.box = new AABB(Math.min(ax, bx), Math.min(ay, by), Math.min(az, bz),
				Math.max(ax, bx) + 1, Math.max(ay, by) + 1, Math.max(az, bz) + 1);
	}

	public void setBlockEnd(BlockPos blockEnd) {
		this.endPos = blockEnd;
		this.setBoundingBox();
	}

	public void render(ResourceKey<Level> currentDimensionKey, PoseStack poseStack, RenderBuffers renderBuffers, Camera camera, Matrix4f projection) {
		if (!dimensionKey.identifier().equals(currentDimensionKey.identifier())) return;

		int color = this.lineColor.getTextureDiffuseColor();
		final float r = (float) ARGB.red(color) / 255F;
		final float g = (float) ARGB.green(color) / 255F;
		final float b = (float) ARGB.blue(color) / 255F;
		final float a = 0.95F;

		Vec3 pos = camera.position();

		double distance = box.getCenter().distanceTo(pos);
		float lineWidth = Services.PLATFORM.getLineWidth();
		if (distance > 48) {
			lineWidth = Services.PLATFORM.getLineWidthMax();
		}

		MultiBufferSource.BufferSource bufferSource = renderBuffers.bufferSource();

		Gizmos.cuboid(box, GizmoStyle.stroke(ARGB.colorFromFloat(a, r, g, b), lineWidth), true);
		
		//Render the line length text
		drawLength(poseStack, camera, projection, bufferSource);
	}

	private void drawLength(PoseStack poseStack, Camera camera, Matrix4f projection, MultiBufferSource.BufferSource bufferSource) {
		final int lengthX = (int) box.getXsize();
		final int lengthY = (int) box.getYsize();
		final int lengthZ = (int) box.getZsize();

		final Vec3 pos = camera.position();

		final Frustum clippingHelper = new Frustum(poseStack.last().pose(), projection);
		clippingHelper.prepare(pos.x, pos.y, pos.z);

		AABB boxT = box.inflate(0.08f);

		List<Line> lines = new ArrayList<>();
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.minY, boxT.minZ, boxT.minX, boxT.minY, boxT.maxZ), pos));
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.maxY, boxT.minZ, boxT.minX, boxT.maxY, boxT.maxZ), pos));
		lines.add(Line.createLine(new AABB(boxT.maxX, boxT.minY, boxT.minZ, boxT.maxX, boxT.minY, boxT.maxZ), pos));
		lines.add(Line.createLine(new AABB(boxT.maxX, boxT.maxY, boxT.minZ, boxT.maxX, boxT.maxY, boxT.maxZ), pos));
		Collections.sort(lines);

		AABB lineZ = lines.getFirst().line;
		var maxZ = lineZ.getMaxPosition();
		var minZ = lineZ.getMinPosition();

		lines.clear();
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.minY, boxT.minZ, boxT.minX, boxT.maxY, boxT.minZ), pos));
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.minY, boxT.maxZ, boxT.minX, boxT.maxY, boxT.maxZ), pos));
		lines.add(Line.createLine(new AABB(boxT.maxX, boxT.minY, boxT.minZ, boxT.maxX, boxT.maxY, boxT.minZ), pos));
		lines.add(Line.createLine(new AABB(boxT.maxX, boxT.minY, boxT.maxZ, boxT.maxX, boxT.maxY, boxT.maxZ), pos));
		Collections.sort(lines);

		var lineY = lines.getFirst().line;
		var maxY = lineY.getMaxPosition();
		var minY = lineY.getMinPosition();

		lines.clear();
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.minY, boxT.minZ, boxT.maxX, boxT.minY, boxT.minZ), pos));
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.minY, boxT.maxZ, boxT.maxX, boxT.minY, boxT.maxZ), pos));
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.maxY, boxT.minZ, boxT.maxX, boxT.maxY, boxT.minZ), pos));
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.maxY, boxT.maxZ, boxT.maxX, boxT.maxY, boxT.maxZ), pos));
		Collections.sort(lines);
		var lineX = lines.getFirst().line;
		var maxX = lineX.getMaxPosition();
		var minX = lineX.getMinPosition();
		lines.clear();


		var lineXPoint = calculateNearestPoint(minX, maxX, pos);
		var lineYPoint = calculateNearestPoint(minY, maxY, pos);
		var lineZPoint = calculateNearestPoint(minZ, maxZ, pos);

		poseStack.pushPose();
		poseStack.translate(-pos.x, -pos.y, -pos.z);

		drawText(poseStack, camera, new Vec3(lineXPoint.x, lineXPoint.y, lineXPoint.z), Component.literal(String.valueOf(lengthX)), this.textX, bufferSource);
		drawText(poseStack, camera, new Vec3(lineYPoint.x, lineYPoint.y, lineYPoint.z), Component.literal(String.valueOf(lengthY)), this.textY, bufferSource);
		drawText(poseStack, camera, new Vec3(lineZPoint.x, lineZPoint.y, lineZPoint.z), Component.literal(String.valueOf(lengthZ)), this.textZ, bufferSource);
		poseStack.popPose();
	}

	/**
	 * Calculates the nearest point on the line segment defined by [min, max] to the given point 'pos',
	 * ensuring the result is at least 0.5 units away from both 'min' and 'max' endpoints,
	 * provided the segment is long enough (>= 1.0 unit).
	 * If the segment is shorter than 1.0 unit, it returns the midpoint of the segment.
	 *
	 * @param min The starting point of the line segment.
	 * @param max The ending point of the line segment.
	 * @param pos The point for which to find the nearest point on the modified segment.
	 * @return The nearest point on the segment, respecting the 0.5 unit margin from each end.
	 */
	private Vec3 calculateNearestPoint(Vec3 min, Vec3 max, Vec3 pos) {
		// Calculate the vector representing the line segment from min to max
		Vec3 lineVec = max.subtract(min);

		// Calculate the squared length of the line segment.
		double lineLengthSq = lineVec.lengthSqr();

		// If min and max are effectively the same point.( Zero-length segment )
		if (lineLengthSq < Mth.EPSILON) {
			// The segment is just a point, so return that point.
			return min;
		}

		// Calculate the actual geometric length of the segment. Needed for the margin calculation.
		double lineLength = Math.sqrt(lineLengthSq);

		// If the total length is less than 1.0 (0.5 margin from min + 0.5 margin from max) then segment is too short for margins.
		if (lineLength < 1.0) {
			// The segment is too short to guarantee a 0.5 margin from both ends.
			// Fallback is to return the geometric midpoint of the original segment.
			return min.add(lineVec.scale(0.5));
		}

		// Calculate the vector from the segment start (min) to the point (pos).
		Vec3 vecFromMinToPos = pos.subtract(min);

		// Calculate the projection parameter 't'. This indicates how far along the *infinite* line
		// (passing through min and max) the projection of 'pos' lies.
		// t = 0 corresponds to 'min', t = 1 corresponds to 'max'.
		double t = vecFromMinToPos.dot(lineVec) / lineLengthSq;

		// Calculate the margin (0.5 block) as a fraction of the total segment length.
		// This converts the absolute margin distance into the 't' parameter space.
		double marginFraction = 0.5 / lineLength;

		// Clamp the parameter 't' to the range [marginFraction, 1.0 - marginFraction].
		// This ensures the final point lies on the segment but respects the margins.
		double t_clamped = Mth.clamp(t, marginFraction, 1.0 - marginFraction);

		// Calculate the final point on the segment using the clamped parameter.
		return min.add(lineVec.multiply(t_clamped,t_clamped,t_clamped));

	}


	private void drawText(PoseStack poseStack, Camera camera, Vec3 pos, Component length, DyeColor textColor, MultiBufferSource.BufferSource bufferSource) {
		final Font font = Minecraft.getInstance().font;
		final float size = Services.PLATFORM.getTextSize();

		poseStack.pushPose();
		poseStack.translate(pos.x, pos.y + size * 5.0, pos.z);
		poseStack.mulPose(camera.rotation());
		poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
		poseStack.scale(-size, -size, -size);
		poseStack.translate(-font.width(length) / 2f, 0, 0);
		Matrix4f pose = poseStack.last().pose();
		font.drawInBatch(length, 0F, 0F, textColor.getTextColor(), false, pose, bufferSource, Font.DisplayMode.SEE_THROUGH, 0, 15728880);
		poseStack.popPose();
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished() {
		this.finished = true;
	}

	private record Line(AABB line, double distance) implements Comparable<Line> {

		public static Line createLine(AABB line, Vec3 pos) {
			return new Line(line, line.distanceToSqr(pos));
		}

		@Override
		public int compareTo(@NotNull Line l) {
			return Double.compare(distance, l.distance);
		}
	}

}
