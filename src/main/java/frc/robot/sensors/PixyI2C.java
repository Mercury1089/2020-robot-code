package frc.robot.sensors;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Notifier;
import frc.robot.util.BoundingBox;

/**
 * PixyCam implementation using the I2C interface
 * Thanks to croadfeldt (https://github.com/croadfeldt/wpilib_pixy_spi_java) for the original implementation
 */
public class PixyI2C{
    private final I2C PIXY;
    private final BoundingBox[] BOXES;
    private final int MIN_BOX_WIDTH = 45; //Arbitrary value, change to what minimum value of width can be for cube to be cassified as "In range"
    private final Notifier PIXY_UPDATE_NOTIFIER = new Notifier(null);
    private BoundingBox curTarget;

    /**
     * Constructs a new PixyCam, assuming that the I2C
     * bus is the one on the roboRIO
     */
    public PixyI2C() {
        // Assume that we are using the onboard port,
        // We never use MXP port for I2C
        PIXY = new I2C(I2C.Port.kOnboard, 0x54);

        BOXES = new BoundingBox[7];

        PIXY_UPDATE_NOTIFIER.setHandler(() -> {
            this.read(1);
        });

        PIXY_UPDATE_NOTIFIER.startPeriodic(0.05);
    }

    /**
     * Converts bytes from the PIXY into readable data
     *
     * @param upper
     * @param lower
     * @return int data from PIXY
     */
    private int cvt(byte upper, byte lower) {
        return (((int) upper & 0xff) << 8) | ((int) lower & 0xff);
    }

    /**
     * Read data from the Pixy
     *
     * @param signature the color signature to get for
     */
    public void read(int signature) { // The signature should be which number object in
        int checksum, sig;                                                // pixymon you are trying to get data for
        byte[] rawData = new byte[32];

        try {
            PIXY.readOnly(rawData, 32);
        } catch (RuntimeException e) {

        }

        if (rawData.length < 32) {
            return;
        }

        for (int i = 0; i <= 16; i++) {
            int syncWord = cvt(rawData[i + 1], rawData[i]); // Parse first 2 bytes
            if (syncWord == 0xaa55) { // Check is first 2 bytes equal a "sync word", which indicates the start of a packet of valid data
                syncWord = cvt(rawData[i + 3], rawData[i + 2]); // Parse the next 2 bytes
                if (syncWord != 0xaa55) { // Shifts everything in the case that one syncword is sent
                    i -= 2;
                }
                //This next block parses the rest of the data
                checksum = cvt(rawData[i + 5], rawData[i + 4]);
                sig = cvt(rawData[i + 7], rawData[i + 6]);
                if (sig <= 0 || sig > BOXES.length) {
                    break;
                }

                BOXES[sig - 1] = new BoundingBox(
                        cvt(rawData[i + 9], rawData[i + 8]),
                        cvt(rawData[i + 11], rawData[i + 10]),
                        cvt(rawData[i + 13], rawData[i + 12]),
                        cvt(rawData[i + 15], rawData[i + 14])
                );

                //Checks whether the data is valid using the checksum *This if block should never be entered*
                if (checksum != sig + BOXES[sig - 1].getX() + BOXES[sig - 1].getY() + BOXES[sig - 1].getWidth() + BOXES[sig - 1].getHeight()) {
                    BOXES[sig - 1] = null;
                }
                break;
            }
        }

        curTarget = BOXES[signature - 1];
        BOXES[signature - 1] = null;
    }

    public BoundingBox getTarget() {
        return curTarget;
    }

    /**
     * Gets whether or not the PixyCam is near the target based on
     * the target's scale
     *
     * @return whether or not we can see a target and if it is close enough
     */
    public boolean inRange() {
        return (curTarget != null && curTarget.getWidth() >= MIN_BOX_WIDTH);
    }
}

