import java.awt.*;
import java.applet.*;
import simulate.*;

public class Applet1 extends Applet
{
  public void init() {
    setSize(600,300);

    //Instantiate classes
    simulation1 = new Simulation2D();
    display1 = new DisplayConfiguration();
    controller1 = new ControllerButton();
    integrator1 = new IntegratorHard();
    potential21 = new P2SquareWell();
    species1 = new SpeciesDisks();
    phase1 = new Phase();

    //Add simulation to applet
    simulation1.setBounds(0,0,600,300);
    simulation1.setLayout(null);
    add(simulation1);

    //Add controller to simulation
    controller1.setBounds(50,0,100,40);
    simulation1.add(controller1);

    //Add display to simulation
    simulation1.add(display1);
    display1.setBounds(150,0,300,300);

    //Add integrator to controller
    controller1.add(integrator1);

    //Add phase to simulation
    simulation1.add(phase1);

    //Add potential to simulation
    simulation1.add(potential21);

    //Add species to simulation
    simulation1.add(species1);
  }
	
  Simulation simulation1;
  Display display1;
  Controller controller1;
  Integrator integrator1;
  Potential2 potential21;
  Species species1;
  Phase phase1;
}
