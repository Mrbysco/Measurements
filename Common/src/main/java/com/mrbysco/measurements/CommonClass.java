package com.mrbysco.measurements;

import com.mrbysco.measurements.registration.MeasurementRegistry;

public class CommonClass {

	public static void init() {
		MeasurementRegistry.loadClass();
	}
}