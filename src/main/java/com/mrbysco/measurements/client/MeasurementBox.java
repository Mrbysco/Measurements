package com.mrbysco.measurements.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrbysco.measurements.config.LineColor;
import com.mrbysco.measurements.config.MeasurementConfig;
import com.mrbysco.measurements.config.TextColor;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
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

		this.lineColor = ((LineColor) MeasurementConfig.CLIENT.lineColor.get()).getColor(ClientHandler.random);
		TextColor textColor = ((TextColor) MeasurementConfig.CLIENT.textColor.get());
		if (textColor == TextColor.XYZRGB) {
			this.textX = ((TextColor) MeasurementConfig.CLIENT.textColor.get()).getColor(ClientHandler.random, Direction.Axis.X);
			this.textY = ((TextColor) MeasurementConfig.CLIENT.textColor.get()).getColor(ClientHandler.random, Direction.Axis.Y);
			this.textZ = ((TextColor) MeasurementConfig.CLIENT.textColor.get()).getColor(ClientHandler.random, Direction.Axis.Z);
		} else {
			DyeColor color = textColor.getColor(ClientHandler.random, null);
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
		if (!dimensionKey.location().equals(currentDimensionKey.location())) return;

		final float[] color = this.lineColor.getTextureDiffuseColors();
		final float r = color[0];
		final float g = color[1];
		final float b = color[2];
		final float a = 0.95F;

		Vec3 pos = camera.getPosition();

		double distance = box.getCenter().distanceTo(pos);
		float lineWidth = MeasurementConfig.CLIENT.lineWidth.get().floatValue();
		if (distance > 48) {
			lineWidth = MeasurementConfig.CLIENT.lineWidthMax.get();
		}

		MultiBufferSource.BufferSource bufferSource = renderBuffers.bufferSource();

		poseStack.pushPose();
		final RenderType renderType = LineRenderType.lineRenderType(lineWidth);
		VertexConsumer builder = bufferSource.getBuffer(renderType);
		//Translate negative camera position
		poseStack.translate(-pos.x, -pos.y, -pos.z);
		//Render the outline
		LevelRenderer.renderLineBox(poseStack, builder, box, r, g, b, a);
		bufferSource.endBatch(renderType);
		poseStack.popPose();

		//Render the line length text
		drawLength(poseStack, camera, projection);
	}

	private void drawLength(PoseStack poseStack, Camera camera, Matrix4f projection) {
		final int lengthX = (int) box.getXsize();
		final int lengthY = (int) box.getYsize();
		final int lengthZ = (int) box.getZsize();

		final Vec3 pos = camera.getPosition();

		final Frustum clippingHelper = new Frustum(poseStack.last().pose(), projection);
		clippingHelper.prepare(pos.x, pos.y, pos.z);

		AABB boxT = box.inflate(0.08f);

		List<Line> lines = new ArrayList<>();
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.minY, boxT.minZ, boxT.minX, boxT.minY, boxT.maxZ), pos, clippingHelper));
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.maxY, boxT.minZ, boxT.minX, boxT.maxY, boxT.maxZ), pos, clippingHelper));
		lines.add(Line.createLine(new AABB(boxT.maxX, boxT.minY, boxT.minZ, boxT.maxX, boxT.minY, boxT.maxZ), pos, clippingHelper));
		lines.add(Line.createLine(new AABB(boxT.maxX, boxT.maxY, boxT.minZ, boxT.maxX, boxT.maxY, boxT.maxZ), pos, clippingHelper));
		Collections.sort(lines);
		final Vec3 lineZ = lines.get(0).line.getCenter();

		lines.clear();
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.minY, boxT.minZ, boxT.minX, boxT.maxY, boxT.minZ), pos, clippingHelper));
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.minY, boxT.maxZ, boxT.minX, boxT.maxY, boxT.maxZ), pos, clippingHelper));
		lines.add(Line.createLine(new AABB(boxT.maxX, boxT.minY, boxT.minZ, boxT.maxX, boxT.maxY, boxT.minZ), pos, clippingHelper));
		lines.add(Line.createLine(new AABB(boxT.maxX, boxT.minY, boxT.maxZ, boxT.maxX, boxT.maxY, boxT.maxZ), pos, clippingHelper));
		Collections.sort(lines);
		final Vec3 lineY = lines.get(0).line.getCenter();

		lines.clear();
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.minY, boxT.minZ, boxT.maxX, boxT.minY, boxT.minZ), pos, clippingHelper));
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.minY, boxT.maxZ, boxT.maxX, boxT.minY, boxT.maxZ), pos, clippingHelper));
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.maxY, boxT.minZ, boxT.maxX, boxT.maxY, boxT.minZ), pos, clippingHelper));
		lines.add(Line.createLine(new AABB(boxT.minX, boxT.maxY, boxT.maxZ, boxT.maxX, boxT.maxY, boxT.maxZ), pos, clippingHelper));
		Collections.sort(lines);
		final Vec3 lineX = lines.get(0).line.getCenter();
		lines.clear();

		poseStack.pushPose();
		poseStack.translate(-pos.x, -pos.y, -pos.z);

		drawText(poseStack, camera, new Vec3(lineX.x, lineX.y, lineX.z), String.valueOf(lengthX), this.textX);
		drawText(poseStack, camera, new Vec3(lineY.x, lineY.y, lineY.z), String.valueOf(lengthY), this.textY);
		drawText(poseStack, camera, new Vec3(lineZ.x, lineZ.y, lineZ.z), String.valueOf(lengthZ), this.textZ);
		poseStack.popPose();
	}

	private void drawText(PoseStack poseStack, Camera camera, Vec3 pos, String length, DyeColor textColor) {
		final Font font = Minecraft.getInstance().font;

		final float size = MeasurementConfig.CLIENT.textSize.get().floatValue();

		poseStack.pushPose();
		poseStack.translate(pos.x, pos.y + size * 5.0, pos.z);
		poseStack.mulPose(camera.rotation());
		poseStack.scale(-size, -size, -size);
		poseStack.translate(-font.width(length) / 2f, 0, 0);
		font.draw(poseStack, length, 0, 0, textColor.getTextColor());
		poseStack.popPose();
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished() {
		this.finished = true;
	}

	private record Line(AABB line, boolean isVisible, double distance) implements Comparable<Line> {

		public static Line createLine(AABB line, Vec3 pos, Frustum clippingHelper) {
			return new Line(line, clippingHelper.isVisible(line), line.getCenter().distanceTo(pos));
		}

		@Override
		public int compareTo(@NotNull Line l) {
			if (isVisible) {
				return l.isVisible ? Double.compare(distance, l.distance) : -1;
			} else {
				return l.isVisible ? 1 : 0;
			}
		}
	}
}
