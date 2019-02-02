package frc.robot.Subsystems;
import edu.wpi.first.wpilibj.Spark;
import com.ctre.phoenix.motorcontrol.can.*;

public class Elevator extends Subsystems {
    int tPort1, tPort2;
    double encodedHeight;
    double encodedVelocity;
    TalonSRX talon1;
    TalonSRX talon2;

    public enum ElevatorState{

    }

    public Elevator(int tPort1, int tPort2) {
        this.tPort1 = tPort1;
        this.tPort2 = tPort2;
        talon1 = new WPI_TalonSRX(tPort1);
        talon2 = new WPI_TalonSRX(tPort2);
        talon2.follow(talon1);
    }

    @Override 
    public void zeroSensors(){

    }

    @Override 
    public void stop(){

    }

    @Override 
    public void displaySmartDashBoard(){

    }

    @Override 
    public void onLoop(){

    }

    public void setHeight(double height) {
        talon1.set(ControlMode.PercentOutput, height );
    }

    public double getRawVelocity() {
        encodedVelocity = talon1.getSelectedSensorVelocity(0);
        return encodedVelocity;
    }

    public double getRawPosition() {
        encodedPosition = talon1.getSelectedSensorPosition(0);
        return encodedPosition;
    }

    public double getHeight(){
        return getRawPosition() * Constants.ELEVATOR_TICKS_TO_DISTANCE;
    }

    public double getVelocity(){
       return 1; 
    }
}