package frc.robot.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;

public class TriggerButton {

  private Joystick gamepad;
  private int trigger;
  private boolean held;
  private final double PRESS_THRESHOLD;

  public TriggerButton(Joystick gamepad, int trigger) {
    this(gamepad, trigger, 0.1);
  }

  public TriggerButton(Joystick gamepad, int trigger, double press_threshold) {
    this.gamepad = gamepad;
    this.trigger = trigger;
    PRESS_THRESHOLD = press_threshold;
  }
  
  /**
   * @return whether or not the trigger is pressed past the set threshold
   */
  public boolean isPressed(){
    if(gamepad.getRawAxis(trigger) > PRESS_THRESHOLD)
      return true;
    return false;
  }

  /**
   * Starts the command when the trigger is pressed, won't run again until the button is released and pressed again.
   * @param command the command to run
   */
  public void whenPressed(Command command) {
    if(isPressed() && !held) {
      command.schedule();
      held = true;
    }
    else if(!isPressed())
      held = false;
  }

  /**
   * Starts the command when the trigger is pressed, ends it when it is released.
   * @param command the command to run
   */
  public void whenHeld(Command command) {
    if(isPressed() && !held) {
      command.schedule();
      held = true;
    }
    else if(!isPressed()) {
      command.cancel();
      held = false;
    }
  }

  /**
   * Runs the command repeatedly while the trigger is held.
   * @param command the command to run
   */
  public void whileHeld(Command command) {
    if(isPressed())
      command.schedule();
  }
    
  /**
   * Starts the command when the trigger is pressed pressed, stops the command when it is pressed again.
   * @param command the command to run
   */
  public void toggleWhenPressed(Command command) {
    if(isPressed())
      if(command.isScheduled())
        command.cancel();
      else
        command.schedule();
  }
}