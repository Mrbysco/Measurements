package com.mrbysco.measurements.client;

import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;

public abstract class LineRenderType extends RenderType {

	public LineRenderType(String name, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload,
	                      Runnable setupState, Runnable clearState) {
		super(name, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
	}

	public static RenderType lineRenderType(float lineWidth) {
		return RenderType.create("lines_no_depth", 256, LinePipelines.LINES_NO_DEPTH, RenderType.CompositeState.builder()
				.setLineState(new LineStateShard(OptionalDouble.of(lineWidth)))
				.setLayeringState(VIEW_OFFSET_Z_LAYERING)
				.setOutputState(ITEM_ENTITY_TARGET)
				.createCompositeState(false));
	}
}
