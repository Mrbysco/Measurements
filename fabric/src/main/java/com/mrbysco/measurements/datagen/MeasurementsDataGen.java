package com.mrbysco.measurements.datagen;

import com.mrbysco.measurements.registration.MeasurementRegistry;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class MeasurementsDataGen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		var pack = generator.createPack();

		pack.addProvider(MeasurementsRecipeProvider.Runner::new);
		pack.addProvider(MeasurementsModels::new);
	}

	public static class MeasurementsModels extends FabricModelProvider {

		public MeasurementsModels(FabricPackOutput output) {
			super(output);
		}

		@Override
		public void generateBlockStateModels(BlockModelGenerators blockModels) {
			// No block models to generate
		}

		@Override
		public void generateItemModels(ItemModelGenerators itemModels) {
			itemModels.generateFlatItem(MeasurementRegistry.TAPE_MEASURE_ITEM.get(), ModelTemplates.FLAT_ITEM);
		}
	}

	public static class MeasurementsRecipeProvider extends RecipeProvider {
		public MeasurementsRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
			super(registries, output);
		}

		@Override
		public void buildRecipes() {
			shaped(RecipeCategory.TOOLS, MeasurementRegistry.TAPE_MEASURE_ITEM.get())
					.pattern(" G ")
					.pattern("GIY")
					.pattern(" GY")
					.define('I', ConventionalItemTags.IRON_INGOTS)
					.define('Y', Items.YELLOW_WOOL)
					.define('G', Items.GRAY_WOOL)
					.unlockedBy("has_iron_ingot", has(ConventionalItemTags.IRON_INGOTS))
					.unlockedBy("has_yellow_wool", has(Items.YELLOW_WOOL))
					.unlockedBy("has_gray_wool", has(Items.GRAY_WOOL))
					.save(output);
		}

		public static class Runner extends FabricRecipeProvider {

			public Runner(FabricPackOutput output, CompletableFuture<Provider> registriesFuture) {
				super(output, registriesFuture);
			}

			@Override
			@NotNull
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
