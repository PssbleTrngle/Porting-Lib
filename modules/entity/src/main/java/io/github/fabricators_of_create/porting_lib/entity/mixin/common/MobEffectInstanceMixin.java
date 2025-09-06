package io.github.fabricators_of_create.porting_lib.entity.mixin.common;

import com.google.common.base.Suppliers;

import com.google.common.collect.Sets;

import io.github.fabricators_of_create.porting_lib.core.util.MixinHelper;
import io.github.fabricators_of_create.porting_lib.entity.EffectCure;
import io.github.fabricators_of_create.porting_lib.entity.injects.MobEffectInstanceInjection;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;
import java.util.function.Supplier;

@Mixin(MobEffectInstance.class)
public class MobEffectInstanceMixin implements MobEffectInstanceInjection {
	@Shadow
	@Final
	private Holder<MobEffect> effect;

	private final Supplier<Set<EffectCure>> porting_lib$cures = Suppliers.memoize(() -> {
		var set = Sets.<EffectCure>newIdentityHashSet();
		this.effect.value().fillEffectCures(set, MixinHelper.cast(this));
		return set;
	});

	/**
	 * {@return the {@link EffectCure}s which can cure the {@link MobEffect} held by this {@link MobEffectInstance}}
	 */
	public Set<EffectCure> getCures() {
		return porting_lib$cures.get();
	}

}
