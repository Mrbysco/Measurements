package com.mrbysco.measurements.datagen;

import com.mrbysco.measurements.Constants;
import com.mrbysco.measurements.registration.MeasurementRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@EventBusSubscriber
public class MeasurementsDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();

		event.createReloadableRegistryObjects(
				new RegistrySetBuilder()
						.add(RecipeProvider.asBootstrap(MeasurementsRecipeProvider::new)),
				Set.of(Constants.MOD_ID));

		generator.addProvider(true, new MeasurementsModels(packOutput));
	}

	public static class MeasurementsModels extends ModelProvider {

		public MeasurementsModels(PackOutput output) {
			super(output, Constants.MOD_ID);
		}

		@Override
		protected void registerModels(@NotNull BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
			itemModels.generateFlatItem(MeasurementRegistry.TAPE_MEASURE_ITEM.get(), ModelTemplates.FLAT_ITEM);
		}
	}

	public static class MeasurementsRecipeProvider extends RecipeProvider {
		public MeasurementsRecipeProvider(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
			super(recipeOutput, advancementOutput);
		}

		@Override
		protected void buildRecipes() {
			shaped(RecipeCategory.TOOLS, MeasurementRegistry.TAPE_MEASURE_ITEM.get())
					.pattern(" G ")
					.pattern("GIY")
					.pattern(" GY")
					.define('I', Tags.Items.INGOTS_IRON)
					.define('Y', Items.WOOL.yellow())
					.define('G', Items.WOOL.gray())
					.unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
					.unlockedBy("has_yellow_wool", has(Items.WOOL.yellow()))
					.unlockedBy("has_gray_wool", has(Items.WOOL.gray()))
					.save(output);
		}
	}
}
