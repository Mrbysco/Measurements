package com.mrbysco.measurements.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.item.DyeColor;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Credit: MadeBaruna - https://github.com/MadeBaruna/BlockMeter/blob/master/src/main/java/win/baruna/blockmeter/MeasureBox.java
public class MeasurementBox {
	private final BlockPos startPos;
	private BlockPos endPos;
	public AxisAlignedBB box;
	private final RegistryKey<World> dimensionKey;
	private boolean finished;
	private final DyeColor color;

	MeasurementBox(BlockPos block, RegistryKey<World> dimensionKey) {
		this.startPos = block;
		this.endPos = block;
		this.dimensionKey = dimensionKey;
		this.finished = false;

		this.color = DyeColor.YELLOW;

		this.setBoundingBox();
	}

	private void setBoundingBox() {
		final int ax = this.startPos.getX();
		final int ay = this.startPos.getY();
		final int az = this.startPos.getZ();
		final int bx = this.endPos.getX();
		final int by = this.endPos.getY();
		final int bz = this.endPos.getZ();

		this.box = new AxisAlignedBB(Math.min(ax, bx), Math.min(ay, by), Math.min(az, bz),
				Math.max(ax, bx) + 1, Math.max(ay, by) + 1, Math.max(az, bz) + 1);
	}

	public void setBlockEnd(BlockPos blockEnd) {
		this.endPos = blockEnd;
		this.setBoundingBox();
	}

	public void render(RegistryKey<World> currentDimensionKey, MatrixStack matrixStack, RenderTypeBuffers buffer, ActiveRenderInfo renderInfo, Matrix4f projection) {
		if (!dimensionKey.location().equals(currentDimensionKey.location())) return;

		final float[] color = this.color.getTextureDiffuseColors();
		final float r = color[0];
		final float g = color[1];
		final float b = color[2];
		final float a = 0.95F;

		Vector3d pos = renderInfo.getPosition();
		double distance = box.getCenter().distanceTo(pos);
		float lineWidth = 2f;
		if (distance > 48) {
			lineWidth = 1.0f;
		}

		matrixStack.pushPose();
		IRenderTypeBuffer.Impl bufferSource = buffer.bufferSource();
		IVertexBuilder builder = bufferSource.getBuffer(LineRenderType.lineRenderType(lineWidth));

		matrixStack.translate(-pos.x, -pos.y, -pos.z);

		WorldRenderer.renderLineBox(matrixStack, builder, box, r, g, b, a);
		bufferSource.endBatch(LineRenderType.lineRenderType(lineWidth));
		matrixStack.popPose();
		drawLength(matrixStack, buffer, renderInfo, projection);
	}

