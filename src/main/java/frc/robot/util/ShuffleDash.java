package frc.robot.util;

import java.util.List;
import java.util.ArrayList;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.interfaces.IMercPIDTunable;
import frc.robot.util.interfaces.IMercShuffleBoardPublisher;

public class ShuffleDash {

    private static final String PID_TUNER = "PIDTuner/Target";
    private static final String PID_TUNER_P = "PIDTuner/kP";
    private static final String PID_TUNER_I = "PIDTuner/kI";
    private static final String PID_TUNER_D = "PIDTuner/kD";
    private static final String PID_TUNER_F = "PIDTuner/kF";

    private static final double UPDATE_PERIOD_SECONDS = 0.100; // Update every 100ms

    private class TunablePIDSlot {
        public IMercPIDTunable tunable;
        public int slot;
        public TunablePIDSlot(IMercPIDTunable tunable, int slot) {
            this.tunable = tunable;
            this.slot = slot;
        }
    }
    private TunablePIDSlot tunableSlot = null;

    private NetworkTableInstance ntInstance;
    private SendableChooser<String> autonFirstStep;
    private List<IMercShuffleBoardPublisher> publishers;
    private SendableChooser<TunablePIDSlot> tunablePIDChooser;
    private String positionColor;
    private Notifier shuffleDashUpdater;

    public ShuffleDash() {

        SmartDashboard.putString("Position Control Color", getPositionControlColor());

        ntInstance = NetworkTableInstance.getDefault();

        autonFirstStep = new SendableChooser<>();

        publishers = new ArrayList<IMercShuffleBoardPublisher>();

        tunablePIDChooser = new SendableChooser<TunablePIDSlot>();
        tunablePIDChooser.setDefaultOption("NONE", null);
        SmartDashboard.putData(PID_TUNER, tunablePIDChooser);
        SmartDashboard.putNumber(PID_TUNER_P, 0.0);
        SmartDashboard.putNumber(PID_TUNER_I, 0.0);
        SmartDashboard.putNumber(PID_TUNER_D, 0.0);
        SmartDashboard.putNumber(PID_TUNER_F, 0.0);

        shuffleDashUpdater = new Notifier(this::updateDash);
        shuffleDashUpdater.startPeriodic(UPDATE_PERIOD_SECONDS);
    }

    public void addPublisher(IMercShuffleBoardPublisher publisher) {
        publishers.add(publisher);
    }

    public void addPIDTunable(IMercPIDTunable pidTunable, String pidName){
        for(int slot : pidTunable.getSlots()) {
            tunablePIDChooser.addOption(pidName + "." + Integer.toString(slot), new TunablePIDSlot(pidTunable, slot));
        }
    }

    public void updateDash() {
        
        for(IMercShuffleBoardPublisher publisher: publishers) {
            publisher.publishValues();
        }

        // PID Tuner
        TunablePIDSlot tunableSlot = tunablePIDChooser.getSelected();
        if (tunableSlot != null) {
            if (tunableSlot != this.tunableSlot) {
                PIDGain gains = tunableSlot.tunable.getPIDGain(tunableSlot.slot);
                SmartDashboard.putNumber(PID_TUNER_P, gains.kP);
                SmartDashboard.putNumber(PID_TUNER_I, gains.kI);
                SmartDashboard.putNumber(PID_TUNER_D, gains.kD);
                SmartDashboard.putNumber(PID_TUNER_F, gains.kF);
            } else {
                PIDGain gains = new PIDGain(
                    SmartDashboard.getNumber(PID_TUNER_P, 0.0),
                    SmartDashboard.getNumber(PID_TUNER_I, 0.0),
                    SmartDashboard.getNumber(PID_TUNER_D, 0.0),
                    SmartDashboard.getNumber(PID_TUNER_F, 0.0));
                tunableSlot.tunable.setPIDGain(tunableSlot.slot, gains);
            }
            this.tunableSlot = tunableSlot;
        }
        SmartDashboard.putData("Auton/First Step", autonFirstStep);
    }

    public String getFirstStep() {
        return autonFirstStep.getSelected();
        
    }

    public String getPositionControlColor() {
        positionColor = DriverStation.getInstance().getGameSpecificMessage();
        if(positionColor.length() > 0)
            switch(positionColor.charAt(0)) {
                case 'R':
                    return "Red";
                case 'G':
                    return "Green";
                case 'B':
                    return "Blue";
                case 'Y':
                    return "Yellow";
                default:
                    return "Unknown/Glitch";
            }
        return "Unknown";
    }
}
