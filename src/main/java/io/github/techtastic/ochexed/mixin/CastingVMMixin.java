package io.github.techtastic.ochexed.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.vm.CastingVM;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.techtastic.ochexed.oc.architecture.ArchitectureCastEnv;
import io.github.techtastic.ochexed.oc.architecture.HexcastingArchitecture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(CastingVM.class)
public class CastingVMMixin {
    @Final
    @Shadow
    private CastingEnvironment env;

    @WrapOperation(method = "queueExecuteAndWrapIotas", at = @At(value = "INVOKE",
            target = "Lat/petrak/hexcasting/api/casting/iota/IotaType;isTooLargeToSerialize(Ljava/lang/Iterable;)Z"))
    public boolean isTooLarge(Iterable<Iota> examinee, Operation<Boolean> original) {
        if (env instanceof ArchitectureCastEnv env) {
            int totalSize = 0;
            for (Iota iota : examinee) {
                if (IotaType.isTooLargeToSerialize(List.of(iota)))
                    return true;
                totalSize += iota.size();
            }
            return totalSize < ((HexcastingArchitecture) env.machine.architecture()).getMaxStackSize();
        } else
            return original.call(examinee);
    }
}