	private void drawLength(MatrixStack matrixStack, RenderTypeBuffers buffers, ActiveRenderInfo renderInfo, Matrix4f projection) {
		final int lengthX = (int) box.getXsize();
		final int lengthY = (int) box.getYsize();
		final int lengthZ = (int) box.getZsize();

		final Vector3d pos = renderInfo.getPosition();

		final ClippingHelper clippingHelper = new ClippingHelper(matrixStack.last().pose(), projection);
		clippingHelper.prepare(pos.x, pos.y, pos.z);

		AxisAlignedBB boxT = box.inflate(0.08f);

		List<Line> lines = new ArrayList<>();
		lines.add(new Line(new AxisAlignedBB(boxT.minX, boxT.minY, boxT.minZ, boxT.minX, boxT.minY, boxT.maxZ), pos, clippingHelper));
		lines.add(new Line(new AxisAlignedBB(boxT.minX, boxT.maxY, boxT.minZ, boxT.minX, boxT.maxY, boxT.maxZ), pos, clippingHelper));
		lines.add(new Line(new AxisAlignedBB(boxT.maxX, boxT.minY, boxT.minZ, boxT.maxX, boxT.minY, boxT.maxZ), pos, clippingHelper));
		lines.add(new Line(new AxisAlignedBB(boxT.maxX, boxT.maxY, boxT.minZ, boxT.maxX, boxT.maxY, boxT.maxZ), pos, clippingHelper));
		Collections.sort(lines);
		final Vector3d lineZ = lines.get(0).line.getCenter();

		lines.clear();
		lines.add(new Line(new AxisAlignedBB(boxT.minX, boxT.minY, boxT.minZ, boxT.minX, boxT.maxY, boxT.minZ), pos, clippingHelper));
		lines.add(new Line(new AxisAlignedBB(boxT.minX, boxT.minY, boxT.maxZ, boxT.minX, boxT.maxY, boxT.maxZ), pos, clippingHelper));
		lines.add(new Line(new AxisAlignedBB(boxT.maxX, boxT.minY, boxT.minZ, boxT.maxX, boxT.maxY, boxT.minZ), pos, clippingHelper));
		lines.add(new Line(new AxisAlignedBB(boxT.maxX, boxT.minY, boxT.maxZ, boxT.maxX, boxT.maxY, boxT.maxZ), pos, clippingHelper));
		Collections.sort(lines);
		final Vector3d lineY = lines.get(0).line.getCenter();

		lines.clear();
		lines.add(new Line(new AxisAlignedBB(boxT.minX, boxT.minY, boxT.minZ, boxT.maxX, boxT.minY, boxT.minZ), pos, clippingHelper));
		lines.add(new Line(new AxisAlignedBB(boxT.minX, boxT.minY, boxT.maxZ, boxT.maxX, boxT.minY, boxT.maxZ), pos, clippingHelper));
		lines.add(new Line(new AxisAlignedBB(boxT.minX, boxT.maxY, boxT.minZ, boxT.maxX, boxT.maxY, boxT.minZ), pos, clippingHelper));
		lines.add(new Line(new AxisAlignedBB(boxT.minX, boxT.maxY, boxT.maxZ, boxT.maxX, boxT.maxY, boxT.maxZ), pos, clippingHelper));
		Collections.sort(lines);
		final Vector3d lineX = lines.get(0).line.getCenter();
		lines.clear();

		matrixStack.pushPose();
		matrixStack.translate(-pos.x, -pos.y, -pos.z);

		drawText(matrixStack, renderInfo, new Vector3d(lineX.x, lineX.y, lineX.z), String.valueOf(lengthX));
		drawText(matrixStack, renderInfo, new Vector3d(lineY.x, lineY.y, lineY.z), String.valueOf(lengthY));
		drawText(matrixStack, renderInfo, new Vector3d(lineZ.x, lineZ.y, lineZ.z), String.valueOf(lengthZ));
		matrixStack.popPose();
	}

	private void drawText(MatrixStack matrixStack, ActiveRenderInfo renderInfo, Vector3d pos, String length) {
		final FontRenderer font = Minecraft.getInstance().font;

		final float size = 0.02f;

		matrixStack.pushPose();
		matrixStack.translate(pos.x, pos.y + size * 5.0, pos.z);
		matrixStack.mulPose(renderInfo.rotation());
		matrixStack.scale(-size, -size, -size);
		matrixStack.translate(-font.width(length) / 2f, 0, 0);
		font.draw(matrixStack, length, 0,0, this.color.getColorValue());
		matrixStack.popPose();
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished() {
		this.finished = true;
	}

	private static class Line implements Comparable<Line> {
		private final AxisAlignedBB line;
		private final boolean isVisible;
		private final double distance;

		Line(AxisAlignedBB line, Vector3d pos, ClippingHelper clippingHelper) {
			this.line = line;
			this.isVisible = clippingHelper.isVisible(line);
			this.distance = line.getCenter().distanceTo(pos);
		}

		@Override
		public int compareTo(Line l) {
			if (isVisible) {
				return l.isVisible ? Double.compare(distance, l.distance) : -1;
			} else {
				return l.isVisible ? 1 : 0;
			}
		}
	}
}
