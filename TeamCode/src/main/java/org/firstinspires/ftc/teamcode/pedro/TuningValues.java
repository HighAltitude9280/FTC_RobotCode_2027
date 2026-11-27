package org.firstinspires.ftc.teamcode.pedro;

import com.bylazar.configurables.annotations.Configurable; // ajusta el paquete a tu versión de Panels
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;

@Configurable
public class TuningValues {
    public static double headingP = 5.616210429942214;
    public static double fwdPrimaryP = 0.44222371695093343;
    public static double fwdSecondaryP = 0.16338984697802655;
    public static double latPrimaryP = 0.45220737542268635;
    public static double latSecondaryP = 0.20707854201507177;
    public static double coastF = 0.012106832418647831;
    public static double brakeF = 0.010290807555850656;

    public static ForesightConfig build() {
        return new ForesightConfig(c -> {
            c.forwardTranslational.set(Controller.piecewise(Controller.proportional(fwdSecondaryP))
                    .put(2.5, Controller.proportional(fwdPrimaryP)));
            c.strafeTranslational.set(Controller.piecewise(Controller.proportional(latSecondaryP))
                    .put(2.5, Controller.proportional(latPrimaryP)));
            c.coast.set(Controller.proportionalFeedforward(coastF));
            c.brake.set(Controller.proportionalFeedforward(brakeF));
            c.headingFeedback.set(Controller.proportional(headingP));

            // Estos los copias tal cual de Constants (no los vas a tunear ahorita)
            c.headingBrakeCoefficients.set(Vector2D.cartesian(0.10704826758082685, 5.666789716817385E-4));
            c.linearBrakeCoefficients.set(Matrix.diag(0.09966852906966608, 0.04806025795528229));
            c.quadraticBrakeCoefficients.set(Matrix.diag(0.0020285905628153788, 9.297545216111125E-4));
            c.maxAchievableForwardVelocity.set(84.07020398804421);
            c.maxAchievableStrafeVelocity.set(130.27697096172543);
            c.naturalForwardDeceleration.set(36.35168988949471);
            c.naturalStrafeDeceleration.set(173.91567186087144);
        });
    }
}