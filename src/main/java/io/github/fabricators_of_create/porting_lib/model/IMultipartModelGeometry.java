package io.github.fabricators_of_create.porting_lib.model;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public interface IMultipartModelGeometry<T extends IMultipartModelGeometry<T>> extends ISimpleModelGeometry<T> {
	@Override
	Collection<? extends IModelGeometryPart> getParts();

	Optional<? extends IModelGeometryPart> getPart(String name);

	@Override
	default void addQuads(IGeometryBakingContext owner, IModelBuilder<?> modelBuilder, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ResourceLocation modelLocation) {
		getParts().stream().filter(part -> owner.getPartVisibility(part))
				.forEach(part -> part.addQuads(owner, modelBuilder, bakery, spriteGetter, modelTransform, modelLocation));
	}

	@Override
	default Collection<Material> getTextures(IGeometryBakingContext owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		Set<Material> combined = Sets.newHashSet();
		for (IModelGeometryPart part : getParts())
			combined.addAll(part.getTextures(owner, modelGetter, missingTextureErrors));
		return combined;
	}
}
