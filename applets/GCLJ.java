import java.awt.*;
import java.applet.*;
import simulate.*;

public class GCLJ extends Applet
{
  public void init() {
    setSize(600,300);

    //Instantiate classes
    simulation1 = new Simulation2D();
    display1 = new DisplayConfiguration();
    controller1 = new ControllerButton();
    integrator1 = new IntegratorMC();
    potential21 = new P2SquareWell();
    species1 = new SpeciesDisks();
    phase1 = new Phase();
    gcMove = new MCMoveInsertDelete();
    atomMove = new MCMoveAtom();
    gcMove.setMu(1000.0);  //set chemical potential to a value for reasonable density

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

    //Add MCMoves to integrator
    integrator1.add(gcMove);
    integrator1.add(atomMove);

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
  IntegratorMC integrator1;
  Potential2 potential21;
  Species species1;
  Phase phase1;
  MCMoveInsertDelete gcMove;
  MCMoveAtom atomMove;
}
