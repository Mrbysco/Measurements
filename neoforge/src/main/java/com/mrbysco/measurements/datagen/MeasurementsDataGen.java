package com.mrbysco.measurements.datagen;

import com.mrbysco.measurements.Constants;
import com.mrbysco.measurements.registration.MeasurementRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class MeasurementsDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new MeasurementsModels(packOutput));
		generator.addProvider(true, new MeasurementsRecipeProvider.Runner(packOutput, lookupProvider));
	}

	public static class MeasurementsModels extends ModelProvider {

		public MeasurementsModels(PackOutput output) {
			super(output, Constants.MOD_ID);
		}

		@Override
		protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
			itemModels.generateFlatItem(MeasurementRegistry.TAPE_MEASURE_ITEM.get(), ModelTemplates.FLAT_ITEM);
		}
	}

	public static class MeasurementsRecipeProvider extends RecipeProvider {
		public MeasurementsRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
			super(registries, output);
		}

		@Override
		protected void buildRecipes() {
			shaped(RecipeCategory.TOOLS, MeasurementRegistry.TAPE_MEASURE_ITEM.get())
					.pattern(" G ")
					.pattern("GIY")
					.pattern(" GY")
					.define('I', Tags.Items.INGOTS_IRON)
					.define('Y', Items.YELLOW_WOOL)
					.define('G', Items.GRAY_WOOL)
					.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
					.unlockedBy("has_yellow_wool", has(Items.YELLOW_WOOL))
					.unlockedBy("has_gray_wool", has(Items.GRAY_WOOL))
					.save(output);
		}

		private static class Runner extends RecipeProvider.Runner {
			public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
				super(packOutput, lookupProvider);
			}

			@Override
			protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
				return new MeasurementsRecipeProvider(provider, recipeOutput);
			}

			@Override
			public String getName() {
				return "Measurements Recipes";
			}
		}
	}
}
