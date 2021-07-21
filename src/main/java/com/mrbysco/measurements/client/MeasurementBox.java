package com.mrbysco.measurements.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mrbysco.measurements.config.MeasurementsConfig;
import com.mrbysco.measurements.util.Utils;
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
	private DyeColor lineColor;
	private DyeColor textX;
	private DyeColor textY;
	private DyeColor textZ;

	MeasurementBox(BlockPos block, RegistryKey<World> dimensionKey) {
		this.startPos = block;
		this.endPos = block;
		this.dimensionKey = dimensionKey;
		this.finished = false;

		// Setup Rendered Line Color
		String configLineColor = MeasurementsConfig.COMMON.lineColor.get();
		switch (configLineColor) {
			case "RANDOM" :
				this.lineColor = Utils.getRandColor();
				break;
			default:
				this.lineColor = DyeColor.valueOf(configLineColor.toUpperCase());
				break;
		}

		// Setup Rendered Text Color
		String textColor = MeasurementsConfig.COMMON.textColor.get();
		switch (MeasurementsConfig.COMMON.textColor.get()) {
			case "RANDOM":
				DyeColor randColor = Utils.getRandColor();
				this.textX = randColor;
				this.textY = randColor;
				this.textZ = randColor;
			break;
			case "XYZRGB":
				this.textX = DyeColor.RED;
				this.textY = DyeColor.GREEN;
				this.textZ = DyeColor.BLUE;
			break;
			default:
				this.textX = DyeColor.valueOf(textColor);
				this.textY = DyeColor.valueOf(textColor);
				this.textZ = DyeColor.valueOf(textColor);
			break;
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

		this.box = new AxisAlignedBB(Math.min(ax, bx), Math.min(ay, by), Math.min(az, bz),
				Math.max(ax, bx) + 1, Math.max(ay, by) + 1, Math.max(az, bz) + 1);
	}

	public void setBlockEnd(BlockPos blockEnd) {
		this.endPos = blockEnd;
		this.setBoundingBox();
	}

	public void render(RegistryKey<World> currentDimensionKey, MatrixStack matrixStack, RenderTypeBuffers buffer, ActiveRenderInfo renderInfo, Matrix4f projection) {
		if (!dimensionKey.getLocation().equals(currentDimensionKey.getLocation())) return;

		final float[] lineColor = this.lineColor.getColorComponentValues();
		final float r = lineColor[0];
		final float g = lineColor[1];
		final float b = lineColor[2];
		final float a = 0.95F;

		Vector3d pos = renderInfo.getProjectedView();
		double distance = box.getCenter().distanceTo(pos);
		float lineWidth = MeasurementsConfig.COMMON.lineWidth.get();//2f;
		if (distance > 48) {
			lineWidth = MeasurementsConfig.COMMON.lineWidthMax.get();//1.0f;
		}

		matrixStack.push();
		IRenderTypeBuffer.Impl bufferSource = buffer.getBufferSource();
		IVertexBuilder builder = bufferSource.getBuffer(LineRenderType.lineRenderType(lineWidth));

		matrixStack.translate(-pos.x, -pos.y, -pos.z);

		WorldRenderer.drawBoundingBox(matrixStack, builder, box, r, g, b, a);
		bufferSource.finish(LineRenderType.lineRenderType(lineWidth));
		matrixStack.pop();
		drawLength(matrixStack, buffer, renderInfo, projection);
	}

	private void drawLength(MatrixStack matrixStack, RenderTypeBuffers buffers, ActiveRenderInfo renderInfo, Matrix4f projection) {
		final int lengthX = (int) box.getXSize();
		final int lengthY = (int) box.getYSize();
		final int lengthZ = (int) box.getZSize();

		final Vector3d pos = renderInfo.getProjectedView();

		final ClippingHelper clippingHelper = new ClippingHelper(matrixStack.getLast().getMatrix(), projection);
		clippingHelper.setCameraPosition(pos.x, pos.y, pos.z);

		AxisAlignedBB boxT = box.grow(0.08f);

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

		matrixStack.push();
		matrixStack.translate(-pos.x, -pos.y, -pos.z);

		drawText(matrixStack, buffers, renderInfo, new Vector3d(lineX.x, lineX.y, lineX.z), String.valueOf(lengthX), this.textX);
		drawText(matrixStack, buffers, renderInfo, new Vector3d(lineY.x, lineY.y, lineY.z), String.valueOf(lengthY), this.textY);
		drawText(matrixStack, buffers, renderInfo, new Vector3d(lineZ.x, lineZ.y, lineZ.z), String.valueOf(lengthZ), this.textZ);

		matrixStack.pop();
	}

	private void drawText(MatrixStack matrixStack, RenderTypeBuffers buffers, ActiveRenderInfo renderInfo, Vector3d pos, String length, DyeColor color) {
		final FontRenderer font = Minecraft.getInstance().fontRenderer;

		final double doubleTextSize = MeasurementsConfig.COMMON.textSize.get();
		final float floatTextSize = (float)doubleTextSize;//0.02f;

		matrixStack.push();
		matrixStack.translate(pos.x, pos.y + floatTextSize * 5.0, pos.z);
		matrixStack.rotate(renderInfo.getRotation());
		matrixStack.scale(-floatTextSize, -floatTextSize, -floatTextSize);
		matrixStack.translate(-font.getStringWidth(length) / 2f, 0, 0);
		font.drawString(matrixStack, length, 0,0, color.getColorValue());
		matrixStack.pop();
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished() {
		this.finished = true;
	}

	private static class Line implements Comparable<Line> {
		AxisAlignedBB line;
		boolean isVisible;
		double distance;

		Line(AxisAlignedBB line, Vector3d pos, ClippingHelper clippingHelper) {
			this.line = line;
			this.isVisible = clippingHelper.isBoundingBoxInFrustum(line);
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
