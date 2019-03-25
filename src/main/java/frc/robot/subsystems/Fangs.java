package frc.robot.subsystems;

import com.ctre.phoenix.ParamEnum;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.climber.ArticulateFangs;
import frc.robot.util.MercTalonSRX;
import frc.robot.util.PIDGain;
import frc.robot.util.interfaces.IMercMotorController;

/**
 * Subsystem to lift the robot's front
 */
public class Fangs extends Subsystem {
    public final int ARTICULATION_PID_SLOT = 0;

    public final PIDGain articulationGain = new PIDGain(0.1, 0.0, 0.0, 0.0, 0.75);

    public final int ARTICULATION_THRESHOLD = 10000;

    private IMercMotorController articulator;

    public Fangs() {
        articulator = new MercTalonSRX(RobotMap.CAN.FANGS);

        articulator.resetEncoder();

        articulator.setPosition(FangsPosition.IN_BOT.getEncTicks());

        articulator.configPID(ARTICULATION_PID_SLOT, articulationGain);

        articulator.configAllowableClosedLoopError(ARTICULATION_PID_SLOT, ARTICULATION_THRESHOLD);

        articulator.configSetParameter(ParamEnum.eClearPositionOnLimitF, 1, 0, 0);
        articulator.configSetParameter(ParamEnum.eClearPositionOnLimitR, 1, 0, 0);
    }

    public void initDefaultCommand() {
        setDefaultCommand(new ArticulateFangs(FangsPosition.IN_BOT));
    }

    public IMercMotorController getArticulator() {
        return articulator;
    }

    public enum FangsPosition {
        //Temporary encoder tick values
        DOWN(0),
        IN_BOT(2000 * 27);

        private int encPos;

        FangsPosition(int encPos) {
            this.encPos = encPos;
        }

        public int getEncTicks() {
            return encPos;
        }
    }
}