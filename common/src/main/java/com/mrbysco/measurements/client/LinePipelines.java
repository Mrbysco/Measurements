//package com.mrbysco.measurements.client;
//
//import com.mojang.blaze3d.pipeline.RenderPipeline;
//import com.mojang.blaze3d.platform.DepthTestFunction;
//import com.mrbysco.measurements.Constants;
//import net.minecraft.client.renderer.RenderPipelines;
//
//public class LinePipelines {
//	public static final RenderPipeline LINES_NO_DEPTH = RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
//			.withLocation(Constants.modLoc("pipeline/lines_no_depth"))
//			.withCull(false)
//			.withDepthWrite(false)
//			.withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
//			.build();
//}
