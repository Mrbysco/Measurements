package com.mrbysco.measurements.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;

import java.util.OptionalDouble;

public class LineRenderType extends RenderType {
	public LineRenderType(String nameIn, VertexFormat formatIn, int drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
		super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
	}

	public static RenderType lineRenderType(float lineWidth) {
		return create("lines_no_depth",
				DefaultVertexFormats.POSITION_COLOR, GL11.GL_LINES, 256,
				RenderType.State.builder()
						.setLineState(new LineState(OptionalDouble.of(lineWidth)))
						.setTransparencyState(NO_TRANSPARENCY)
						.setTextureState(NO_TEXTURE)
						.setCullState(NO_CULL)
						.setDepthTestState(NO_DEPTH_TEST)
						.createCompositeState(false));
	}
}
