package com.mrbysco.measurements.datagen;

import com.mrbysco.measurements.registration.MeasurementRegistry;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class MeasurementsDataGen implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		var pack = generator.createPack();

		pack.addProvider(MeasurementsRecipeProvider::new);
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

	public static class MeasurementsRecipeProvider extends FabricRecipeProvider {

		public MeasurementsRecipeProvider(FabricPackOutput output, CompletableFuture<Provider> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected RecipeProvider createRecipeProvider(Provider registries, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
			return new RecipeProvider(recipes, advancements) {
				@Override
				public void buildRecipes() {
					shaped(RecipeCategory.TOOLS, MeasurementRegistry.TAPE_MEASURE_ITEM.get())
							.pattern(" G ")
							.pattern("GIY")
							.pattern(" GY")
							.define('I', ConventionalItemTags.IRON_INGOTS)
							.define('Y', Items.WOOL.yellow())
							.define('G', Items.WOOL.gray())
							.unlockedBy("has_iron_ingot", has(ConventionalItemTags.IRON_INGOTS))
							.unlockedBy("has_yellow_wool", has(Items.WOOL.yellow()))
							.unlockedBy("has_gray_wool", has(Items.WOOL.gray()))
							.save(output);
				}
			};
		}
	}
}
